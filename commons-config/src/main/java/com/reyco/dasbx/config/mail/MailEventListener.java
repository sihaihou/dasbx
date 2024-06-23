package com.reyco.dasbx.config.mail;

import java.io.File;
import java.util.Collection;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.LongAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import com.reyco.dasbx.commons.utils.MailUtils;
import com.reyco.dasbx.commons.utils.MailUtils.MailAccount;
import com.reyco.dasbx.commons.utils.ToString;
import com.reyco.dasbx.config.mail.MailEventListener.MailEvent;

@Component
public class MailEventListener implements ApplicationListener<MailEvent>  {
	
	private static final Logger log = LoggerFactory.getLogger(MailEventListener.class);
	
	private static ThreadPoolExecutor executor;

	static {
		System.getProperties().setProperty("mail.mime.splitlongparameters", "false");
		int availableProcessors = Runtime.getRuntime().availableProcessors();
		executor = new ThreadPoolExecutor(availableProcessors,availableProcessors*2+1,60L,TimeUnit.SECONDS,new LinkedBlockingQueue<Runnable>(50),new ThreadFactory() {
			LongAdder longAdder = new LongAdder();
			@Override
			public Thread newThread(Runnable r) {
				Thread thread = new Thread(r);
				thread.setDaemon(true);
				longAdder.increment();
				thread.setName("com.vanvan.machine.email.sender-"+longAdder.intValue());
				return thread;
			}
		},new ThreadPoolExecutor.CallerRunsPolicy())  ;
	}

	@Override
	public void onApplicationEvent(MailEvent event) {
		MailContent mailContent = event.getMailContent();
		sendAsync(mailContent);
	}
	public void sendAsync(MailContent mailContent) {
		executor.execute(()->{
			MailUtils.send(mailContent.getMailAccount(), mailContent.getTos(), mailContent.getSubject(),
					mailContent.getContent(), mailContent.getIsHtml(), mailContent.getFiles());
		});
	}
	public String sendSync(MailContent mailContent) {
		try {
			return executor.submit(()->{
				return MailUtils.send(mailContent.getMailAccount(), mailContent.getTos(), mailContent.getSubject(),
						mailContent.getContent(), mailContent.getIsHtml(), mailContent.getFiles());
			}).get();
		} catch (Exception e) {
			log.error("邮件发送失败,mailContent：" + mailContent);
			e.printStackTrace();
		}
		return null;
	}
	public static class MailEvent extends ApplicationEvent {
		/**
		 * 
		 */
		private static final long serialVersionUID = -3772748748635331931L;
		/**
		 * mail内容
		 */
		private MailContent mailContent;

		/**
		 * @param source
		 */
		public MailEvent(Object source, MailContent mailContent) {
			super(source);
			this.mailContent = mailContent;
		}

		public MailContent getMailContent() {
			return mailContent;
		}

		public void setMailContent(MailContent mailContent) {
			this.mailContent = mailContent;
		}
	}
	public static class MailContent extends ToString {
		/**
		 * 
		 */
		private static final long serialVersionUID = 2892127090032223621L;
		private MailAccount mailAccount = new MailAccount();
		private String tos;
		private String subject;
		private String content;
		private Boolean isHtml;
		private Collection<File> files;

		public MailAccount getMailAccount() {
			return mailAccount;
		}

		public void setMailAccount(MailAccount mailAccount) {
			this.mailAccount = mailAccount;
		}

		public String getTos() {
			return tos;
		}

		public void setTos(String tos) {
			this.tos = tos;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public Boolean getIsHtml() {
			return isHtml;
		}

		public void setIsHtml(Boolean isHtml) {
			this.isHtml = isHtml;
		}

		public Collection<File> getFiles() {
			return files;
		}

		public void setFiles(Collection<File> files) {
			this.files = files;
		}
	}

}
