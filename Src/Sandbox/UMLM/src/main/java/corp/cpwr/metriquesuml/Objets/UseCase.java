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
 * Cette classe permet de gérer les cas d'utilisation des diagrammes de cas
 * d'utilisation.
 *
 * IMPORTANT : UN USE CASE PEUT AVOIR DES ATTRIBUTS ET DES OPERATIONS (d'après les
 * spécifications de l'OMG pour UML 1.5). Pourtant, si Entreprise Architect permet
 * de les définir, il ne permet pas de les exporter dans un flux XMI.
 * @author  cwfr-dzysman
 */
public class UseCase extends XMIObject{
    
    /** Le package père. */
    private PackageUC packagePere = null;
    
    /** LES METRIQUES **/
    private int      nbActeursAssocies = 0;
    private int      nbMessagesAssocies= 0;
    /** Ce constructeur est private afin de ne pas pouvoir créer de cas d'utilisations
     * sans les informations nécessaires.
     */
    private UseCase() {}
    
    /**
     * Ce constructeur permet d'instancier la classe UseCase. Il remplit tous les
     * champs nécessaires pour pouvoir ensuite analyser le UseCase.
     * @param e L'élément racine du sous-arbre généré par JDOM et définissant le
     *          cas d'utilisation.
     * @param p Le package contenant ce cas d'utilisation.
     * @param m le modèle auquel appartient l'objet.
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
