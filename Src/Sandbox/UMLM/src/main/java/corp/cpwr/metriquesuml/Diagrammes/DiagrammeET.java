/*
 * DiagrammeET.java
 *
 * Created on 1 juin 2004, 09:38
 */

package corp.cpwr.metriquesuml.Diagrammes;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;
import java.util.*;
import corp.cpwr.metriquesuml.Objets.*;
import corp.cpwr.metriquesuml.Liens.*;

/**
 *
 * @author  cwfr-dzysman
 */
public class DiagrammeET extends DiagrammeActET {
    
    /**
     * Ce constructeur est <code>private</code> car il ne doit pas �tre possible
     * de cr�er de diagramme de classes sans toutes les informations n�cessaires.
     */
    private DiagrammeET() {
        super();
    }
    
    /**
     * Ce constructeur de classe permet la cr�ation d'un diagramme de classes.
     * Il ne lance pas automatiquement l'analyse de ce diagramme.
     *
     * @param e objet de type Element �tant la racine du sous-arbre repr�sentant
     *          le diagramme de classes dans l'arbre g�n�r� par JDOM.
     * @param n le nom du diagramme de classes.
     * @param m le mod�le auquel appartient l'objet.
     */
    public DiagrammeET(Element e, String n, Modele m){
        super(e, n, m, XMIObject.DIAGRAMME_ET);
    }
    
    /**
     * Cette m�thode lance l'analyse du diagramme de classes en cherchant toutes
     * les balises "Package" � partir de l'�l�ment racine du diagramme et en
     * les rajoutant dans le diagramme.
     * Le nouveau package racine est le premier package trouv�.
     */
    public void analyseDiagramme() {
        super.analyseDiagramme();
        //on commence par r�cup�rer tous les �tats.
        ArrayList vect = FonctionsParser.findAllBalises(getRootElement(), "SimpleState");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new SimpleState((Element)it.next(), XMIObject.DIAGRAMME_ET, getModele()));
        }
    }
    
    /**
     * Cette m�thode remplit tous les champs manquants.
     */
    public void fillAll() {
        super.fillAll();
    }
    
    public void calculerMetriques() {
        complexite = 0.0f;
        ArrayList transitions = getAll(XMIObject.TRANSITION, true);
        ArrayList pseudoStates = getAll(XMIObject.PSEUDO_STATE, true);
        ArrayList actionStates = getAll(XMIObject.SIMPLE_STATE, true);
        //ArrayList objetsUML = getAll(XMIObject.OBJECT_UML, true);
        //on calcule la complexit�.
        float diviseur = (float)((float)objets.size()+(float)actionStates.size()+(float)pseudoStates.size());
        if(diviseur!=0.0f)
            complexite = ((float)transitions.size()) / diviseur;
        calculerBienForme();
    }
    
    public ArrayList getAllDecisions() {
        ArrayList retour = new ArrayList();
        ArrayList pseudoStates = getAll(XMIObject.PSEUDO_STATE, true);
        for(Iterator e=pseudoStates.iterator(); e.hasNext(); ) {
            PseudoState ps = (PseudoState)e.next();
            if(ps.getTypeState()==State.DECISION) retour.add(ps);
        }
        return retour;
    }
    
    public ArrayList getAllTransitionsEntrantes(PseudoState decision) {
        ArrayList retour = new ArrayList();
        if(decision.getTypeState()==State.DECISION){
            ArrayList transitions = getAll(XMIObject.TRANSITION, true);
            for(Iterator e=transitions.iterator(); e.hasNext(); ) {
                Transition trans = (Transition) e.next();
                XMIObject dest = trans.getDestination();
                if(dest==decision) retour.add(trans);
            }
        }
        return retour;
    }
    
    
    public ArrayList getAllTransitionsSortantes(PseudoState decision) {
        ArrayList retour = new ArrayList();
        if(decision.getTypeState()==State.DECISION) {
            ArrayList transitions = getAll(XMIObject.TRANSITION, true);
            for(Iterator e=transitions.iterator(); e.hasNext(); ) {
                Transition trans = (Transition) e.next();
                XMIObject src = trans.getSource();
                if(src==decision) retour.add(trans);
            }
        }
        return retour;
    }
}
