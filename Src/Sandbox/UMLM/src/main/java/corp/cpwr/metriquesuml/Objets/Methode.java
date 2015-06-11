/*
 * Methode.java
 *
 * Created on 11 mai 2004, 09:49
 */

package corp.cpwr.metriquesuml.Objets;


import org.jdom.*;
import corp.cpwr.metriquesuml.*;
import java.util.*;


/**
 * Cette classe permet la gestion et l'analyse des méthodes définie dans les classes
 * d'un diagramme de classes.
 * @author  cwfr-dzysman
 */
public class Methode extends XMIObject{
    /** Le type de retour de la méthode. */
    private String typeRetour = null;
    /** La classe contenant la méthode. */
    private DC classeMere = null;

    //LES METHODES N'ONT PAS DE NUMERO DE VERSION DANS ENTREPRISE ARCHITECT
    /** Les méthodes ont une compléxité par défaut de 1 */
    private int    complexiteCyclomatique = 1;
    private boolean hasActivityDiagram=false;
    
    /**
     * Ce constructeur est private afin d'empêcher la création d'objets de type
     * Methode sans les informations requises à son analyse.
     */
    private Methode() {
    }
    
    /**
     * Ce constructeur permet la création d'instances de Methode. En plus d'initialiser
     * toutes les variables de la classe, le constructeur recherche tous les
     * paramètres de la méthode.
     *
     * @param e  L'élément racine du sous-arbre généré par JDOM et définissant
     *           la méthode.
     * @param cM la classe définissant la méthode.
     * @param m le modèle auquel appartient l'objet.
     */
    public Methode(Element e, DC cM, Modele m){
        super(e, XMIObject.METHODE, m, XMIObject.DIAGRAMME_CLASSE, true);
        classeMere = cM;
        
        typeRetour = FonctionsParser.getPropertyString(e, "type");
        if(typeRetour==null) typeRetour="void";
        String temp = FonctionsParser.getPropertyString(e, "ea_guid");
        setId(temp);
        
        ArrayList vect = FonctionsParser.findAllBalises(e, "Parameter");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            Element elt = (Element) it.next();
        /*Nous sommes obligés de faire le test suivant pour les paramètres :
         une balise Parameter doit contenir un attribut "name". En effet,
         Entreprise Architect exporte aussi pour "stocker" des informations qui
         lui sont spécifique un UML:Parameter sans cet attribut "name"*/
            if(elt.getAttributeValue("name")!=null)
                objets.add(new Attribut(elt, this, getModele(), XMIObject.PARAMETRE));
        }
        
    }
    
    /**
     * Cette méthode renvoie le type de retour de la méthode définie dans l'objet
     * de type <code>Methode</code>.
     *
     * @return le type de retour.
     */
    public String getTypeRetour() {return typeRetour;}
    
    /**
     * Cette méthode retourne la liste des paramètres de l'objet tels qu'ils sont
     * définis dans le flux XMI pour la méthode.
     *
     * @return La liste des paramètres.
     */
    public ArrayList getParametres() {
        return getAll(XMIObject.PARAMETRE, false);
    }
    
    /*
     * Cette méthode renvoie la compléxité cyclomatique de la méthode.
     * @return compléxité cyclomatique de la méthode.
     */
    public int getComplexiteCyclomatique() {return complexiteCyclomatique;}
    
    /**
     * Cette méthode met à jour la compléxité cyclomatique de la méthode.
     * @param c la nouvelle compléxité cyclomatique de la méthode.
     */
    public void setComplexiteCyclomatique(int c) {complexiteCyclomatique = c;}
    
    /**
     * Cette méthode indique l'égalité de signature entre deux méthodes.
     * @return un booléen représentant l'égalité de signature entre les méthodes.
     */
    public boolean equals(Methode m) {
        if(!getName().equals(m.getName())) return false;
        if(!typeRetour.equals(m.getTypeRetour())) return false;
        
        ArrayList parametres = getParametres();
        ArrayList save = (ArrayList)m.getParametres();
        if(parametres.size()!=save.size()) return false;
        int taille = parametres.size();
        boolean retour = true;
        for(int i=0; i<taille && retour; i++) {
            String type = ((Attribut)parametres.get(i)).getType();
            String type2 = ((Attribut)save.get(i)).getType();
            if(!type.equals(type2)) retour=false;
        }
        return retour;
    }
    
    public void setHasActivityDiagram(boolean s) {
        hasActivityDiagram=s;
    }
    
    public boolean isDocumentee() {return hasActivityDiagram;}
    
}
