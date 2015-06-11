package com.compuware.caqs.service;

import com.compuware.caqs.dao.factory.DaoFactory;
import com.compuware.caqs.dao.interfaces.ElementDao;
import com.compuware.caqs.dao.interfaces.MetriqueDao;
import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.DialecteBean;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.MetriqueBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.treemap.TreeMapElementBean;
import com.compuware.caqs.util.CaqsConfigUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 *
 * @author cwfr-dzysman
 */
public class TreeMapSvc {

    private static final TreeMapSvc instance = new TreeMapSvc();

    /**
     * Constructeur prive ==> singleton
     */
    private TreeMapSvc() {
    }

    public static final TreeMapSvc getInstance() {
        return instance;
    }

    /**
     *
     * @param root l'identifiant de l'element racine
     * @param leafType le type d'element qui sera une feuille
     * @param idFac l'identifiant de l'objectif pour lequel recuperer la note ou
     * Constants.ALL_FACTORS pour la note moyenne de tous les objectifs
     * @param idUser identifiant de l'utilisateur accredite
     * @return l'element racine avec sa note et sa hierarchie
     */
    public TreeMapElementBean retrieveMarkedTree(
            String root, String leafType, String idFac, String idUser) {
        ElementDao elementFacade = DaoFactory.getInstance().getElementDao();
        return elementFacade.retrieveMarkedTree(root, leafType, idFac, idUser);
    }

    /**
     *
     * @param root l'identifiant de l'element racine
     * @param leafType le type d'element qui sera une feuille
     * @param idFac l'identifiant de l'objectif pour lequel recuperer la note ou
     * Constants.ALL_FACTORS pour la note moyenne de tous les objectifs
     * @param idUser identifiant de l'utilisateur accredite
     * @return l'element racine avec sa note et sa hierarchie
     */
    public TreeMapElementBean retrieveProjectMarkedTree(
            String root, BaselineBean baseline, String idFac, String idUser) {
        ElementDao elementFacade = DaoFactory.getInstance().getElementDao();
        return elementFacade.retrieveProjectMarkedTree(root, baseline, idFac, idUser);
    }

    /**
     *
     * @param elt
     * @param leaf
     * @return
     */
    public double getAllCodeValue(TreeMapElementBean elt, String leaf) {
        double retour = 0.0;

        if (!elt.isAllCodeComputed()) {
            if (leaf.equals(elt.getTypeElt())) {
                //c'est une feuille, on recupere la metrique
                retour = MetricSvc.getInstance().getAllCodeValue(elt);
            } else {
                for (TreeMapElementBean child : elt.getChildren()) {
                    retour += this.getAllCodeValue(child, leaf);
                }
            }
            elt.setAllCode(retour);
        } else {
            retour = elt.getAllCode();
        }

        return retour;
    }

    public double getIFPUG(TreeMapElementBean elt, String leaf,
            List<ElementBean> eas) {
        double retour = 0.0;

        if (!elt.isIfpugComputed()) {
            if (leaf.equals(elt.getTypeElt())) {
                List<ElementBean> thoseEAs = this.getAllEAsForProject(elt, eas);
                //c'est une feuille, on recupere la metrique
                if (!thoseEAs.isEmpty()) {
                    retour = ElementSvc.getInstance().retrieveGlobalIFPUG(thoseEAs);
                }
            } else {
                for (TreeMapElementBean child : elt.getChildren()) {
                    retour += this.getIFPUG(child, leaf, eas);
                }
            }
            elt.setIfpug(retour);
        } else {
            retour = elt.getIfpug();
        }

        return retour;
    }

    public double getAverageVGValue(TreeMapElementBean elt, String leaf) {
        double retour = 0.0;

        if (!elt.isAvgVgComputed()) {
            if (leaf.equals(elt.getTypeElt())) {
                if (elt.getDialecte() == null) {
                    DialecteBean db = DialecteSvc.getInstance().retrieveDialecteByElementId(elt.getId());
                    elt.setDialecte(db);
                }
                String languageId = elt.getDialecte().getLangage().getId();
                Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
                String metId = prop.getProperty("vg." + languageId);
                if (metId == null) {
                    metId = "VG";
                }
                //c'est une feuille, on recupere la metrique
                MetriqueDao metriqueDao = DaoFactory.getInstance().getMetriqueDao();
                MetriqueBean mb = metriqueDao.retrieveAverageQametriqueFromMetEltBline(
                        elt.getBaseline().getId(), metId, elt.getProject().getId());
                retour = mb.getValbrute();
            } else {
                int nbElements = 0;
                for (TreeMapElementBean child : elt.getChildren()) {
                    double n = this.getAverageVGValue(child, leaf);
                    if (n > 0.0) {
                        retour += n * child.getPoids();
                        nbElements += child.getPoids();
                    }
                }
                if (nbElements > 0) {
                    retour = retour / nbElements;
                }
            }
            elt.setAvgVg(retour);
        } else {
            retour = elt.getAvgVg();
        }

        return retour;
    }

    public double getNbFileElements(TreeMapElementBean elt, String leaf,
            List<ElementBean> eas) {
        double retour = 0.0;

        if (!elt.isNbFileElementsComputed()) {
            if (leaf.equals(elt.getTypeElt())) {
                List<ElementBean> thoseEAs = this.getAllEAsForProject(elt, eas);
                //c'est une feuille, on recupere la metrique
                if (!thoseEAs.isEmpty()) {
                    retour = ElementSvc.getInstance().retrieveGlobalNumberOfElements(
                            thoseEAs, true);
                }
            } else {
                for (TreeMapElementBean child : elt.getChildren()) {
                    retour += this.getNbFileElements(child, leaf, eas);
                }
            }
            elt.setNbFileElements(retour);
        } else {
            retour = elt.getNbFileElements();
        }

        return retour;
    }

