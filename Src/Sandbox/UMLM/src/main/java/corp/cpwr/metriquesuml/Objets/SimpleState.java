/*
 * SimpleState.java
 *
 * Created on 1 juin 2004, 10:33
 */

package corp.cpwr.metriquesuml.Objets;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;

/**
 * Un état simple est un état accessible par des transitions. Il peut aussi être
 * un état représentant une sous-machine, cette représentation simplifiant alors
 * la visualisation du diagramme et sa compréhension.
 * id INTEGER, id_xmi VARCHAR(100), nom VARCHAR(30), id_Pere INTEGER, id_diagramme INTEGER,
 *   diagramme_Type INTEGER, version VARCHAR(10)
 * @author  cwfr-dzysman
 */
public class SimpleState extends State{
    /** Ce booléen indique si l'état représente une sous-machine ou non */
    private boolean isSubMachine = false;
    
    /**
     * Ce constructeur est private afin d'empecher l'instanciation d'états sans
     * informations nécessaires.
     */
    private SimpleState() {}
    
    /**
     * Ce constructeur permet d'instancier un objet de type SimpleState.
     * @param e l'élément dans l'arbre généré par JDOM représentant l'objet.
     * @param dt le type du diagramme qui contient cet objet.
     */
    public SimpleState(Element e, int dt, Modele m) {
        super(e, XMIObject.SIMPLE_STATE, dt, m);
        typeState = State.SIMPLE;
    }
    
    /**
     * Cette méthode permet de dire que l'état représente une sous-machine.
     * @param m la sous-machine que représente cet état.
     */
    public void setSubMachine() {
        isSubMachine=true;
    }
    
    public boolean isSubMachine() {
        return isSubMachine;
    }
}
