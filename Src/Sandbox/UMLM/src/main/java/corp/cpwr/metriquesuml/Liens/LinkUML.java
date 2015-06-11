/*
 * LinkUML.java
 *
 * Created on 3 juin 2004, 13:05
 */

package corp.cpwr.metriquesuml.Liens;

import org.jdom.*;
import corp.cpwr.metriquesuml.*;

/**
 * Cette classe permet de g�rer les d�pendances entre deux classes ou interfaces
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
     * Ce constructeur est <code>private</code> afin d'emp�cher la cr�ation
     * de d�pendances sans les informations n�cessaires.
     */
    protected LinkUML() {
    }
    
    /**
     * Ce constructeur de classe permet la cr�ation d'instances de Dependency car
     * il demande en param�tres toutes les informations n�cessaires � cette cr�ation.
     *
     * @param e   un objet de type Element qui repr�sente la d�pendance � analyser.
     *            Cet objet est extrait de l'arbre cr�� par JDOM et repr�sentant
     *            le flux XMI.
     * @param dt  le type du diagramme qui contient cette d�pendance.
     * @param table la table dans la base de donn�es contenant les informations sur l'objet.
     * @param src le nom de l'attribut donnant la source.
     * @param dest le nom de l'attribut donnant la destination.
     * @param m le mod�le auquel appartient l'objet.
     */
    public LinkUML(Element e, int tobj, int dt, String src, String dest, Modele m, XMIObject pkg) {
        super(e, tobj, m, dt, false);
        packagePere = pkg;
        sourceId = e.getAttributeValue(src);
        destinationId = e.getAttributeValue(dest);
    }
    
    /**
     * Cette m�thode retourne l'identifiant de la source du lien.
     * @return L'identifiant de la source du lien.
     */
    public String getSourceId() { return sourceId;}
    
    /**
     * Cette m�thode retourne l'identifiant de la destination du lien.
     * @return L'identifiant de la destination du lien.
     */
    public String getDestinationId() { return destinationId;}
    
    /**
     * Cette m�thode retourne l'identifiant de la source du lien.
     * @return L'identifiant de la source du lien.
     */
    public XMIObject getSource() { return source;}
    
    /**
     * Cette m�thode retourne l'identifiant de la destination du lien.
     * @return L'identifiant de la destination du lien.
     */
    public XMIObject getDestination() { return destination;}

    /**
     * Cette m�thode met � jour la source du lien.
     * @param o La source du lien.
     */
    public void setSource(XMIObject o) { source = o;}
    
    /**
     * Cette m�thode met � jour la destination du lien.
     * @param o La destination du lien.
     */
    public void setDestination(XMIObject o) { destination = o;}
    
    /**
     * Cette m�thode indique si l'objet donn� en param�tre est source du lien.
     * @param obj L'objet dont on veut savoir s'il est source du lien.
     * @return un bool�en qui vaut "true" si l'objet est source du lien, "false" sinon.
     */
    public boolean isSource(XMIObject obj) {return obj.getId().equals(sourceId);}
    
    /**
     * Cette m�thode indique si l'objet donn� en param�tre est destination du lien.
     * @param obj L'objet dont on veut savoir s'il est destination du lien.
     * @return un bool�en qui vaut "true" si l'objet est destination du lien, "false" sinon.
     */
    public boolean isDestination(XMIObject obj) {return obj.getId().equals(destinationId);}
    
}
