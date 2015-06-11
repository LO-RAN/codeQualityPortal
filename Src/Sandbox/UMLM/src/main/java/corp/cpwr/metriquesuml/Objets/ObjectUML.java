/*
 * ObjectET.java
 *
 * Created on 1 juin 2004, 15:55
 */

package corp.cpwr.metriquesuml.Objets;

import corp.cpwr.metriquesuml.*;
import corp.cpwr.metriquesuml.Objets.DC;
import org.jdom.*;
import corp.cpwr.metriquesuml.MetriquesUMLExceptions.*;

/**
 *
 * id INTEGER, id_xmi VARCHAR(100), nom VARCHAR(30), stereotype VARCHAR(30), id_diagramme INTEGER, diagramme_Type INTEGER, version VARCHAR(10)
 * @author  cwfr-dzysman
 */
public class ObjectUML extends XMIObject{
    /** booléen qui indique si l'objet est une instance de classe ou non*/
    private boolean instance = false;
    /** objet qui référence l'objet dont celui-ci est une instance dans les diagrammes */
    private DC objetInstance = null;
    /** identifiant de l'objet qui référence l'objet dont celui-ci est une instance dans les diagrammes */
    private String    objetInstanceName = null;
    
    /* LES METRIQUES */
    private int       nbMessagesRecus = -1;//spécifique diagramme de séquences.
    private int       nbMessagesEnvoyes = -1;//spécifique diagramme de séquences.
    /**
     * Ce constructeur est private afin d'empecher la création d'ObjectET sans les
     * informations nécessaires
     */
    private ObjectUML() {}
    
    /**
     * Ce constructeur permet de créer des instances d'ObjectUML.
     * @param e L'élément racine du sous-arbre généré par JDOM et définissant
     *          l'objet.
     * @param dt le type du diagramme qui contient cet ObjectUML.
     * @param m le modèle auquel appartient l'objet.
     */
    public ObjectUML(Element e, int dt, Modele m) {
        super(e, XMIObject.OBJECT_UML, m, dt, false);
        if(getName()==null) {
            //alors c'est une instance de classe.
            setInstanceNameOf(FonctionsParser.getPropertyString(e, "classname"));
        }
    }
    
    /**
     * Cette méthode indique si l'objet représente une instance d'un autre objet, ou non.
     * @return un booléen donnant cette information.
     */
    public boolean isInstance() {return instance;}
    /**
     * Cette méthode permet d'indiquer que l'objet représente dans son diagramme
     * une instance d'un autre objet.
     * @param obj l'objet dont c'est l'instance.
     */
    public void setInstanceOf(DC obj) {
        instance=true;
        objetInstance = obj;
    }
    /**
     * Cette méthode permet d'indiquer que l'objet représente dans son diagramme
     * une instance d'un autre objet. Cette méthode indique l'identifiant de cet objet.
     * @param obj l'identifiant de l'objet dont c'est l'instance.
     */
    public void setInstanceNameOf(String obj) {
        objetInstanceName = obj;
    }
    /**
     * Cette méthode retourne l'identifiant de l'objet dont cet objet est l'instance.
     * @param l'identifiant de l'objet dont c'est l'instance.
     */
    public String getInstanceNameOf() {
        return objetInstanceName;
    }
    /**
     * Cette méthode retourne l'objet dont cet objet est l'instance.
     * @param l'objet dont c'est l'instance.
     */
    public DC getInstanceOf() {
        return objetInstance;
    }
    
    /**
     * Cette méthode permet de mettre à jour le nombre de messages liés à cet objet.
     */
    public void setMessagesLies(int env, int rec) {
        if(this.getTypeDiagramme()!=XMIObject.DIAGRAMME_SEQ) {
            return;
        }
        nbMessagesEnvoyes = env;
        nbMessagesRecus = rec;
    }
    
    public void fillIt() {
        if(getName()==null) {
            //alors c'est une instance de classe.
            String classname = getInstanceNameOf();
            try{
                XMIObject classInstance = getByInModele(classname,false);
                setInstanceOf((DC)classInstance);
            }
            catch(XMIObjectNotFoundException exc) {
            }
        }
    }
}
