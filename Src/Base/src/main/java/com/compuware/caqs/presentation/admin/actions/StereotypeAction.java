/**
*  Created by Struts Assistant.
*  Date: 07.02.2006  Time: 10:06:01
*/

package com.compuware.caqs.presentation.admin.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.Action;

import com.compuware.caqs.domain.dataschemas.StereotypeBean;
import com.compuware.caqs.presentation.admin.forms.StereotypeForm;

public class StereotypeAction extends Action {

    protected static final String FORWARD_SUCCESS = "success";

    public static final String CURRENT_EA = "currentAdminEa";

    protected String getAndForward(String key, HttpServletRequest request) {
        String result = null;
        if (key != null && request != null) {
            result = request.getParameter(key);
            if (result == null) {
                result = (String)request.getAttribute(key);
            }
            request.setAttribute(key, result);
        }
        return result;
    }

    protected StereotypeBean formToBean(StereotypeForm stereotypeForm) {
        StereotypeBean stereotypeBean = new StereotypeBean();
        stereotypeBean.setId(stereotypeForm.getId());
        stereotypeBean.setLib(stereotypeForm.getLib());
        stereotypeBean.setDesc(stereotypeForm.getDesc());
        return stereotypeBean;
    }

    protected StereotypeForm beanToForm(StereotypeBean stereotypeBean) {
        StereotypeForm stereotypeForm = new StereotypeForm();
        stereotypeForm.setId(stereotypeBean.getId());
        stereotypeForm.setLib(stereotypeBean.getLib());
        stereotypeForm.setDesc(stereotypeBean.getDesc());
        return stereotypeForm;
    }

}
