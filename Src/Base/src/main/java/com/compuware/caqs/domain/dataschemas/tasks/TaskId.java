package com.compuware.caqs.domain.dataschemas.tasks;

import java.util.ArrayList;
import java.util.List;

public enum TaskId {

    JUSTIF_REJECTED(false, false, false),
    JUSTIF_ACCEPTED(false, false, true),
    JUSTIF_CANCELLED(false, false, true),
    NEW_JUSTIF(false, false, false),
    COMPUTING(false, false, false),
    GENERATING_REPORT(false, true, false),
    EXPORT_MODEL(false, true, false),
    EXPORT_PROJECT(false, true, false),
    ANALYSING(false, true, false),
    IMPORT_MODEL(false, false, false),
    IMPORT_PROJECT(false, false, false),
    DELETE_ELEMENTS(true, false, false),
    DATA_UPLOAD(false, false, true),
    UNDEFINED(false, false, false);
    /**
     * indique si la servlet de lancement de tache peut lancer cette tache
     */
    private boolean processable = false;
    /**
     * indique si un message special peut etre affiche pour cette tache
     */
    private boolean specialMessage = false;
    /**
     * indique si un recalcul est necessaire apres completion de cette tache
     */
    private boolean recomputeNeededAfterCompletion = false;

    private TaskId(boolean p, boolean h, boolean r) {
        this.processable = p;
        this.specialMessage = h;
        this.recomputeNeededAfterCompletion = r;
    }

    public boolean recomputeIsNeededAfterCompletion() {
        return this.recomputeNeededAfterCompletion;
    }

    public static boolean equalsId(TaskId tid, String sid) {
        return tid.toString().equals(sid);
    }

    public boolean isProcessable() {
        return this.processable;
    }

    public boolean hasSpecialMessage() {
        return this.specialMessage;
    }

    public static TaskId[] getAllProcessable() {
        TaskId[] tasks = TaskId.values();
        List<TaskId> l = new ArrayList<TaskId>();
        for (int i = 0; i < tasks.length; i++) {
            if (tasks[i].isProcessable()) {
                l.add(tasks[i]);
            }
        }
        return l.toArray(new TaskId[0]);
    }

    public static TaskId safeValueOf(String s) {
        TaskId retour = TaskId.UNDEFINED;
        if (s != null) {
            TaskId[] values = TaskId.values();
            for (int i = 0; i < values.length
                    && (retour.equals(TaskId.UNDEFINED)); i++) {
                if (s.equals(values[i].toString())) {
                    retour = values[i];
                }
            }
        }
        return retour;
    }
}
