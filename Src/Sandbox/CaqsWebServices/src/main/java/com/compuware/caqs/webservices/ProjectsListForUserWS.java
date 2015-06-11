package com.compuware.caqs.webservices;

import com.compuware.caqs.domain.dataschemas.webservices.ProjectWS;
import java.util.List;
import javax.jws.WebService;
import javax.jws.WebMethod;

/**
 *
 * @author cwfr-dzysman
 */
@WebService
public interface ProjectsListForUserWS {

    @WebMethod
    public List<ProjectWS> getProjectsListForUser();
}
