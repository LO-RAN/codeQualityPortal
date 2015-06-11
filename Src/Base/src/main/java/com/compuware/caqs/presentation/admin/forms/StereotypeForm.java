package com.compuware.caqs.presentation.admin.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 8 févr. 2006
 * Time: 17:52:44
 * To change this template use File | Settings | File Templates.
 */
public class StereotypeForm extends ActionForm {

	private static final long serialVersionUID = -8549968552818368714L;

	private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private String id;
    private String lib;
    private String desc;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLib() {
        return lib;
    }

    public void setLib(String lib) {
        this.lib = lib;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */


    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors errors = new ActionErrors();
        if (id != null) {
            id = id.toUpperCase();
        }
        validateRequired(id, errors, "Stereotype.id");
        validateRequired(lib, errors, "Stereotype.lib");
        validateRequired(lib, errors, "Stereotype.desc");
        logger.info("Validate Formbean returns " + ((errors.isEmpty()) ? "no errors" : errors.size() + " ActionErrors"));
        return errors;
    }


    private void validateRequired(String attr, ActionErrors errors, String key) {
        if ((attr == null) || (attr.length() < 1)) {
            errors.add(key, new ActionMessage("message.field.required", key));
        }
    }
}
