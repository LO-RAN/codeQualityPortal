/*
 * AssociationEnd.java
 *
 * Created on 11 mai 2004, 15:38
 */

package corp.cpwr.metriquesuml.Liens;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;

/**
 * Cette classe permet la réprésentation d'une extrémité d'une association, avec
 * son nom, ses cardinalités, son type et la classe à laquelle elle est reliée.
 * @see Cardinalite
 * @author  cwfr-dzysman
 */
public class AssociationEnd extends XMIObject{
    
    /** La cardinalité associée à l'instance de l'AssociationEnd. */
    private Cardinalite card=null;
    /** Le type de l'AssociationEnd. Par défaut, NOT_DEFINED. */
    private int         typeAgregation = Association.NOT_DEFINED;
    /** L'identifiant de la classe associée à l'AssociationEnd. */
    private String      idObject=null;
    /** La classe associée à l'AssociationEnd. */
    private XMIObject   objectEnd=null;
    /** Indique si l'associationEnd est navigable ou non.
     * C'est déterminé de la manière suivante :
     * false -------<> true
     */
    private boolean     navigable=false;
    
    /**
     * Ce constructeur est <code>private</code> pour empêcher la création d'une
     * AssociationEnd sans ses paramètres essentiels.
     */
    private AssociationEnd(){}
    
    /**
     * Ce constructeur permet la création de l'objet AssociationEnd. Est envoyé
     * en paramètre l'élément dans l'arbre créé par JDOM et qui correspond au
     * fichier XMI.
     * <p>
     * Le constructeur cherche dans l'élément l'attribut "multiplicity" qui permet
     * de connaître la cardinalité de l'AssociationEnd. L'attribut "name" donne le
     * nom de l'AssociationEnd. L'attribut "type" indique à quelle classe est reliée
     * l'AssociationEnd. L'attribut "aggregation" indique, quant à lui, quel est
     * le type de l'AssociationEnd.
     *
     * @param e  l'élément dans l'arbre créé par JDOM qui correspond à l'AssociationEnd.
     * @param m  le modèle qui contient cet objet.
     * @param dt le type du diagramme qui contient cet objet.
     */
    public AssociationEnd(Element e, Modele m, int dt) {
        super(e, XMIObject.ASSOCIATIONEND, m, dt, false);
        String temp = e.getAttributeValue("multiplicity");
        if(temp!=null) card = new Cardinalite(temp);
        idObject = e.getAttributeValue("type");
        
        String sValue = e.getAttributeValue("aggregation");
        if(sValue!=null) {
            if(sValue.equals("none")) typeAgregation = Association.ASSOCIATION;
            else if(sValue.equals("composite")) typeAgregation = Association.COMPOSITION;
            else if(sValue.equals("shared")) typeAgregation = Association.AGGREGATION;
        }
        
        sValue = e.getAttributeValue("isNavigable");
        navigable = sValue.equals("true");
    }
    
    /**
     * Cette méthode retourne l'identifiant de la classe associée à cette instance
     * de AssociationEnd.
     *
     * @return L'identifiant de la classe associée à l'AssociationEnd.
     */
    public String getIdExtremite() {return idObject;}
    
    /**
     * Cette méthode indique quel est l'objet associé à l'AssociationEnd. Cet objet
     * peut être soit une InterfaceDC, soit une ClasseDC. C'est donc une DC.
     *
     * @param c l'objet qui est lié à l'AssociationEnd.
     * @see InterfaceDC
     * @see ClasseDC
     * @see DC
     */
    public void setEnd(XMIObject c) {
        objectEnd = c;
    }
    
    /**
     * Cette méthode permet de dire si l'associationEnd est navigable ou non.
     * @return la navigabilité de l'associationEnd.
     */
    public boolean isNavigable() {return navigable;}
    
    /**
     * Cette méthode retourne la classe ou interface qui se trouve à l'extrémité
     * de l'association.
     *
     * @return L'extrémité de l'association.
     */
    public XMIObject getExtremite() {return objectEnd;}
    
    /**
     * Cette méthode retourne la cardinalité de l'AssociationEnd.
     *
     * @return La cardinalité de l'AssociationEnd.
     */
    public Cardinalite getCardinalite() {return card;}
    
    /**
     * Cette méthode renvoie le type de l'agrégation.
     * @return le type de l'agrégation.
     */
    public int getTypeAgregation() {return typeAgregation;}
}
