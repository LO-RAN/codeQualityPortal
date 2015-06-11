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
 * Cette classe permet la gestion et l'analyse des m�thodes d�finie dans les classes
 * d'un diagramme de classes.
 * @author  cwfr-dzysman
 */
public class Methode extends XMIObject{
    /** Le type de retour de la m�thode. */
    private String typeRetour = null;
    /** La classe contenant la m�thode. */
    private DC classeMere = null;

    //LES METHODES N'ONT PAS DE NUMERO DE VERSION DANS ENTREPRISE ARCHITECT
    /** Les m�thodes ont une compl�xit� par d�faut de 1 */
    private int    complexiteCyclomatique = 1;
    private boolean hasActivityDiagram=false;
    
    /**
     * Ce constructeur est private afin d'emp�cher la cr�ation d'objets de type
     * Methode sans les informations requises � son analyse.
     */
    private Methode() {
    }
    
    /**
     * Ce constructeur permet la cr�ation d'instances de Methode. En plus d'initialiser
     * toutes les variables de la classe, le constructeur recherche tous les
     * param�tres de la m�thode.
     *
     * @param e  L'�l�ment racine du sous-arbre g�n�r� par JDOM et d�finissant
     *           la m�thode.
     * @param cM la classe d�finissant la m�thode.
     * @param m le mod�le auquel appartient l'objet.
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
        /*Nous sommes oblig�s de faire le test suivant pour les param�tres :
         une balise Parameter doit contenir un attribut "name". En effet,
         Entreprise Architect exporte aussi pour "stocker" des informations qui
         lui sont sp�cifique un UML:Parameter sans cet attribut "name"*/
            if(elt.getAttributeValue("name")!=null)
                objets.add(new Attribut(elt, this, getModele(), XMIObject.PARAMETRE));
        }
        
    }
    
    /**
     * Cette m�thode renvoie le type de retour de la m�thode d�finie dans l'objet
     * de type <code>Methode</code>.
     *
     * @return le type de retour.
     */
    public String getTypeRetour() {return typeRetour;}
    
    /**
     * Cette m�thode retourne la liste des param�tres de l'objet tels qu'ils sont
     * d�finis dans le flux XMI pour la m�thode.
     *
     * @return La liste des param�tres.
     */
    public ArrayList getParametres() {
        return getAll(XMIObject.PARAMETRE, false);
    }
    
    /*
     * Cette m�thode renvoie la compl�xit� cyclomatique de la m�thode.
     * @return compl�xit� cyclomatique de la m�thode.
     */
    public int getComplexiteCyclomatique() {return complexiteCyclomatique;}
    
    /**
     * Cette m�thode met � jour la compl�xit� cyclomatique de la m�thode.
     * @param c la nouvelle compl�xit� cyclomatique de la m�thode.
     */
    public void setComplexiteCyclomatique(int c) {complexiteCyclomatique = c;}
    
    /**
     * Cette m�thode indique l'�galit� de signature entre deux m�thodes.
     * @return un bool�en repr�sentant l'�galit� de signature entre les m�thodes.
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
