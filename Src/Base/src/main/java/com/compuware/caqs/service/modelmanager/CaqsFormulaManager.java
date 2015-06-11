package com.compuware.caqs.service.modelmanager;

import com.compuware.caqs.constants.Constants;
import com.compuware.caqs.domain.dataschemas.modelmanager.ErroneousFormulaPart;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaError;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaOperators;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaPartType;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaPart;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaPartOperand;
import com.compuware.caqs.domain.dataschemas.modelmanager.FormulaPartOperator;
import com.compuware.caqs.service.MetricSvc;
import com.compuware.toolbox.util.logging.LoggerManager;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class CaqsFormulaManager {

    protected static org.apache.log4j.Logger mLog = LoggerManager.getLogger(Constants.LOG4J_IHM_LOGGER_KEY);

    private static FormulaOperators getOperatorFromIndice(String f) {
        FormulaOperators retour = FormulaOperators.NONE;

        String g = f.substring(0, f.indexOf(']'));
        int indiceRetour = Integer.parseInt(g);
        retour = FormulaOperators.fromIndice(indiceRetour);

        return retour;
    }

    /**
     * renvoie la liste des types d'operateurs trouves a ce niveau de parenthesage
     * @param p le niveau de parenthesage
     * @return la liste
     */
    private static List<FormulaOperators> getAllDistinctOperatorsAtSameLevel(String p) {
        List<FormulaOperators> operatorsFoundAtThisLevel = new ArrayList<FormulaOperators>();

        int indice = 0;
        boolean inDoubleQuote = false;
        boolean inSimpleQuote = false;

        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c == '(') {
                indice++;
                continue;
            }
            if (c == ')') {
                indice--;
                continue;
            }
            if (c == '\"') {
                indice = (inDoubleQuote) ? indice - 1 : indice + 1;
                inDoubleQuote = !inDoubleQuote;
                continue;
            }
            if (c == '\'') {
                indice = (inSimpleQuote) ? indice - 1 : indice + 1;
                inSimpleQuote = !inSimpleQuote;
                continue;
            }
            if (indice == 0) {
                if (c == '[') {
                    FormulaOperators op = CaqsFormulaManager.getOperatorFromIndice(p.substring(i
                            + 1));
                    boolean found = false;
                    for (FormulaOperators foundHere : operatorsFoundAtThisLevel) {
                        if (foundHere.equals(op)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        operatorsFoundAtThisLevel.add(op);
                    }
                    i = i + 1 + (new String("" + op.getIndice())).length();
                }
            }
        }
        return operatorsFoundAtThisLevel;
    }

    private static void checkOperatorsUse(FormulaPart p) {
        //on verifie que des & et des | ne sont pas combines au meme niveau de parenthesage
        boolean hasAnd = false;
        boolean hasOr = false;
        for (FormulaPart part : p.getChildren()) {
            if (part instanceof FormulaPartOperator) {
                FormulaOperators op = ((FormulaPartOperator) part).getOperator();
                if (op.equals(FormulaOperators.AND)) {
                    hasAnd = true;
                    continue;
                }
                if (op.equals(FormulaOperators.OR)) {
                    hasOr = true;
                    continue;
                }
            }
        }
        if (hasAnd && hasOr) {
            p.setError(FormulaError.AND_OR_SAME_LEVEL);
        }
    }

    /**
     * retourne l'operateur le moins prioritaire a ce niveau de parenthesage
     * @param f
     * @return
     */
    private static FormulaOperators getOperatorWithLessPriorityAtThisLevel(String f) {
        FormulaOperators retour = FormulaOperators.NONE;

        boolean inDoubleQuote = false;
        boolean inSimpleQuote = false;
        int indice = 0;

        for (int i = 0; i < f.length(); i++) {
            char c = f.charAt(i);
            if (c == '(') {
                indice++;
                continue;
            }
            if (c == ')') {
                indice--;
                continue;
            }
            if (c == '\"' && !inSimpleQuote) {
                indice = (inDoubleQuote) ? indice - 1 : indice + 1;
                inDoubleQuote = !inDoubleQuote;
                continue;
            }
            if (c == '\'' && !inDoubleQuote) {
                indice = (inSimpleQuote) ? indice - 1 : indice + 1;
                inSimpleQuote = !inSimpleQuote;
                continue;
            }
            if (indice == 0) {
                if (c == '[') {
                    FormulaOperators op = getOperatorFromIndice(f.substring(i
                            + 1));
                    if (op.getPriority() > retour.getPriority()) {
                        retour = op;
                    }
                    i = i + 1 + (new String("" + op.getIndice())).length();
                }
            }
        }
        return retour;
    }

    public static void checkOperators(FormulaPart elt) {
        List<FormulaPart> children = new ArrayList<FormulaPart>();
        CaqsFormulaManager.getAllChildren(children, elt, FormulaPartOperator.class.getName());
        for (FormulaPart child : children) {
            CaqsFormulaManager.checkGoodOperands((FormulaPartOperator) child);
        }
    }

    private static void checkGoodOperands(FormulaPartOperator elt) {
        FormulaOperators op = elt.getOperator();
        FormulaPartType t = op.getChildType();
        for (FormulaPart part : elt.getChildren()) {
            if (!t.equals(part.getValueType())) {
                elt.setError(FormulaError.OPERATOR_NOT_GOOD_TYPE_OPERANDS);
                break;
            }
        }
    }

    public static List<FormulaPartOperand> getAllMetrics(FormulaPart formula) {
        List<FormulaPartOperand> liste = new ArrayList<FormulaPartOperand>();
        CaqsFormulaManager.getAllMetrics(formula, liste);
        return liste;
    }

    private static void getAllMetrics(FormulaPart elt, List<FormulaPartOperand> liste) {
        if (elt instanceof FormulaPartOperand
                && elt.getType().equals(FormulaPartType.VARIABLE)) {
            liste.add((FormulaPartOperand) elt);
        }

        for (FormulaPart part : elt.getChildren()) {
            getAllMetrics(part, liste);
        }
    }

    private static void checkMetrics(FormulaPart elt, List<String> allowedMetrics) {
        if (elt instanceof FormulaPartOperand
                && elt.getType().equals(FormulaPartType.VARIABLE)) {
            String id = ((FormulaPartOperand) elt).getValue();
            if (!allowedMetrics.contains(id)) {
                elt.setError(FormulaError.METRIC_NOT_ASSOCIATED);
                elt.setErrorParams(new Object[]{id});
            }
        }

        for (FormulaPart part : elt.getChildren()) {
            checkMetrics(part, allowedMetrics);
        }
    }

    public static FormulaPart getFormulaFromXML(Element elt) {
        FormulaPart retour = null;
        if (("FORMULA".equalsIgnoreCase(elt.getName())
                || "COSTFORMULA".equalsIgnoreCase(elt.getName())
                || "IFPUG".equalsIgnoreCase(elt.getName())) && elt.getChildren().size()
                == 1) {
            retour = convertFormula((Element) elt.getChildren().get(0));
        }

        if (retour != null) {
            List<FormulaPart> elts = new ArrayList<FormulaPart>();
            CaqsFormulaManager.getAllChildren(elts, retour, FormulaPartOperand.class.getName());
            for (FormulaPart child : elts) {
                if (child.getValueType().equals(FormulaPartType.UNKNOWN_TYPE_VARIABLE)) {
                    CaqsFormulaManager.giveTypeToMetrics((FormulaPartOperand) child);
                }
            }
        }
        return retour;
    }

    public static FormulaPart convertFormula(Element elt) {
        FormulaPart retour = null;
        if (elt.getName().equalsIgnoreCase("OP")) {
            if (!elt.getChildren().isEmpty()) {
                retour = new FormulaPartOperator();
                String id = elt.getAttributeValue("id");
                ((FormulaPartOperator) retour).setOperator(FormulaOperators.fromString(id));
                retour.setType(((FormulaPartOperator) retour).getOperator().getType());
                if ("gt".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("ge".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("+".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("le".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("-".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("*".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("/".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("lt".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("eq".equalsIgnoreCase(id)) {
                    if (elt.getChildren().size() == 2) {
                        FormulaPart c1 = convertFormula((Element) elt.getChildren().get(0));
                        retour.addChild(c1);
                        FormulaPart c2 = convertFormula((Element) elt.getChildren().get(1));
                        retour.addChild(c2);
                    }
                } else if ("AND".equalsIgnoreCase(id)) {
                    Iterator<Element> it = elt.getChildren().iterator();
                    while (it.hasNext()) {
                        Element eltNext = (Element) it.next();
                        FormulaPart c = convertFormula(eltNext);
                        retour.addChild(c);
                    }
                } else if ("OR".equalsIgnoreCase(id)) {
                    Iterator<Element> it = elt.getChildren().iterator();
                    while (it.hasNext()) {
                        Element eltNext = (Element) it.next();
                        FormulaPart c = convertFormula(eltNext);
                        retour.addChild(c);
                    }
                } else if ("/m".equalsIgnoreCase(id)) {
                    Iterator<Element> it = elt.getChildren().iterator();
                    while (it.hasNext()) {
                        Element eltNext = (Element) it.next();
                        FormulaPart c = convertFormula(eltNext);
                        retour.addChild(c);
                    }
                }
            }
        } else if (elt.getName().equals("VAL")) {
            retour = new FormulaPartOperand();
            String val = elt.getAttributeValue("value");
            ((FormulaPartOperand) retour).setValue(val);
            retour.setType(FormulaPartType.CONSTANT);
            if (elt.getAttributeValue("type").equalsIgnoreCase("STRING")) {
                retour.setValueType(FormulaPartType.STRING);
            } else {
                try {
                    Double.parseDouble(val);
                    retour.setValueType(FormulaPartType.NUMERIC);
                } catch (NumberFormatException e) {
                    retour.setValueType(FormulaPartType.BOOLEAN);
                }
            }
        } else if (elt.getName().equals("VAR")) {
            retour = new FormulaPartOperand();
            String val = elt.getAttributeValue("id");
            retour.setType(FormulaPartType.VARIABLE);
            ((FormulaPartOperand) retour).setValue(val);
            retour.setValueType(FormulaPartType.UNKNOWN_TYPE_VARIABLE);
        }
        return retour;
    }

    private static FormulaPart checkParenthesisNumber(String p) {
        FormulaPart elt = null;
        //1. meme nombre de ( et de ) et un operateur entre chaque expression
        int nbOpenPar = 0;
        int nbClosePar = 0;
        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c == '(') {
                nbOpenPar++;
            }
            if (c == ')') {
                nbClosePar++;
            }
        }
        if (nbClosePar != nbOpenPar) {
            elt = new ErroneousFormulaPart(FormulaError.BAD_PARENTHESIS);
            ((ErroneousFormulaPart) elt).setValue(p);
            return elt;
        }
        return elt;
    }

    private static FormulaPart getFormulaPartFromString(String p) {
        FormulaPart elt = null;
        if (p == null || p.equals("")) {
            elt = new ErroneousFormulaPart(FormulaError.BAD_EXPRESSION);
            return elt;
        }

        FormulaOperators currentOp = CaqsFormulaManager.getOperatorWithLessPriorityAtThisLevel(p);

        if (currentOp.equals(FormulaOperators.NONE)) {
            //cas possible : l'expression est integralement entouree de parentheses
            if (p.startsWith("(") && p.endsWith(")")) {
                p = p.substring(1, p.length() - 1);//suppression des parentheses
                //on supprime les parentheses et on recommence
                currentOp = CaqsFormulaManager.getOperatorWithLessPriorityAtThisLevel(p);
            }
        }

        if (!currentOp.equals(FormulaOperators.NONE)) {
            elt = new FormulaPartOperator();
            ((FormulaPartOperator) elt).setOperator(currentOp);
            List<String> al = CaqsFormulaManager.getChildrenStringParts(p, currentOp);
            for (int i = 0; i < al.size(); i++) {
                FormulaPart child = getFormulaPartFromString((String) al.get(i));
                if (child == null) {
                    elt = new ErroneousFormulaPart(FormulaError.BAD_EXPRESSION);
                    ((ErroneousFormulaPart) elt).setValue(p);
                    break;
                }
                elt.addChild(child);
            }
        } else {
//			numeric, boolean ou var
            try {
                Double.parseDouble(p);
                elt = new FormulaPartOperand();
                elt.setType(FormulaPartType.CONSTANT);
                elt.setValueType(FormulaPartType.NUMERIC);
                ((FormulaPartOperand) elt).setValue(p);
            } catch (NumberFormatException nfe) {
                //pas un numeric
                if ("true".equalsIgnoreCase(p) || "false".equalsIgnoreCase(p)) {
                    elt = new FormulaPartOperand();
                    elt.setType(FormulaPartType.CONSTANT);
                    elt.setValueType(FormulaPartType.BOOLEAN);
                    ((FormulaPartOperand) elt).setValue(p.toLowerCase());
                } else {
                    if (CaqsFormulaManager.isEncapsulatedIn(p, "\"")) {
                        elt = new FormulaPartOperand();
                        elt.setType(FormulaPartType.CONSTANT);
                        elt.setValueType(FormulaPartType.STRING);
                        ((FormulaPartOperand) elt).setValue(p.substring(1, p.length()
                                - 1));
                    } else if (CaqsFormulaManager.isEncapsulatedIn(p, "'")) {
                        elt = new FormulaPartOperand();
                        elt.setType(FormulaPartType.VARIABLE);
                        elt.setValueType(FormulaPartType.UNKNOWN_TYPE_VARIABLE);
                        ((FormulaPartOperand) elt).setValue(p.substring(1, p.length()
                                - 1));
                    } else {
                        //il n'y a pas d'encapsulation de la variable
                        elt = new ErroneousFormulaPart(FormulaError.VARIABLE_NOT_ENCAPSULATED);
                        ((ErroneousFormulaPart) elt).setValue(p);
                        ((ErroneousFormulaPart) elt).setErrorParams(new Object[]{p});
                    }
                }
            }
        }


        return elt;
    }

    /**
     * determine si s est encapsulee par encap.
     * @param s chaine de caracteres a tester
     * @param encap chaine de caracteres encapsulante
     * @return true si s commence et termine par encap, false sinon.
     */
    private static boolean isEncapsulatedIn(String s, String encap) {
        boolean retour = false;
        if (s.startsWith(encap) && s.endsWith(encap)) {
            retour = true;
        }
        return retour;
    }

    /**
     * donne un type a la metrique selon le type de l'element parent.
     * si la metrique n'a pas d'element parent, le type BOOLEAN est automatiquement
     * associe.
     * @param elt element pour lequel le type doit etre determine.
     */
    private static void giveTypeToMetrics(FormulaPartOperand elt) {
        //une variable
        if (elt.getParent() != null) {
            //si elle a un parent, on doit deviner son type
            //si l'operateur parent est and ou or, c'est un booleen
            FormulaPart parent = elt.getParent();
            if (parent instanceof FormulaPartOperator) {
                FormulaPartOperator parentOpPart = (FormulaPartOperator) parent;
                FormulaOperators parentOp = parentOpPart.getOperator();
                elt.setValueType(parentOp.getChildType());
            }
        } else {
            //sinon, c'est un always true (pourquoi sommes nous la alors?) donc booleen
            elt.setValueType(FormulaPartType.BOOLEAN);
        }
    }

    /**
     * renvoie true si au moins un element de la formule est tagge comme ayant
     * une erreur, false sinon.
     * @param formula la formule a verifier
     * @return true si au moins un element de la formule est tagge comme ayant
     * une erreur, false sinon.
     */
    public static boolean formulaHasError(FormulaPart formula) {
        boolean retour = false;
        if (formula.getError() != null
                && !FormulaError.NO_ERROR.equals(formula.getError())) {
            retour = true;
        } else {
            for (Iterator<FormulaPart> it = formula.getChildren().iterator(); it.hasNext()
                    && !retour;) {
                retour = formulaHasError(it.next());
            }
        }
        return retour;
    }

    /**
     * retourne une FormulaPart a partir d'une formule formattee
     * @param f formule formattee
     * @return equivalent FormulaPart
     */
    public static FormulaPart getFormulaFromFormattedFormula(String f) {
        FormulaPart elt = CaqsFormulaManager.checkParenthesisNumber(f);
        if (elt == null) {
            f = CaqsFormulaManager.encodeOperators(f);
            elt = CaqsFormulaManager.getFormulaPartFromString(f);
            if (elt != null) {
                List<FormulaPart> elts = new ArrayList<FormulaPart>();
                CaqsFormulaManager.getAllChildren(elts, elt, FormulaPartOperand.class.getName());
                for (FormulaPart child : elts) {
                    if (child.getValueType().equals(FormulaPartType.UNKNOWN_TYPE_VARIABLE)) {
                        CaqsFormulaManager.giveTypeToMetrics((FormulaPartOperand) child);
                    }
                }
            }
        }
        return elt;
    }

    /**
     * Cette methode encode tous les operateurs afin de simplifier la "XMLisation" de la formule
     * @param formula la formule a encoder
     * @return la formule encodee
     */
    private static String encodeOperators(String formula) {
        String retour = "";

        int indice = 0;
        boolean insideSingleQuote = false;
        boolean insideDoubleQuote = false;

        for (int i = 0; i < formula.length(); i++) {
            char c = formula.charAt(i);

            if (c == '\'' && !insideDoubleQuote) {
                indice = (insideSingleQuote) ? (indice - 1) : (indice + 1);
                insideSingleQuote = !insideSingleQuote;
            }
            if (c == '\"' && !insideSingleQuote) {
                indice = (insideDoubleQuote) ? (indice - 1) : (indice + 1);
                insideDoubleQuote = !insideDoubleQuote;
            }

            if (indice == 0 && isOperatorStart(c)) {
                FormulaOperators op = FormulaOperators.NONE;
                if ((i < (formula.length() - 2)) && (formula.charAt(i + 1)
                        == '=')) {
                    if (c == '<') {
                        op = FormulaOperators.LESS_OR_EQUAL;
                    } else if (c == '>') {
                        op = FormulaOperators.GREATER_OR_EQUAL;
                    }
                    i++;//on passe le caractere =
                } else if ((i < (formula.length() - 2)) && (c == '/' && ((formula.charAt(i
                        + 1) == 'm') || (formula.charAt(i + 1) == 'M')))) {
                    op = FormulaOperators.SPECIAL_DIVIDE;
                    i++;//on passe le caractere m
                } else {
                    if (c == '<') {
                        op = FormulaOperators.LESS_THAN;
                    } else if (c == '>') {
                        op = FormulaOperators.GREATER_THAN;
                    } else {
                        op = CaqsFormulaManager.getOperatorInt(c);
                    }
                }
                if (!op.equals(FormulaOperators.NONE)) {
                    retour += "[" + op.getIndice() + "]";
                }
                continue;
            }
            retour += c;
        }

        return retour;
    }

    private static List<String> getChildrenStringParts(String p, FormulaOperators operator) {
        List<String> retour = new ArrayList<String>();

        p = p.trim();
        int indice = 0;
        boolean inSingleQuote = false;
        boolean inDoubleQuote = false;
        String part = "";
        int nbOccFound = 0;

        for (int i = 0; i < p.length(); i++) {
            char c = p.charAt(i);
            if (c == '(') {
                indice++;
            }
            if (c == ')') {
                indice--;
            }
            if (c == '\'') {
                if (inSingleQuote) {
                    indice--;
                } else {
                    indice++;
                }
                inSingleQuote = !inSingleQuote;
            }
            if (c == '\"') {
                if (inDoubleQuote) {
                    indice--;
                } else {
                    indice++;
                }
                inDoubleQuote = !inDoubleQuote;
            }
            if (indice == 0 && c == '[') {
                //c est un operateur
                FormulaOperators op = getOperatorFromIndice(p.substring(i + 1));
                if (op.equals(operator)) {
                    nbOccFound++;
                    if (nbOccFound == 1 || multipleOccurencePossible(operator)) {
                        retour.add(part.trim());
                        part = "";
                        i = i + 1 + (new String("" + op.getIndice())).length();
                        continue;
                    }
                } else {
                    if (operator.getPriority() >= op.getPriority()) {
                        //l'operateur trouve est prioritaire. il fait partie de l'element fils.
                    } else {
                        i = i + 1 + (new String("" + op.getIndice())).length();
                    }
                }
            }
            part += c;
        }

        if (!part.equals("")) {
            retour.add(part.trim());
        }

        return retour;
    }

    private static boolean multipleOccurencePossible(FormulaOperators op) {
        boolean retour = false;
        if (op.equals(FormulaOperators.AND) || op.equals(FormulaOperators.OR)) {
            retour = true;
        }
        return retour;
    }

    private static boolean isOperatorStart(char c) {
        boolean retour = false;
        for (FormulaOperators op : FormulaOperators.values()) {
            if (c == op.getSplitter()) {
                retour = true;
                break;
            }
        }
        return retour;
    }

    private static FormulaOperators getOperatorInt(char c) {
        FormulaOperators retour = FormulaOperators.NONE;
        for (FormulaOperators op : FormulaOperators.values()) {
            if (c == op.getSplitter()) {
                retour = op;
                break;
            }
        }
        return retour;
    }

    public static Element createAlwaysTrueElement() {
        //<VAL value="true" type="BOOLEAN" />
        Element retour = new Element("VAL");
        retour.setAttribute("value", "true");
        retour.setAttribute("type", "BOOLEAN");
        return retour;
    }

    public static Element convertToXMLElement(String s) {
        Element retour = null;
        Document doc2 = null;
        SAXBuilder saxbuilder2 = new SAXBuilder();

        try {
            StringReader reader = new StringReader(s);
            doc2 = saxbuilder2.build(reader);
            retour = doc2.getRootElement();
            doc2.detachRootElement();
        } catch (org.jdom.JDOMException e) {
            mLog.error("Le XML est malforme", e);
        } catch (java.io.IOException e) {
            mLog.error("Le XML est malforme", e);
        }
        return retour;
    }

    private static void getAllChildren(List<FormulaPart> l, FormulaPart elt, String className) {
        if (l != null) {
            if (elt.getClass().getName().equals(className)) {
                l.add(elt);
            }
            for (FormulaPart child : elt.getChildren()) {
                CaqsFormulaManager.getAllChildren(l, child, className);
            }
        }
    }

    private static List<String> convertMetricsListToIdList(List<FormulaPartOperand> metrics) {
        List<String> retour = new ArrayList<String>(metrics.size());
        for (FormulaPartOperand metric : metrics) {
            retour.add(metric.getValue());
        }
        return retour;
    }

    private static FormulaPartOperand getMetricFromListById(List<FormulaPartOperand> liste, String id) {
        FormulaPartOperand retour = null;
        for (FormulaPartOperand metric : liste) {
            if (metric.getValue().equals(id)) {
                retour = metric;
                break;
            }
        }
        return retour;
    }

    /**
     * verifie la formule defini l'ifpug.
     * @param idUsa identifiant du modele qualimetrique pour lequel verifier l'ifpug
     * @param formula formule a verifier
     */
    public static void checkIFPUGFormulaPart(String idUsa, FormulaPart formula) {
        if (formula != null) {
            CaqsFormulaManager.checkOperators(formula);
            List<FormulaPartOperand> liste = new ArrayList<FormulaPartOperand>();
            CaqsFormulaManager.getAllMetrics(formula, liste);
            List<String> providedIds = CaqsFormulaManager.convertMetricsListToIdList(liste);
            List<String> allowedMetrics = MetricSvc.getInstance().retrieveCorrectMetricsFromProvidedForModel(providedIds, idUsa);
            providedIds.removeAll(allowedMetrics);
            if (!providedIds.isEmpty()) {
                //certaines metriques demandees ne sont pas utilisables
                for (String id : providedIds) {
                    FormulaPartOperand elt = CaqsFormulaManager.getMetricFromListById(liste, id);
                    if (elt != null) {
                        elt.setError(FormulaError.METRIC_NOT_USABLE_FOR_MODEL);
                        elt.setErrorParams(new Object[]{id});
                    }
                }
            }
        }
    }

    public static void checkFormulaPart(FormulaPart formula, List<String> allowedMetrics) {
        if (formula != null) {
            CaqsFormulaManager.checkOperators(formula);
            if (allowedMetrics != null) {
                CaqsFormulaManager.checkMetrics(formula, allowedMetrics);
            }

        }
    }

    public static FormulaPart checkFormulaFromFormattedString(
            String formula, HttpServletRequest request, List<String> allowedMetrics) {
        FormulaPart formulaPart = CaqsFormulaManager.getFormulaFromFormattedFormula(formula);
        if (formulaPart != null) {
            CaqsFormulaManager.checkOperators(formulaPart);
            CaqsFormulaManager.checkMetrics(formulaPart, allowedMetrics);
        }

        return formulaPart;
    }

    public static Element getXMLFormulaFromFormula(
            FormulaPart formula) {
        return formula.getXMLElement();
    }
    /*
    public static void main(String[] args) {
    String formula = "<OP id=\"OR\" type=\"BOOLEAN\"> <OP id=\"lt\" type=\"BOOLEAN\">" +
    "<VAR type=\"NUMERIC\" id=\"VG\" />" +
    "<VAL type=\"NUMERIC\" value=\"10\" />" +
    "</OP>" +
    "<OP id=\"AND\" type=\"BOOLEAN\">" +
    " <OP id=\"ge\" type=\"BOOLEAN\">" +
    "  <VAR type=\"NUMERIC\" id=\"VG\" />" +
    " <VAL type=\"NUMERIC\" value=\"10\" />" +
    "    </OP>" +
    "   <OP id=\"ge\" type=\"BOOLEAN\">" +
    "    <VAR type=\"NUMERIC\" id=\"CLOC\" />" +
    "   <OP id=\"*\" type=\"NUMERIC\">" +
    "    <OP id=\"+\" type=\"NUMERIC\">" +
    "     <VAL type=\"NUMERIC\" value=\"0.3\" />" +
    "    <OP id=\"/\" type=\"NUMERIC\">" +
    "     <OP id=\"-\" type=\"NUMERIC\">" +
    "      <VAR type=\"NUMERIC\" id=\"VG\" />" +
    "     <VAL type=\"NUMERIC\" value=\"10\" />" +
    "  </OP>" +
    "                    <VAL type=\"NUMERIC\" value=\"100\" />" +
    "                 </OP>" +
    "              </OP>" +
    "             <VAR type=\"NUMERIC\" id=\"LOC\" />" +
    "          </OP>" +
    "       </OP>" +
    "    </OP>" +
    " </OP>";
    Element elt = CaqsFormulaManager.convertToXMLElement(formula);
    List<String> metrics = new ArrayList<String>();
    FormulaPart formulaPart = CaqsFormulaManager.getFormulaFromXML(elt);
    System.out.println(formulaPart.toFormatedString(false));
    System.out.println("output");
    CaqsFormulaManager.checkFormulaPart(formulaPart, metrics);
    System.out.println(formulaPart.toFormatedString(true));
    elt = CaqsFormulaManager.getXMLFormulaFromFormula(formulaPart);
    System.out.println(elt.toString());
    }*/
}
