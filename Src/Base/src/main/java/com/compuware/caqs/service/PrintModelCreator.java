package com.compuware.caqs.service;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.CriterionDefinition;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.UsageBean;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaPartOperand;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormuleForm;
import com.compuware.caqs.domain.dataschemas.modelmanager.IFPUGFormulaForm;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.Agregation;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationAvgExclus;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationAvgExclusSeuil;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationExclus;
import com.compuware.caqs.domain.dataschemas.modelmanager.agregations.AgregationMultiSeuil;
import com.compuware.caqs.service.modelmanager.CaqsFormulaManager;
import com.compuware.caqs.service.modelmanager.CaqsQualimetricModelManager;
import com.compuware.toolbox.util.logging.LoggerManager;
import com.lowagie.text.Chunk;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDestination;
import com.lowagie.text.pdf.PdfOutline;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class PrintModelCreator {

    protected static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private static final float evenRowColor = 1.0f;
    private static final float oddRowColor = 0.90f;
    private static final float headerColor = 0.80f;
    private PdfTemplate tpl = null;
    private Map<String, CriterionDefinition> criterionsList = null;
    private Map<String, MetriqueDefinitionBean> metricsList = null;

    public PrintModelCreator() {
        this.criterionsList = new HashMap<String, CriterionDefinition>();
        this.metricsList = new HashMap<String, MetriqueDefinitionBean>();
    }

    public ByteArrayOutputStream createPrintableVersion(String modelId,
            MessageResources resources,
            HttpServletRequest request,
            Locale locale)
            throws Exception {
        ByteArrayOutputStream retour = new ByteArrayOutputStream();

        if (modelId != null) {
            UsageBean modele = new UsageBean();
            modele.setId(modelId);
            Document doc = new Document(PageSize.A4);

            PdfWriter docWriter = PdfWriter.getInstance(doc, retour);

            String enteteLib = resources.getMessage(locale,
                    "caqs.modeleditor.modelEdition.impression.title",
                    new Object[]{modele.getLib(locale)});
            doc.addTitle(enteteLib);

            docWriter.setPageEvent(new EventHandler());
            try {
                doc.open();
                this.tpl = docWriter.getDirectContent().createTemplate(100, 100);

                this.createFirstPage(doc, request, resources, locale, modele);
                this.createIFPUGSection(doc, resources, locale, request, modele, docWriter);
                this.createGoalsDefinitionTable(doc, resources, locale, request, modele, docWriter);
            } catch (DocumentException de) {
                logger.error(de.getMessage());
            }
            doc.close();
        }
        return retour;
    }

    private void createFirstPage(Document doc, HttpServletRequest request,
            MessageResources resources,
            Locale locale,
            UsageBean modele)
            throws DocumentException {
        try {
            Image logo = Image.getInstance("http://" + request.getServerName()
                    + ":" + request.getServerPort() + request.getContextPath()
                    + "/images/logoCAC.png");
            Paragraph p = new Paragraph();
            SimpleDateFormat sdf = new SimpleDateFormat(resources.getMessage(locale,
                    "caqs.modeleditor.modelEdition.impression.dateFormat"));
            String edited = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.editedThe",
                    new Object[]{sdf.format(new Date())});
            p.add(new Phrase(edited, FontFactory.getFont(FontFactory.TIMES, 9, Font.ITALIC)));
            p.setSpacingAfter(200.0f);
            doc.add(p);
            logo.setAlignment(Element.ALIGN_CENTER);
            doc.add(logo);
        } catch (IOException exc) {
            logger.error("Impossible de trouver le logo CAC", exc);
        }
        String enteteLib = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.title",
                modele.getLib(locale));
        Paragraph entete = this.getHeaderTitle(enteteLib, true);
        doc.add(entete);
        doc.newPage();

    }

    /**
     * Crée la section ifpug
     * @param doc Le document
     * @param request La requête
     * @param id_usa L'identifiant du modèle qualimétrique
     * @param docWriter Le PdfWriter
     */
    public void createIFPUGSection(Document doc,
            MessageResources resources, Locale locale,
            HttpServletRequest request, UsageBean modele, PdfWriter docWriter)
            throws DocumentException {
        CaqsQualimetricModelManager modelMan = CaqsQualimetricModelManager.getQualimetricModelManager(modele.getId());
        if (modelMan != null) {
            IFPUGFormulaForm ifpug = modelMan.getIFPUGFormula();
            if (ifpug != null) {
                String b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.ifpugsection");
                this.addBookmark(docWriter, b);

                Paragraph p = this.getSectionTitle(b, null, true);
                doc.add(p);

                String subSectionTitleTxt = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.ifpugsection.metriclist");
                Paragraph subTitleParagraph = this.getSubSectionTitle(subSectionTitleTxt, null, false);
                doc.add(subTitleParagraph);

                List<FormulaPartOperand> metrics = CaqsFormulaManager.getAllMetrics(ifpug.getFormula());

                for (FormulaPartOperand met : metrics) {
                    p = new Paragraph();
                    MetriqueDefinitionBean metrique = MetricSvc.getInstance().retrieveMetricById(met.getValue());
                    if (metrique != null) {
                        Chunk chk = new Chunk(" - " + metrique.getLib(locale)
                                + " ( "
                                + metrique.getId() + " )", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
                        chk.setLocalGoto("met_" + metrique.getId());
                        p.add(new Phrase(chk));
                        doc.add(p);

                        if (!this.metricsList.containsKey(metrique.getId())) {
                            this.metricsList.put(metrique.getId(), metrique);
                        }
                    }
                }
                this.addSpacingAfter(5.0f, doc);

                p = new Paragraph();
                Chunk chk = new Chunk(resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.ifpugsection.explanation")
                        + " " + ifpug.getReadableFormula(false, resources, locale),
                        FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
                p.add(new Phrase(chk));
                doc.add(p);
                this.addSpacingAfter(30.0f, doc);
            }
        }
    }

    /**
     * Crée le tableau des objectifs
     * @param doc Le document
     * @param request La requête
     * @param id_usa L'identifiant du modèle qualimétrique
     * @param docWriter Le PdfWriter
     */
    public void createGoalsDefinitionTable(Document doc,
            MessageResources resources, Locale locale,
            HttpServletRequest request, UsageBean modele, PdfWriter docWriter)
            throws DocumentException {
        FactorSvc factorSvc = FactorSvc.getInstance();

        String b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.bookmark.listeObjs");
        this.addBookmark(docWriter, b);

        Paragraph p = this.getSectionTitle(b, null, true);
        doc.add(p);

        List<FactorBean> goals = factorSvc.retrieveGoalsListByModel(modele.getId());
        if (goals != null) {
            float[] columnDefinitionSize = {20.0F, 80.0F};
            PdfPTable table = new PdfPTable(columnDefinitionSize);

            table.setWidthPercentage(100);

            //constitution de l'header
            table.setHeaderRows(1);//la premiere ligne constitue l'header
            b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.header.objName");
            table.addCell(this.createTableHeaderCell(b, null, null));
            b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.header.objDesc");
            table.addCell(this.createTableHeaderCell(b, null, null));

            int i = 0;
            for (FactorBean goal : goals) {
                table.addCell(this.createTableRowCell(
                        goal.getLib(locale), "fact_" + goal.getId(), null, i));
                table.addCell(this.createTableRowCell(this.formatString(goal.getDesc(locale)), null, null, i));
                i++;
            }

            doc.add(table);

            //pour chaque objectif, on crée le tableau de critères associés.
            for (FactorBean goal : goals) {
                createFacteurCriterionListTable(modele, goal, docWriter, doc, resources, locale, request);
            }

            doc.newPage();

            Set<Map.Entry<String, CriterionDefinition>> criteres = this.criterionsList.entrySet();
            if (criteres != null) {
                for (Map.Entry<String, CriterionDefinition> critere : criteres) {
                    this.createCriterionDefinitionTable(modele,
                            critere.getValue(), resources, locale,
                            docWriter, doc);
                }
            }

            doc.newPage();
            p = this.getSectionTitle(resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.title.metTitle"), null, true);
            doc.add(p);

            Set<Map.Entry<String, MetriqueDefinitionBean>> metriques = this.metricsList.entrySet();
            if (metriques != null) {
                for (Map.Entry<String, MetriqueDefinitionBean> metrique : metriques) {
                    this.createMetriqueDefinitionTable(metrique.getValue(), resources, locale, docWriter, doc);
                }
            }

        }

    }

    public void createFacteurCriterionListTable(UsageBean modele,
            FactorBean goal, PdfWriter docWriter, Document doc,
            MessageResources resources, Locale locale,
            HttpServletRequest request)
            throws DocumentException {
        doc.newPage();

        CriterionSvc critereSvc = CriterionSvc.getInstance();

        PdfPCell cell = null;

        String sectionTitleTxt = resources.getMessage(locale,
                "caqs.modeleditor.modelEdition.impression.title.critere", new Object[]{goal.getLib(locale)});
        Paragraph titleParagraph = this.getSectionTitle(sectionTitleTxt, "fact_"
                + goal.getId(), true);
        doc.add(titleParagraph);
        this.addBookmark(docWriter, goal.getLib(locale));

        List<CriterionDefinition> criteres = critereSvc.retrieveCriterionDefinitionByGoalAndModel(goal.getId(), modele.getId());
        if (criteres != null) {
            float[] columnDefinitionSize = {45.0F, 45.0F, 10.0F};
            PdfPTable table = new PdfPTable(columnDefinitionSize);

            table.setWidthPercentage(100);

            //constitution de l'header
            table.setHeaderRows(1);//la premiere ligne constitue l'header
            String b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.header.critName");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);
            b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.header.critDesc");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);
            b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.header.poids");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            int i = 0;
            for (CriterionDefinition criterion : criteres) {
                if (!this.criterionsList.containsKey(criterion.getId())) {
                    this.criterionsList.put(criterion.getId(), criterion);
                }

                cell = this.createTableRowCell(criterion.getLib(locale), "crit_"
                        + criterion.getId(), null, i);
                table.addCell(cell);

                cell = this.createTableRowCell(this.formatString(criterion.getDesc(locale)), null, null, i);
                table.addCell(cell);
                table.addCell(this.createTableRowCell("" + criterion.getWeight(), null, null, i));
                i++;
            }

            doc.add(table);
        }

    }

    private String formatString(String input) {
        String retour = input;
        retour = retour.replaceAll("<BR\\s*/>", "\n");
        return retour;
    }

    private void createCriterionDefinitionTable(UsageBean modele, CriterionDefinition criterion,
            MessageResources resources, Locale locale,
            PdfWriter docWriter, Document doc)
            throws DocumentException {
        MetricSvc metricSvc = MetricSvc.getInstance();

        PdfPCell cell = null;

        String sectionTitleTxt = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.title.critTitle", new Object[]{criterion.getLib(locale)});
        Paragraph titleParagraph = this.getSectionTitle(sectionTitleTxt, "crit_"
                + criterion.getId(), false);
        doc.add(titleParagraph);
        this.addBookmark(docWriter, criterion.getLib(locale));
        this.addSpacingAfter(5.0f, doc);

        List<MetriqueDefinitionBean> metriques = metricSvc.retrieveMetriqueDefinitionByIdCrit(criterion.getId(), modele.getId());
        if (metriques != null) {
            String subSectionTitleTxt = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.subtitle.listeMetriques");
            Paragraph subTitleParagraph = this.getSubSectionTitle(subSectionTitleTxt, null, false);
            doc.add(subTitleParagraph);

            for (MetriqueDefinitionBean metrique : metriques) {
                Paragraph p = new Paragraph();
                Chunk chk = new Chunk(" - " + metrique.getLib(locale) + " ( "
                        + metrique.getId() + " )", FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
                chk.setLocalGoto("met_" + metrique.getId());
                p.add(new Phrase(chk));
                doc.add(p);

                if (!this.metricsList.containsKey(metrique.getId())) {
                    this.metricsList.put(metrique.getId(), metrique);
                }

            }
            this.addSpacingAfter(5.0f, doc);
        }


        CaqsQualimetricModelManager man = CaqsQualimetricModelManager.getQualimetricModelManager(modele.getId());

        if (man != null && man.setCritere(criterion.getId())) {
            //impression de l'agrégation du critère
            String subSectionTitleTxt = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.subtitle.aggregation");
            Paragraph subTitleParagraph = this.getSubSectionTitle(subSectionTitleTxt, null, false);
            doc.add(subTitleParagraph);

            List<Agregation> aggs = man.getAggregations();
            for (Agregation agregation : aggs) {
                Paragraph p = new Paragraph();
                String type = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.agg.typeAgg", resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.agg."
                        + agregation.getId()));
                boolean hasParam = false;
                if (agregation instanceof AgregationExclus) {
                    hasParam = true;
                } else if (agregation instanceof AgregationAvgExclus) {
                    hasParam = true;
                } else if (agregation instanceof AgregationAvgExclusSeuil) {
                    hasParam = true;
                } else if ("MULTI_SEUIL".equals(agregation.getId())) {
                    hasParam = true;
                }
                Chunk chk = new Chunk(type, FontFactory.getFont(FontFactory.HELVETICA, 10, Font.NORMAL));
                p.add(new Phrase(chk));
                doc.add(p);


                if (hasParam) {
                    p = new Paragraph();
                    String s = "(";
                    if (agregation instanceof AgregationExclus) {
                        s += resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.agg.default",
                                ((AgregationExclus) agregation).getDefaultValue());
                    } else if (agregation instanceof AgregationAvgExclus) {
                        s += resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.agg.valexclus",
                                ((AgregationAvgExclus) agregation).getValueExclus());
                    } else if (agregation instanceof AgregationAvgExclusSeuil) {
                        s += resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.agg.valexclusseuil",
                                ((AgregationAvgExclusSeuil) agregation).getValueExclus(),
                                ((AgregationAvgExclusSeuil) agregation).getSeuil());
                    } else if (agregation instanceof AgregationMultiSeuil) {
                        s += resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.agg.valexclusmultiseuil",
                                ((AgregationMultiSeuil) agregation).getValueExclus(),
                                ((AgregationMultiSeuil) agregation).getSeuil1(),
                                ((AgregationMultiSeuil) agregation).getSeuil2(),
                                ((AgregationMultiSeuil) agregation).getSeuil3());
                    }
                    s += ")";
                    chk = new Chunk(s, FontFactory.getFont(FontFactory.HELVETICA, 9, Font.ITALIC));
                    p.add(new Phrase(chk));
                    doc.add(p);
                }

                this.addSpacingAfter(5.0f, doc);
            }

            //impression des formules du critere
            subSectionTitleTxt = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.subtitle.listeFormules");
            subTitleParagraph = this.getSubSectionTitle(subSectionTitleTxt, null, false);
            doc.add(subTitleParagraph);

            this.addSpacingAfter(5.0f, doc);

            PdfPTable table = new PdfPTable(new float[]{10.0F, 90.0F});
            table.setWidthPercentage(100);
            //constitution de l'header
            table.setHeaderRows(1);//la premiere ligne constitue l'header
            String b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.header.formulaNote");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);
            b = resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.header.formulaValue");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            List<FormuleForm> formulas = man.getFormulas();
            if (formulas != null && !formulas.isEmpty()) {
                int i = 0;
                for (Iterator it = formulas.iterator(); it.hasNext(); i++) {
                    FormuleForm f = (FormuleForm) it.next();
                    cell = this.createTableRowCell("" + f.getScore(), null, null, i);
                    cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    table.addCell(cell);

                    String txt = (f.isAlwaysTrue()) ? resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.alwaysTrueFormula") : f.getReadableFormula(false, resources, locale);
                    cell = this.createTableRowCell(txt, null, null, i);
                    table.addCell(cell);

                }
            }
            doc.add(table);

            FormuleForm f = man.getCostFormula();
            if (f != null) {
                Paragraph p = new Paragraph();
                Chunk chk = new Chunk(resources.getMessage(locale, "caqs.modeleditor.modelEdition.impression.costFormula")
                        + " ",
                        FontFactory.getFont(FontFactory.HELVETICA, 10, Font.BOLD));
                p.add(new Phrase(chk));
                p.add(new Phrase(f.getReadableFormula(false, resources, locale)));
                doc.add(p);
            }
        }
        this.addSpacingAfter(30.0f, doc);
    }

    private void createMetriqueDefinitionTable(MetriqueDefinitionBean metrique,
            MessageResources resources, Locale loc,
            PdfWriter docWriter, Document doc)
            throws DocumentException {

        Paragraph titleParagraph = new Paragraph();
        Chunk chk = new Chunk(metrique.getLib(loc), FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLD));
        chk.setLocalDestination("met_" + metrique.getId());
        titleParagraph.add(chk);
        doc.add(titleParagraph);
        this.addBookmark(docWriter, metrique.getId());

        Paragraph p = new Paragraph();
        Chunk desc = new Chunk(resources.getMessage(loc, "caqs.modeleditor.modelEdition.impression.metDesc")
                + " ", FontFactory.getFont(FontFactory.TIMES, 10, Font.UNDERLINE));
        p.add(new Phrase(desc));
        desc = new Chunk(this.formatString(metrique.getDesc(loc)), FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL));
        p.add(new Phrase(desc));
        doc.add(p);

        String compl = metrique.getComplement(loc);
        if (compl != null && !"".equals(compl)) {
            p = new Paragraph();
            desc = new Chunk(resources.getMessage(loc, "caqs.modeleditor.modelEdition.impression.metCompl")
                    + " ", FontFactory.getFont(FontFactory.TIMES, 10, Font.UNDERLINE));
            p.add(new Phrase(desc));
            desc = new Chunk(this.formatString(compl), FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL));
            p.add(new Phrase(desc));
            doc.add(p);
        }
        this.addSpacingAfter(10.0f, doc);
    }

    private void addSpacingAfter(float s, Document doc) throws DocumentException {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(s);
        doc.add(p);
    }

    private PdfPCell createTableHeaderCell(String txt, String localGoTo, String localDestination) {
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.BOLD);

        PdfPCell retour = null;

        Paragraph p = new Paragraph();
        Chunk chk = new Chunk(txt, textFont);
        if (localGoTo != null && !"".equals(localGoTo)) {
            chk.setLocalGoto(localGoTo);
        }

        if (localDestination != null && !"".equals(localDestination)) {
            chk.setLocalDestination(localDestination);
        }

        Phrase ph = new Phrase(chk);
        p.add(ph);
        retour =
                new PdfPCell(p);
        retour.setHorizontalAlignment(Element.ALIGN_CENTER);
        retour.setVerticalAlignment(Element.ALIGN_CENTER);
        retour.setGrayFill(PrintModelCreator.headerColor);
        return retour;
    }

    private PdfPCell createTableRowCell(String txt, String localGoTo, String localDestination, int rowIndex) {
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);

        if (txt == null) {
            txt = "";
        }

        PdfPCell retour = null;

        Paragraph p = new Paragraph();
        Chunk chk = new Chunk(txt, textFont);
        if (localGoTo != null && !"".equals(localGoTo)) {
            chk.setLocalGoto(localGoTo);
        }

        if (localDestination != null && !"".equals(localDestination)) {
            chk.setLocalDestination(localDestination);
        }

        Phrase ph = new Phrase(chk);
        p.add(ph);
        retour = new PdfPCell(p);
        retour.setHorizontalAlignment(Element.ALIGN_JUSTIFIED);
        retour.setVerticalAlignment(Element.ALIGN_CENTER);
        float color = ((rowIndex % 2) == 0) ? PrintModelCreator.evenRowColor : PrintModelCreator.oddRowColor;
        retour.setGrayFill(color);
        return retour;
    }

    private Paragraph getHeaderTitle(String txt, boolean spacing) {
        Paragraph retour = new Paragraph();

        retour.add(new Chunk(txt, FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD)));
        retour.setAlignment(Element.ALIGN_CENTER);
        if (spacing) {
            retour.setSpacingAfter(40.0f);
        }

        return retour;
    }

    private Paragraph getSectionTitle(String txt, String localDestination, boolean spacing) {
        Paragraph retour = new Paragraph();

        Chunk chk = new Chunk(txt, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC));
        if (localDestination != null) {
            chk.setLocalDestination(localDestination);
        }

        retour.add(chk);
        retour.setAlignment(Element.ALIGN_LEFT);
        if (spacing) {
            retour.setSpacingAfter(30.0f);
        }

        return retour;
    }

    private Paragraph getSubSectionTitle(String txt, String localDestination, boolean spacing) {
        Paragraph retour = new Paragraph();

        Chunk chk = new Chunk(txt, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.ITALIC));
        if (localDestination != null) {
            chk.setLocalDestination(localDestination);
        }

        retour.add(chk);
        retour.setAlignment(Element.ALIGN_LEFT);
        if (spacing) {
            retour.setSpacingAfter(20.0f);
        }

        return retour;
    }

    private void addBookmark(PdfWriter docWriter, String bookmark) {
        PdfDestination destination = new PdfDestination(PdfDestination.FITH);
        new PdfOutline(docWriter.getDirectContent().getRootOutline(), destination, bookmark);
    }

    private class EventHandler extends PdfPageEventHelper {

        BaseFont f = null;

        public void onOpenDocument(PdfWriter writer, Document document) {
            try {
                f = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
            } catch (Exception e) {
            }
        }

        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            cb.saveState();
            // compose the footer
            String text = writer.getPageNumber() + "/";
            float textSize = f.getWidthPoint(text, 12);
            float textBase = document.bottom() - 20;
            cb.beginText();
            cb.setFontAndSize(f, 10);
            float adjust = f.getWidthPoint("0", 12);
            cb.setTextMatrix(document.right() - textSize - adjust, textBase);
            cb.showText(text);
            cb.endText();
            cb.addTemplate(tpl, document.right() - adjust, textBase);
            cb.saveState();
        }

        /**
         * @see com.lowagie.text.pdf.PdfPageEventHelper#onCloseDocument(com.lowagie.text.pdf.PdfWriter, com.lowagie.text.Document)
         */
        public void onCloseDocument(PdfWriter writer, Document document) {
            tpl.beginText();
            tpl.setFontAndSize(f, 10);
            tpl.setTextMatrix(0, 0);
            tpl.showText("" + (writer.getPageNumber() - 1));
            tpl.endText();
        }
    }
}
