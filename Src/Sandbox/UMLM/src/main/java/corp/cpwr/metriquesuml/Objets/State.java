/*
 * State.java
 *
 * Created on 28 juin 2004, 09:30
 */

package corp.cpwr.metriquesuml.Objets;


import corp.cpwr.metriquesuml.*;
import org.jdom.*;


/**
 *
 * @author  cwfr-dzysman
 */
public class State extends XMIObject{
    /** type de state : DECISION */
    public static final int DECISION = 0;
    /** type de state : SYNCHRONISATION */
    public static final int SYNCHRONISATION = 1;
    /** type de state : INITIAL */
    public static final int INITIAL = 3;
    /** type de state : FINAL */
    public static final int FINAL = 4;
    /** type de state : HISTORY */
    public static final int HISTORY = 5;
    /** type de state : SIMPLE */
    public static final int SIMPLE = 6;
    
    
    /** L'état père de cet état, si celui-ci appartient à une sous-machine */
    protected SimpleState pere = null;
    /** L'id tel qu'il est lu dans le fichier XMI de l'état père de l'état */
    protected String idPere = null;
    /** Le type de l'état */
    protected int    typeState = XMIObject.NOT_DEFINED;
    
    /** Creates a new instance of State */
    protected State() { }

    public State(Element e, int typeO, int dt, Modele m) {
        super(e, typeO, m, dt, false);
        idPere = FonctionsParser.getPropertyString(e, "owner");
    }
    
     /**
     * Cette méthode met à jour l'information de l'objet concernant son "père".
     * @param p le nouveau père de l'objet.
     */
    public void setEtatPere(SimpleState p) {
        pere = p;
    }
    
    public SimpleState getEtatPere() {return pere;}
    
    /**
     * Cette méthode retourne l'identifiant du père, s'il en a un.
     * @return L'identifiant du père.
     */
    public String getPereId() {return idPere;}
   
    /**
     * Cette méthode retourne le type du pseudoState.
     * @return le type du pseudoState.
     */
    public int getTypeState() {return typeState;}
}
