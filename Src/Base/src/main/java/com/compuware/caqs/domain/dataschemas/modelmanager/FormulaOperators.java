package com.compuware.caqs.domain.dataschemas.modelmanager;

/**
 *
 * @author cwfr-dzysman
 */
public enum FormulaOperators {

    GREATER_THAN(0, "gt", 3, '>', FormulaPartType.BOOLEAN, ">", FormulaPartType.NUMERIC),
    GREATER_OR_EQUAL(1, "ge", 3, '>', FormulaPartType.BOOLEAN, ">=", FormulaPartType.NUMERIC),
    LESS_OR_EQUAL(2, "le", 3, '<', FormulaPartType.BOOLEAN, "<=", FormulaPartType.NUMERIC),
    LESS_THAN(3, "lt", 3, '<', FormulaPartType.BOOLEAN, "<", FormulaPartType.NUMERIC),
    PLUS(4, "+", 2, '+', FormulaPartType.NUMERIC, "+", FormulaPartType.NUMERIC),
    MINUS(5, "-", 2, '-', FormulaPartType.NUMERIC, "-", FormulaPartType.NUMERIC),
    MULT(6, "*", 1, '*', FormulaPartType.NUMERIC, "*", FormulaPartType.NUMERIC),
    DIV(7, "/", 1, '/', FormulaPartType.NUMERIC, "/", FormulaPartType.NUMERIC),
    EQUAL(8, "eq", 3, '=', FormulaPartType.BOOLEAN, "=", FormulaPartType.NUMERIC),
    AND(9, "AND", 4, '&', FormulaPartType.BOOLEAN, " & ", FormulaPartType.BOOLEAN),
    OR(10, "OR", 4, '|', FormulaPartType.BOOLEAN, " | ", FormulaPartType.BOOLEAN),
    SPECIAL_DIVIDE(11, "/m", 1, '/', FormulaPartType.NUMERIC, "/m", FormulaPartType.NUMERIC),
    NONE(12, "NONE", -1, ' ', FormulaPartType.NONE, "", FormulaPartType.NONE);
    private char splitter;
    private String value;
    private int priority;
    private FormulaPartType type;
    private String id;
    private int indice;
    private FormulaPartType childType;

    public int getIndice() {
        return indice;
    }

    public String getId() {
        return id;
    }

    public int getPriority() {
        return priority;
    }

    public char getSplitter() {
        return splitter;
    }

    public FormulaPartType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    FormulaOperators(int indice, String i, int p, char sp, FormulaPartType t, String val, FormulaPartType ct) {
        this.id = i;
        this.splitter = sp;
        this.priority = p;
        this.type = t;
        this.value = val;
        this.indice = indice;
        this.childType = ct;
    }

    public FormulaPartType getChildType() {
        return childType;
    }

    public void setChildType(FormulaPartType childType) {
        this.childType = childType;
    }

    public static FormulaOperators fromIndice(int indice) {
        return FormulaOperators.values()[indice];
    }

    public static FormulaOperators fromString(String id) {
        FormulaOperators retour = FormulaOperators.NONE;
        if ("gt".equalsIgnoreCase(id)) {
            retour = FormulaOperators.GREATER_THAN;
        } else if ("ge".equalsIgnoreCase(id)) {
            retour = FormulaOperators.GREATER_OR_EQUAL;
        } else if ("+".equalsIgnoreCase(id)) {
            retour = FormulaOperators.PLUS;
        } else if ("le".equalsIgnoreCase(id)) {
            retour = FormulaOperators.LESS_OR_EQUAL;
        } else if ("-".equalsIgnoreCase(id)) {
            retour = FormulaOperators.MINUS;
        } else if ("*".equalsIgnoreCase(id)) {
            retour = FormulaOperators.MULT;
        } else if ("/".equalsIgnoreCase(id)) {
            retour = FormulaOperators.DIV;
        } else if ("lt".equalsIgnoreCase(id)) {
            retour = FormulaOperators.LESS_THAN;
        } else if ("eq".equalsIgnoreCase(id)) {
            retour = FormulaOperators.EQUAL;
        } else if ("AND".equalsIgnoreCase(id)) {
            retour = FormulaOperators.AND;
        } else if ("OR".equalsIgnoreCase(id)) {
            retour = FormulaOperators.OR;
        } else if ("/m".equalsIgnoreCase(id)) {
            retour = FormulaOperators.SPECIAL_DIVIDE;
        }
        return retour;
    }

    public boolean equals(FormulaOperators op) {
        boolean retour = false;
        if(op instanceof FormulaOperators) {
            retour = this.id.equals(op.getId());
        }
        return retour;
    }
}
