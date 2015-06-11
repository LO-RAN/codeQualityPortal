/*
 * DiagrammeActivites.java
 *
 * Created on 2 juin 2004, 09:46
 */

package corp.cpwr.metriquesuml.Diagrammes;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;
import java.util.*;
import corp.cpwr.metriquesuml.MetriquesUMLExceptions.*;
import corp.cpwr.metriquesuml.Objets.*;
import corp.cpwr.metriquesuml.Liens.*;

/**
 *
 * @author  cwfr-dzysman
 */

public class DiagrammeActivites extends DiagrammeActET {
    
    /**
     * Ce constructeur est private afin d'empecher la création d'instances de
     * DiagrammeActivites sans les informations nécessaires.
     */
    private DiagrammeActivites() {
        super();
    }
    
    public DiagrammeActivites(Element e, String n, Modele m) {
        super(e,n,m, XMIObject.DIAGRAMME_ACT);
    }
    
    public void analyseDiagramme() {
        super.analyseDiagramme();
        //on commence par récupérer tous les états.
        ArrayList vect = FonctionsParser.findAllBalises(getRootElement(), "ActionState");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new SimpleState((Element)it.next(),XMIObject.DIAGRAMME_ACT, getModele()));
        }
        
        //et les acteurs
        vect = FonctionsParser.findAllBalises(getRootElement(), "Actor");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new Actor((Element)it.next(), null, XMIObject.DIAGRAMME_ACT, getModele()));
        }
        
        //on récupère tous les events
        vect = FonctionsParser.findAllBalises(getRootElement(), "Event");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new EventAct((Element)it.next(), XMIObject.DIAGRAMME_ACT, getModele()));
        }
        
        //on récupère toutes les dépendences
        vect = FonctionsParser.findAllBalises(getRootElement(), "Dependency");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new LinkUML((Element)it.next(), XMIObject.DEPENDENCY, XMIObject.DIAGRAMME_ACT, "supplier", "client", getModele(), (XMIObject)null));
        }
        
    }
    
    public void fillAll() {
        super.fillAll();
        int taille = objets.size();
        for(int i=0; i<taille; i++){
            XMIObject obj = (XMIObject) objets.get(i);
            switch(obj.getTypeObjet()) {
                case XMIObject.DEPENDENCY:
                    try {
                        String src = ((LinkUML)obj).getSourceId();
                        String dest = ((LinkUML)obj).getDestinationId();
                        ((LinkUML)obj).setDestination(getBy(dest,true));
                        ((LinkUML)obj).setSource(getBy(src,true));
                    } catch(XMIObjectNotFoundException exc) {
                        objets.remove(obj);
                        i--; taille--;
                        continue;
                    }
                    break;
            }
        }
    }
    
    public void calculerMetriques() {
        calculerComplexite();
        calculerBienForme();
    }
    
    public int getComplexiteCyclomatique() {
        ArrayList transitions = getAll(XMIObject.TRANSITION, true);
        ArrayList dependencies = getAll(XMIObject.DEPENDENCY, true);
        ArrayList actors = getAll(XMIObject.ACTOR, true);
        ArrayList pseudoStates = getAll(XMIObject.PSEUDO_STATE, true);
        ArrayList actionStates = getAll(XMIObject.SIMPLE_STATE, true);
        ArrayList objetsUML = getAll(XMIObject.OBJECT_UML, true);
        ArrayList events = getAll(XMIObject.EVENT_ACT, true);
        int somme = transitions.size() + dependencies.size();
        somme = somme - actors.size() - pseudoStates.size() - actionStates.size() - events.size() - objetsUML.size() + 2;
        return somme;
    }
    
    public void calculerComplexite() {
        ArrayList transitions = getAll(XMIObject.TRANSITION, true);
        ArrayList dependencies = getAll(XMIObject.DEPENDENCY, true);
        ArrayList actors = getAll(XMIObject.ACTOR, true);
        ArrayList pseudoStates = getAll(XMIObject.PSEUDO_STATE, true);
        ArrayList actionStates = getAll(XMIObject.SIMPLE_STATE, true);
        ArrayList objetsUML = getAll(XMIObject.OBJECT_UML, true);
        ArrayList events = getAll(XMIObject.EVENT_ACT, true);
        float diviseur = ((float)pseudoStates.size()+ (float)actionStates.size()+(float)actors.size()+(float)events.size()+(float)objetsUML.size());
        if(diviseur!=0.0f)  complexite = ((float)transitions.size()+(float)dependencies.size()) / diviseur;
    }
}
