package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.IOException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.LabelBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.consult.forms.LabelForm;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.LabelSvc;
import com.compuware.caqs.util.RequestHelper;

public final class LabelSelectAction extends Action {
    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    // --------------------------------------------------------- Public Methods

    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response)
            throws IOException, ServletException {

        ActionForward forward = null;
        LabelForm formBean = (LabelForm) form;

        forward = doRetrieve(mapping, request, formBean);

        if (forward == null) {
	        String req = RequestHelper.getParameterAndForward(request, "req");
	        if (req != null) {
	        	forward = mapping.findForward("successFromLabellisation");
	        }
	        else {
	        	forward = mapping.findForward("success");
	        }
        }
        
        return forward;	
    }

    private ActionForward doRetrieve(ActionMapping mapping,
                                     HttpServletRequest request,
                                     LabelForm formBean) {
        return retrieveLabel(mapping, request, formBean);
    }

    private ActionForward retrieveLabel(ActionMapping mapping,
                                        HttpServletRequest request,
                                        LabelForm formBean) {
    	ActionForward result = null;
        String idLabel = request.getParameter("id_label");
        if(idLabel==null) {
        	idLabel = (String)request.getAttribute("id_label");
        }
        String idElt = request.getParameter("id_elt");
        if(idElt==null) {
        	idElt = (String)request.getAttribute("id_elt");
        }
        String idBline = request.getParameter("id_bline");
        if(idBline==null) {
        	idBline = (String)request.getAttribute("id_bline");
        }
        try {
	        LabelSvc labelSvc = LabelSvc.getInstance();
	        LabelBean labelBean = labelSvc.retrieveLabel(idLabel);
	        beanToForm(labelBean, formBean, request);
	        if (idElt != null && idBline != null) {
	        	ElementBean labelElement = ElementSvc.getInstance().retrieveElement(idElt, idBline);
	        	request.setAttribute("labelElement", labelElement);
	        }
        }
        catch (CaqsException e) {
            logger.error("error while retrieving label", e);
        	result = mapping.findForward("failure");
        }
        return result;
    }
    
    private void beanToForm(LabelBean bean, LabelForm form, HttpServletRequest request) {
    	if (bean != null && form != null) {
	        boolean isValidation = request.getParameter("req") != null;
        	if (isValidation && (bean.getLabelLink() == null)) {
        		form.init(request);
	    		form.setUser(RequestUtil.getConnectedUserId(request));
	    		form.setLib(bean.getLib());
	    		form.setDinst(RequestUtil.formatDate(new Date(), request));
	    		LabelForm demand = new LabelForm();
	    		demand.setId(bean.getId());
	    		demand.setLib(bean.getLib());
	    		demand.setDesc(bean.getDesc());
	    		demand.setDinst(RequestUtil.formatDate(bean.getDinst(), request));
	    		demand.setDmaj(RequestUtil.formatDate(bean.getDmaj(), request));
	    		demand.setUser(bean.getUser());
	    		demand.setStatus(bean.getStatut());
	    		form.setDemand(demand);
        	}
        	else {
	    		form.setId(bean.getId());
	    		form.setLib(bean.getLib());
	    		form.setDesc(bean.getDesc());
	    		form.setDinst(RequestUtil.formatDate(bean.getDinst(), request));
	    		form.setDmaj(RequestUtil.formatDate(bean.getDmaj(), request));
	    		form.setUser(bean.getUser());
	    		form.setStatus(bean.getStatut());
	    		LabelForm demand = new LabelForm();
	    		LabelBean link = bean.getLabelLink();
	    		if (link != null) {
		    		demand.setId(link.getId());
		    		demand.setLib(link.getLib());
		    		demand.setDesc(link.getDesc());
		    		demand.setDinst(RequestUtil.formatDate(link.getDinst(), request));
		    		demand.setDmaj(RequestUtil.formatDate(link.getDmaj(), request));
		    		demand.setUser(link.getUser());
		    		demand.setStatus(link.getStatut());
	    		}
	    		form.setDemand(demand);
        	}
    	}
    }
}
