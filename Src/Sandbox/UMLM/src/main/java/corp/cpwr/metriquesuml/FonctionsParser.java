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
 * Ces informations doivent être stockées dans un arbre généré précédemment
 * par JDOM.<br>
 * Il est possible de rajouter différents type de flux. Pour le moment, seuls
 * les fichiers XMI générés par Entreprise Architect 3.60, sans compatibilité
 * Unisys/Rose et en version 1.1 sont gérés.
 *
 * @author  cwfr-dzysman
 */
public class FonctionsParser {
    /**
     * Cette variable de classe permet d'indiquer quel est le type du fichier
     * XMI à analyser.<br>Ici : Entreprise Architect 3.60, sans compatibilité
     * Unisys/Rose et en version 1.1.
     */
    public static final int ENTREPRISE_ARCHITECT=1;
    /** Cette variable permet de connaître le type de flux à analyser. */
    private static int parser = -1;
    private static boolean dejaInitialise = false;
    
    /** Ce constructeur permet la création d'une instance de la classe. */
    private FonctionsParser() {}
    
    public static void setParseur(int i) {
        if(!dejaInitialise) {
            dejaInitialise=true;
            parser=i;
        }
    }
    
    
    /**
     * Cette méthode permet de rechercher la valeur d'une propriété d'un élément.
     * La manière de rechercher une propriété peut changer en fonction du générateur
     * du flux XMI. Ainsi, Entreprise Architect mettra cette propriété comme valeur
     * d'un attribut "value" d'une balise "TaggedValue" dont l'attribut "tag" vaut
     * le nom de la propriété.
     *<p>
     *Ainsi, pour la balise suivante :
     *<br>&lt;UML:TaggedValue tag="date_created" value="2001-03-05 00:00:00" /&gt;
     *<br> La propriété sera "date_created" tandis que sa valeur sera "2001-03-05 00:00:00".
     *
     * @param e     Element racine du sous-arbre dans lequel se fera la recherche.
     * @param prop  La propriété à rechercher.
     * @return  La valeur de la propriété recherchée. <code>null</code> si cette
     * propriété n'existe pas.
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
     * Cette méthode recherche de la même manière que <code>getPropertyString</code> la valeur d'une
     * propriété, mais en extrait un flottant et le retourne.
     *
     * @param e     Element racine du sous-arbre dans lequel se fera la recherche.
     * @param prop  La propriété à rechercher.
     * @return Flottant contenant la valeur de la propriété recherchée. La méthode
     *         retourne <code>-1.0f</code> si la valeur n'a pas été trouvée.
     */
    public static float getPropertyFloat(Element e, String prop) {
        String r = getPropertyString(e,prop);
        if(r.equals("")) return -1.0f;
        return Float.parseFloat(r);
    }
    
    /**
     * Cette méthode recherche de la même manière que <code>getPropertyString</code> la valeur d'une
     * propriété, mais en extrait un entier et le retourne.
     *
     * @param e     Element racine du sous-arbre dans lequel se fera la recherche.
     * @param prop  La propriété à rechercher.
     * @return Entier contenant la valeur de la propriété recherchée. La méthode
     *         retourne <code>-1</code> si la valeur n'a pas été trouvée.
     */
    public static int getPropertyInt(Element e, String prop) {
        String r = getPropertyString(e,prop);
        if(r.equals("")) return -1;
        return Integer.parseInt(r);
    }
    
    /**
     * Cette méthode recherche le premier élément dans un arbre généré par JDOM qui
     * représente la balise <code>balise</code>, ayant un attribut <code>attribute</code>
     * dont la valeur est <code>value</code>.
     *
     * @param root      La racine du sous-arbre généré par JDOM dans lequel se fera la recherche.
     * @param balise    Le nom de la balise à rechercher.
     * @param attribute Le nom de l'attribut que doit avoir la balise recherchée.
     * @param value     La valeur de l'attribut de la balise.
     *
     * @return L'élément contenant la balise recherchée. La méthode renvoie <code>null</code>
     *         si cette balise n'existe pas dans le sous-arbre donné en paramètre.
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
     * Cette méthode recherche tous les éléments dans le sous-arbre généré par JDOM de nom
     * <code>balise</code>, et dont l'attribut <code>attribute</code> a la valeur <code>value</code>.
     *
     * @param root      La racine du sous-arbre généré par JDOM dans lequel se fera la recherche.
     * @param balise    Le nom de la balise à rechercher.
     * @param attribute Le nom de l'attribut que doit avoir la balise recherchée.
     * @param value     La valeur de l'attribut de la balise.
     *
     * @return Un objet de type ArrayList contenant toutes les balises recherchées. La méthode renvoie un Vecotr de taille 0
     *         si cette balise n'existe pas dans le sous-arbre donné en paramètre.
     */
    public static ArrayList findAllElements(Element root, String balise, String attribute, String value) {
        ArrayList allElements = new ArrayList();
        findAllElementsRecursivite(root, balise, attribute, value, allElements);
        return allElements;
    }
    
    /**
     * Cette méthode est uniquement utilisée pour la récursivité de la méthode <code>findAllElements</code>.
     * @param e l'élément dans lequel rechercher les informations.
     * @param balise la balise à rechercher.
     * @param attribute l'attribut à rechercher dans la balise.
     * @param value la valeur que doit avoir l'attribut recherché dans la balise.
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
     * Cette méthode recherche tous les éléments dans le sous-arbre généré par JDOM de nom
     * <code>balise</code>.
     *
     * @param root      La racine du sous-arbre généré par JDOM dans lequel se fera la recherche.
     * @param balise    Le nom de la balise à rechercher.
     *
     * @return Un objet de type ArrayList contenant toutes les balises recherchées. La méthode renvoie ArrayList de taille 0
     *         si cette balise n'existe pas dans le sous-arbre donné en paramètre. Le ArrayList contient des objets de
     *         type Element.
     */
    public static ArrayList findAllBalises(Element root, String balise) {
        ArrayList allElements = new ArrayList();
        findAllBalisesRecursivite(root, balise, allElements);
        return allElements;
    }
    
    /**
     * Cette méthode est uniquement utilisée pour la récursivité de la méthode <code>findAllBalises</code>.
     * @param e l'élément dans lequel on doit rechercher la balise.
     * @param balise la balise à rechercher.
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
     * Cette méthode est utilisée pour trouver la première balise du type donné en paramètre.
     * @param e l'élément dans lequel on doit rechercher la balise.
     * @param balise la balise à rechercher.
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
