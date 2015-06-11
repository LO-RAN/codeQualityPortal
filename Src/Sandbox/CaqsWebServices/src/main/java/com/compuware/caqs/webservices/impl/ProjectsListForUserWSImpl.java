package com.compuware.caqs.webservices.impl;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.webservices.ProjectWS;
import com.compuware.caqs.service.ElementSvc;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContextHolder;
import javax.jws.WebService;

/**
 *
 * @author cwfr-dzysman
 */
@WebService(//endpointInterface = "com.compuware.caqs.webservices.ProjectsListForUserWS",
serviceName = "ProjectsListForUserWS")
public class ProjectsListForUserWSImpl /*implements ProjectsListForUserWS */{

    public List<ProjectWS> getProjectsListForUser() {
        List<ProjectWS> retour = new ArrayList<ProjectWS>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            List<ElementBean> eas = ElementSvc.getInstance().retrieveAllElementsForTypeBelongingToParentByUser(ElementType.ENTRYPOINT, authentication.getName(), ElementType.EA);
            if(eas!=null) {
                for(ElementBean ea : eas) {
                    ProjectWS pws = new ProjectWS();
                    pws.setId(ea.getId());
                    pws.setLib(ea.getLib());
                    String fullPath = ElementSvc.getInstance().retrieveParentPathByLib(ea.getId());
                    if(!Constants.ELEMENT_PATH_SEPARATOR.equals(fullPath)) {
                        fullPath += Constants.ELEMENT_PATH_SEPARATOR;
                    }
                    fullPath += ea.getLib();
                    pws.setFullPath(fullPath);
                    retour.add(pws);
                }
            }
        }

        return retour;
    }

}
