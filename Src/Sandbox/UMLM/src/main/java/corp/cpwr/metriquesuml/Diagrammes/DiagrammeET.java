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
     * Ce constructeur est <code>private</code> car il ne doit pas être possible
     * de créer de diagramme de classes sans toutes les informations nécessaires.
     */
    private DiagrammeET() {
        super();
    }
    
    /**
     * Ce constructeur de classe permet la création d'un diagramme de classes.
     * Il ne lance pas automatiquement l'analyse de ce diagramme.
     *
     * @param e objet de type Element étant la racine du sous-arbre représentant
     *          le diagramme de classes dans l'arbre généré par JDOM.
     * @param n le nom du diagramme de classes.
     * @param m le modèle auquel appartient l'objet.
     */
    public DiagrammeET(Element e, String n, Modele m){
        super(e, n, m, XMIObject.DIAGRAMME_ET);
    }
    
    /**
     * Cette méthode lance l'analyse du diagramme de classes en cherchant toutes
     * les balises "Package" à partir de l'élément racine du diagramme et en
     * les rajoutant dans le diagramme.
     * Le nouveau package racine est le premier package trouvé.
     */
    public void analyseDiagramme() {
        super.analyseDiagramme();
        //on commence par récupérer tous les états.
        ArrayList vect = FonctionsParser.findAllBalises(getRootElement(), "SimpleState");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new SimpleState((Element)it.next(), XMIObject.DIAGRAMME_ET, getModele()));
        }
    }
    
    /**
     * Cette méthode remplit tous les champs manquants.
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
        //on calcule la complexité.
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
