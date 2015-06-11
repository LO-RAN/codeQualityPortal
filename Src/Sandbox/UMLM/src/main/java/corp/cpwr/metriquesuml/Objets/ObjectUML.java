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
    /** bool�en qui indique si l'objet est une instance de classe ou non*/
    private boolean instance = false;
    /** objet qui r�f�rence l'objet dont celui-ci est une instance dans les diagrammes */
    private DC objetInstance = null;
    /** identifiant de l'objet qui r�f�rence l'objet dont celui-ci est une instance dans les diagrammes */
    private String    objetInstanceName = null;
    
    /* LES METRIQUES */
    private int       nbMessagesRecus = -1;//sp�cifique diagramme de s�quences.
    private int       nbMessagesEnvoyes = -1;//sp�cifique diagramme de s�quences.
    /**
     * Ce constructeur est private afin d'empecher la cr�ation d'ObjectET sans les
     * informations n�cessaires
     */
    private ObjectUML() {}
    
    /**
     * Ce constructeur permet de cr�er des instances d'ObjectUML.
     * @param e L'�l�ment racine du sous-arbre g�n�r� par JDOM et d�finissant
     *          l'objet.
     * @param dt le type du diagramme qui contient cet ObjectUML.
     * @param m le mod�le auquel appartient l'objet.
     */
    public ObjectUML(Element e, int dt, Modele m) {
        super(e, XMIObject.OBJECT_UML, m, dt, false);
        if(getName()==null) {
            //alors c'est une instance de classe.
            setInstanceNameOf(FonctionsParser.getPropertyString(e, "classname"));
        }
    }
    
    /**
     * Cette m�thode indique si l'objet repr�sente une instance d'un autre objet, ou non.
     * @return un bool�en donnant cette information.
     */
    public boolean isInstance() {return instance;}
    /**
     * Cette m�thode permet d'indiquer que l'objet repr�sente dans son diagramme
     * une instance d'un autre objet.
     * @param obj l'objet dont c'est l'instance.
     */
    public void setInstanceOf(DC obj) {
        instance=true;
        objetInstance = obj;
    }
    /**
     * Cette m�thode permet d'indiquer que l'objet repr�sente dans son diagramme
     * une instance d'un autre objet. Cette m�thode indique l'identifiant de cet objet.
     * @param obj l'identifiant de l'objet dont c'est l'instance.
     */
    public void setInstanceNameOf(String obj) {
        objetInstanceName = obj;
    }
    /**
     * Cette m�thode retourne l'identifiant de l'objet dont cet objet est l'instance.
     * @param l'identifiant de l'objet dont c'est l'instance.
     */
    public String getInstanceNameOf() {
        return objetInstanceName;
    }
    /**
     * Cette m�thode retourne l'objet dont cet objet est l'instance.
     * @param l'objet dont c'est l'instance.
     */
    public DC getInstanceOf() {
        return objetInstance;
    }
    
    /**
     * Cette m�thode permet de mettre � jour le nombre de messages li�s � cet objet.
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
