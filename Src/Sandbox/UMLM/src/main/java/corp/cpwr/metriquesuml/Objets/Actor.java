/*
 * Actor.java
 *
 * Created on 19 mai 2004, 10:35
 */

package corp.cpwr.metriquesuml.Objets;
import java.util.*;
import corp.cpwr.metriquesuml.*;
import org.jdom.*;

/**
 * Cette classe permet de repr�senter un acteur d'un diagramme de cas d'utilisation ou d'un
 * diagramme de s�quence.
 * @author  cwfr-dzysman
 */
public class Actor extends XMIObject{
    /** Le package contenant l'Actor. */
    private XMIObject  packagePere = null;
    /** La liste des acteurs dont il h�rite */
    private ArrayList     acteursHerites = null;
    /** La liste des acteurs qui h�ritent de lui */
    private ArrayList     acteursHeritants = null;
    
    /**
     * Ce constructeur est <code>private</code> car il ne doit pas �tre possible
     * de cr�er d'Actor sans toutes les informations n�cessaires.
     */
    private Actor() {}
    
    /**
     * Ce constructeur permet la cr�ation d'une instance de la classe Actor.
     *
     * @param e L'�l�ment extrait de l'arbre g�n�r� par JDOM et repr�sentant l'Actor.
     * @param p Le package contenant l'Actor.
     * @param t Le type du diagramme auquel appartient l'objet.
     */
    public Actor(Element e, XMIObject p, int t, Modele m) {
        super(e, XMIObject.ACTOR, m, t, false);
        acteursHerites = new ArrayList();
        acteursHeritants = new ArrayList();
        packagePere = p;
    }

    /**
     * Cette m�thode permet d'ajouter un acteur h�ritant.
     * @param a l'acteur h�ritant.
     */
    public void addHeritant(Actor a) {acteursHeritants.add(a);}
    
    /**
     * Cette m�thode permet d'ajouter un acteur h�rit�.
     * @param a l'acteur h�rit�.
     */
    public void addHerite(Actor a) {acteursHerites.add(a);}
}
