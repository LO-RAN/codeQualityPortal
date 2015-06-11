/*
 * SimpleState.java
 *
 * Created on 1 juin 2004, 10:33
 */

package corp.cpwr.metriquesuml.Objets;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;

/**
 * Un �tat simple est un �tat accessible par des transitions. Il peut aussi �tre
 * un �tat repr�sentant une sous-machine, cette repr�sentation simplifiant alors
 * la visualisation du diagramme et sa compr�hension.
 * id INTEGER, id_xmi VARCHAR(100), nom VARCHAR(30), id_Pere INTEGER, id_diagramme INTEGER,
 *   diagramme_Type INTEGER, version VARCHAR(10)
 * @author  cwfr-dzysman
 */
public class SimpleState extends State{
    /** Ce bool�en indique si l'�tat repr�sente une sous-machine ou non */
    private boolean isSubMachine = false;
    
    /**
     * Ce constructeur est private afin d'empecher l'instanciation d'�tats sans
     * informations n�cessaires.
     */
    private SimpleState() {}
    
    /**
     * Ce constructeur permet d'instancier un objet de type SimpleState.
     * @param e l'�l�ment dans l'arbre g�n�r� par JDOM repr�sentant l'objet.
     * @param dt le type du diagramme qui contient cet objet.
     */
    public SimpleState(Element e, int dt, Modele m) {
        super(e, XMIObject.SIMPLE_STATE, dt, m);
        typeState = State.SIMPLE;
    }
    
    /**
     * Cette m�thode permet de dire que l'�tat repr�sente une sous-machine.
     * @param m la sous-machine que repr�sente cet �tat.
     */
    public void setSubMachine() {
        isSubMachine=true;
    }
    
    public boolean isSubMachine() {
        return isSubMachine;
    }
}
