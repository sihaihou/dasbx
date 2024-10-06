package com.reyco.dasbx.commons.utils.mail;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import com.reyco.dasbx.commons.utils.serializable.ToString;
import com.sun.mail.util.MailSSLSocketFactory;

public class MailUtils {
	private static final Logger log = LoggerFactory.getLogger(MailUtils.class);
	private final static String CHARSET = "UTF-8";
	
	/**
	 * 
	 * @param mailAccount	账号主体
	 * @param tos			接受人
	 * @param subject		主题
	 * @param content		内容
	 * @param isHtml		是否html
	 * @return
	 */
	public static String send(MailAccount mailAccount, String to, String subject, String content,boolean isHtml) {
		return send(mailAccount,splitToCollection(to) , subject, content, isHtml,null);
	}
	/**
	 * 
	 * @param mailAccount	账号主体
	 * @param tos			接受人
	 * @param subject		主题
	 * @param content		内容
	 * @param isHtml		是否html
	 * @return
	 */
	public static String send(MailAccount mailAccount, Collection<String> tos, String subject, String content,boolean isHtml) {
		return send(mailAccount, tos, subject, content, isHtml,null);
	}
	/**
	 * 
	 * @param mailAccount	账号主体
	 * @param to			接受人
	 * @param subject		主题
	 * @param content		内容
	 * @param isHtml		是否html
	 * @param file			文件
	 * @return
	 */
	public static String send(MailAccount mailAccount, String to, String subject, String content,boolean isHtml,File file) {
		return send(mailAccount, splitToCollection(to), subject, content, isHtml,Arrays.asList(file));
	}
	/**
	 * 
	 * @param mailAccount	账号主体
	 * @param to			接受人
	 * @param subject		主题
	 * @param content		内容
	 * @param isHtml		是否html
	 * @param file			文件
	 * @return
	 */
	public static String send(MailAccount mailAccount, String to, String subject, String content,boolean isHtml,Collection<File> file) {
		return send(mailAccount, splitToCollection(to), subject, content, isHtml,file);
	}
	/**
	 * 
	 * @param mailAccount	账号主体
	 * @param tos			接受人
	 * @param subject		主题
	 * @param content		内容
	 * @param isHtml		是否html
	 * @param files			附件
	 * @return
	 */
	public static String send(MailAccount mailAccount, Collection<String> tos, String subject, String content,boolean isHtml, Collection<File> files) {
		try {
			Properties properties = new Properties();
			properties.setProperty("mail.smtp.host", mailAccount.getHost());
			properties.put("mail.smtp.auth", mailAccount.getAuth());
			properties.put("mail.smtp.ssl.enable", mailAccount.getSslEnable());
			MailSSLSocketFactory sf = new MailSSLSocketFactory();
			sf.setTrustAllHosts(true);
			properties.put("mail.smtp.ssl.socketFactory", sf);
			Session session = Session.getDefaultInstance(properties, new Authenticator() {
				public PasswordAuthentication getPasswordAuthentication() { // 
					return new PasswordAuthentication(mailAccount.getAccount(), mailAccount.getPassword()); 
				}
			});
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(mailAccount.getFrom()));
			InternetAddress[] internetAddresses = new InternetAddress[tos.size()];
			Object[] tosArray = tos.toArray();
			for (int i=0;i<tosArray.length;i++) {
				internetAddresses[i] = new InternetAddress(tosArray[i].toString());
			}
			message.addRecipients(Message.RecipientType.TO,internetAddresses);
			message.setSubject(subject);
			
			
			BodyPart fileBodyPart = null;
			if(!CollectionUtils.isEmpty(files)) {
				//
				Multipart multipart = new MimeMultipart();
				//消息部分
				BodyPart body = new MimeBodyPart();
				body.setContent(content, String.format("text/"+(isHtml ? "html" : "plain")+"; charset="+CHARSET));
				multipart.addBodyPart(body);
				//附件部分
				for (File file : files) {
					fileBodyPart = new MimeBodyPart();
					fileBodyPart.setDataHandler(new DataHandler(new FileDataSource(file)));
					fileBodyPart.setFileName(MimeUtility.encodeText(file.getName()));
					multipart.addBodyPart(fileBodyPart);
				}
				message.setContent(multipart);
			}else {
				message.setContent(content, String.format("text/"+(isHtml ? "html" : "plain")+"; charset="+CHARSET));
			}
			Transport.send(message);
			return message.getMessageID();
		} catch (Exception e) {
			log.error("邮件发送失败，【mailAccount："+mailAccount+",tos:"+tos+",subject:"+subject+",content:"+content+"】");
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param str
	 * @return
	 */
	private static Collection<String> splitToCollection(String str){
		if(StringUtils.isBlank(str)) {
			return null;
		}
		if(str.contains(",")) {
			return Arrays.asList(str.split(","));
		}
		if(str.contains(";")) {
			return Arrays.asList(str.split(";"));
		}
		return Arrays.asList(str);
	}
	/**
	 * 账号主体
	 * @author reyco
	 * @date 2022.03.30
	 * @version v1.0.1
	 */
	public static class MailAccount extends ToString{
		/**
		 * 
		 */
		private static final long serialVersionUID = -1691409713420645901L;
		private String host = "smtp.163.com";
		private Integer port = 465;
		private Boolean auth = true;
		private String from = "18307200213@163.com";
		private String account = "18307200213@163.com";
		private String password = "YYDDLLMRGCJVSUYJ";
		private Boolean sslEnable = true;
		public MailAccount() {
		}
		public MailAccount(Map<String,Object> map) {
			this.host = StringUtils.trim(map.get("host").toString());
			this.port = NumberUtils.toInt(map.get("port").toString());
			this.auth = BooleanUtils.toBooleanObject(map.get("auth").toString());
			this.from = map.get("from").toString();
			this.account = map.get("account").toString();
			this.password = map.get("password").toString();
			this.sslEnable = BooleanUtils.toBooleanObject(map.get("sslEnable").toString());
		}
		public MailAccount(Properties properties) {
			this.host = StringUtils.trim(properties.get("host").toString());
			this.port = NumberUtils.toInt(properties.get("port").toString());
			this.auth = BooleanUtils.toBooleanObject(properties.get("auth").toString());
			this.from = properties.get("from").toString();
			this.account = properties.get("account").toString();
			this.password = properties.get("password").toString();
			this.sslEnable = BooleanUtils.toBooleanObject(properties.get("sslEnable").toString());
		}
		public MailAccount(String host, Integer port, Boolean auth, String from, String account, String password,
				Boolean sslEnable) {
			super();
			this.host = host;
			this.port = port;
			this.auth = auth;
			this.from = from;
			this.account = account;
			this.password = password;
			this.sslEnable = sslEnable;
		}
		public String getHost() {
			return host;
		}

		public void setHost(String host) {
			this.host = host;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public Boolean getAuth() {
			return auth;
		}

		public void setAuth(Boolean auth) {
			this.auth = auth;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getAccount() {
			return account;
		}

		public void setAccount(String account) {
			this.account = account;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public Boolean getSslEnable() {
			return sslEnable;
		}

		public void setSslEnable(Boolean sslEnable) {
			this.sslEnable = sslEnable;
		}
	}
}
