/*
 * UseCase.java
 *
 * Created on 24 mai 2004, 11:13
 */

package corp.cpwr.metriquesuml.Objets;

import org.jdom.*;
import corp.cpwr.metriquesuml.Packages.*;
import corp.cpwr.metriquesuml.*;

/**
 * Cette classe permet de g�rer les cas d'utilisation des diagrammes de cas
 * d'utilisation.
 *
 * IMPORTANT : UN USE CASE PEUT AVOIR DES ATTRIBUTS ET DES OPERATIONS (d'apr�s les
 * sp�cifications de l'OMG pour UML 1.5). Pourtant, si Entreprise Architect permet
 * de les d�finir, il ne permet pas de les exporter dans un flux XMI.
 * @author  cwfr-dzysman
 */
public class UseCase extends XMIObject{
    
    /** Le package p�re. */
    private PackageUC packagePere = null;
    
    /** LES METRIQUES **/
    private int      nbActeursAssocies = 0;
    private int      nbMessagesAssocies= 0;
    /** Ce constructeur est private afin de ne pas pouvoir cr�er de cas d'utilisations
     * sans les informations n�cessaires.
     */
    private UseCase() {}
    
    /**
     * Ce constructeur permet d'instancier la classe UseCase. Il remplit tous les
     * champs n�cessaires pour pouvoir ensuite analyser le UseCase.
     * @param e L'�l�ment racine du sous-arbre g�n�r� par JDOM et d�finissant le
     *          cas d'utilisation.
     * @param p Le package contenant ce cas d'utilisation.
     * @param m le mod�le auquel appartient l'objet.
     * @param to le type de l'objet.
     */
    public UseCase(Element e, PackageUC p, Modele m, int to) {
        super(e, to, m, XMIObject.DIAGRAMME_UC, false);
        packagePere = p;
    }
    
    public void setNbActeursAssocies(int n)  {nbActeursAssocies = n;}
    public void setNbMessagesAssocies(int n) {nbMessagesAssocies = n;}
    public int  getNbActeursAssocies()  {return nbActeursAssocies;}
    public int  getNbMessagesAssocies() {return nbMessagesAssocies;}
}
