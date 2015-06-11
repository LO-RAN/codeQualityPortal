package com.compuware.caqs.presentation.consult.actions.actionplan;

import com.compuware.caqs.business.util.StringFormatUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.MetriqueDefinitionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanCriterionBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanImpactedElementBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanUnit;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanCollection;
import com.compuware.caqs.domain.dataschemas.actionplan.list.ActionPlanElementBeanMap;
import com.compuware.caqs.presentation.consult.actions.actionplan.util.ActionPlanKiviatUtils;
import com.compuware.caqs.presentation.util.RequestUtil;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.MetricSvc;
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

public class ActionPlanPrintAction extends Action {

    private static org.apache.log4j.Logger logger = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private final float evenRowColor = 1.0f;
    private final float oddRowColor = 0.90f;
    private final float headerColor = 0.80f;
    private PdfTemplate tpl = null;

    public ActionForward execute(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);
        ElementBean eltBean = (ElementBean) session.getAttribute(Constants.CAQS_CURRENT_ELEMENT_SESSION_KEY);

        response.setContentType("application/pdf");

        Document doc = new Document(PageSize.A4);
        ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();

        try {
            PdfWriter docWriter = PdfWriter.getInstance(doc, baosPDF);
            docWriter.setPageEvent(new EventHandler());
            doc.open();
            tpl = docWriter.getDirectContent().createTemplate(100, 100);

            this.createFirstPage(doc, request, eltBean, docWriter, resources);
            this.createIntroSection(doc, request, eltBean, docWriter, resources);
            this.createKiviatSection(doc, request, eltBean, docWriter, resources);
            this.createImpactedElementsSection(doc, request, eltBean, docWriter, resources);
            this.createCriterionsSection(doc, request, eltBean, docWriter, resources);
        } catch (DocumentException de) {
            logger.error(de.getMessage());
        }
        doc.close();
        response.setContentLength(baosPDF.size());

        ServletOutputStream sos = response.getOutputStream();
        baosPDF.writeTo(sos);
        sos.flush();