    private List<ElementBean> getAllEAsForProject(TreeMapElementBean elt, List<ElementBean> eas) {
        List<ElementBean> thoseEAs = new ArrayList<ElementBean>();
        for (ElementBean ea : eas) {
            if (ea.getProject().getId().equals(elt.getProject().getId())) {
                thoseEAs.add(ea);
            }
        }
        return thoseEAs;
    }

    public double getActionPlansCosts(TreeMapElementBean elt, String leaf,
            List<ElementBean> eas) {
        double retour = 0.0;

        if (!elt.isActionsPlansCostsComputed()) {
            if (leaf.equals(elt.getTypeElt())) {
                List<ElementBean> thoseEAs = this.getAllEAsForProject(elt, eas);
                //c'est une feuille, on recupere la metrique
                if (!thoseEAs.isEmpty()) {
                    for(ElementBean ea : thoseEAs) {
                        ApplicationEntityActionPlanBean ap = (ApplicationEntityActionPlanBean) ActionPlanSvc.getInstance().getCompleteActionPlan(ea, ea.getBaseline().getId(), false, null);
                        retour += ap.getCorrectionCost();
                    }
                }
            } else {
                for (TreeMapElementBean child : elt.getChildren()) {
                    retour += this.getActionPlansCosts(child, leaf, eas);
                }
            }
            elt.setActionsPlansCosts(retour);
        } else {
            retour = elt.getActionsPlansCosts();
        }

        return retour;
    }

    /**
     *
     * @param elt
     * @param leaf
     * @return
     */
    public double getProjectAllCodeValue(TreeMapElementBean elt) {
        double retour = 0.0;

        if (!elt.isAllCodeComputed()) {
            if (ElementType.EA.equals(elt.getTypeElt())) {
                //c'est une feuille, on recupere la metrique
                retour = MetricSvc.getInstance().getAllCodeValue(elt);
            } else {
                for (TreeMapElementBean child : elt.getChildren()) {
                    retour += this.getProjectAllCodeValue(child);
                }
            }
            elt.setAllCode(retour);
        } else {
            retour = elt.getAllCode();
        }

        return retour;
    }

    public double getProjectIFPUG(TreeMapElementBean elt) {
        double retour = 0.0;

        if (!elt.isIfpugComputed()) {
            if (ElementType.EA.equals(elt.getTypeElt())) {
                retour = MetricSvc.getInstance().getIFPUGValue(elt);
            } else {
                for (TreeMapElementBean child : elt.getChildren()) {
                    retour += this.getProjectIFPUG(child);
                }
            }
            elt.setIfpug(retour);
        } else {
            retour = elt.getIfpug();
        }

        return retour;
    }

    public double getProjectAverageVGValue(TreeMapElementBean elt) {
        double retour = 0.0;

        if (!elt.isAvgVgComputed()) {
            if (ElementType.EA.equals(elt.getTypeElt())) {
                if (elt.getDialecte() == null) {
                    DialecteBean db = DialecteSvc.getInstance().retrieveDialecteByElementId(elt.getId());
                    elt.setDialecte(db);
                }
                String languageId = elt.getDialecte().getLangage().getId();
                Properties prop = CaqsConfigUtil.getCaqsGlobalConfigProperties();
                String metId = prop.getProperty("vg." + languageId);
                if (metId == null) {
                    metId = "VG";
                }
                //c'est une feuille, on recupere la metrique
                MetriqueDao metriqueDao = DaoFactory.getInstance().getMetriqueDao();
                MetriqueBean mb = metriqueDao.retrieveAverageQametriqueFromMetEltBline(
                        elt.getBaseline().getId(), metId, elt.getProject().getId());
                retour = mb.getValbrute();
            } else {
                int nbElements = 0;
                for (TreeMapElementBean child : elt.getChildren()) {
                    double n = this.getProjectAverageVGValue(child);
                    if (n > 0.0) {
                        retour += n * child.getPoids();
                        nbElements += child.getPoids();
                    }
                }
                if (nbElements > 0) {
                    retour = retour / nbElements;
                }
            }
            elt.setAvgVg(retour);
        } else {
            retour = elt.getAvgVg();
        }

        return retour;
    }

    public double getProjectNbFileElements(TreeMapElementBean elt) {
        double retour = 0.0;

        if (!elt.isNbFileElementsComputed()) {
            if (ElementType.EA.equals(elt.getTypeElt())) {
                List<ElementBean> thoseEAs = new ArrayList<ElementBean>();
                thoseEAs.add(elt);
                retour = ElementSvc.getInstance().retrieveGlobalNumberOfElements(
                        thoseEAs, true);
            } else {
                for (TreeMapElementBean child : elt.getChildren()) {
                    retour += this.getProjectNbFileElements(child);
                }
            }
            elt.setNbFileElements(retour);
        } else {
            retour = elt.getNbFileElements();
        }

        return retour;
    }

    public double getProjectActionPlansCosts(TreeMapElementBean elt) {
        double retour = 0.0;

        if (!elt.isActionsPlansCostsComputed()) {
            if (ElementType.EA.equals(elt.getTypeElt())) {
                ApplicationEntityActionPlanBean ap = (ApplicationEntityActionPlanBean) ActionPlanSvc.getInstance().getCompleteActionPlan(elt, elt.getBaseline().getId(), false, null);
                retour = ap.getCorrectionCost();
            } else {
                for (TreeMapElementBean child : elt.getChildren()) {
                    retour += this.getProjectActionPlansCosts(child);
                }
            }
            elt.setActionsPlansCosts(retour);
        } else {
            retour = elt.getActionsPlansCosts();
        }

        return retour;
    }
}
