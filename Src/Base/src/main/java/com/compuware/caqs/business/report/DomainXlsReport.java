package com.compuware.caqs.business.report;

import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.ElementType;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.actionplan.ActionPlanPriority;
import com.compuware.caqs.domain.dataschemas.actionplan.ApplicationEntityActionPlanBean;
import com.compuware.caqs.domain.dataschemas.comparators.FactorBeanComparator;
import com.compuware.caqs.service.ActionPlanSvc;
import com.compuware.caqs.service.ElementSvc;
import com.compuware.caqs.service.MetricSvc;
import com.compuware.caqs.service.SyntheseSvc;
import com.compuware.toolbox.util.math.Deviation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.util.MessageResources;

/**
 *
 * @author cwfr-dzysman
 */
public class DomainXlsReport {

    private CellStyle columnHeaderCellStyle = null;
    private int projectsTableStartRowNumber = 2;
    private CellStyle numberCellStyle = null;
    private CellStyle percentageCellStyle = null;
    private CellStyle decimalCellStyle = null;
    private CellStyle dateCellStyle = null;
    private CellStyle textCellStyle = null;
    private CellStyle paleBlueTextCellStyle = null;
    private CellStyle paleBlueRedTextCellStyle = null;
    private CellStyle redTextCellStyle = null;
    private CellStyle yellowNumberCellStyle = null;
    private CellStyle yellowDecimalCellStyle = null;
    private CellStyle paleBlueDecimalCellStyle = null;
    private DataFormat format = null;
    private Workbook workbook = null;
    private Map<String, Double> linesOfCode = null;
    private double totalShortTerm = 0.0;
    private double totalMediumTerm = 0.0;
    private double totalLongTerm = 0.0;
    private String domainId = null;
    private Locale locale = null;
    private String userId = null;
    private Sheet worksheet = null;
    private MessageResources resources = null;
    private int actionPlanDetailColumnNumber = 0;
    private int locColumnNumber = 0;
    private List<FactorBean> goals = new ArrayList<FactorBean>();
    private Map<String, Map<String, Double>> projectsKiviats = new HashMap<String, Map<String, Double>>();
    private Map<String, Double> averages = new HashMap<String, Double>();
    int firstGoalCell = -1;
    /**
     * tous les plans d'actions niveau ea par projet, pour calcul des couts
     */
    private Map<String, List<ApplicationEntityActionPlanBean>> actionsPlans = null;

    public DomainXlsReport(String di, String userId, Locale locale, MessageResources resources) {
        this.domainId = di;
        this.locale = locale;
        this.userId = userId;
        this.resources = resources;
    }

    private void addCellStyleBorder(CellStyle style) {
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
    }

