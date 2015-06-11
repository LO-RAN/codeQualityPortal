package com.compuware.caqs.domain.dataschemas.tasks;

import java.util.List;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

public class MessageStatusBean implements Internationalizable {
	private MessageStatus 	id;
	private TaskId			taskId;
	
	private MessageStatusBean() {}
	
	public MessageStatusBean(MessageStatus i, TaskId t) {
		this();
		this.id = i;
		this.taskId = t;
	}

	public String getComplement(Locale loc) {
		return null;
	}

	public String getDesc(Locale loc) {
		return null;
	}

	public String getId() {
		return this.taskId+"_"+this.id.toString();
	}

	public String getLib(Locale loc) {
		return DbmsResourceBundle.getString(this, this.getBundleName(),
				Internationalizable.LIB_PROPERTY_KEY, loc);
	}
	
	public String getLib(List<String> args, Locale loc) {
		return DbmsResourceBundle.getString(this, this.getBundleName(),
				Internationalizable.LIB_PROPERTY_KEY, args, loc);
	}

	public String getTextKey(String columnName) {
		return this.getBundleName() + Internationalizable.KEY_SEPARATOR +
			columnName + Internationalizable.KEY_SEPARATOR + getId();
	}

    public String getBundleName() {
        return I18nUtil.MESSAGE_STATUS_BUNDLE_NAME;
    }
}
