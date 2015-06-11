/*
 * LinkUML.java
 *
 * Created on 3 juin 2004, 13:05
 */

package corp.cpwr.metriquesuml.Liens;

import org.jdom.*;
import corp.cpwr.metriquesuml.*;

/**
 * Cette classe permet de gérer les dépendances entre deux classes ou interfaces
 * d'un diagramme de classes.
 * @author  cwfr-dzysman
 */
public class LinkUML extends XMIObject{
    /** L'indentifiant de la destination du lien. */
    protected String    destinationId      = null;
    /** L'identifiant de la source du lien. */
    protected String    sourceId    = null;
    /** La source du lien. */
    protected XMIObject source = null;
    /** La destination du lien. */
    protected XMIObject destination = null;
    /** Le package Pere, s'il en a un*/
    protected XMIObject  packagePere = null;
    
    /**
     * Ce constructeur est <code>private</code> afin d'empêcher la création
     * de dépendances sans les informations nécessaires.
     */
    protected LinkUML() {
    }
    
    /**
     * Ce constructeur de classe permet la création d'instances de Dependency car
     * il demande en paramètres toutes les informations nécessaires à cette création.
     *
     * @param e   un objet de type Element qui représente la dépendance à analyser.
     *            Cet objet est extrait de l'arbre créé par JDOM et représentant
     *            le flux XMI.
     * @param dt  le type du diagramme qui contient cette dépendance.
     * @param table la table dans la base de données contenant les informations sur l'objet.
     * @param src le nom de l'attribut donnant la source.
     * @param dest le nom de l'attribut donnant la destination.
     * @param m le modèle auquel appartient l'objet.
     */
    public LinkUML(Element e, int tobj, int dt, String src, String dest, Modele m, XMIObject pkg) {
        super(e, tobj, m, dt, false);
        packagePere = pkg;
        sourceId = e.getAttributeValue(src);
        destinationId = e.getAttributeValue(dest);
    }
    
    /**
     * Cette méthode retourne l'identifiant de la source du lien.
     * @return L'identifiant de la source du lien.
     */
    public String getSourceId() { return sourceId;}
    
    /**
     * Cette méthode retourne l'identifiant de la destination du lien.
     * @return L'identifiant de la destination du lien.
     */
    public String getDestinationId() { return destinationId;}
    
    /**
     * Cette méthode retourne l'identifiant de la source du lien.
     * @return L'identifiant de la source du lien.
     */
    public XMIObject getSource() { return source;}
    
    /**
     * Cette méthode retourne l'identifiant de la destination du lien.
     * @return L'identifiant de la destination du lien.
     */
    public XMIObject getDestination() { return destination;}

    /**
     * Cette méthode met à jour la source du lien.
     * @param o La source du lien.
     */
    public void setSource(XMIObject o) { source = o;}
    
    /**
     * Cette méthode met à jour la destination du lien.
     * @param o La destination du lien.
     */
    public void setDestination(XMIObject o) { destination = o;}
    
    /**
     * Cette méthode indique si l'objet donné en paramètre est source du lien.
     * @param obj L'objet dont on veut savoir s'il est source du lien.
     * @return un booléen qui vaut "true" si l'objet est source du lien, "false" sinon.
     */
    public boolean isSource(XMIObject obj) {return obj.getId().equals(sourceId);}
    
    /**
     * Cette méthode indique si l'objet donné en paramètre est destination du lien.
     * @param obj L'objet dont on veut savoir s'il est destination du lien.
     * @return un booléen qui vaut "true" si l'objet est destination du lien, "false" sinon.
     */
    public boolean isDestination(XMIObject obj) {return obj.getId().equals(destinationId);}
    
}
