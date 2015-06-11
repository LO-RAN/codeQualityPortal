package com.compuware.caqs.presentation.admin.actions;

import com.compuware.caqs.business.analysis.SourceManager;
import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.ProjectBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.UserBean;
import com.compuware.caqs.exception.CaqsException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.presentation.common.ExtJSAjaxAction;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.security.auth.Users;
import com.compuware.caqs.service.AdminSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.ProjectSvc;
import com.compuware.caqs.util.CaqsConfigUtil;
import com.compuware.caqs.util.IDCreator;
import com.compuware.toolbox.io.FileTools;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.apache.struts.action.ActionForward;

public class SaveElementDataAction extends ExtJSAjaxAction {

    public static final int NO_ACTION = 0;
    public static final int UPDATE_ACTION = 2;
    public static final int CANCEL_ACTION = 4;
    public static final int APPLY_CHILDREN_ACTION = 5;

    private ElementBean getElementFromJSON(JSONObject obj) {
        ElementBean retour = new ElementBean();
        retour.setId(obj.getString("id"));
        String lib = obj.getString("lib");
        retour.setLib(lib.toUpperCase());
        retour.setDesc(obj.getString("desc"));
        retour.setPoids(obj.getInt("weight"));
        retour.setInfo1(obj.getString("info1"));
        retour.setInfo2(obj.getString("info2"));
        retour.setTypeElt(obj.getString("telt"));
        if (obj.get("idDialecte") != null
                && !"".equals(obj.getString("idDialecte"))) {
            retour.setDialecte(new DialecteBean(obj.getString("idDialecte")));
        }
        if (obj.get("idModel") != null && !"".equals(obj.getString("idModel"))) {
            retour.setUsage(new UsageBean(obj.getString("idModel")));
        }
        if (obj.get("scmRepository") != null
                && !"".equals(obj.getString("scmRepository"))) {
            retour.setScmRepository(obj.getString("scmRepository"));
        }
        if (obj.get("scmModule") != null
                && !"".equals(obj.getString("scmModule"))) {
            retour.setScmModule(obj.getString("scmModule"));
        }
        if (obj.get("libraries") != null
                && !"".equals(obj.getString("libraries"))) {
            retour.setLibraries(obj.getString("libraries"));
        }
        if (obj.get("sourceDir") != null
                && !"".equals(obj.getString("sourceDir"))) {
            retour.setSourceDir(obj.getString("sourceDir"));
        }
        if (obj.get("projectFilePath") != null
                && !"".equals(obj.getString("projectFilePath"))) {
            retour.setProjectFilePath(obj.getString("projectFilePath"));
        }
        if (obj.get("scmModule") != null
                && !"".equals(obj.getString("scmModule"))) {
            retour.setScmModule(obj.getString("scmModule"));
        }
        return retour;
    }