    private CellStyle getPaleBlueTextStyle() {
        if (this.paleBlueTextCellStyle == null) {
            this.paleBlueTextCellStyle = this.workbook.createCellStyle();
            this.addCellStyleBorder(this.paleBlueTextCellStyle);
            this.paleBlueTextCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            this.paleBlueTextCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            this.paleBlueTextCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        return this.paleBlueTextCellStyle;
    }

    private CellStyle getPaleBlueRedTextStyle() {
        if (this.paleBlueRedTextCellStyle == null) {
            this.paleBlueRedTextCellStyle = this.workbook.createCellStyle();
            this.addCellStyleBorder(this.paleBlueRedTextCellStyle);
            this.paleBlueRedTextCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            this.paleBlueRedTextCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            this.paleBlueRedTextCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            //la font
            Font paleBlueRedFont = this.workbook.createFont();
            paleBlueRedFont.setFontHeightInPoints((short) 10);
            paleBlueRedFont.setColor(IndexedColors.RED.getIndex());
            paleBlueRedFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            this.paleBlueRedTextCellStyle.setFont(paleBlueRedFont);
        }
        return this.paleBlueRedTextCellStyle;
    }

    private CellStyle getRedTextStyle() {
        if (this.redTextCellStyle == null) {
            this.redTextCellStyle = this.workbook.createCellStyle();
            this.addCellStyleBorder(this.redTextCellStyle);
            this.redTextCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            //la font
            Font columnHeaderFont = this.workbook.createFont();
            columnHeaderFont.setFontHeightInPoints((short) 10);
            columnHeaderFont.setColor(IndexedColors.RED.getIndex());
            columnHeaderFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            this.redTextCellStyle.setFont(columnHeaderFont);
        }
        return this.redTextCellStyle;
    }

    private CellStyle getTextStyle() {
        if (this.textCellStyle == null) {
            this.textCellStyle = this.workbook.createCellStyle();
            this.addCellStyleBorder(this.textCellStyle);
            this.textCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        }
        return this.textCellStyle;
    }

    private CellStyle getDateStyle() {
        if (this.dateCellStyle == null) {
            this.dateCellStyle = this.workbook.createCellStyle();
            this.dateCellStyle.setDataFormat(this.format.getFormat(resources.getMessage(locale, "caqs.dateFormat")));
            this.addCellStyleBorder(this.dateCellStyle);
            this.dateCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        }
        return this.dateCellStyle;
    }

    private CellStyle getDecimalStyle() {
        if (this.decimalCellStyle == null) {
            this.decimalCellStyle = this.workbook.createCellStyle();
            this.decimalCellStyle.setDataFormat(this.format.getFormat("#,##0.00"));
            this.addCellStyleBorder(this.decimalCellStyle);
            this.decimalCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        }
        return this.decimalCellStyle;
    }

    private CellStyle getPaleBlueDecimalStyle() {
        if (this.paleBlueDecimalCellStyle == null) {
            this.paleBlueDecimalCellStyle = this.workbook.createCellStyle();
            this.paleBlueDecimalCellStyle.setDataFormat(this.format.getFormat("#,##0.00"));
            this.addCellStyleBorder(this.paleBlueDecimalCellStyle);
            this.paleBlueDecimalCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            this.paleBlueDecimalCellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
            this.paleBlueDecimalCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        return this.paleBlueDecimalCellStyle;
    }

    private CellStyle getYellowDecimalStyle() {
        if (this.yellowDecimalCellStyle == null) {
            this.yellowDecimalCellStyle = this.workbook.createCellStyle();
            this.yellowDecimalCellStyle.setDataFormat(this.format.getFormat("#,##0.00"));
            this.addCellStyleBorder(this.yellowDecimalCellStyle);
            this.yellowDecimalCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            this.yellowDecimalCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            this.yellowDecimalCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        return this.yellowDecimalCellStyle;
    }

    private CellStyle getPercentageStyle() {
        if (this.percentageCellStyle == null) {
            this.percentageCellStyle = this.workbook.createCellStyle();
            this.percentageCellStyle.setDataFormat(this.format.getFormat("0.00%"));
            this.addCellStyleBorder(this.percentageCellStyle);
            this.percentageCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        }
        return this.percentageCellStyle;
    }

    private CellStyle getYellowNumberStyle() {
        if (this.yellowNumberCellStyle == null) {
            this.yellowNumberCellStyle = this.workbook.createCellStyle();
            this.yellowNumberCellStyle.setDataFormat(this.format.getFormat("#,##0"));
            this.addCellStyleBorder(this.yellowNumberCellStyle);
            this.yellowNumberCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
            this.yellowNumberCellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            this.yellowNumberCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        }
        return this.yellowNumberCellStyle;
    }

    private CellStyle getNumberStyle() {
        if (this.numberCellStyle == null) {
            this.numberCellStyle = this.workbook.createCellStyle();
            this.numberCellStyle.setDataFormat(this.format.getFormat("#,##0"));
            this.addCellStyleBorder(this.numberCellStyle);
            this.numberCellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        }
        return this.numberCellStyle;
    }

    private CellStyle getColumnHeaderStyle() {
        if (this.columnHeaderCellStyle == null) {
            this.columnHeaderCellStyle = this.workbook.createCellStyle();
            this.columnHeaderCellStyle.setFillForegroundColor(IndexedColors.BLUE.getIndex());
            this.columnHeaderCellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            this.columnHeaderCellStyle.setWrapText(true);
            // on cree la police des entete de colonne : taille 10, gras, blanche sur fond bleu
            Font columnHeaderFont = this.workbook.createFont();
            columnHeaderFont.setFontHeightInPoints((short) 10);
            columnHeaderFont.setColor(IndexedColors.WHITE.getIndex());
            columnHeaderFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
            this.columnHeaderCellStyle.setFont(columnHeaderFont);
            this.addCellStyleBorder(this.columnHeaderCellStyle);
            this.columnHeaderCellStyle.setAlignment(CellStyle.ALIGN_CENTER);
            this.columnHeaderCellStyle.setWrapText(true);
        }
        return this.columnHeaderCellStyle;
    }

    private void generateStyles(Workbook wb) {
        this.format = wb.createDataFormat();
    }

    public Workbook generateWorkbook() {
        // create a new workbook
        this.workbook = new HSSFWorkbook();
        this.worksheet = this.workbook.createSheet();
        this.generateStyles(this.workbook);

        List<ElementBean> projects = ElementSvc.getInstance().retrieveAllElementsForTypeBelongingToParentByUser(this.domainId, this.userId,
                ElementType.PRJ, true);
        //on cree une map qui contiendra toutes les lignes de code pour chaque projet
        double totalLinesOfCode = 0.0;
        this.linesOfCode = new HashMap<String, Double>();
        this.actionsPlans = new HashMap<String, List<ApplicationEntityActionPlanBean>>();
        for (ElementBean project : projects) {
            //lignes de codes
            double l = MetricSvc.getInstance().getAllCodeValue(project);
            this.linesOfCode.put(project.getId(), l);
            totalLinesOfCode += l;

            //plans d'actions
            List<ElementBean> eas = ElementSvc.getInstance().retrieveAllApplicationEntitiesForProject(project);
            List<ApplicationEntityActionPlanBean> aps = new ArrayList<ApplicationEntityActionPlanBean>();
            for (ElementBean ea : eas) {
                ApplicationEntityActionPlanBean ap = (ApplicationEntityActionPlanBean) ActionPlanSvc.getInstance().getCompleteActionPlan(ea,
                        ea.getBaseline().getId(), false, null);
                if (ap != null) {
                    aps.add(ap);
                }
            }
            this.actionsPlans.put(project.getId(), aps);
            this.retrieveGoalsData(project);
        }

        //on trie les objectifs recuperes
        Collections.sort(this.goals, new FactorBeanComparator(false, "lib", this.locale));

        //on cree le deuxieme tableau.
        //il contient les informations des projets fils
        //on cree la ligne d'entete
        Row entete = this.worksheet.createRow(1);
        this.createProjectTableHeader(entete);
        this.createProjectsTable(projects, totalLinesOfCode);
        return this.workbook;
    }

    private void retrieveGoalsData(ElementBean project) {
        List<FactorBean> fList = SyntheseSvc.getInstance().retrieveKiviat(project, project.getBaseline().getId());
        if (fList != null) {
            Map<String, Double> projectKiviat = new HashMap<String, Double>();
            for (FactorBean goal : fList) {
                //est-il dans la liste globale des objectifs ?
                boolean found = false;
                for (FactorBean goalInList : this.goals) {
                    if (goalInList.getId().equals(goal.getId())) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    this.goals.add(goal);
                }
                projectKiviat.put(goal.getId(), goal.getNote());
            }
            this.projectsKiviats.put(project.getId(), projectKiviat);
        }
    }

    private void createProjectTableHeader(Row r) {
        String[] headers = {
            "project", "lastbaseline", "loc", "locPct", "apShort", "apMedium", "apLong", "apTotal", "apTotalPct"
        };
        Cell c = null;
        for (int i = 0; i < headers.length; i++) {
            c = r.createCell(i);
            c.setCellStyle(this.getColumnHeaderStyle());
            c.setCellValue(this.resources.getMessage(this.locale, "caqs.domainsynthesis.report."
                    + headers[i]));
        }

        int lastGoalCell = 0;
        //on ajoute les objectifs
        for (FactorBean goal : this.goals) {
            c = r.createCell(r.getLastCellNum());
            if (this.firstGoalCell == -1) {
                this.firstGoalCell = c.getColumnIndex();
            }
            c.setCellStyle(this.getColumnHeaderStyle());
            c.setCellValue(goal.getLib(this.locale));
            //plus difference ecart type
            c = r.createCell(r.getLastCellNum());
            c.setCellStyle(this.getColumnHeaderStyle());
            c.setCellValue(this.resources.getMessage(this.locale, "caqs.domainsynthesis.report.diffAvgGreater"));
        }
        c = r.createCell(r.getLastCellNum());
        c.setCellStyle(this.getColumnHeaderStyle());
        c.setCellValue(this.resources.getMessage(this.locale, "caqs.domainsynthesis.report.avg"));
        lastGoalCell = c.getColumnIndex();

        //premier entete
        Row firstHeader = this.worksheet.createRow(r.getRowNum() - 1);
        c = firstHeader.createCell(2);
        c.setCellStyle(this.getColumnHeaderStyle());
        c.setCellValue(this.resources.getMessage(this.locale, "caqs.domainsynthesis.report.locTitle"));
        this.worksheet.addMergedRegion(new CellRangeAddress(
                firstHeader.getRowNum(), //first row (0-based)
                firstHeader.getRowNum(), //last row  (0-based)
                2, //first column (0-based)
                3 //last column  (0-based)
                ));
        c = firstHeader.createCell(4);
        c.setCellStyle(this.getColumnHeaderStyle());
        c.setCellValue(this.resources.getMessage(this.locale, "caqs.domainsynthesis.report.apTitle"));
        this.worksheet.addMergedRegion(new CellRangeAddress(
                firstHeader.getRowNum(), //first row (0-based)
                firstHeader.getRowNum(), //last row  (0-based)
                4, //first column (0-based)
                8 //last column  (0-based)
                ));
        c = firstHeader.createCell(this.firstGoalCell);
        c.setCellStyle(this.getColumnHeaderStyle());
        c.setCellValue(this.resources.getMessage(this.locale, "caqs.domainsynthesis.report.goalTitle"));
        this.worksheet.addMergedRegion(new CellRangeAddress(
                firstHeader.getRowNum(), //first row (0-based)
                firstHeader.getRowNum(), //last row  (0-based)
                this.firstGoalCell, //first column (0-based)
                lastGoalCell //last column  (0-based)
                ));

    }

    private void createProjectRow(Row row,
            ElementBean project,
            double totalLinesOfCodes) {
        //nom du projet
        Cell c = row.createCell(0);
        c.setCellValue(project.getLib());
        c.setCellStyle(this.getTextStyle());

        //derniere analyse
        c = row.createCell(row.getLastCellNum());
        c.setCellStyle(this.getDateStyle());
        c.setCellValue(project.getBaseline().getDmaj());

        //nombre de lignes de code
        c = row.createCell(row.getLastCellNum());
        Double l = this.linesOfCode.get(project.getId());
        c.setCellStyle(this.getYellowNumberStyle());
        c.setCellValue(l);
        this.locColumnNumber = c.getColumnIndex();

        //pourcentage du total de lignes de code
        c = row.createCell(row.getLastCellNum());
        c.setCellStyle(this.getPercentageStyle());
        double pct = l / totalLinesOfCodes;
        c.setCellValue(pct);

        List<ApplicationEntityActionPlanBean> aps = this.actionsPlans.get(project.getId());
        double totalProjectShortTermCost = 0.0;
        double totalProjectMediumTermCost = 0.0;
        double totalProjectLongTermCost = 0.0;
        for (ApplicationEntityActionPlanBean ap : aps) {
            totalProjectShortTermCost += ap.getCorrectionCost(ActionPlanPriority.SHORT_TERM);
            totalProjectMediumTermCost += ap.getCorrectionCost(ActionPlanPriority.MEDIUM_TERM);
            totalProjectLongTermCost += ap.getCorrectionCost(ActionPlanPriority.LONG_TERM);
        }
        this.totalLongTerm += totalProjectLongTermCost;
        this.totalMediumTerm += totalProjectMediumTermCost;
        this.totalShortTerm += totalProjectShortTermCost;

        //coût court terme
        c = row.createCell(row.getLastCellNum());
        c.setCellStyle(this.getYellowDecimalStyle());
        c.setCellValue(totalProjectShortTermCost);
        this.actionPlanDetailColumnNumber = c.getColumnIndex();

        //coût moyen terme
        c = row.createCell(row.getLastCellNum());
        c.setCellStyle(this.getDecimalStyle());
        c.setCellValue(totalProjectMediumTermCost);

        //coût long terme
        c = row.createCell(row.getLastCellNum());
        c.setCellStyle(this.getDecimalStyle());
        c.setCellValue(totalProjectLongTermCost);

        //coût total terme
        c = row.createCell(row.getLastCellNum());
        c.setCellStyle(this.getDecimalStyle());
        c.setCellValue((totalProjectShortTermCost + totalProjectMediumTermCost
                + totalProjectLongTermCost));

        //section des objectifs
        double avgScore = 0.0;
        Map<String, Double> projectKiviat = this.projectsKiviats.get(project.getId());
        int i = row.getLastCellNum() + 1;//plus 1 car on met le pourcentage avant cette colonne, mais apres cette methode
        int nbGoal = 0;
        for (FactorBean goal : this.goals) {
            Double score = projectKiviat.get(goal.getId());
            Cell c1 = row.createCell(i);
            i++;
            Cell c2 = row.createCell(i);
            if ((nbGoal % 2) == 0) {
                c1.setCellStyle(this.getPaleBlueDecimalStyle());
                c2.setCellStyle(this.getPaleBlueTextStyle());
            } else {
                c1.setCellStyle(this.getDecimalStyle());
                c2.setCellStyle(this.getTextStyle());
            }
            this.addCellStyleBorder(c2.getCellStyle());
            i++;
            if (score != null) {
                avgScore += score.doubleValue();
                c1.setCellValue(score);
                c2.setCellValue(resources.getMessage(this.locale, "caqs.domainsynthesis.report.notGreaterToDeviation"));
            }
            nbGoal++;
        }
        //puis on place la moyenne
        avgScore = avgScore / projectKiviat.size();
        c = row.createCell(row.getLastCellNum());
        c.setCellStyle(this.getDecimalStyle());
        c.setCellValue(avgScore);
    }

    private void createActionsPlanPercentageRatio(List<ElementBean> projects) {
        int i = 0;
        double totalCost = this.totalLongTerm + this.totalMediumTerm
                + this.totalShortTerm;
        for (ElementBean project : projects) {
            Row r = this.worksheet.getRow(this.projectsTableStartRowNumber + i);
            Cell c = r.createCell(this.actionPlanDetailColumnNumber + 4);
            c.setCellStyle(this.getPercentageStyle());
            Cell totalCostCell = r.getCell(this.actionPlanDetailColumnNumber + 3);
            double pct = totalCostCell.getNumericCellValue() / totalCost;
            c.setCellValue(pct);
            i++;
        }
    }

    private void createGoalAverage(Row totalRow) {
        int i = 0;
        for (FactorBean goal : this.goals) {
            double avg = 0.0;
            int nbElts = 0;

            for (Map<String, Double> kiviat : this.projectsKiviats.values()) {
                Double d = kiviat.get(goal.getId());
                if (d != null) {
                    avg += d.doubleValue();
                    nbElts++;
                }
            }

            avg /= nbElts;
            Cell c = totalRow.createCell(this.firstGoalCell + i);
            c.setCellStyle(this.getDecimalStyle());
            c.setCellValue(avg);
            c = totalRow.createCell(this.firstGoalCell + i + 1);
            c.setCellStyle(this.getDecimalStyle());
            this.averages.put(goal.getId(), avg);
            this.worksheet.addMergedRegion(new CellRangeAddress(
                    totalRow.getRowNum(), //first row (0-based)
                    totalRow.getRowNum(), //last row  (0-based)
                    this.firstGoalCell + i, //first column (0-based)
                    this.firstGoalCell + i + 1 //last column  (0-based)
                    ));
            i += 2;
        }
    }

    private void createProjectsTable(List<ElementBean> projects,
            double totalLoc) {
        Row row = null;
        int i = 0;

        for (ElementBean project : projects) {
            row = this.worksheet.createRow(this.projectsTableStartRowNumber + i);
            this.createProjectRow(row, project, totalLoc);
            i++;
        }
        this.createActionsPlanPercentageRatio(projects);

        //on crée la ligne total
        row = this.worksheet.createRow(this.projectsTableStartRowNumber
                + i);
        Cell c = row.createCell(0);
        c.setCellValue("Total");
        c.setCellStyle(this.getTextStyle());
        //total de lignes de code
        c = row.createCell(this.locColumnNumber);
        c.setCellStyle(this.getNumberStyle());
        c.setCellValue(totalLoc);
        //total de court terme
        c = row.createCell(this.actionPlanDetailColumnNumber);
        c.setCellStyle(this.getDecimalStyle());
        c.setCellValue(this.totalShortTerm);
        //total de moyen terme
        c = row.createCell(this.actionPlanDetailColumnNumber + 1);
        c.setCellStyle(this.getDecimalStyle());
        c.setCellValue(this.totalMediumTerm);
        //total de long terme
        c = row.createCell(this.actionPlanDetailColumnNumber + 2);
        c.setCellStyle(this.getDecimalStyle());
        c.setCellValue(this.totalLongTerm);
        //total de plan d'actions
        c = row.createCell(this.actionPlanDetailColumnNumber + 3);
        c.setCellStyle(this.getDecimalStyle());
        c.setCellValue((this.totalShortTerm + this.totalMediumTerm
                + this.totalLongTerm));
        this.createGoalAverage(row);

        //ecart type
        Row deviationRow = this.worksheet.createRow(row.getRowNum() + 1);
        this.createDeviationRow(deviationRow, row, projects);
    }

    private void createDeviationRow(Row deviationRow, Row averageRow, List<ElementBean> projects) {
        Cell c = deviationRow.createCell(this.firstGoalCell - 1);
        c.setCellStyle(this.getColumnHeaderStyle());
        c.setCellValue(this.resources.getMessage(this.locale, "caqs.domainsynthesis.report.deviation"));

        //on calcule l'ecart type pour chaque objectif
        List<Double> scores = new ArrayList<Double>();
        int i = 0;
        int nbGoal = 0;
        for (FactorBean goal : this.goals) {
            for (Map<String, Double> kiviat : this.projectsKiviats.values()) {
                Double d = kiviat.get(goal.getId());
                if (d != null) {
                    scores.add(d);
                }
            }

            double deviation = Deviation.deviation(scores.toArray(new Double[scores.size()]));
            c = deviationRow.createCell(this.firstGoalCell + i);
            c.setCellStyle(this.getDecimalStyle());
            c.setCellValue(deviation);
            c = deviationRow.createCell(this.firstGoalCell + i + 1);
            c.setCellStyle(this.getDecimalStyle());
            this.worksheet.addMergedRegion(new CellRangeAddress(
                    deviationRow.getRowNum(), //first row (0-based)
                    deviationRow.getRowNum(), //last row  (0-based)
                    this.firstGoalCell + i, //first column (0-based)
                    this.firstGoalCell + i + 1 //last column  (0-based)
                    ));
            scores.clear();
            //maintenant, on met a jour la difference avec la deviation
            //pour chaque projet
            int j = 0;
            for (ElementBean projet : projects) {
                Row prjRow = this.worksheet.getRow(this.projectsTableStartRowNumber
                        + j);
                Map<String, Double> projectKiviat = this.projectsKiviats.get(projet.getId());
                if (projectKiviat.get(goal.getId()) != null) {
                    double prjScore = projectKiviat.get(goal.getId());
                    double avgScore = this.averages.get(goal.getId());
                    //superieur ?
                    if ((prjScore < (avgScore - deviation)) || (prjScore > (avgScore
                            + deviation))) {
                        Cell tmpCell = prjRow.getCell(this.firstGoalCell + i + 1);
                        if ((nbGoal % 2) == 0) {
                            tmpCell.setCellStyle(this.getPaleBlueRedTextStyle());
                        } else {
                            tmpCell.setCellStyle(this.getRedTextStyle());
                        }
                        tmpCell.setCellValue(resources.getMessage(this.locale, "caqs.domainsynthesis.report.greaterToDeviation"));
                    }
                }
                j++;
            }
            nbGoal++;
            i += 2;
        }
    }
}
