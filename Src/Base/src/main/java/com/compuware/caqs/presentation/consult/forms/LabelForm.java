package com.compuware.caqs.presentation.consult.forms;

/**
 *
 *
 */

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class LabelForm extends ActionForm  {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7738142431478975654L;

	private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    
    /**
     *  id
     */
    private String id;

    /**
     * Set id_label
     */
    public void setId(String value) {

        this.id = value;
    }

    /**
     * Get geo_zonesCode
     */
    public String getId() {

        return this.id;
    }

    /**
     *  libellé
     */
    private String lib;

    /**
     * Set libellé
     */
    public void setLib(String value) {

        this.lib = value;
    }

    /**
     * Get libellé
     */
    public String getLib() {

        return this.lib;
    }
    
    public String getEscapedLib(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getLib(), quote, bracket, doubleQuote);
    }

    /**
     *  description
     */
    private String desc;

    /**
     * Set description
     */
    public void setDesc(String value) {

        this.desc = value;
    }

    public String getEscapedDesc(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getDesc(), quote, bracket, doubleQuote);
    }

    /**
     * Get description
     */
    public String getDesc() {

        return this.desc;
    }

    /**
     *  Utilisateur
     */
    private String user;

    /**
     * Set Utilisateur
     */
    public void setUser(String value) {
        this.user = value;
    }

    /**
     * Get Utilisateur
     */
    public String getUser() {
        return this.user;
    }

    /**
     *  Utilisateur
     */
    private String status;

    /**
     * Set Utilisateur
     */
    public void setStatus(String value) {
        this.status = value;
    }

    /**
     * Get Utilisateur
     */
    public String getStatus() {
        return this.status;
    }
    
    private String dinst = null;
    
    public void setDinst(String value) {
        this.dinst = value;
    }
    
    public String getDinst() {
        return this.dinst;
    }

    private String dmaj = null;
    
    public void setDmaj(String value) {
        this.dmaj = value;
    }
    
    public String getDmaj() {
        return this.dmaj;
    }

    /*************************** DEMAND label if exists ********************************/
    /**
     *  Demand label.
     */
    private LabelForm demand = null;

    /**
     * Set demandId
     */
    public void setDemand(LabelForm value) {

        this.demand = value;
    }

    /**
     * Get demand
     */
    public LabelForm getDemand() {
        return this.demand;
    }
    
    /**
     * The constructor
     */
    public LabelForm() {
    	this.demand = new LabelForm(false);
    }

    public LabelForm(boolean initDemand) {
    	if (initDemand) {
    		this.demand = new LabelForm();
    	}
    }
    
    /**
     * The constructor
     */
    public LabelForm(HttpServletRequest request) {
        init(request);
    }

    /**
     * Initialize all properties to their default values.
     * If a initial value is specified in the model, it'll be used.
     * Otherwise, for types like date, a default value will be set,
     * for instance the current date.
     *
     */
    public void init(HttpServletRequest request) {
        this.setId(IDCreator.getID());
        this.setLib("");
        this.setDesc("");
        this.setUser(RequestUtil.getConnectedUserId(request));
        this.setStatus("DEMAND");
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

        validateRequired(id, errors, "Label.id");
        validateRequired(lib, errors, "Label.lib");
        validateRequired(desc, errors, "Label.desc");

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

    public Locale getLocale(HttpServletRequest request) {

	    HttpSession session = request.getSession();
	    Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
	    if (locale == null) {
	        locale = Locale.getDefault();
	    }
	    return locale;
    }

    /* include your methods here */

}
