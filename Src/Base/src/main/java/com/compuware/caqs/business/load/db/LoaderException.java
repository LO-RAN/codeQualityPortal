package com.compuware.caqs.business.load.db;

import com.compuware.caqs.exception.CaqsException;

/**
 * @author cwfr-fdubois
 *
 */
public class LoaderException extends CaqsException {

	private static final long serialVersionUID = -2225169358974477176L;

	public LoaderException(String message) {
		super(message);
	}

	public LoaderException(String msgKey, String defaultMessage) {
		super(msgKey, defaultMessage);
	}

	public LoaderException(String msgKey, String defaultMessage, Throwable e) {
		super(msgKey, defaultMessage, e);
	}

	public LoaderException(String message, Throwable e) {
		super(message, e);
	}
	
	public LoaderException(Throwable e) {
		super(e);
	}
	
}
