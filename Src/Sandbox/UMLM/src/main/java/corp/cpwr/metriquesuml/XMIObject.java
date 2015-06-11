/*
 * XMIObject.java
 *
 * Created on 24 mai 2004, 14:07
 */

package corp.cpwr.metriquesuml;
import org.jdom.*;
import java.util.*;
import corp.cpwr.metriquesuml.MetriquesUMLExceptions.*;

/**
 * La classe de base de tous les objets extraits d'un flux XMI.
 * @author  cwfr-dzysman
 */
public class XMIObject {
    //          --- LES TYPES DES OBJETS --- Initialisé à la construction.
    /** type : non initialisé */
    public static final int NOT_DEFINED = -1;
    
    //les nombres 1 à 19 sont réservés pour les classes du package Commun.
    /** type : Dependency */
    public static final int LINKSUML = 1;
    /** type : Generalization */
    public static final int GENERALIZATION = 2;
    /** type : SimpleState */
    public static final int SIMPLE_STATE = 3;
    /** type : TransitionET */
    public static final int TRANSITION = 4;
    /** type : ObjectET */
    public static final int OBJECT_UML = 5;
    /** type : PseudoState */
    public static final int PSEUDO_STATE = 6;
    /** type : Actor */
    public static final int ACTOR = 7;
    /** type : Association */
    public static final int ASSOCIATION = 8;
    /** type : AssociationEndDC */
    public static final int ASSOCIATIONEND = 9;
    /** type : Modele */
    public static final int MODELE = 10;
    /** type : Dependency */
    public static final int DEPENDENCY = 11;
    
    
    //les nombres 20 à 39 sont réservés au diagramme de classes
    /** type : DiagrammeClasse */
    public static final int DIAGRAMME_CLASSE = 20;
    /** type : PackageDC */
    public static final int PACKAGEDC = 21;
    /** type : ClasseDC */
    public static final int CLASSEDC = 22;
    /** type : AssociationDC */
    public static final int ASSOCIATIONDC = 23;
    /** type : Parametre */
    public static final int PARAMETRE = 24;
    /** type : Attribut */
    public static final int ATTRIBUT = 25;
    /** type : InterfaceDC */
    public static final int INTERFACEDC = 26;
    /** type : Methode */
    public static final int METHODE = 27;
    
    //les nombres 40 à 59 sont réservés pour le diagramme de cas d'utilisation.
    /** type : DiagrammeUC */
    public static final int DIAGRAMME_UC = 40;
    /** type : PackageUC */
    public static final int PACKAGEUC = 41;
    /** type : AssociationUC */
    public static final int ASSOCIATIONUC = 42;
    /** type : UseCase */
    public static final int USECASE = 43;
    /** type : CollaborationUC */
    public static final int COLLABORATIONUC = 44;
    
    //les nombres 60 à 79 sont réservés pour le diagramme d'activités.
    /** type : DiagrammeActivites */
    public static final int DIAGRAMME_ACT = 60;
    /** type : EventAct */
    public static final int EVENT_ACT = 61;
    
    //les nombres 80 à 99 sont réservés pour le diagramme d'états-transitions.
    /** type : DiagrammeET */
    public static final int DIAGRAMME_ET = 80;
    /** type : MachineET */
    public static final int MACHINE_ET = 81;
    
    //les nombres 100 à 119 sont réservés pour le diagramme de séquence.
    /** type : DiagrammeSequence */
    public static final int DIAGRAMME_SEQ = 100;
    /** type : Message */
    public static final int MESSAGE = 101;
    
    //les nombres 120 à 139 sont réservés pour le diagramme de collaboration.
    /** type : DiagrammeCollaboration */
    public static final int DIAGRAMME_COLL = 120;
    /** type : PackageCollaboration */
    public static final int PACKAGE_COLL = 121;
    
    
    /** Un type de visibilité : PRIVATE.*/
    public static final int PRIVATE = 1;
    /** Un type de visibilité : PROTECTED. */
    public static final int PROTECTED = 2;
    /** Un type de visibilité : PUBLIC. */
    public static final int PUBLIC = 4;
    /** Un type de visibilité : PACKAGE.*/
    public static final int PACKAGE = 8;
    
    
    
