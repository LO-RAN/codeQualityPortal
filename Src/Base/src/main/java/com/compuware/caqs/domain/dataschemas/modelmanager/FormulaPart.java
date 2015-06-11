package com.compuware.caqs.domain.dataschemas.modelmanager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import org.apache.struts.util.MessageResources;
import org.jdom.Element;

/**
 *
 * @author cwfr-dzysman
 */
public abstract class FormulaPart {

    protected FormulaPartType type;
    protected FormulaError error;
    protected List<FormulaPart> children;
    private Object[] errorParams;

    public abstract FormulaPartType getValueType();
    public abstract void setValueType(FormulaPartType valueType);

    public Object[] getErrorParams() {
        return errorParams;
    }

    public void setErrorParams(Object[] params) {
        this.errorParams = params;
    }

    public List<FormulaPart> getChildren() {
        return children;
    }
    protected FormulaPart parent;

    public FormulaPart getParent() {
        return parent;
    }

    public void setParent(FormulaPart parent) {
        this.parent = parent;
    }

    protected FormulaPart() {
        this.error = FormulaError.NO_ERROR;
        this.children = new ArrayList<FormulaPart>();
    }

    public FormulaError getError() {
        return error;
    }

    public void setError(FormulaError error) {
        this.error = error;
    }

    public FormulaPartType getType() {
        return type;
    }

    public void setType(FormulaPartType type) {
        this.type = type;
    }

    public void addChild(FormulaPart fp) {
        this.children.add(fp);
        fp.setParent(this);
    }

    public FormulaPart getChild(int index) {
        FormulaPart retour = null;
        if (index < this.children.size()) {
            retour = this.children.get(index);
        }
        return retour;
    }

    public String toFormatedString(boolean includeErrors, MessageResources resources, Locale loc) {
        String retour = "";
        if (this instanceof FormulaPartOperator) {
            if (!this.getChildren().isEmpty()) {
                FormulaPartOperator operatorPart = (FormulaPartOperator) this;
                retour += "(";

                if (FormulaOperators.AND.equals(operatorPart.getOperator()) ||
                        (FormulaOperators.OR.equals(operatorPart.getOperator())) ||
                        (FormulaOperators.SPECIAL_DIVIDE.equals(operatorPart.getOperator()))) {
                    Iterator<FormulaPart> it = this.getChildren().iterator();
                    while (it.hasNext()) {
                        FormulaPart eltNext = it.next();
                        retour += eltNext.toFormatedString(includeErrors, resources, loc);
                        if (it.hasNext()) {
                            retour += operatorPart.getOperator().getValue();
                        }
                    }
                } else if (this.getChildren().size() == 2) {
                    String c1 = this.getChild(0).toFormatedString(includeErrors, resources, loc);
                    String c2 = this.getChild(1).toFormatedString(includeErrors, resources, loc);
                    retour += c1 + operatorPart.getOperator().getValue() + c2;
                }

                retour += ")";
            }
        } else if (this instanceof FormulaPartOperand) {
            FormulaPartOperand operand = (FormulaPartOperand) this;
            if (this.getType().equals(FormulaPartType.CONSTANT)) {
                retour = operand.getValue();
                if (this.getValueType().equals(FormulaPartType.STRING)) {
                    retour = "\"" + retour + "\"";
                }
            } else if (this.getType().equals(FormulaPartType.VARIABLE)) {
                retour = "'" + operand.getValue() + "'";
            }
        }else if(this instanceof ErroneousFormulaPart) {
            retour = ((ErroneousFormulaPart)this).getValue();
        }
        if (includeErrors && this.getError() != null && !this.getError().equals(FormulaError.NO_ERROR)) {
            String encap = "<span style=\"color:red; font-weight: bold;\" ";
            encap += " ext:qtip=\"";
            if(this.getErrorParams()!=null) {
                encap += resources.getMessage(loc, "caqs.modeleditor.modelEdition.formulaEdition.error."+this.getError().getId(), this.getErrorParams());
            } else {
                encap += resources.getMessage(loc, "caqs.modeleditor.modelEdition.formulaEdition.error."+this.getError().getId());
            }
            encap += "\" ";
            encap += ">";
            retour = encap + retour;
            retour += "</span>";
        }
        return retour;
    }

    public Element getXMLElement() {
        Element retour = null;
        if (this instanceof FormulaPartOperator) {
            retour = new Element("OP");
            FormulaOperators op = ((FormulaPartOperator) this).getOperator();
            retour.setAttribute("id", op.getId());
            retour.setAttribute("type", op.getType().toString());
        } else if (this instanceof FormulaPartOperand) {
            if (this.getType().equals(FormulaPartType.VARIABLE)) {
                retour = new Element("VAR");
                retour.setAttribute("id", ((FormulaPartOperand) this).getValue());
                retour.setAttribute("type", this.getValueType().toString());
            } else {
                retour = new Element("VAL");
                retour.setAttribute("value", ((FormulaPartOperand) this).getValue());
                retour.setAttribute("type", this.getValueType().toString());
            }
        }
        if (retour != null) {
            for (FormulaPart child : this.getChildren()) {
                Element eltChild = child.getXMLElement();
                if (eltChild != null) {
                    retour.getChildren().add(eltChild);
                }
            }
        }
        return retour;
    }
}