package com.compuware.form;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.Action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 21 nov. 2005
 * Time: 17:56:30
 * To change this template use File | Settings | File Templates.
 */
public class ResultForm {


    private String id;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }

    private String content;

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return this.content;
    }

    /**
     * The constructor
     */
    public ResultForm() {
    }

    /**
     * The constructor
     */
    public ResultForm(HttpServletRequest request) {
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
        this.setId("");
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

        return (errors);
    }

    /**
     * Reset all properties an empty string. This method is used by
     * Struts to make sure all formbean fields are empty.
     *
     * @param mapping The mapping used to select this instance
     * @param request The servlet request we are processing
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        setId("");
    }

    public Locale getLocale(HttpServletRequest request) {

    HttpSession session = request.getSession();
    Locale locale = (Locale) session.getAttribute(Action.LOCALE_KEY);
    if (locale == null)
        locale = Locale.getDefault();
    return (locale);
    }

    /* include your methods here */

}