    /** L'identifiant de l'objet tel qu'il est lu dans le flux XMI.*/
    protected String    id_xmi      = null;
    /** Le nom de l'objet, s'il en a un */
    protected String    name        = null;
    /** La visibilité de l'attribut. */
    protected int       visibility  = XMIObject.NOT_DEFINED;
    /** Le stéréotype */
    protected String    stereotype  = null;
    /** Le type de l'objet */
    protected int      typeObjet    = XMIObject.NOT_DEFINED;
    /** Le modèle auquel appartient l'objet*/
    protected Modele   modele       = null;
    /** L'élément racine dans l'arbre généré par JDOM et représentant cet objet.*/
    protected Element  elementRoot  = null;
    /** Le type du diagramme auquel appartient l'objet */
    protected int      typeDiagramme = XMIObject.NOT_DEFINED;
    
    protected boolean  children     = false;
    protected ArrayList objets      = null;
    
    /**
     * Ce constructeur est <code>protected</code> car il ne doit pas être possible
     * de créer d'objets sans toutes les informations nécessaires.
     */
    protected XMIObject() {}
    
    /**
     * Ce constructeur permet d'initialiser un objet avec les informations communes
     * à la plupart des objets extraits d'un flux XMI.
     * @param e L'élément racine du sous-arbre définissant l'objet et généré par JDOM.
     * @param type Le type de l'objet.
     * @param m Le modèle auquel appartient l'objet.
     * @param td Le type du diagramme contenant cet objet.
     */
    public XMIObject(Element e, int type, Modele m, int td, boolean hC) {
        if(e!=null) {
            elementRoot = e;
            name = e.getAttributeValue("name");
            if(name!=null)  {
                name = name.replace('.', '_');
                name = name.replace(' ', '_');
            }
            id_xmi = e.getAttributeValue("xmi.id");
            String v = e.getAttributeValue("visibility");
            if(v!=null) {
                if(v.equals("public"))     visibility = XMIObject.PUBLIC;
                else if(v.equals("protected"))  visibility = XMIObject.PROTECTED;
                else if(v.equals("private"))    visibility = XMIObject.PRIVATE;
                else if(v.equals("package"))    visibility = XMIObject.PACKAGE;
            }
            Element elt = FonctionsParser.findFirstBalise(e, "Stereotype");
            if(elt!=null) stereotype = elt.getAttributeValue("name");
            else stereotype="";
        }
        typeObjet = type;
        objets = new ArrayList();
        modele = m;
        typeDiagramme = td;
        children = hC;
    }
    
    /**
     * Cette méthode met à jour l'élément racine de l'objet.
     * Tous les champs en dépendant sont aussi mis à jour.
     * @param e L'élément permettant la nouvelle analyse de l'élément racine.
     */
    public void setRootElement(Element e) {
        if(e==null) return;
        elementRoot = e;
        name = e.getAttributeValue("name");
        if(name!=null)  {
            name = name.replace('.', '_');
            name = name.replace(' ', '_');
        }
        id_xmi = e.getAttributeValue("xmi.id");
        String v = e.getAttributeValue("visibility");
        if(v!=null) {
            if(v.equals("public"))     visibility = XMIObject.PUBLIC;
            else if(v.equals("protected"))  visibility = XMIObject.PROTECTED;
            else if(v.equals("private"))    visibility = XMIObject.PRIVATE;
            else if(v.equals("package"))    visibility = XMIObject.PACKAGE;
        }
        Element elt = FonctionsParser.findFirstBalise(e, "Stereotype");
        if(elt!=null) stereotype = elt.getAttributeValue("name");
        else stereotype = "";
    }
    
    /**
     * Cette méthode retourne le type du diagramme.
     * @return Le type du diagramme.
     */
    public int getTypeDiagramme() {return typeDiagramme;}
    
    /**
     * Cette méthode retourne le nom de l'objet
     * @return Le nom de l'objet.
     */
    public String getName() {return name; }
    /**
     * Cette méthode retourne le stéréotype de l'objet
     * @return Le stéréotype de l'objet.
     */
    public String getStereotype() {return stereotype; }
    /**
     * Cette méthode retourne l'élément racine du sous-arbre généré par JDOM et représentant cet objet.
     * @return L'élément racine de l'arbre de JDOM représentant cet objet.
     */
    public Element getRootElement() {return elementRoot; }
    /**
     * Cette méthode retourne l'id de l'objet.
     * @return L'id de l'objet tel qu'il est lu dans le flux XMI.
     */
    public String getId() {
        return id_xmi;
    }
    /**
     * Cette méthode met à jour l'identifiant de l'objet tel qu'il doit être lu
     * dans le fichier XMI. Cette méthode ne doit être utilisée que si l'objet n'a
     * pas de balises xmi.id.
     * @param i Le nouvel identifiant.
     */
    public void setId(String i) {id_xmi = i;}
    
