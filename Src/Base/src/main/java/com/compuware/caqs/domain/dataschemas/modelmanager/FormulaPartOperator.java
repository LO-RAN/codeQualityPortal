package com.compuware.caqs.domain.dataschemas.modelmanager;

/**
 *
 * @author cwfr-dzysman
 */
public class FormulaPartOperator extends FormulaPart {
    private FormulaOperators operator;

    public FormulaOperators getOperator() {
        return operator;
    }

    public void setOperator(FormulaOperators operator) {
        this.operator = operator;
    }

    public FormulaPartOperator(FormulaPartType type) {
        super();
        this.type = type;
    }

    public FormulaPartOperator() {
        super();
        this.type = FormulaPartType.ERRONEOUS;
    }

    @Override
    public FormulaPartType getValueType() {
        FormulaPartType retour = FormulaPartType.NONE;
        if(this.getOperator()!=null) {
            retour = this.getOperator().getType();
        }
        return retour;
    }

    @Override
    public void setValueType(FormulaPartType type) {
        throw new UnsupportedOperationException("Not supported.");
    }
}
