package com.compuware.caqs.service.modelmanager;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.constants.MessagesCodes;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaPart;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import com.compuware.caqs.domain.dataschemas.modelmanager.IFPUGFormulaForm;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.Agregation;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.util.Map;
import java.util.Set;

public class CaqsQualimetricModelManager {

    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    protected static XMLOutputter xmlOutputter = new XMLOutputter(org.jdom.output.Format.getPrettyFormat());
    private Document doc = null;
    private String idUsa = null;
    private Element critereToEdit = null;
    private Element modelRootElement = null;

    public static CaqsQualimetricModelManager getQualimetricModelManager(String idUsa) {
        CaqsQualimetricModelManager retour = null;
        retour = new CaqsQualimetricModelManager(idUsa);
        if (!retour.parseFile()) {
            retour = null;
        }
        return retour;
    }

    public static MessagesCodes duplicateModel(String idUsaSrc, String idUsaTarget) {
        MessagesCodes retour = MessagesCodes.MODEL_DUPLICATION_ERROR;
        CaqsQualimetricModelManager src = CaqsQualimetricModelManager.getQualimetricModelManager(idUsaSrc);
        if (src != null) {
            src.modelRootElement.setAttribute("id", idUsaTarget);
            if (src.saveToDisk(idUsaTarget)) {
                retour = MessagesCodes.NO_ERROR;
            }
        }
        return retour;
    }

    public MessagesCodes deleteModel(String id) {
        MessagesCodes retour = MessagesCodes.MODEL_DELETION_ERROR;
        File f = this.getFile();
        if (f != null && f.exists()) {
            if (f.delete()) {
                retour = MessagesCodes.NO_ERROR;
            }
        }
        return retour;
    }

    public boolean saveToDisk(String id) {
        boolean retour = false;
        File fichier = this.getFile(id);
        if (fichier != null) {
            try {
                FileWriter fw = new FileWriter(fichier);
                CaqsQualimetricModelManager.xmlOutputter.output(doc, fw);
                fw.close();
                retour = true;
            } catch (IOException e) {
                mLog.error("Erreur lors de la creation du FileWriter pour "
                        + fichier.getAbsolutePath(), e);
            }
        }
        return retour;
    }

