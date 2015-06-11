/*
 * Attribut.java
 *
 * Created on 11 mai 2004, 09:49
 */

package corp.cpwr.metriquesuml.Objets;

import org.jdom.*;
import corp.cpwr.metriquesuml.*;

/**
 * Cette classe permet la repr�sentation d'un attribut de classe, tel qu'il est
 * possible de le d�finir dans un diagramme de classes.
 * @author  cwfr-dzysman
 */
public class Attribut extends XMIObject{
    /**
     * Le type de l'attribut.
     */
    private String type = null;
    /**
     * La classe "propri�taire" de l'attribut.
     */
    private XMIObject objetPere = null;
    
    /**
     * Ce constructeur de classe est <code>private</code> afin d'empecher la cr�ation d'un
     * attribut sans informations.
     */
    private Attribut() { }
    
    /**
     * Ce constructeur de classe permet de cr�er un attribut avec les informations
     * n�cessaires. L'�l�ment envoy� en param�tre correspond au noeud dans l'arbre
     * cr�� par JDOM d�finissant l'attribut.
     *
     * @param e  l'�l�ment dans l'arbre de JDOM d�finissant l'attribut.
     * @param cM la classe "propri�taire" de l'attribut.
     * @see   ClasseDC
     */
    public Attribut(Element e, XMIObject cM, Modele m, int typeO) {
        super(e, typeO, m, XMIObject.DIAGRAMME_CLASSE, false);
        objetPere = cM;
        type = FonctionsParser.getPropertyString(e, "type");
        setId(FonctionsParser.getPropertyString(e, "ea_guid"));
    }
    
    /**
     * Cette m�thode retourne le type de l'attribut.
     *
     * @return le type de l'attribut.
     */
    public String getType() {return type;}
}
