/*
 * © 2002 Compuware Corporation. All rights reserved.
 * Unpublished - rights reserved under the Copyright Laws of the United States.
 */

/* Disclaimer
 * You have a royalty-free right to use, modify, reproduce and distribute this 
 * sample code (and/or any modified version) in any way you find useful,
 * provided that you agree that Compuware has no warranty obligations or 
 * liability for any sample code which has been modified.
 */
/*
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999-2001 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Struts", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package com.compuware.optimal.flow.webconsole;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;


/**
 * Form bean for the History TaskList page.
 * This form has the following properties:
 *
 * <ul>
 * <li><b>filterselect</b> - The filter option to select
 * <li><b>filtervalue</b>  - Entered filter value
 * <li><b>sorterselect</b> - The sort option to select
 * <li><b>stepsize</b>     - Entered step size value
 * </ul>
 *
 * The step size, selected filter option, filter value and selected
 * sort option are set on the list objects.
 */

public final class HistoryTaskListForm extends ActionForm {


    // --------------------------------------------------- Instance Variables


    /**
     * The filterselect
     *
     * The filterselect is related to a selected filter option.
     */
    private String filterselect = "none";

    /**
     * The filtervalue
     *
     * The filtervalue to use by the selected filter option.
     */
    private String filtervalue = null;

    /**
     * The sortselect
     *
     * The sortselect is related to a selected sort option.
     */
    private String sortselect = "none";

    /**
     * The stepsize
     *
     * The stepsize is related to the number of objects returned by the list
     * objects of the WorkflowConnector.
     */
    private String stepsize = null;

    // ----------------------------------------------------------- Properties


    /**
     * Return the selected filter.
     */
    public String getFilterselect() {

	return (this.filterselect);

    }


    /**
     * Set the filterselect.
     *
     * @param filterselect Selected filter option.
     */
    public void setFilterselect(String filterselect) {

        this.filterselect = filterselect;

    }

    /**
     * Return the filter value.
     */
    public String getFiltervalue() {

	return (this.filtervalue);

    }


    /**
     * Set the value of the filter.
     *
     * @param filtervalue. New filter value.
     */
    public void setFiltervalue(String filtervalue) {

        this.filtervalue = filtervalue;

    }

    /**
     * Return the selected sort order.
     */
    public String getSortselect() {

	return (this.sortselect);

    }


    /**
     * Set the sortselect.
     *
     * @param sortselect Selected sort option
     */
    public void setSortselect(String sortselect) {

        this.sortselect = sortselect;

    }

    /**
     * Return the step size.
     */
    public String getStepsize() {

	return (this.stepsize);

    }


    /**
     * Set the stepsize.
     *
     * @param stepsize New step size
     */
    public void setStepsize(String stepsize) {

        this.stepsize = stepsize;

    }

    // --------------------------------------------------------- Public Methods


    /**
     * Reset all properties to their default values.
     *
     * @param mapping Mapping used to select this instance.
     * @param request Servlet request being processed.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request) {

        this.filterselect = "none";
        this.filtervalue  = null;
        this.sortselect   = "none";
        this.stepsize     = "10";

    }


    /**
     * Validate the properties that have been set from this HTTP request,
     * and return an <code>ActionErrors</code> object that encapsulates any
     * validation errors that have been found.  If no errors are found, return
     * <code>null</code> or an <code>ActionErrors</code> object with no
     * recorded error messages.
     *
     * @param mapping Mapping used to select this instance.
     * @param request Servlet request being processed.
     */
    public ActionErrors validate(ActionMapping mapping,
                                 HttpServletRequest request) {

        ActionErrors errors = new ActionErrors();
        if ((stepsize == null) || (stepsize.length() < 1))
            errors.add("stepsize", new ActionError("error.stepsize.required"));
        // check if stepsize has numbers only
        try {
            Integer i = new Integer(stepsize);
		}
		catch (NumberFormatException e) {
			errors.add("stepsizenotanumber", new ActionError("error.stepsize.notanumber"));
		}

        // more validation code can be added here...

        return errors;

    }


}
