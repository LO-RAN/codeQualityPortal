/*
 * FonctionParser.java
 *
 * Created on 18 mai 2004, 10:04
 */

package corp.cpwr.metriquesuml;
import org.jdom.*;
import java.util.*;
/**
 * Cette classe permet d'extraire des informations au niveau d'un flux XMI.
 * Ces informations doivent �tre stock�es dans un arbre g�n�r� pr�c�demment
 * par JDOM.<br>
 * Il est possible de rajouter diff�rents type de flux. Pour le moment, seuls
 * les fichiers XMI g�n�r�s par Entreprise Architect 3.60, sans compatibilit�
 * Unisys/Rose et en version 1.1 sont g�r�s.
 *
 * @author  cwfr-dzysman
 */
public class FonctionsParser {
    /**
     * Cette variable de classe permet d'indiquer quel est le type du fichier
     * XMI � analyser.<br>Ici : Entreprise Architect 3.60, sans compatibilit�
     * Unisys/Rose et en version 1.1.
     */
    public static final int ENTREPRISE_ARCHITECT=1;
    /** Cette variable permet de conna�tre le type de flux � analyser. */
    private static int parser = -1;
    private static boolean dejaInitialise = false;
    
    /** Ce constructeur permet la cr�ation d'une instance de la classe. */
    private FonctionsParser() {}
    
    public static void setParseur(int i) {
        if(!dejaInitialise) {
            dejaInitialise=true;
            parser=i;
        }
    }
    
    
    /**
     * Cette m�thode permet de rechercher la valeur d'une propri�t� d'un �l�ment.
     * La mani�re de rechercher une propri�t� peut changer en fonction du g�n�rateur
     * du flux XMI. Ainsi, Entreprise Architect mettra cette propri�t� comme valeur
     * d'un attribut "value" d'une balise "TaggedValue" dont l'attribut "tag" vaut
     * le nom de la propri�t�.
     *<p>
     *Ainsi, pour la balise suivante :
     *<br>&lt;UML:TaggedValue tag="date_created" value="2001-03-05 00:00:00" /&gt;
     *<br> La propri�t� sera "date_created" tandis que sa valeur sera "2001-03-05 00:00:00".
     *
     * @param e     Element racine du sous-arbre dans lequel se fera la recherche.
     * @param prop  La propri�t� � rechercher.
     * @return  La valeur de la propri�t� recherch�e. <code>null</code> si cette
     * propri�t� n'existe pas.
     */
    public static String getPropertyString(Element e, String prop) {
        Element elt    = null;
        String  retour = "";
        switch(parser) {
            case ENTREPRISE_ARCHITECT:
                elt = findElement(e, "TaggedValue", "tag", prop);
                break;
        }
        if(elt!=null)
            retour = elt.getAttributeValue("value");
        return retour;
    }
    
    /**
     * Cette m�thode recherche de la m�me mani�re que <code>getPropertyString</code> la valeur d'une
     * propri�t�, mais en extrait un flottant et le retourne.
     *
     * @param e     Element racine du sous-arbre dans lequel se fera la recherche.
     * @param prop  La propri�t� � rechercher.
     * @return Flottant contenant la valeur de la propri�t� recherch�e. La m�thode
     *         retourne <code>-1.0f</code> si la valeur n'a pas �t� trouv�e.
     */
    public static float getPropertyFloat(Element e, String prop) {
        String r = getPropertyString(e,prop);
        if(r.equals("")) return -1.0f;
        return Float.parseFloat(r);
    }
    
    /**
     * Cette m�thode recherche de la m�me mani�re que <code>getPropertyString</code> la valeur d'une
     * propri�t�, mais en extrait un entier et le retourne.
     *
     * @param e     Element racine du sous-arbre dans lequel se fera la recherche.
     * @param prop  La propri�t� � rechercher.
     * @return Entier contenant la valeur de la propri�t� recherch�e. La m�thode
     *         retourne <code>-1</code> si la valeur n'a pas �t� trouv�e.
     */
    public static int getPropertyInt(Element e, String prop) {
        String r = getPropertyString(e,prop);
        if(r.equals("")) return -1;
        return Integer.parseInt(r);
    }
    