    private File getFile() {
        File retour = null;

        String m_config_dir = "";
        File directory = null;
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            if (!this.idUsa.equals("")) {
                m_config_dir = (String) ctx.lookup("java:comp/env/conf.dir");
            }
        } catch (javax.naming.NamingException e) {
            m_config_dir = "../config";
        }
        directory = new File(m_config_dir);
        retour = new File(directory, "method-" + this.idUsa.toLowerCase()
                + ".xml");
        return retour;
    }

    private File getFile(String id) {
        File retour = null;

        String m_config_dir = "";
        File directory = null;
        try {
            javax.naming.Context ctx = new javax.naming.InitialContext();
            if (!this.idUsa.equals("")) {
                m_config_dir = (String) ctx.lookup("java:comp/env/conf.dir");
            }
        } catch (javax.naming.NamingException e) {
            m_config_dir = "../config";
        }
        directory = new File(m_config_dir);
        retour = new File(directory, "method-" + id.toLowerCase() + ".xml");
        return retour;
    }

    private CaqsQualimetricModelManager(String usa) {
        this.idUsa = usa;
    }

    private boolean parseFile() {
        boolean retour = false;

        File fichier = this.getFile();
        try {
            if (fichier != null && fichier.exists() && idUsa != null) {
                SAXBuilder saxbuilder = new SAXBuilder();
                try {
                    doc = saxbuilder.build(fichier.getAbsolutePath());
                    Element root = doc.getRootElement();
                    if ("METHOD".equals(root.getName())) {
                        Element usage = root.getChild("USAGE");
                        if (usage != null
                                && this.idUsa.equals(usage.getAttributeValue("id"))) {
                            this.modelRootElement = usage;
                            retour = true;
                        }
                    }
                } catch (org.jdom.JDOMException e) {
                    mLog.error("Error parsing qualimetric model file "
                            + fichier.getAbsolutePath(), e);
                }

            } else if (fichier != null && !fichier.exists() && idUsa != null) {
                fichier.createNewFile();
                FileWriter fw = new FileWriter(fichier);
                Element root = new Element("METHOD");
                Element usage = new Element("USAGE");
                usage.setAttribute("id", idUsa);
                root.addContent(usage);
                doc = new Document();
                doc.setRootElement(root);
                this.modelRootElement = usage;
                CaqsQualimetricModelManager.xmlOutputter.output(doc, fw);
                fw.close();
                retour = true;
            }
        } catch (java.io.IOException e) {
            mLog.error("Error parsing qualimetric model file "
                    + fichier.getAbsolutePath(), e);
        }

        return retour;
    }

    public boolean setCritere(String idCrit) {
        boolean retour = false;

        if (this.modelRootElement != null && idCrit != null) {
            List<Element> criteres = this.modelRootElement.getChildren();
            if (criteres != null) {
                for (Iterator<Element> it = criteres.iterator(); it.hasNext();) {
                    Element elt = (Element) it.next();
                    if (!"CRITDEF".equals(elt.getName())) {
                        continue;
                    }
                    if (idCrit.equals(elt.getAttributeValue("id"))) {
                        this.critereToEdit = elt;
                        retour = true;
                        break;
                    }
                }
            }
        }

        if (!retour) {
            Element elt = new Element("CRITDEF");
            elt.setAttribute("id", idCrit);
            //Locale l = (request!=null)?request.getLocale():null;
            //elt.setAttribute("telt", CaqsmodelerRessourceManager.getTechnicalRessource("caqsmodeler.defaultTelt", null, l));
            this.critereToEdit = elt;
            this.modelRootElement.addContent(elt);
            retour = true;
        }

        return retour;
    }

    public boolean hasAFormulaWithError() {
        boolean retour = false;
        if (this.modelRootElement != null) {
            List<Element> criteres = this.modelRootElement.getChildren();
            if (criteres != null) {
                for (Iterator<Element> it = criteres.iterator(); it.hasNext();) {
                    Element elt = (Element) it.next();
                    if (!"CRITDEF".equals(elt.getName())) {
                        continue;
                    }
                    this.critereToEdit = elt;
                    List<FormuleForm> formulas = this.getFormulas();
                    if(formulas != null) {
                        if(formulas.isEmpty()) {
                            retour = true;
                        } else for(FormuleForm formula : formulas) {
                            if(CaqsFormulaManager.formulaHasError(formula.getFormula())) {
                                retour = true;
                                break;
                            }
                        }
                    }
                    if(this.getAggregations().isEmpty()) {
                        retour = true;
                    }
                    if(retour) {
                        break;
                    }
                }
            }
        }
        this.critereToEdit = null;
        return retour;
    }

    public boolean removeCritere(String idCrit) {
        boolean retour = false;

        if (this.modelRootElement != null && idCrit != null) {
            List<Element> criteres = this.modelRootElement.getChildren();
            if (criteres != null) {
                for (Iterator<Element> it = criteres.iterator(); it.hasNext();) {
                    Element elt = (Element) it.next();
                    if (!"CRITDEF".equals(elt.getName())) {
                        continue;
                    }
                    if (idCrit.equals(elt.getAttributeValue("id"))) {
                        this.modelRootElement.removeContent(elt);
                        retour = true;
                        break;
                    }
                }
            }
        }

        return retour;
    }

    public boolean saveToDisk() {
        boolean retour = false;
        File fichier = this.getFile();
        if (fichier != null) {
            try {
                FileWriter fw = new FileWriter(fichier);
                CaqsQualimetricModelManager.xmlOutputter.output(doc, fw);
                fw.close();
                retour = true;
            } catch (IOException e) {
                mLog.error("Erreur lors de la creation du FileWriter pour "
                        + fichier.getAbsolutePath(), e);
            }
        }
        return retour;
    }

    public IFPUGFormulaForm getIFPUGFormula() {
        IFPUGFormulaForm retour = null;

        if (this.modelRootElement != null) {
            List<Element> children = this.modelRootElement.getChildren("IFPUG");
            if (children != null && children.size() == 1) {
                Element elt = (Element) children.get(0);
                retour = new IFPUGFormulaForm();
                ElementType et = new ElementType();
                et.setId(elt.getAttributeValue("telt"));
                retour.setElementType(et);
                retour.setFormula(CaqsFormulaManager.getFormulaFromXML(elt));
            }
        }

        return retour;
    }

    public FormuleForm getCostFormula() {
        FormuleForm retour = null;

        if (this.critereToEdit != null) {
            List<Element> children = this.critereToEdit.getChildren("COSTFORMULA");
            if (children != null && children.size() == 1) {
                Element elt = (Element) children.get(0);
                retour = new FormuleForm();
                retour.setFormula(CaqsFormulaManager.getFormulaFromXML(elt));
            }
        }

        return retour;
    }

    public List<FormuleForm> getFormulas() {
        List<FormuleForm> retour = new ArrayList<FormuleForm>();

        if (this.critereToEdit != null) {
            List<Element> children = this.critereToEdit.getChildren("FORMULA");
            if (children != null) {
                for (Element elt : children) {
                    FormuleForm f = new FormuleForm();
                    f.setScore(Integer.parseInt(elt.getAttributeValue("value")));
                    f.setFormula(CaqsFormulaManager.getFormulaFromXML(elt));
                    String readableFormula = f.getReadableFormula(false, null, null);
                    f.setAlwaysTrue(("true".equalsIgnoreCase(readableFormula)
                            || "false".equalsIgnoreCase(readableFormula)));
                    retour.add(f);
                }
            }
        }

        return retour;
    }

    public void setCostFormula(FormuleForm formula) {
        if (this.critereToEdit != null) {
            this.removeCostFormulaFromSelectedCriterion();
            this.putCostFormulaInXML(formula);
        }
    }

    public boolean saveIFPUGToDisk(String ifpug, String idTelt) {
        boolean retour = false;
        if (this.modelRootElement != null) {
            this.modelRootElement.removeChildren("IFPUG");
            FormulaPart fp = CaqsFormulaManager.getFormulaFromFormattedFormula(ifpug);
            Element elt = new Element("IFPUG");
            elt.setAttribute("telt", idTelt);
            elt.addContent(CaqsFormulaManager.getXMLFormulaFromFormula(fp));
            this.modelRootElement.addContent(elt);
            retour = this.saveToDisk();
        }
        return retour;
    }

    public boolean saveFormulasToDisk(List<FormuleForm> formulas) {
        boolean retour = false;
        if (this.critereToEdit != null) {
            this.removeAllFormulasFromSelectedCriterion();
            for (FormuleForm formula : formulas) {
                this.putFormulaInXML(formula);
            }
            retour = this.saveToDisk();
        }
        return retour;
    }

    public boolean saveAgregationsToDisk(List<Agregation> agregations) {
        boolean retour = false;
        if (this.critereToEdit != null) {
            this.saveAggregations(agregations);
            retour = this.saveToDisk();
        }
        return retour;
    }

    private void removeAllFormulasFromSelectedCriterion() {
        if (this.critereToEdit != null) {
            this.critereToEdit.removeChildren("FORMULA");
        }
    }

    private void removeCostFormulaFromSelectedCriterion() {
        if (this.critereToEdit != null) {
            this.critereToEdit.removeChildren("COSTFORMULA");
        }
    }

    private void putFormulaInXML(FormuleForm f) {
        Element elt = new Element("FORMULA");
        elt.setAttribute("value", "" + f.getScore());

        Element nouvelleFormule = null;
        if (f.isAlwaysTrue()) {
            nouvelleFormule = CaqsFormulaManager.createAlwaysTrueElement();

        } else {
            nouvelleFormule = CaqsFormulaManager.getXMLFormulaFromFormula(f.getFormula());
        }
        elt.addContent(nouvelleFormule);

        this.critereToEdit.addContent(elt);
    }

    private void putCostFormulaInXML(FormuleForm f) {
        Element elt = new Element("COSTFORMULA");

        Element nouvelleFormule = CaqsFormulaManager.getXMLFormulaFromFormula(f.getFormula());
        elt.addContent(nouvelleFormule);

        this.critereToEdit.addContent(elt);
    }

    public void saveAggregations(List<Agregation> agregations) {
        if (agregations != null) {
            this.critereToEdit.removeChildren("AGGREG");
            for (Agregation agregation : agregations) {
                Element agg = new Element("AGGREG");
                agg.setAttribute("id", agregation.getId());
                agg.setAttribute("telt", agregation.getIdTelt());
                Set<Map.Entry<String, String>> params = agregation.getParamsSet();
                for (Map.Entry<String, String> param : params) {
                    if (param.getValue() != null && param.getValue().length()
                            > 0) {
                        Element paramDefault = new Element("PARAM");
                        paramDefault.setAttribute("id", param.getKey());
                        paramDefault.setAttribute("value", param.getValue());
                        paramDefault.setAttribute("type", "NUMERIC");
                        agg.addContent(paramDefault);
                    }
                }
                //nous recherchons l'ancienne aggregation dans le critere pour la retirer
                this.critereToEdit.addContent(agg);
            }
        }
    }

    private Agregation getAggregation(Element elt) {
        Agregation retour = Agregation.getAgregationFromId(elt.getAttributeValue("id"));
        if (elt != null) {
            String typeEltAgg = elt.getAttributeValue("telt");
            retour.setIdTelt(typeEltAgg);

            //rien a faire pour AVG et AVG_ALL
            List l = elt.getChildren();
            for (Iterator iter = l.iterator(); iter.hasNext();) {
                Element element = (Element) iter.next();
                if ("PARAM".equals(element.getName())) {
                    String paramId = element.getAttributeValue("id");
                    String paramValue = element.getAttributeValue("value");
                    retour.setParam(paramId, paramValue);
                }
            }
        }
        return retour;
    }

    public List<Agregation> getAggregations() {
        List<Agregation> al = new ArrayList<Agregation>();
        if (this.critereToEdit != null) {
            List l = this.critereToEdit.getChildren();
            for (Iterator iter = l.iterator(); iter.hasNext();) {
                Element element = (Element) iter.next();
                if ("AGGREG".equals(element.getName())) {
                    Agregation f = this.getAggregation(element);
                    al.add(f);
                }
            }
        }
        return al;
    }

    
}
