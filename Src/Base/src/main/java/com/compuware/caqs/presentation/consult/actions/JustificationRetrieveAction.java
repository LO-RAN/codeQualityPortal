package com.compuware.caqs.presentation.consult.actions;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.domain.dataschemas.JustificationBean;
import com.compuware.caqs.exception.CaqsException;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.JustificationSvc;
import com.compuware.caqs.util.RequestHelper;
import java.util.Locale;

public final class JustificationRetrieveAction extends Action {


    // --------------------------------------------------------- Public Methods

    @Override
    public ActionForward execute(ActionMapping mapping,
                                 ActionForm form,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

    	ActionForward forward = mapping.findForward("success");
    	
        String page = RequestHelper.getParameterAndForward(request, "page");

    	if (page != null) {
    		forward = executeDemand(mapping, form, request, response);
    	}
    	else {
    		forward = executeValidation(mapping, form, request, response);
    	}
    	return forward;
    }        

    public ActionForward executeDemand(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
    	Locale loc = RequestUtil.getLocale(request);
		ActionForward forward = mapping.findForward("successDemand");

        RequestHelper.forwardParameter(request, "id_elt");
	    RequestHelper.forwardParameter(request, "id_telt");
	    RequestHelper.forwardParameter(request, "sub_elt");
	    RequestHelper.forwardParameter(request, "id_pere");
	    RequestHelper.forwardParameter(request, "lib_elt");
	    RequestHelper.forwardParameter(request, "id_bline");
	    RequestHelper.forwardParameter(request, "id_pro");
	    RequestHelper.forwardParameter(request, "id_crit");
	    RequestHelper.forwardParameter(request, "id_fac");
	    RequestHelper.forwardParameter(request, "id_met");
	    
	    String idJust = RequestHelper.getParameterAndForward(request, "id_just");
        RequestHelper.forwardParameter(request, "notecalc");
        try {
            String notecalc = (String)request.getParameter("notecalc");
            Double d = Double.parseDouble(notecalc);
            request.setAttribute("notecalc", StringFormatUtil.getMarkFormatter(loc).format(d.doubleValue()));
        } catch(NumberFormatException e) {
        
        }
        String nj = RequestHelper.getParameterAndForward(request, "notejust");
	    RequestHelper.forwardParameter(request, "only_crit");
	    RequestHelper.forwardParameter(request, "crit_list");

		double notejust = 0.0;
	    Double notejustobj = StringFormatUtil.parseDecimal(nj);
		if (notejustobj != null) {
			notejust = notejustobj.doubleValue();
		}
	    
	    try {
	    	JustificationSvc justifSvc = JustificationSvc.getInstance();
	        JustificationBean justif = justifSvc.getJustificatif(idJust, notejust);
		    if (justif == null) {
		        justif = new JustificationBean();
		    }
	        request.setAttribute("justificatif", justif);
	    }
	    catch (CaqsException e) {
	    	forward = mapping.findForward("failure");
	    }
	    
	    return forward;
	
	}

    public ActionForward executeValidation(ActionMapping mapping,
                ActionForm form,
                HttpServletRequest request,
                HttpServletResponse response) {
    	ActionForward forward = mapping.findForward("successValid");

    	RequestHelper.forwardParameter(request, "req");
        String idElt = RequestHelper.getParameterAndForward(request, "id_elt");
        if(idElt != null) {
            ElementBean eb = ElementSvc.getInstance().retrieveElementById(idElt);
            if(eb != null && eb.getProject()!=null) {
                request.setAttribute("libPro", eb.getProject().getLib());
            }
            if(!ElementType.EA.equals(eb.getTypeElt())) {
                ElementBean ea = ElementSvc.getInstance().retrieveApplicationEntityByElementId(idElt);
                if(ea != null) {
                    request.setAttribute("libEA", ea.getLib());
                }
            }
        }

        String dbtable = RequestHelper.getParameterAndForward(request, "dbtable");
        RequestHelper.forwardParameter(request, "lib_elt");
        String idJust = RequestHelper.getParameterAndForward(request, "id_just");
        String idCritfac = RequestHelper.getParameterAndForward(request, "id_critfac");
        String idBline = RequestHelper.getParameterAndForward(request, "id_bline");

        try {
	    	JustificationSvc justifSvc = JustificationSvc.getInstance();
	        double[] justifiedNoteArray = justifSvc.getJustifiedNote(idElt, idCritfac, idBline, dbtable);
	        request.setAttribute("justifiedNoteArray", justifiedNoteArray);

	        JustificationBean justif = justifSvc.getJustificatif(idJust, justifiedNoteArray[1]);
	        request.setAttribute("justificatif", justif);
        }
        catch (CaqsException e) {
        	forward = mapping.findForward("failure");
        }
        
        return forward;

    }

}
