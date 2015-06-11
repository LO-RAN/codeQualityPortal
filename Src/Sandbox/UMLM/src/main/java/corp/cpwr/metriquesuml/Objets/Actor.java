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
 * Cette classe permet de représenter un acteur d'un diagramme de cas d'utilisation ou d'un
 * diagramme de séquence.
 * @author  cwfr-dzysman
 */
public class Actor extends XMIObject{
    /** Le package contenant l'Actor. */
    private XMIObject  packagePere = null;
    /** La liste des acteurs dont il hérite */
    private ArrayList     acteursHerites = null;
    /** La liste des acteurs qui héritent de lui */
    private ArrayList     acteursHeritants = null;
    
    /**
     * Ce constructeur est <code>private</code> car il ne doit pas être possible
     * de créer d'Actor sans toutes les informations nécessaires.
     */
    private Actor() {}
    
    /**
     * Ce constructeur permet la création d'une instance de la classe Actor.
     *
     * @param e L'élément extrait de l'arbre généré par JDOM et représentant l'Actor.
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
     * Cette méthode permet d'ajouter un acteur héritant.
     * @param a l'acteur héritant.
     */
    public void addHeritant(Actor a) {acteursHeritants.add(a);}
    
    /**
     * Cette méthode permet d'ajouter un acteur hérité.
     * @param a l'acteur hérité.
     */
    public void addHerite(Actor a) {acteursHerites.add(a);}
}
