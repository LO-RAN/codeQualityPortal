package com.compuware.caqs.domain.dataschemas.evolutions;

/**
 *
 * @author cwfr-dzysman
 */
public enum ElementsCategory {

    NEW_OK(1, "newOk"),
    NEW_BAD(2, "newBad"),
    OLD_WORST(3, "oldWorst"),
    OLD_BETTER(4, "oldBetter"),
    OLD_BETTER_WORST(5, "oldBetterWorst"),
    BAD_STABLE(6, "badStable"),
    STABLE_OK(7, "stableOk"),
    DELETED(8, "deleted"),
    CORRECTED_ELEMENTS(9, "correctedElements"),
    DEGRADED_ELEMENTS(10, "degradedElements"),
    PARTIALLY_CORRECTED_ELEMENTS(11, "partiallyCorrectedElements"),
    STABLE_ELEMENTS(12, "stableElements"),
    SUPPRESSED_ELEMENTS(13, "suppressedElements"),
    ELEMENTS_TO_CORRECT(14, "elementsToCorrect");

    private int code;
    private String stringCode;

    private ElementsCategory(int c, String stringCode) {
        this.code = c;
        this.stringCode = stringCode;
    }

    public int getCode() {
        return this.code;
    }

    public String getStringCode() {
        return this.stringCode;
    }

    public static ElementsCategory fromCode(int c) {
        ElementsCategory retour = null;
        for(ElementsCategory cat : ElementsCategory.values()) {
            if(cat.code==c) {
                retour = cat;
                break;
            }
        }
        return retour;
    }

    public boolean equals(ElementsCategory o) {
        boolean retour = false;
        if(o!=null) {
            retour = (this.code == o.getCode());
        }
        return retour;
    }
}
