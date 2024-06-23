package com.reyco.dasbx.es.core.event;

import java.io.Serializable;
import java.util.List;

import org.springframework.context.ApplicationEvent;

public class ElasticSearcchErrorEvent extends ApplicationEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 408672255886173224L;
	
	private List<ProcessFailureMessage> processFailureMessages;
	
	public ElasticSearcchErrorEvent(Object source,List<ProcessFailureMessage> processFailureMessages) {
		super(source);
		this.processFailureMessages = processFailureMessages;
	}
	public List<ProcessFailureMessage> getProcessFailureMessages() {
		return processFailureMessages;
	}

	public void setProcessFailureMessages(List<ProcessFailureMessage> processFailureMessages) {
		this.processFailureMessages = processFailureMessages;
	}
	public static class ProcessFailureMessage implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = -7450030831342231273L;
		/**
		 * 
		 */
		private String index;
		private String type;
		private String primarykey;
		private String failureMessage;
		public String getIndex() {
			return index;
		}
		public void setIndex(String index) {
			this.index = index;
		}
		public String getType() {
			return type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getPrimarykey() {
			return primarykey;
		}
		public void setPrimarykey(String primarykey) {
			this.primarykey = primarykey;
		}
		public String getFailureMessage() {
			return failureMessage;
		}
		public void setFailureMessage(String failureMessage) {
			this.failureMessage = failureMessage;
		}
		@Override
		public String toString() {
			return "ProcessFailureMessage [index=" + index + ", type=" + type + ", primarykey=" + primarykey
					+ ", failureMessage=" + failureMessage + "]";
		}
	}
}
