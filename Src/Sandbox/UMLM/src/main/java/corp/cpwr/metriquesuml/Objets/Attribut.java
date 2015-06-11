/*
 * Attribut.java
 *
 * Created on 11 mai 2004, 09:49
 */

package corp.cpwr.metriquesuml.Objets;

import org.jdom.*;
import corp.cpwr.metriquesuml.*;

/**
 * Cette classe permet la représentation d'un attribut de classe, tel qu'il est
 * possible de le définir dans un diagramme de classes.
 * @author  cwfr-dzysman
 */
public class Attribut extends XMIObject{
    /**
     * Le type de l'attribut.
     */
    private String type = null;
    /**
     * La classe "propriétaire" de l'attribut.
     */
    private XMIObject objetPere = null;
    
    /**
     * Ce constructeur de classe est <code>private</code> afin d'empecher la création d'un
     * attribut sans informations.
     */
    private Attribut() { }
    
    /**
     * Ce constructeur de classe permet de créer un attribut avec les informations
     * nécessaires. L'élément envoyé en paramètre correspond au noeud dans l'arbre
     * créé par JDOM définissant l'attribut.
     *
     * @param e  l'élément dans l'arbre de JDOM définissant l'attribut.
     * @param cM la classe "propriétaire" de l'attribut.
     * @see   ClasseDC
     */
    public Attribut(Element e, XMIObject cM, Modele m, int typeO) {
        super(e, typeO, m, XMIObject.DIAGRAMME_CLASSE, false);
        objetPere = cM;
        type = FonctionsParser.getPropertyString(e, "type");
        setId(FonctionsParser.getPropertyString(e, "ea_guid"));
    }
    
    /**
     * Cette méthode retourne le type de l'attribut.
     *
     * @return le type de l'attribut.
     */
    public String getType() {return type;}
}
