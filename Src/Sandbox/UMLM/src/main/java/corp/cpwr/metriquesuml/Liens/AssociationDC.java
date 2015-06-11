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
     * La classe associée à cette association. Ne doit être initialisée que si
     * l'association est une AssociationClass.
     */
    private XMIObject  associationClassDC = null;
    /**
     * L'identifiant de la classe associée à cette association. N'est initialisé
     * que si l'association est une AssociationClass.
     */
    private String    associationClassId = null;
    
    
    /** Ce constructeur est private afin d'empecher la création d'instances d'associations sans
     * les informations nécessaires.
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
     * Cette méthode indique si l'association est une AssociationClass, c'est-à-dire
     * une association entre des classes définie par une autre classe.
     *
     * @return un booléen indiquant si l'association est une AssociationClass (true)
     *         ou non (false).
     */
    public boolean isAssociationClass() {return associationClass;}
    
    /**
     * Cette méthode permet de définir quelle est la ClasseDC qui définie l'association,
     * c'est-à-dire quelle est la classe qui définie l'association.
     *
     * @param cdc la ClasseDC qui définie l'association.
     * @see   ClasseDC
     */
    public void setAssociationClassDC(DC cdc) {
        if(cdc!=null) {
            associationClass = true;
            associationClassDC = cdc;
        }
    }
    
    /**
     * Cette méthode retourne le <i>id</i> de l'AssociationClass.
     *
     * @return l'<i>id</i> de la classe.
     */
    public String getAssociationClassId() {return associationClassId;}
}
