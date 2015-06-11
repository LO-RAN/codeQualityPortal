/*
 * Diagramme.java
 *
 * Created on 18 mai 2004, 17:44
 */

package corp.cpwr.metriquesuml.Diagrammes;
import org.jdom.*;
import java.util.*;
import corp.cpwr.metriquesuml.*;

/**
 * Cette classe regroupe les caractéristiques communes à l'ensemble des diagrammes
 * d'un modèle UML. Logiquement, toutes les classes implémentant un type de diagramme
 * particulier (de classes, de cas d'utilisation, etc) héritent de cette classe.
 * @author  cwfr-dzysman
 */
public abstract class Diagramme extends XMIObject{
    /** LES METRIQUES UML */
    protected float  complexite = -1.0f;
    protected ArrayList  blackList = null;
    protected ArrayList  whiteList = null;
    /**
     * Ce constructeur est <code>protected</code> car il ne doit pas être possible
     * de créer de diagramme sans toutes les informations nécessaires.
     */
    protected Diagramme() {}
    
    /**
     * Ce constructeur permet d'instancier un Diagramme.
     *
     * @param e La racine du sous-arbre qui défini le diagramme.
     * @param tableDiag Le nom de la table dans la base de données concernant ce diagramme.
     * @param type le type du diagramme.
     * @param m le modèle auquel appartient l'objet.
     */
    public Diagramme(Element e, int type, Modele m) {
        super(e, type, m, XMIObject.NOT_DEFINED, true);
        blackList = new ArrayList();
        whiteList = new ArrayList();
    }
    
    public abstract void fillAll();
    public abstract void analyseDiagramme();
    public abstract void calculerMetriques();
    public abstract void writeCSV(String fic, String pere);

    public void addInBlackList(Element e) {
        if(!isInWhiteList(e))  blackList.add(e);
    }
    
    public void addInWhiteList(Element e) {
        whiteList.add(e);
        if(isInBlackList(e)) blackList.remove(e);
    }
    
    public boolean isInBlackList(Element e) {
        return blackList.contains(e);
    }

    public boolean isInWhiteList(Element e) {
        return whiteList.contains(e);
    }
    
    public float getComplexite() {return complexite;}

}
