package com.compuware.caqs.presentation.consult.forms;

/**
 *
 *
 */

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class QametriqueForm extends ActionForm  {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1070821026030277423L;

	private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    
    /** Element id. */
    private String id_elt;
    /** Element description. */
    private String desc_elt;
    /** Metrique id. */
    private String id_met;
    /** Metrique lib. */
    private String lib_met;
    /** Metrique value. */
    private String valbrute;

    /**
     * Set id_elt
     * @param id the element ID.
     */
    public void setIdElt(String id) {
        this.id_elt = id;
    }

    /**
     * Set desc_elt
     * @param desc the element description.
     */
    public void setDescElt(String desc) {
        this.desc_elt = desc;
    }

    /**
     * Set id_met
     * @param id the metric ID.
     */
    public void setIdMet(String id) {
        this.id_met = id;
    }

    /**
     * Set lib_met
     * @param lib the metric lib.
     */
    public void setLibMet(String lib) {
        this.lib_met = lib;
    }

    /**
     * Set valbrute
     * @param val the metric valbrute.
     */
    public void setValbrute(String val) {
        this.valbrute = val;
    }
    
    /**
     * Get id_elt
     * @return the element ID.
     */
    public String getIdElt() {
        return this.id_elt;
    }

    /**
     * Get desc_elt
     * @return the element description.
     */
    public String getDescElt() {
        return this.desc_elt;
    }

    /**
     * Get id_met
     * @return the metric ID.
     */
    public String getIdMet() {
        return this.id_met;
    }

    /**
     * Get lib_met
     * @return the metric lib.
     */
    public String getLibMet() {
        return this.lib_met;
    }

    /**
     * Get valbrute
     * @return the metric valbrute.
     */
    public String getValbrute() {
        return this.valbrute;
    }

    /**
     * Initialize all properties to their default values.
     * If a initial value is specified in the model, it'll be used.
     * Otherwise, for types like date, a default value will be set,
     * for instance the current date.
     *
     */
    public void init(HttpServletRequest request) {
        this.setIdElt("");
        this.setDescElt("");
        this.setIdMet("");
        this.setLibMet("");
        this.setValbrute("1");
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

        validateRequired(valbrute, errors, "Qametrique.valbrute");

        logger.info("Validate Formbean returns "+((errors.isEmpty())?"no errors":errors.size()+" ActionErrors"));

        return errors;
    }
    
    private void validateRequired(String attr, ActionErrors errors, String key) {
        if ((attr == null) || (attr.length() < 1)) {
            errors.add(key, new ActionMessage("message.field.required", key));
        }
    }

    /**
     * Reset all properties an empty string. This method is used by
     * Struts to make sure all formbean fields are empty.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        logger.info("resetting properties to their default values");


    }

    /* include your methods here */

}
