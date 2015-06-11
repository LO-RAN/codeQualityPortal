package com.compuware.caqs.domain.dataschemas.modelmanager;

/**
 *
 * @author cwfr-dzysman
 */
public enum FormulaError {
    AND_OR_SAME_LEVEL("and_or_same_level"),
    BAD_EXPRESSION("bad_expression"),
    BAD_PARENTHESIS("bad_parenthesis"),
    VARIABLE_NOT_ENCAPSULATED("variable_not_encapsulated"),
    OPERATOR_NOT_GOOD_TYPE_OPERANDS("operator_not_same_type_operands"),
    METRIC_NOT_ASSOCIATED("metric_not_associated"),
    METRIC_NOT_USABLE_FOR_MODEL("metric_not_usable_for_model"),
    NO_ERROR("no_error");
    
    private String id;
    
    FormulaError(String id) {
        this.id = id;
    }

    public boolean equals(FormulaError e) {
        return this.id.equals(e.id);
    }

    public String getId() {
        return this.id;
    }
}