    /**
     * Cette méthode retourne la visibilité de l'attribut.
     *
     * @return la visibilité
     */
    public int getVisibility() {return visibility;}
    
    /**
     * Cette méthode retourne le type de l'objet.
     * @return le type de l'objet.
     */
    public int getTypeObjet() { return typeObjet; }
    
    /**
     * Cette méthode recherche un objet dans un ArrayList en fonction de son identifiant.
     * @param id_recherche L'identifiant à rechercher dans le ArrayList.
     * @param vect Le ArrayList dans lequel on fait la rechercher.
     * @return l'objet recherché s'il est trouvé, null sinon.
     */
    public XMIObject rechercheDansArrayById(String id_recherche, ArrayList vect) {
        for(Iterator e=vect.iterator(); e.hasNext(); ) {
            XMIObject oet = (XMIObject) e.next();
            if(oet.getId().equals(id_recherche)) return oet;
        }
        return null;
    }
    
    /**
     * Cette méthode recherche un objet dans un ArrayList en fonction de son nom.
     * @param id_recherche Le nom à rechercher dans le ArrayList.
     * @param vect Le ArrayList dans lequel on fait la rechercher.
     * @return l'objet recherché s'il est trouvé, null sinon.
     */
    public XMIObject rechercheDansArrayByName(String id_recherche, ArrayList vect) {
        for(Iterator e=vect.iterator(); e.hasNext(); ) {
            XMIObject oet = (XMIObject) e.next();
            if(oet.getName()==null) continue;
            if(oet.getName().equals(id_recherche)) return oet;
        }
        return null;
    }
    
    /**
     * Cette méthode retourne le modèle auquel appartient l'objet.
     * @return Le modèle auquel appartient l'objet.
     */
    public Modele getModele() {return modele;}
    
    /**
     * Cette méthode recherche un identifiant dans un modèle.
     * @param id_recherche l'identifiant à rechercher dans le modèle.
     * @return l'objet recherché, s'il est trouvé.
     * @throws XMIObjectNotFoundException Exception renvoyée quand l'objet recherché n'est pas trouvé.
     */
    public XMIObject getByInModele(String id_recherche, boolean id) throws XMIObjectNotFoundException {
        XMIObject retour = getModele().getBy(id_recherche, id);
        if(retour==null) throw new XMIObjectNotFoundException();
        return retour;
    }

    public boolean hasChildren() {
        return children;
    }
    
    public XMIObject getBy(String id_recherche, boolean id) throws XMIObjectNotFoundException{
        if(id) {
            if(getId()!=null)
                if(getId().equals(id_recherche)) return (XMIObject)this;
        }
        else {
            if(getName()!=null)
                if(getName().equals(id_recherche)) return (XMIObject)this;
        }
        
        XMIObject retour = null;
        //maintenant, on recherche dans les fils.
        if(hasChildren()) {
            for(Iterator it=objets.iterator(); it.hasNext(); ) {
                XMIObject obj = (XMIObject) it.next();
                try {
                    retour = obj.getBy(id_recherche, id);
                } catch(XMIObjectNotFoundException exc) {
                    continue;
                }
                if(retour!=null) break;
            }
        }
        if(retour==null) throw new XMIObjectNotFoundException();
        return retour;
    }
    
    public ArrayList getAll(int type_objet, boolean recursivite) {
        ArrayList retour = new ArrayList();
        
        for(Iterator it=objets.iterator(); it.hasNext(); ) {
            XMIObject obj = (XMIObject)it.next();
            if(obj.getTypeObjet()==type_objet) retour.add(obj);
        }
        
        if(recursivite) {
            for(Iterator it=objets.iterator(); it.hasNext(); ) {
                XMIObject obj = (XMIObject)it.next();
                retour.addAll(obj.getAll(type_objet, recursivite));
            }
        }
        return retour;
    }
}
