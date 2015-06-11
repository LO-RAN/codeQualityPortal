package com.compuware.caqs.presentation.consult.forms;

/**
 *
 *
 */
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;

import com.compuware.caqs.constants.Constants;
import com.compuware.toolbox.util.logging.LoggerManager;

public final class ElementCriterionForm extends ActionForm {

    /**
     *
     */
    private static final long serialVersionUID = -2076537146657308953L;
    private static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);
    private ElementForm element;
    private String[] metrics;
    private String note;
    private boolean needJustification = false;
    private JustificationForm justificatif;
    private String tendance;
    private String tendanceLib;

    /**
     * @return Returns the element.
     */
    public ElementForm getElement() {
        return element;
    }

    /**
     * @param element The element to set.
     */
    public void setElement(ElementForm element) {
        this.element = element;
    }

    /**
     * @return Returns the justificatif.
     */
    public JustificationForm getJustificatif() {
        return justificatif;
    }

    /**
     * @param justificatif The justificatif to set.
     */
    public void setJustificatif(JustificationForm justificatif) {
        this.justificatif = justificatif;
    }

    /**
     * @return Returns the metrics.
     */
    public String[] getMetrics() {
        return metrics;
    }

    /**
     * @param metrics The metrics to set.
     */
    public void setMetrics(String[] metrics) {
        this.metrics = metrics;
    }

    /**
     * @return Returns the note.
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note The note to set.
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return Returns the tendance.
     */
    public String getTendance() {
        return tendance;
    }

    /**
     * @param tendance The tendance to set.
     */
    public void setTendance(String tendance) {
        this.tendance = tendance;
    }

    /**
     * @return Returns the tendanceLib.
     */
    public String getTendanceLib() {
        return tendanceLib;
    }

    /**
     * @param tendanceLib The tendanceLib to set.
     */
    public void setTendanceLib(String tendanceLib) {
        this.tendanceLib = tendanceLib;
    }

    /**
     * @return Returns the needJustification.
     */
    public boolean getNeedJustification() {
        return needJustification;
    }

    /**
     * @param needJustification The needJustification to set.
     */
    public void setNeedJustification(boolean needJustification) {
        this.needJustification = needJustification;
    }

    public Locale getLocale(HttpServletRequest request) {

        HttpSession session = request.getSession();
        Locale locale = (Locale) session.getAttribute(Globals.LOCALE_KEY);
        if (locale == null) {
            locale = Locale.getDefault();
        }
        return locale;
    }
}