    public JSONObject retrieveDatas(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) {
        JSONObject retour = new JSONObject();
        String elementJSONData = request.getParameter("element");
        JSONObject obj = JSONObject.fromObject(elementJSONData);
        int action = RequestUtil.getIntParam(request, "typeAction", SaveElementDataAction.NO_ACTION);
        ElementBean elt = this.getElementFromJSON(obj);
        Users user = RequestUtil.getConnectedUser(request);
        boolean canModifyDomains = user.access("ADMIN_DOMAIN_MODIFICATION");
        boolean canModifyAllProjets = user.access("ALL_PROJECT_ADMIN");
        boolean success = false;

        switch (action) {
            case SaveElementDataAction.UPDATE_ACTION:
            case SaveElementDataAction.APPLY_CHILDREN_ACTION:
                if (ElementType.EA.equals(elt.getTypeElt())) {
                    //forward = saveSrcFile(eltForm, mapping, errors, request);
                }
                boolean isInCache = true;

                String userList = obj.getString("users");
                String[] userArray = userList.split(",");

                if (elt.getId() == null || "".equals(elt.getId())) {
                    //mode creation
                    String fatherId = obj.getString("fatherId");
                    String idElt = IDCreator.getID();
                    elt.setId(idElt);
                    if (!ElementType.DOMAIN.equals(elt.getTypeElt())) {
                        if (ElementType.PRJ.equals(elt.getTypeElt())) {
                            String idPro = IDCreator.getID();
                            ProjectBean pb = new ProjectBean();
                            pb.setId(idPro);
                            elt.setProject(pb);
                            AdminSvc.getInstance().generateProject(idPro, elt.getLib(), elt.getDesc());
                            AdminSvc.getInstance().createBaseLineInstanciation(idPro);
                        } else {
                            ElementBean fatherBean = AdminSvc.getInstance().retrieveElementById(fatherId);
                            elt.setProject(fatherBean.getProject());
                        }
                    } else {
                        ProjectBean pb = new ProjectBean();
                        pb.setId(ElementType.ENTRYPOINT);
                        elt.setProject(pb);
                    }
                    AdminSvc.getInstance().setElement(elt);
                    AdminSvc.getInstance().setTreeElementLink(elt, fatherId);
                    retour.put("beenCreated", "true");
                    retour.put("fatherId", fatherId);
                    retour.put("id", elt.getId());
                    isInCache = false;
                } else {
                    //mode mise a jour
                    if (ElementType.EA.equals(elt.getTypeElt())
                            || ElementType.PRJ.equals(elt.getTypeElt())) {
                        this.majSourceFilesDir(elt.getId(), elt);
                    }
                    AdminSvc.getInstance().setElement(elt);
                    if (action == SaveElementDataAction.APPLY_CHILDREN_ACTION) {
                        AdminSvc.getInstance().updateChildrenRights(elt.getId(), userList, user.getId(), canModifyDomains, canModifyAllProjets);
                    }
                    if (ElementType.PRJ.equals(elt.getTypeElt())) {
                        String idPro = request.getParameter("idPro");
                        if (idPro != null) {
                            AdminSvc.getInstance().updateProject(idPro, elt.getLib(), elt.getDesc());
                        }
                    }
                    retour.put("beenUpdated", "true");
                    this.removeRightsToChildren(userArray, elt, user.getId(), canModifyDomains, canModifyAllProjets);
                }
                request.setAttribute("id_elt", elt.getId());

                AdminSvc.getInstance().updateRights(elt.getId(), userArray);

                if (initializeEaStructure(elt)) {
                    if (isInCache) {
                        this.removeProjectFromCache(elt);
                    }
                    success = true;
                }
                break;
        }

        return retour;
    }

