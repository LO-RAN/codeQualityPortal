package com.compuware.caqs.domain.dataschemas.modelmanager;

/**
 *
 * @author cwfr-dzysman
 */
public class ErroneousFormulaPart extends FormulaPart {
    private String value = "";

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ErroneousFormulaPart(FormulaError error) {
        super();
        this.type = FormulaPartType.ERRONEOUS;
        this.setError(error);
    }

    @Override
    public FormulaPartType getValueType() {
        return null;
    }

    @Override
    public void setValueType(FormulaPartType type) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
