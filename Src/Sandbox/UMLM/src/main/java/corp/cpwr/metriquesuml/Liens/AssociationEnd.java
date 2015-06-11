/*
 * AssociationEnd.java
 *
 * Created on 11 mai 2004, 15:38
 */

package corp.cpwr.metriquesuml.Liens;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;

/**
 * Cette classe permet la r�pr�sentation d'une extr�mit� d'une association, avec
 * son nom, ses cardinalit�s, son type et la classe � laquelle elle est reli�e.
 * @see Cardinalite
 * @author  cwfr-dzysman
 */
public class AssociationEnd extends XMIObject{
    
    /** La cardinalit� associ�e � l'instance de l'AssociationEnd. */
    private Cardinalite card=null;
    /** Le type de l'AssociationEnd. Par d�faut, NOT_DEFINED. */
    private int         typeAgregation = Association.NOT_DEFINED;
    /** L'identifiant de la classe associ�e � l'AssociationEnd. */
    private String      idObject=null;
    /** La classe associ�e � l'AssociationEnd. */
    private XMIObject   objectEnd=null;
    /** Indique si l'associationEnd est navigable ou non.
     * C'est d�termin� de la mani�re suivante :
     * false -------<> true
     */
    private boolean     navigable=false;
    
    /**
     * Ce constructeur est <code>private</code> pour emp�cher la cr�ation d'une
     * AssociationEnd sans ses param�tres essentiels.
     */
    private AssociationEnd(){}
    
    /**
     * Ce constructeur permet la cr�ation de l'objet AssociationEnd. Est envoy�
     * en param�tre l'�l�ment dans l'arbre cr�� par JDOM et qui correspond au
     * fichier XMI.
     * <p>
     * Le constructeur cherche dans l'�l�ment l'attribut "multiplicity" qui permet
     * de conna�tre la cardinalit� de l'AssociationEnd. L'attribut "name" donne le
     * nom de l'AssociationEnd. L'attribut "type" indique � quelle classe est reli�e
     * l'AssociationEnd. L'attribut "aggregation" indique, quant � lui, quel est
     * le type de l'AssociationEnd.
     *
     * @param e  l'�l�ment dans l'arbre cr�� par JDOM qui correspond � l'AssociationEnd.
     * @param m  le mod�le qui contient cet objet.
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
     * Cette m�thode retourne l'identifiant de la classe associ�e � cette instance
     * de AssociationEnd.
     *
     * @return L'identifiant de la classe associ�e � l'AssociationEnd.
     */
    public String getIdExtremite() {return idObject;}
    
    /**
     * Cette m�thode indique quel est l'objet associ� � l'AssociationEnd. Cet objet
     * peut �tre soit une InterfaceDC, soit une ClasseDC. C'est donc une DC.
     *
     * @param c l'objet qui est li� � l'AssociationEnd.
     * @see InterfaceDC
     * @see ClasseDC
     * @see DC
     */
    public void setEnd(XMIObject c) {
        objectEnd = c;
    }
    
    /**
     * Cette m�thode permet de dire si l'associationEnd est navigable ou non.
     * @return la navigabilit� de l'associationEnd.
     */
    public boolean isNavigable() {return navigable;}
    
    /**
     * Cette m�thode retourne la classe ou interface qui se trouve � l'extr�mit�
     * de l'association.
     *
     * @return L'extr�mit� de l'association.
     */
    public XMIObject getExtremite() {return objectEnd;}
    
    /**
     * Cette m�thode retourne la cardinalit� de l'AssociationEnd.
     *
     * @return La cardinalit� de l'AssociationEnd.
     */
    public Cardinalite getCardinalite() {return card;}
    
    /**
     * Cette m�thode renvoie le type de l'agr�gation.
     * @return le type de l'agr�gation.
     */
    public int getTypeAgregation() {return typeAgregation;}
}