    /**
     * Cette m�thode recherche le premier �l�ment dans un arbre g�n�r� par JDOM qui
     * repr�sente la balise <code>balise</code>, ayant un attribut <code>attribute</code>
     * dont la valeur est <code>value</code>.
     *
     * @param root      La racine du sous-arbre g�n�r� par JDOM dans lequel se fera la recherche.
     * @param balise    Le nom de la balise � rechercher.
     * @param attribute Le nom de l'attribut que doit avoir la balise recherch�e.
     * @param value     La valeur de l'attribut de la balise.
     *
     * @return L'�l�ment contenant la balise recherch�e. La m�thode renvoie <code>null</code>
     *         si cette balise n'existe pas dans le sous-arbre donn� en param�tre.
     */
    public static Element findElement(Element root, String balise, String attribute, String value) {
        if(root.getName().equals(balise)) {
            if(root.getAttributeValue(attribute).equals(value))
                return root;
        }
        
        Iterator   it = root.getContent().iterator();
        while(it.hasNext()) {
            Object o = it.next();
            if(o instanceof Element ) {
                Element retour = findElement((Element)o, balise, attribute, value);
                if(retour!=null) {
                    return retour;
                }
            }
        }
        return null;
    }
    
    /**
     * Cette m�thode recherche tous les �l�ments dans le sous-arbre g�n�r� par JDOM de nom
     * <code>balise</code>, et dont l'attribut <code>attribute</code> a la valeur <code>value</code>.
     *
     * @param root      La racine du sous-arbre g�n�r� par JDOM dans lequel se fera la recherche.
     * @param balise    Le nom de la balise � rechercher.
     * @param attribute Le nom de l'attribut que doit avoir la balise recherch�e.
     * @param value     La valeur de l'attribut de la balise.
     *
     * @return Un objet de type ArrayList contenant toutes les balises recherch�es. La m�thode renvoie un Vecotr de taille 0
     *         si cette balise n'existe pas dans le sous-arbre donn� en param�tre.
     */
    public static ArrayList findAllElements(Element root, String balise, String attribute, String value) {
        ArrayList allElements = new ArrayList();
        findAllElementsRecursivite(root, balise, attribute, value, allElements);
        return allElements;
    }
    
    /**
     * Cette m�thode est uniquement utilis�e pour la r�cursivit� de la m�thode <code>findAllElements</code>.
     * @param e l'�l�ment dans lequel rechercher les informations.
     * @param balise la balise � rechercher.
     * @param attribute l'attribut � rechercher dans la balise.
     * @param value la valeur que doit avoir l'attribut recherch� dans la balise.
     */
    private static void findAllElementsRecursivite(Element e, String balise, String attribute, String value, ArrayList allElements) {
        if(e.getName().equals(balise)) {
            String attributeValue = e.getAttributeValue(attribute);
            if(attributeValue!=null)
                if(attributeValue.equals(value))
                    allElements.add(e);
        }
        
        Iterator   it = e.getContent().iterator();
        while(it.hasNext()) {
            Object o = it.next();
            if(o instanceof Element) {
                findAllElementsRecursivite((Element)o,balise,attribute,value, allElements);
            }
        }
        
    }
    
    /**
     * Cette m�thode recherche tous les �l�ments dans le sous-arbre g�n�r� par JDOM de nom
     * <code>balise</code>.
     *
     * @param root      La racine du sous-arbre g�n�r� par JDOM dans lequel se fera la recherche.
     * @param balise    Le nom de la balise � rechercher.
     *
     * @return Un objet de type ArrayList contenant toutes les balises recherch�es. La m�thode renvoie ArrayList de taille 0
     *         si cette balise n'existe pas dans le sous-arbre donn� en param�tre. Le ArrayList contient des objets de
     *         type Element.
     */
    public static ArrayList findAllBalises(Element root, String balise) {
        ArrayList allElements = new ArrayList();
        findAllBalisesRecursivite(root, balise, allElements);
        return allElements;
    }
    
    /**
     * Cette m�thode est uniquement utilis�e pour la r�cursivit� de la m�thode <code>findAllBalises</code>.
     * @param e l'�l�ment dans lequel on doit rechercher la balise.
     * @param balise la balise � rechercher.
     */
    private static void findAllBalisesRecursivite(Element e, String balise, ArrayList allElements) {
        String name = e.getName();
        if(name!=null)
            if(name.equals(balise))
                allElements.add(e);
        
        Iterator   it = e.getContent().iterator();
        while(it.hasNext()) {
            Object o = it.next();
            if(o instanceof Element) {
                findAllBalisesRecursivite((Element)o,balise, allElements);
            }
        }
        
    }
    
    /**
     * Cette m�thode est utilis�e pour trouver la premi�re balise du type donn� en param�tre.
     * @param e l'�l�ment dans lequel on doit rechercher la balise.
     * @param balise la balise � rechercher.
     */
    public static Element findFirstBalise(Element e, String balise) {
        String name = e.getName();
        if(name!=null)
            if(name.equals(balise))
                return e;
        
        Iterator   it = e.getContent().iterator();
        while(it.hasNext()) {
            Object o = it.next();
            if(o instanceof Element) {
                Element retour = findFirstBalise((Element)o,balise);
                if(retour!=null) return retour;
            }
        }
        return null;
    }
}
