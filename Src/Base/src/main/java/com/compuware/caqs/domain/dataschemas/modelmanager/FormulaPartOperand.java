package com.compuware.caqs.domain.dataschemas.modelmanager;

/**
 *
 * @author cwfr-dzysman
 */
public class FormulaPartOperand extends FormulaPart {
    private String value = "";
    private FormulaPartType valueType = FormulaPartType.NONE;

    public FormulaPartType getValueType() {
        return valueType;
    }

    public void setValueType(FormulaPartType valueType) {
        this.valueType = valueType;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public FormulaPartOperand() {
        super();
    }
}
