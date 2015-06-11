/*
 * AssociationDC.java
 *
 * Created on 4 juin 2004, 11:18
 */

package corp.cpwr.metriquesuml.Liens;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;
import corp.cpwr.metriquesuml.Objets.*;

/**
 *
 * @author  cwfr-dzysman
 */
public class AssociationDC extends Association{
    /**
     * Indique si l'association est une AssociationClass ou non.
     */
    private boolean   associationClass    = false;
    /**
     * La classe associ�e � cette association. Ne doit �tre initialis�e que si
     * l'association est une AssociationClass.
     */
    private XMIObject  associationClassDC = null;
    /**
     * L'identifiant de la classe associ�e � cette association. N'est initialis�
     * que si l'association est une AssociationClass.
     */
    private String    associationClassId = null;
    
    
    /** Ce constructeur est private afin d'empecher la cr�ation d'instances d'associations sans
     * les informations n�cessaires.
     */
    private AssociationDC() {}
    
    public AssociationDC(Element e, XMIObject p, Modele m) {
        super(e, p, m, XMIObject.ASSOCIATIONDC, XMIObject.DIAGRAMME_CLASSE);
        String temp=null;
        if((temp=FonctionsParser.getPropertyString(e,"associationclass"))!=null){
            associationClassId = temp;
        }
    }
    
    /**
     * Cette m�thode indique si l'association est une AssociationClass, c'est-�-dire
     * une association entre des classes d�finie par une autre classe.
     *
     * @return un bool�en indiquant si l'association est une AssociationClass (true)
     *         ou non (false).
     */
    public boolean isAssociationClass() {return associationClass;}
    
    /**
     * Cette m�thode permet de d�finir quelle est la ClasseDC qui d�finie l'association,
     * c'est-�-dire quelle est la classe qui d�finie l'association.
     *
     * @param cdc la ClasseDC qui d�finie l'association.
     * @see   ClasseDC
     */
    public void setAssociationClassDC(DC cdc) {
        if(cdc!=null) {
            associationClass = true;
            associationClassDC = cdc;
        }
    }
    
    /**
     * Cette m�thode retourne le <i>id</i> de l'AssociationClass.
     *
     * @return l'<i>id</i> de la classe.
     */
    public String getAssociationClassId() {return associationClassId;}
}