    private void removeRightsToChildren(String[] newRights, ElementBean elt, String userId,
            boolean canModifyDomains, boolean canModifyAllProjets) {
        AdminSvc adminSvc = AdminSvc.getInstance();
        List<UserBean> oldEltUserCollection = adminSvc.retrieveAllUsersByElementId(elt.getId());
        if (oldEltUserCollection != null && !oldEltUserCollection.isEmpty()) {
            List<String> removeList = new ArrayList<String>();
            for (Iterator<UserBean> it = oldEltUserCollection.iterator(); it.hasNext();) {
                UserBean ub = it.next();
                boolean found = false;
                for (int i = 0; i < newRights.length; i++) {
                    if (newRights[i].equals(ub.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    removeList.add(ub.getId());
                }
            }
            //il ne reste que les droits supprim�s
            if (!removeList.isEmpty()) {
                String[] rights = removeList.toArray(new String[0]);
                adminSvc.removeRightsToChildren(elt.getId(), rights, userId, canModifyDomains, canModifyAllProjets);
            }
        }
    }
    private static final String EXTRACT_SRC_FILES_TARGET = "extractSrc";

    private ActionForward saveSrcFile(ElementBean elt, HttpServletRequest request) {
        ActionForward result = null;
        boolean uploadOK = true;
        /*if (elt != null && elt.getFile() != null && elt.getSourceDir()
        != null) {
        FormFile f = elt.getFile();
        if (f.getFileSize() > 0) {
        try {
        f.getFileData();
        String baseDir = getSourcePath(elt.getSourceDir());
        File dir = new File(baseDir);
        if (!dir.exists()) {
        dir.mkdirs();
        }
        File tmpFile = new File(dir, "src.zip");
        FileOutputStream writer = new FileOutputStream(tmpFile);
        writer.write(f.getFileData());
        writer.flush();
        writer.close();
        AntExecutor command = new AntExecutor(logger);
        command.processAntScript(EXTRACT_SRC_FILES_TARGET, getAntProperties(baseDir));
        File srcDir = new File(dir, "/src/");
        if (!srcDir.exists()) {
        result = mapping.findForward("uploadFailed");
        errors.add("Element.src", new ActionMessage("caqs.element.upload.src.nosrcdir"));
        uploadOK = false;
        }
        } catch (FileNotFoundException e) {
        logger.error("Source upload file not found", e);
        errors.add("Element.src", new ActionMessage("caqs.element.upload.src.filenotfound", "Element.src"));
        uploadOK = false;
        } catch (IOException e) {
        logger.error("Error getting source upload file data", e);
        errors.add("Element.src", new ActionMessage("caqs.element.upload.src.ioexception", "Element.src"));
        uploadOK = false;
        }
        request.setAttribute("uploadStatus", new Boolean(uploadOK));
        }
        }*/
        return result;
    }

    private String getSourcePath(String relativePath) {
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String basePath = null;
        try {
            basePath = new File(dynProp.getProperty(Constants.SRC_BASE_PATH)).getCanonicalPath();
        } catch (IOException ex) {
            mLog.error("Can't get full path : " + ex.getMessage());
        }
        String result = relativePath;
        if (basePath != null) {
            result = FileTools.concatPath(basePath, result);
        }
        return result;
    }

    private Properties getAntProperties(String srcPath) {
        Properties result = new Properties();
        result.put("SRC_DIR", srcPath);
        return result;
    }

    public void removeProjectFromCache(ElementBean eltBean) {
        String idPro = "";
        AdminSvc adminSvc = AdminSvc.getInstance();
        if (eltBean != null) {
            if (eltBean.getProject() != null) {
                idPro = eltBean.getProject().getId();
            } else {
                ElementBean elt = adminSvc.retrieveElementById(eltBean.getId());
                if (elt.getProject() != null) {
                    idPro = elt.getProject().getId();
                }
            }
            adminSvc.clearCache(idPro);
        }
    }

    private void majSourceFilesDir(String idElt, ElementBean newElt) {
        //mise a jour des repertoires de sources au format HTML
        Properties dynProp = CaqsConfigUtil.getCaqsGlobalConfigProperties();
        String dataDir = dynProp.getProperty(Constants.WEB_DATA_DIRECTORY_KEY);
        File dataDirFile = new File(dataDir);

        ElementSvc eltSvc = ElementSvc.getInstance();
        ElementBean oldEltBean = eltSvc.retrieveElementById(idElt);

        //recuperation du projet
        ProjectSvc projectFacade = ProjectSvc.getInstance();
        ProjectBean pb = projectFacade.retrieveProjectById(oldEltBean.getProject().getId());

        File projectDir = new File(dataDirFile, pb.getLib() + "-" + pb.getId());

        if (ElementType.PRJ.equals(oldEltBean.getTypeElt())) {
            if (projectDir.exists()) {
                File newdir = new File(dataDirFile, newElt.getLib() + "-"
                        + pb.getId());
                projectDir.renameTo(newdir);
            }
        } else if (ElementType.EA.equals(oldEltBean.getTypeElt())) {
            File eaDir = new File(projectDir, oldEltBean.getLib() + "-"
                    + oldEltBean.getId());
            if (eaDir.exists()) {
                File newdir = new File(projectDir, newElt.getLib() + "-"
                        + newElt.getId());
                eaDir.renameTo(newdir);
            }
        }
    }
    private static final String INIT_EA_TARGET = "initEA";

    private boolean initializeEaStructure(ElementBean eltBean) {
        boolean retour = false;
        if (eltBean != null && ElementType.EA.equals(eltBean.getTypeElt())) {
            try {
                SourceManager manager = new SourceManager();
                manager.manageEa(eltBean.getId(), INIT_EA_TARGET);
                retour = true;
            } catch (IOException e) {
                mLog.error("Error during module structure initialization", e);
            } catch (CaqsException e) {
                mLog.error("Error during module structure initialization", e);
            }
        }
        return retour;
    }
}