        return null;
    }

    /**
     * Cr�e la page d'ent�te
     * @param doc Le document
     * @param request La requ�te
     * @param ea L'entit� applicative
     * @param docWriter Le PdfWriter
     * @param resources Les ressources internationalis�es
     * @throws DocumentException
     */
    private void createFirstPage(
            Document doc,
            HttpServletRequest request,
            ElementBean ea,
            PdfWriter docWriter,
            ResourceBundle resources)
            throws DocumentException {
        try {
            Image logo = Image.getInstance("http://" + request.getServerName() +
                    ":" +
                    request.getServerPort() + request.getContextPath() +
                    "/images/logoCAC.png");
            Paragraph p = new Paragraph();
            SimpleDateFormat sdf = new SimpleDateFormat(resources.getString("caqs.actionplan.impression.dateFormat"));
            String msg = resources.getString("caqs.actionplan.impression.editedThe");
            String edited = MessageFormat.format(msg, new Object[]{sdf.format(new Date())});
            p.add(new Phrase(edited, FontFactory.getFont(FontFactory.TIMES, 9, Font.ITALIC)));
            p.setSpacingAfter(200.0f);
            doc.add(p);
            logo.setAlignment(Element.ALIGN_CENTER);
            doc.add(logo);
        } catch (IOException exc) {
            logger.error("Impossible de trouver le logo CAC");
        }

        Chunk chk = new Chunk(resources.getString("caqs.actionplan"), FontFactory.getFont(FontFactory.HELVETICA, 24, Font.BOLD));
        Paragraph entete = new Paragraph();
        entete.setAlignment(Element.ALIGN_CENTER);
        entete.add(chk);
        String msg = resources.getString("caqs.actionplan.impression.title");
        String enteteLib = MessageFormat.format(msg, new Object[]{
                    ea.getLib(),
                    ea.getProject().getLib(),
                    ea.getBaseline().getLib()
                });
        entete.add(Chunk.NEWLINE);
        Paragraph detailEntete = new Paragraph();
        detailEntete.setAlignment(Element.ALIGN_LEFT);
        chk = new Chunk(enteteLib, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC));
        detailEntete.add(chk);
        entete.add(detailEntete);

        doc.add(entete);
        doc.newPage();
    }

    /**
     * Cr�e la section des �l�ments impact�s, tri�s par type d'�l�ments
     * @param doc Le document
     * @param request La requ�te
     * @param ea L'entit� applicative
     * @param docWriter Le PdfWriter
     * @param resources Les ressources internationalis�es
     * @throws DocumentException
     */
    private void createImpactedElementsSection(
            Document doc,
            HttpServletRequest request,
            ElementBean ea,
            PdfWriter docWriter,
            ResourceBundle resources)
            throws DocumentException {
        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
        ActionPlanBean ap = actionPlanSvc.getCompleteActionPlan(ea, ea.getBaseline().getId(), false, request);
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = ap.getCorrectedElements();
        if (criterions != null && !criterions.isEmpty()) {
            String sectionTitle = resources.getString("caqs.actionplan.elementList");
            this.addSectionTitle(sectionTitle, null, 30.0f, 15.0f, doc, docWriter);
            ActionPlanElementBeanMap elementMap = actionPlanSvc.getElementsImpactedByActionPlan(criterions, ea.getUsage().getId());
            if (elementMap != null) {
                //on les trie par type d'�l�ment
                Map<String, List<ActionPlanImpactedElementBean>> tri = new HashMap<String, List<ActionPlanImpactedElementBean>>();
                for (ActionPlanImpactedElementBean elt : elementMap.values()) {
                    List<ActionPlanImpactedElementBean> liste = tri.get(elt.getIdTelt());
                    if (liste == null) {
                        liste = new ArrayList<ActionPlanImpactedElementBean>();
                        tri.put(elt.getIdTelt(), liste);
                    }
                    liste.add(elt);
                }
                Locale loc = RequestUtil.getLocale(request);
                Iterator<Entry<String, List<ActionPlanImpactedElementBean>>> iter = tri.entrySet().iterator();
                while (iter.hasNext()) {
                    Entry<String, List<ActionPlanImpactedElementBean>> entry = iter.next();
                    String key = entry.getKey();
                    List<ActionPlanImpactedElementBean> liste = entry.getValue();
                    ElementType et = new ElementType(key);
                    this.addSubSectionTitle(et.getLib(loc), null, 10.0f, 10.0f, doc, docWriter);
                    this.addElementListTable(doc, request, liste, docWriter, resources);
                }
            }
        }
    }

    /**
     * Cr�e un tableau donnant la liste des �l�ments impact�s
     * @param doc Le document
     * @param request La requ�te
     * @param elts La liste des �l�ments impact�s
     * @param docWriter Le PdfWriter
     * @param resources Les ressources internationalis�es
     * @throws DocumentException
     */
    private void addElementListTable(
            Document doc,
            HttpServletRequest request,
            List<ActionPlanImpactedElementBean> elts,
            PdfWriter docWriter,
            ResourceBundle resources)
            throws DocumentException {
        Locale loc = RequestUtil.getLocale(request);

        float[] columnDefinitionSize = {65.0F, 35.0F};
        PdfPTable table = new PdfPTable(columnDefinitionSize);

        table.setWidthPercentage(100);

        PdfPCell cell = null;

        //constitution de l'header
        table.setHeaderRows(1);//la premiere ligne constitue l'header
        String b = resources.getString("caqs.actionplan.element");
        cell = this.createTableHeaderCell(b, null, null);
        table.addCell(cell);

        b = resources.getString("caqs.actionplan.elementCriterions");
        cell = this.createTableHeaderCell(b, null, null);
        table.addCell(cell);

        int i = 0;
        for (ActionPlanImpactedElementBean elt : elts) {
            cell = this.createTableRowCell(elt.getDescElt(), null, null, Element.ALIGN_CENTER, i);
            table.addCell(cell);

            String criterionsList = "";
            int taille = elt.getCriterions().size();
            for (int j = 0; j < taille; j++) {
                criterionsList += "- " +
                        elt.getCriterions().get(j).getInternationalizableProperties().getLib(loc);
                if (j < (taille - 1)) {
                    criterionsList += "\n";
                }
            }

            cell = this.createTableRowCell(criterionsList, null, null, Element.ALIGN_LEFT, i);
            table.addCell(cell);
            i++;
        }

        doc.add(table);
    }

    /**
     * Cr�e la section "Co�t du plan action" donnant le co�t total du plan d'action et le d�tail des crit�res
     * s�lectionn�s, le nombre d'�l�ments qu'ils impactent et le co�t de correction de ces crit�res
     * @param doc Le document
     * @param request La requ�te
     * @param ea L'entit� applicative
     * @param docWriter Le PdfWriter
     * @param resources Les ressources internationalis�es
     * @throws DocumentException
     */
    private void createIntroSection(
            Document doc,
            HttpServletRequest request,
            ElementBean ea,
            PdfWriter docWriter,
            ResourceBundle resources)
            throws DocumentException {
        Locale loc = RequestUtil.getLocale(request);
        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
        ActionPlanBean ap = actionPlanSvc.getCompleteActionPlan(ea, ea.getBaseline().getId(), false, request);
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = ap.getCorrectedElements();
        this.addActionPlanUnitsSection(doc, docWriter, loc, resources);


        if (criterions != null && !criterions.isEmpty()) {
            String sectionTitle = resources.getString("caqs.actionplan.impression.apdefinition");
            this.addSectionTitle(sectionTitle, null, 0.0f, 15.0f, doc, docWriter);

            Paragraph p = null;

            if (ap.getActionPlanComment() != null &&
                    !"".equals(ap.getActionPlanComment())) {
                String commentTitle = resources.getString("caqs.actionplan.impression.apcomment");
                this.addSubSectionTitle(commentTitle, null, 0.0f, 5.0f, doc, docWriter);

                p = new Paragraph();
                p.add(new Chunk(ap.getActionPlanComment(), this.getTextFont()));
                p.setSpacingAfter(10.0f);
                doc.add(p);
            }

            String globalAP = resources.getString("caqs.actionplan.impression.apglobal");
            this.addSubSectionTitle(globalAP, null, 0.0f, 5.0f, doc, docWriter);

            p = new Paragraph();
            p.add(new Chunk(resources.getString("caqs.actionplan.costIntro"), this.getTextFont()));
            p.setSpacingAfter(10.0f);
            doc.add(p);

            float totalCost = 0.0f;

            float[] columnDefinitionSize = {60.0F, 10.0F, 5.0F, 15.0F, 10.0F};
            PdfPTable table = new PdfPTable(columnDefinitionSize);

            table.setWidthPercentage(100);

            PdfPCell cell = null;

            //constitution de l'header
            table.setHeaderRows(1);//la premiere ligne constitue l'header
            String b = resources.getString("caqs.actionplan.critLib");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            b = resources.getString("caqs.actionplan.nbEltImpact");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            b = resources.getString("caqs.actionplan.cost");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            b = resources.getString("caqs.actionplan.percentCost");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            b = resources.getString("caqs.actionplan.priority");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            //recuperation du cout total
            for (ActionPlanCriterionBean criterion : criterions) {
                totalCost += criterion.getCost();
            }

            int i = 0;
            NumberFormat nf = StringFormatUtil.getMarkFormatter(loc);
            NumberFormat intformatter = StringFormatUtil.getIntegerFormatter(loc);

            for (ActionPlanCriterionBean criterion : criterions) {
                cell = this.createTableRowCell(criterion.getInternationalizableProperties().getLib(loc), "crit_" +
                        criterion.getId(), null, Element.ALIGN_LEFT, i);
                table.addCell(cell);

                String format = "" +
                        intformatter.format(criterion.getNumberElt());
                cell = this.createTableRowCell(format, null, null, Element.ALIGN_RIGHT, i);
                table.addCell(cell);

                String costFormat = "" + nf.format(criterion.getCost());
                cell = this.createTableRowCell(costFormat, null, null, Element.ALIGN_RIGHT, i);
                table.addCell(cell);

                double percent = (100.0 * criterion.getCost()) / totalCost;
                cell = this.createTableRowCell(nf.format(percent), null, null, Element.ALIGN_RIGHT, i);
                table.addCell(cell);

                String priorityString = "caqs.actionplan.priority." +
                        criterion.getPriority().toString();
                cell = this.createTableRowCell(resources.getString(priorityString), null, null, Element.ALIGN_CENTER, i);
                table.addCell(cell);

                i++;
            }

            doc.add(table);

            this.addTermCriterionSection(doc, criterions, ActionPlanPriority.SHORT_TERM, totalCost, docWriter, loc, resources);
            this.addTermCriterionSection(doc, criterions, ActionPlanPriority.MEDIUM_TERM, totalCost, docWriter, loc, resources);
            this.addTermCriterionSection(doc, criterions, ActionPlanPriority.LONG_TERM, totalCost, docWriter, loc, resources);
            this.addOtherCommentsSection(doc, ap.getElements(), docWriter, loc, resources);
        }
    }

    private void addActionPlanUnitsSection(
            Document doc,
            PdfWriter docWriter,
            Locale loc,
            ResourceBundle resources)
            throws DocumentException {
        List<ActionPlanUnit> units = ActionPlanSvc.getInstance().retrieveAllActionPlanUnits();

        String title = resources.getString("caqs.actionplan.impression.subsection.units");
        this.addSubSectionTitle(title, null, 10.0f, 5.0f, doc, docWriter);
        Paragraph p = new Paragraph();
        p.add(new Chunk(resources.getString("caqs.actionplan.impression.subsection.units.explanation"), this.getTextFont()));
        doc.add(p);
        for (ActionPlanUnit unit : units) {
            p = new Paragraph();
            p.add(new Chunk(" - "+unit.getNbUnits()+" "+unit.getLib(loc), this.getTextFont()));
            doc.add(p);
        }
        p.setSpacingAfter(10.0f);
    }

    private void addOtherCommentsSection(
            Document doc,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> allCriterions,
            PdfWriter docWriter,
            Locale loc,
            ResourceBundle resources)
            throws DocumentException {

        ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(null, null);
        for (ActionPlanCriterionBean criterion : allCriterions) {
            if (!criterion.isCorrected()) {
                criterions.add(criterion);
            }
        }

        if (!criterions.isEmpty()) {
            String title = resources.getString("caqs.actionplan.impression.subsection.otherComments");
            this.addSubSectionTitle(title, null, 10.0f, 5.0f, doc, docWriter);

            for (ActionPlanCriterionBean criterion : criterions) {
                if (criterion.getComment() == null ||
                        "".equals(criterion.getComment())) {
                    continue;
                }
                Paragraph p = new Paragraph();
                p.add(new Chunk(criterion.getInternationalizableProperties().getLib(loc) +
                        " : ", this.getUnderlinedTextFont()));
                doc.add(p);

                p = new Paragraph();
                p.add(new Chunk(criterion.getComment(), this.getTextFont()));
                p.setSpacingAfter(5.0f);
                doc.add(p);
            }
        }
    }

    private void addTermCriterionSection(
            Document doc,
            ActionPlanElementBeanCollection<ActionPlanCriterionBean> includedCriterions,
            ActionPlanPriority priority,
            double totalCost,
            PdfWriter docWriter,
            Locale loc,
            ResourceBundle resources)
            throws DocumentException {
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = new ActionPlanElementBeanCollection<ActionPlanCriterionBean>(null, null);
        for (ActionPlanCriterionBean criterion : includedCriterions) {
            if (criterion.getPriority().equals(priority)) {
                criterions.add(criterion);
            }
        }

        if (!criterions.isEmpty()) {
            String title = resources.getString("caqs.actionplan.impression.subsection." +
                    priority.toString());
            this.addSubSectionTitle(title, null, 10.0f, 5.0f, doc, docWriter);

            float[] columnDefinitionSize = {60.0F, 10.0F, 15.0F, 15.0F};
            PdfPTable table = new PdfPTable(columnDefinitionSize);

            table.setWidthPercentage(100);

            PdfPCell cell = null;

            //constitution de l'header
            table.setHeaderRows(1);//la premiere ligne constitue l'header
            String b = resources.getString("caqs.actionplan.critLib");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            b = resources.getString("caqs.actionplan.nbEltImpact");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            b = resources.getString("caqs.actionplan.cost");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            b = resources.getString("caqs.actionplan.percentCost");
            cell = this.createTableHeaderCell(b, null, null);
            table.addCell(cell);

            int i = 0;
            NumberFormat nf = StringFormatUtil.getMarkFormatter(loc);
            NumberFormat intformatter = StringFormatUtil.getIntegerFormatter(loc);

            for (ActionPlanCriterionBean criterion : criterions) {
                cell = this.createTableRowCell(criterion.getInternationalizableProperties().getLib(loc), "crit_" +
                        criterion.getId(), null, Element.ALIGN_LEFT, i);
                table.addCell(cell);

                String format = "" +
                        intformatter.format(criterion.getNumberElt());
                cell = this.createTableRowCell(format, null, null, Element.ALIGN_RIGHT, i);
                table.addCell(cell);

                String costFormat = "" + nf.format(criterion.getCost());
                cell = this.createTableRowCell(costFormat, null, null, Element.ALIGN_RIGHT, i);
                table.addCell(cell);

                double percent = (100.0 * criterion.getCost()) / totalCost;
                cell = this.createTableRowCell(nf.format(percent), null, null, Element.ALIGN_RIGHT, i);
                table.addCell(cell);

                i++;
            }

            doc.add(table);

            for (ActionPlanCriterionBean criterion : criterions) {
                if (criterion.getComment() == null ||
                        "".equals(criterion.getComment())) {
                    continue;
                }
                Paragraph p = new Paragraph();
                p.add(new Chunk(criterion.getInternationalizableProperties().getLib(loc) +
                        " : ", this.getUnderlinedTextFont()));
                doc.add(p);

                p = new Paragraph();
                p.add(new Chunk(criterion.getComment(), this.getTextFont()));
                p.setSpacingAfter(5.0f);
                doc.add(p);
            }
        }
    }

    /**
     * Cree le tableau des criteres
     * @param doc Le document
     * @param request La requete
     * @param ea L'entite applicative
     * @param docWriter Le PdfWriter
     * @param resources Les ressources internationalisees
     */
    private void createCriterionsSection(
            Document doc,
            HttpServletRequest request,
            ElementBean ea,
            PdfWriter docWriter,
            ResourceBundle resources)
            throws DocumentException {
        ActionPlanSvc actionPlanSvc = ActionPlanSvc.getInstance();
        ActionPlanBean ap = actionPlanSvc.getCompleteActionPlan(ea, ea.getBaseline().getId(), false, request);
        ActionPlanElementBeanCollection<ActionPlanCriterionBean> criterions = ap.getCorrectedElements();

        Locale loc = RequestUtil.getLocale(request);

        if (criterions != null && !criterions.isEmpty()) {
            String sectionTitle = resources.getString("caqs.actionplan.impression.criterionListTitle");
            this.addSectionTitle(sectionTitle, null, 30.0f, 15.0f, doc, docWriter);

            String idUsa = ea.getUsage().getId();
            MetricSvc metricSvc = MetricSvc.getInstance();

            for (ActionPlanCriterionBean criterion : criterions) {
                Paragraph titleParagraph = new Paragraph();
                Chunk chk = new Chunk(criterion.getInternationalizableProperties().getLib(loc), FontFactory.getFont(FontFactory.TIMES, 14, Font.BOLD));
                chk.setLocalDestination("crit_" + criterion.getId());
                titleParagraph.add(chk);
                doc.add(titleParagraph);
                this.addBookmark(docWriter, criterion.getId());

                Paragraph p = new Paragraph();
                p.setAlignment(Element.ALIGN_JUSTIFIED);
                Chunk desc = new Chunk(resources.getString("caqs.actionplan.impression.description"), FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD));
                p.add(new Phrase(desc));
                this.addMultiLinedTextToParagraph(" : " +
                        criterion.getInternationalizableProperties().getDesc(loc), p, doc);
                doc.add(p);

                String compl = criterion.getInternationalizableProperties().getComplement(loc);
                if (!"".equals(compl) && compl != null) {
                    p = new Paragraph();
                    p.setAlignment(Element.ALIGN_JUSTIFIED);
                    desc = new Chunk(resources.getString("caqs.actionplan.impression.compl"), FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD));
                    p.add(new Phrase(desc));
                    this.addMultiLinedTextToParagraph(" : " + compl, p, doc);
                    doc.add(p);
                }

                Collection<MetriqueDefinitionBean> metriques = metricSvc.retrieveMetriqueDefinitionByIdCrit(criterion.getId(), idUsa);
                if (metriques != null && !metriques.isEmpty()) {
                    desc = new Chunk(resources.getString("caqs.actionplan.impression.listeMetriques"), FontFactory.getFont(FontFactory.TIMES, 12, Font.BOLDITALIC));
                    p = new Paragraph();
                    p.add(new Phrase(desc));
                    doc.add(p);
                    for (MetriqueDefinitionBean metrique : metriques) {
                        p = new Paragraph();
                        desc = new Chunk(metrique.getLib(loc), FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD));
                        p.add(new Phrase(desc));
                        doc.add(p);
                        p = new Paragraph();
                        p.setAlignment(Element.ALIGN_JUSTIFIED);
                        desc = new Chunk(resources.getString("caqs.actionplan.impression.descMet"), FontFactory.getFont(FontFactory.TIMES, 10, Font.UNDERLINE));
                        p.add(new Phrase(desc));
                        this.addMultiLinedTextToParagraph(" : " +
                                metrique.getDesc(loc), p, doc);
                        doc.add(p);
                        compl = metrique.getComplement(loc);
                        if (compl != null && !"".equals(compl)) {
                            p = new Paragraph();
                            p.setAlignment(Element.ALIGN_JUSTIFIED);
                            desc = new Chunk(resources.getString("caqs.actionplan.impression.complMet"), FontFactory.getFont(FontFactory.TIMES, 10, Font.UNDERLINE));
                            p.add(new Phrase(desc));
                            this.addMultiLinedTextToParagraph(" : " + compl, p, doc);
                            doc.add(p);
                        }
                    }
                }

                this.addSpacingAfter(15.0f, doc);
            }
        }
    }

    private void addMultiLinedTextToParagraph(String line, Paragraph p, Document doc)
            throws DocumentException {
        String[] lines = line.split("<BR\\s*/?+>");
        if (lines != null) {
            for (int i = 0; i < lines.length; i++) {
                p.add(new Chunk(lines[i], FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL)));
                if (i < (lines.length - 1)) {
                    p.add(Chunk.NEWLINE);
                }
            }
        }
    }

    private void createKiviatSection(
            Document doc,
            HttpServletRequest request,
            ElementBean ea,
            PdfWriter docWriter,
            ResourceBundle resources)
            throws DocumentException {

        String sectionTitle = resources.getString("caqs.actionplan.impression.kiviatTitle");
        this.addSectionTitle(sectionTitle, null, 30.0f, 15.0f, doc, docWriter);

        Paragraph descParagraph = new Paragraph();
        descParagraph.setAlignment(Element.ALIGN_JUSTIFIED);
        Chunk chk = new Chunk(resources.getString("caqs.actionplan.impression.descKiviat"),
                this.getItalicTextFont());
        descParagraph.add(chk);
        doc.add(descParagraph);
        this.addKiviat(doc, request, ea, docWriter, ActionPlanPriority.SHORT_TERM, "caqs.actionplan.impression.kiviatTitleC", resources);
        this.addKiviat(doc, request, ea, docWriter, ActionPlanPriority.MEDIUM_TERM, "caqs.actionplan.impression.kiviatTitleCM", resources);
        this.addKiviat(doc, request, ea, docWriter, ActionPlanPriority.LONG_TERM, "caqs.actionplan.impression.kiviatTitleCML", resources);
    }

    private void addKiviat(
            Document doc,
            HttpServletRequest request,
            ElementBean ea,
            PdfWriter docWriter,
            ActionPlanPriority maxPriority,
            String captionKey,
            ResourceBundle resources)
            throws DocumentException {
        File kiviatFile = ActionPlanKiviatUtils.getInstance().getKiviatTemporaryFile(
                ea, ea.getBaseline().getId(), maxPriority, null, 300, 300, false, true, request);
        if (kiviatFile != null && kiviatFile.exists()) {
            try {
                Image img = Image.getInstance(kiviatFile.getAbsolutePath());
                if (img != null) {
                    docWriter.setStrictImageSequence(true);
                    img.setAlignment(Element.ALIGN_CENTER);
                    doc.add(img);
                    Chunk chk = new Chunk(resources.getString(captionKey));
                    chk.setFont(this.getBoldTextFont());
                    Paragraph p = new Paragraph();
                    p.setAlignment(Element.ALIGN_CENTER);
                    p.add(chk);
                    doc.add(p);
                }
            } catch (IOException exc) {
                logger.error("Error inserting kiviat file into action plan report", exc);
            }
        }
    }

    private Font getTextFont() {
        return FontFactory.getFont(FontFactory.TIMES, 10, Font.NORMAL);
    }

    private Font getBoldTextFont() {
        return FontFactory.getFont(FontFactory.TIMES, 10, Font.BOLD);
    }

    private Font getItalicTextFont() {
        return FontFactory.getFont(FontFactory.TIMES, 10, Font.ITALIC);
    }

    private Font getUnderlinedTextFont() {
        return FontFactory.getFont(FontFactory.TIMES, 10, Font.UNDERLINE);
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
        retour = new PdfPCell(p);
        retour.setHorizontalAlignment(Element.ALIGN_CENTER);
        retour.setVerticalAlignment(Element.ALIGN_MIDDLE);
        retour.setGrayFill(this.headerColor);
        return retour;
    }

    private PdfPCell createTableRowCell(String txt, String localGoTo, String localDestination, int horizontalAlignment, int rowIndex) {
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 9, Font.NORMAL);

        if (txt == null) {
            txt = "";
        }


        PdfPCell retour = null;

        Chunk chk = new Chunk(txt, textFont);
        if (localGoTo != null && !"".equals(localGoTo)) {
            chk.setLocalGoto(localGoTo);
        }
        if (localDestination != null && !"".equals(localDestination)) {
            chk.setLocalDestination(localDestination);
        }
        Paragraph p = new Paragraph();
        p.add(chk);
        retour = new PdfPCell(p);
        retour.setHorizontalAlignment(horizontalAlignment);
        retour.setVerticalAlignment(Element.ALIGN_MIDDLE);
        float color = ((rowIndex % 2) == 0) ? this.evenRowColor : this.oddRowColor;
        retour.setGrayFill(color);
        return retour;
    }

    private void addSectionTitle(String txt, String localDestination, float spacingBefore,
            float spacingAfter, Document doc, PdfWriter docWriter) {
        try {
            Paragraph retour = new Paragraph();

            Chunk chk = new Chunk(txt, FontFactory.getFont(FontFactory.HELVETICA, 14, Font.BOLDITALIC));
            if (localDestination != null) {
                chk.setLocalDestination(localDestination);
            }
            retour.add(chk);
            retour.setAlignment(Element.ALIGN_LEFT);
            if (spacingBefore > 0.0f) {
                retour.setSpacingBefore(spacingBefore);
            }
            if (spacingAfter > 0.0f) {
                retour.setSpacingAfter(spacingAfter);
            }
            this.addBookmark(docWriter, txt);
            doc.add(retour);
        } catch (DocumentException e) {
            logger.error("Error creating section title");
        }
    }

    private void addSubSectionTitle(String txt, String localDestination, float spacingBefore,
            float spacingAfter, Document doc, PdfWriter docWriter) {
        try {
            Paragraph retour = new Paragraph();

            Chunk chk = new Chunk(txt, FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
            if (localDestination != null) {
                chk.setLocalDestination(localDestination);
            }
            retour.add(chk);
            retour.setAlignment(Element.ALIGN_LEFT);
            if (spacingBefore > 0.0f) {
                retour.setSpacingBefore(spacingBefore);
            }
            if (spacingAfter > 0.0f) {
                retour.setSpacingAfter(spacingAfter);
            }
            doc.add(retour);
        } catch (DocumentException e) {
            logger.error("Error creating section title");
        }
    }

    private void addBookmark(PdfWriter docWriter, String bookmark) {
        PdfDestination destination = new PdfDestination(PdfDestination.FITH);
        new PdfOutline(docWriter.getDirectContent().getRootOutline(), destination, bookmark);
    }

    private void addSpacingAfter(float s, Document doc) throws DocumentException {
        Paragraph p = new Paragraph();
        p.setSpacingAfter(s);
        doc.add(p);
    }
    /*
    private void addSpacingBefore(float s, Document doc) throws DocumentException {
    Paragraph p = new Paragraph();
    p.setSpacingBefore(s);
    doc.add(p);
    }
     */

    private class EventHandler extends PdfPageEventHelper {

        BaseFont f = null;

        @Override
        public void onOpenDocument(PdfWriter writer, Document document) {
            try {
                f = BaseFont.createFont("Helvetica", BaseFont.WINANSI, false);
            } catch (DocumentException e) {
                logger.error("Error while printing action plan :" +
                        e.getMessage());
            } catch (IOException e) {
                logger.error("Error while printing action plan :" +
                        e.getMessage());
            }
        }

        @Override
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

        @Override
        public void onCloseDocument(PdfWriter writer, Document document) {
            tpl.beginText();
            tpl.setFontAndSize(f, 10);
            tpl.setTextMatrix(0, 0);
            tpl.showText("" + (writer.getPageNumber() - 1));
            tpl.endText();
        }
    }
}
