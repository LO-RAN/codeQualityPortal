package com.compuware.caqs.domain.dataschemas.tasks;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

public class TaskBean implements Serializable, Internationalizable {

    /**
     *
     */
    private static final long serialVersionUID = -6953237769008535078L;
    /**
     * Le type de la t�che : INFO, ERROR, WARNING, PROGRESS
     */
    private TaskType type = TaskType.NOT_DEFINED;
    /**
     * L'identifiant unique de la t�che
     */
    private TaskId id = null;
    /**
     *
     */
    private ShowUserTask showUser = ShowUserTask.NOT_DEFINED;

    /**
     * Gestion de l'internationalisation
     */
    /** Get the usage complement.
     * @param loc the locale 
     * @return the usage complement.
     */
    public String getComplement(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc);
    }

    /** Get the usage description.
     * @param loc the locale 
     * @return the usage description.
     */
    public String getDesc(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
    }

    /** Get the usage label.
     * @param loc the locale 
     * @return the usage label.
     */
    public String getLib(Locale loc, List<String> args) {
        String lib = null;
        if (args == null) {
            lib = DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
        } else {
            lib = DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, args, loc);
        }
        return lib;
    }

    public String getLib(Locale loc) {
        return null;
    }

    public String getTextKey(String columnName) {
        return this.getBundleName() + Internationalizable.KEY_SEPARATOR
                + columnName + Internationalizable.KEY_SEPARATOR + getId();
    }

    public TaskType getType() {
        return type;
    }

    public void setType(TaskType type) {
        this.type = type;
    }

    public String getId() {
        return id.toString();
    }

    public TaskId getTaskId() {
        return id;
    }

    public void setId(TaskId id) {
        this.id = id;
    }

    public ShowUserTask getShowUser() {
        return showUser;
    }

    public void setShowUser(ShowUserTask showUser) {
        this.showUser = showUser;
    }

    public String getBundleName() {
        return I18nUtil.TASK_BUNDLE_NAME;
    }
}
