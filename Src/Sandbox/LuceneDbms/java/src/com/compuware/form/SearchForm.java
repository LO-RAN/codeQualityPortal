package com.compuware.form;

/**
 *
 *
 */

import java.util.*;
import org.apache.struts.action.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public final class SearchForm extends ActionForm  {

    private String filter;

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getFilter() {
        return this.filter;
    }

    /**
     * The constructor
     */
    public SearchForm() {
    }

    /**
     * The constructor
     */
    public SearchForm(HttpServletRequest request) {
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
        this.setFilter("");
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

        if (filter == null || filter.length() < 1)
            errors.add("Search.filter", new ActionError("message.field.required", "Search.filter"));
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
        setFilter("");
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
