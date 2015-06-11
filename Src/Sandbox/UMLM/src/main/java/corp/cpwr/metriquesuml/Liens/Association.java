/*
 * Association.java
 *
 * Created on 11 mai 2004, 15:37
 */

package corp.cpwr.metriquesuml.Liens;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;
import java.util.*;

/**
 * Cette classe permet la repr�sentation d'une association (de type association,
 * agr�gation ou composition) telle qu'elle est d�finie dans un diagramme de classes.
 * @author  cwfr-dzysman
 */
public class Association extends XMIObject{
    /** L'Association est une association. */
    public static final int ASSOCIATION = 1;
    /** L'Association est une agr�gation. */
    public static final int AGGREGATION = 2;
    /** L'Association est une composition. */
    public static final int COMPOSITION = 4;
    /** L'association est unidirectionnelle */
    public static final int UNIDIR      = 5;
    /** Source -> destination */
    public static final int SRC_DEST    = 6;
    /** Destination -> Source */
    public static final int DEST_SRC    = 7;
    /** Bidirectionnelle */
    public static final int BIDIR       = 8;
    
    /** Le package comprenant cette association. */
    protected XMIObject packagePere         = null;
    /** Le type de l'association. */
    protected int       type                = XMIObject.NOT_DEFINED;
    /** Le sens de l'association */
    protected int       sens                = XMIObject.NOT_DEFINED;
    
    /**
     * Ce constructeur de classe est <code>private</code> afin de ne pas �tre utilis�. Il ne
     * doit pas �tre possible de cr�er une association sans certaines informations
     * fondamentales.
     */
    protected Association() {
    }
    
    /**
     * Ce constructeur de classe initialise les principales variables gr�ce aux param�tres
     * et lance automatiquement l'analyse de l'association. A la fin de l'ex�cution
     * de ce constructeur, toutes les informations sur l'association sont connues.
     *<p>
     * Il recherche une balise "TaggedValue" dont l'attribut <i>tag</i> vaut
     * <i>ea_type</i> afin de conna�tre le type de l'association.
     * <p>
     * Si la balise "TaggedValue" a un attribut <i>tag</i> valant <i>associationclass</i>,
     * alors cette association est une AssociationClass et l'<i>id</i> de la classe
     * est la valeur de l'attribut <i>value</i>.
     *<p>
     *
     * @param e un noeud de l'arbre r�sultant du parsing du fichier xmi et �tant
     *          la base de la d�finition de l'association.
     * @param p le package dans lequel l'association est d�finie.
     * @see FonctionsParser
     * @see PackageDC
     */
    public Association(Element e, XMIObject p, Modele m, int typeObj, int typeDiag) {
        super(e, typeObj, m, typeDiag, true);
        packagePere = p;
        analyseAssociation(e);
    }
    
    public Association(Element e, XMIObject p, Modele m, int typeDiag) {
        super(e, XMIObject.ASSOCIATION, m, typeDiag, true);
        packagePere = p;
        analyseAssociation(e);
    }

    public void analyseAssociation(Element e) {
        String temp = FonctionsParser.getPropertyString(e, "ea_type");
        if(temp.equals("Aggregation")) type = Association.AGGREGATION;
        else if(temp.equals("Association")) type = Association.ASSOCIATION;
        
        temp = FonctionsParser.getPropertyString(e, "direction");
        if(temp.equals("Unspecified")) sens=Association.UNIDIR;
        else if(temp.equals("Bi-Directional")) sens=Association.BIDIR;
        else if(temp.equals("Source -> Destination")) sens=Association.SRC_DEST;
        else if(temp.equals("Destination -> Source")) sens=Association.DEST_SRC;
        
        String nameEnd = null;
        if(e.getName().equals("Association")) nameEnd = "AssociationEnd";
        else if(e.getName().equals("AssociationRole")) nameEnd = "AssociationEndRole";
        ArrayList assocEndElt = FonctionsParser.findAllBalises(e, nameEnd);
        for(Iterator it=assocEndElt.iterator(); it.hasNext(); ) {
            objets.add(new AssociationEnd((Element)it.next(), getModele(), getTypeDiagramme()));
        }
        
    }
    
    /**
     * Cette m�thode retourne la liste des AssociationEnds de l'association.
     *
     * @return la liste des AssociationEnds
     */
    public ArrayList getAssociationEnds(){
        return getAll(XMIObject.ASSOCIATIONEND, false);
    }
    
    /**
     * Cette m�thode retourne l'autre extr�mit� de l'association, qui n'a toujours que deux
     * associationEnds.
     * @param extr L'extremite "r�f�rence" pour d�terminer quelle est "l'autre" extr�mit�.
     * @return l'autre extr�mit� de l'association, null s'il n'y a pas d'autres extr�mit�s.
     */
    public XMIObject getAutreExtremite(XMIObject extr) {
        for(Iterator e=getAssociationEnds().iterator(); e.hasNext(); ) {
            XMIObject obj = ((AssociationEnd)e.next()).getExtremite();
            if(obj!=extr)  return obj;
        }
        //S'il semble que l'on a rien trouv�, c'est que l'association fait une "boucle", c'est � dire
        //que la classe source de l'association est la m�me que la classe "destination".
        return extr;
    }
    
    /**
     * Cette m�thode retourne l'extr�mit� de l'association, qui contient l'objet donn� en param�tre.
     * Si l'association fait une boucle, alors cette m�thode renverra toujours la m�me extr�mit�.
     * Dans ce cas, il serait plus judicieux de parcourir "� la main" la liste des associationEnds.
     * @param extr L'extremit� que l'on recherche dans l'association.
     * @return l'extr�mit� de l'association, null s'il n'y a pas cette extr�mit�.
     */
    public AssociationEnd getExtremite(XMIObject extr) {
        for(Iterator e=getAssociationEnds().iterator(); e.hasNext(); ) {
            AssociationEnd ae = (AssociationEnd)e.next();
            XMIObject obj = ae.getExtremite();
            if(obj==extr) return ae;
        }
        return null;
    }
    
    /**
     * Cette m�thode indique si l'objet donn� en param�tre est une extr�mit� de l'association.
     * @param extr L'objet repr�sentant l'extremit� que l'on recherche dans l'association.
     * @return Un bool�en indiquant si l'objet donn� en param�tre est une extr�mit� de l'association (true)
     *         ou non (false).
     */
    public boolean isExtremite(XMIObject extr) {
        for(Iterator e=getAssociationEnds().iterator(); e.hasNext(); ) {
            XMIObject obj = ((AssociationEnd)e.next()).getExtremite();
            if(obj==extr) return true;
        }
        return false;
    }
    
    public int getTypeAssociation() {return type;}
    
}
