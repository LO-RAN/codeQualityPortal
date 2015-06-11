/**
 * 
 */
package com.compuware.caqs.workflow.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cwfr-fdubois
 *
 */
public class ResultBean {

	private boolean success = false;
	
	private List<String> messageList = new ArrayList<String>();

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the messageList
	 */
	public List<String> getMessageList() {
		return messageList;
	}

	/** add a message to the message list.
	 * @param message the message to add.
	 */
	public void addMessage(String message) {
		this.messageList.add(message);
	}

}
