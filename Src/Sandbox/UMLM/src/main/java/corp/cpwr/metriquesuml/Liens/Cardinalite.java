package corp.cpwr.metriquesuml.Liens;

/*
 * Cardinalite.java
 *
 * Created on 11 mai 2004, 15:38
 */

/**
 * Cette classe permet la repr�sentation des cardinalit�s d'une AssociationEnd.
 * @see AssociationEndDC
 * @author  cwfr-dzysman
 */
public class Cardinalite {
    /**
     * La limite de la cardinalit� n'est pas d�finie.
     */
    public static final int NOT_DEFINED = -2;
    /**
     * La limite de la cardinalit� est l'�toile (*).
     */
    public static final int ETOILE = -1;
    /**
     * La limite haute de la cardinalit�.
     */
    private int limiteHaute = Cardinalite.NOT_DEFINED;
    /**
     * La limite basse de la cardinalit�.
     */
    private int limiteBasse = Cardinalite.NOT_DEFINED;
    
    /**
     * Ce constructeur de classe est <code>private</code> afin d'emp�cher la cr�ation
     * d'instance de Cardinalite sans les informations n�cessaires.
     */
    private Cardinalite() {  }
    
    /**
     * Ce constructeur de classe permet la cr�ation d'une instance de Cardinalite.
     * Il prend en param�tre une String qui contient la d�finition de la cardinalit�.
     * La String peut �tre de la forme :
     *  <ul>
     *  <li>0
     *  <li> 1
     *  <li> 0..*
     *  <li> 0........* (autant de . que n�cessaire)
     *  <li> etc
     *  </ul>
     * @param c la String d�finissant la cardinalit�.
     */
    public Cardinalite(String c) {
        if(c.indexOf('.')==-1)
            if(c.equals("*"))
                limiteBasse = ETOILE;
            else
                limiteBasse = Integer.parseInt(c);
        else {
            String temp = c.substring(0,c.indexOf('.'));
            if(temp.equals("*"))
                limiteBasse = ETOILE;
            else
                limiteBasse = Integer.parseInt(temp);

            temp = c.substring(c.lastIndexOf('.')+1);
            if(temp.equals("*"))
                limiteHaute = ETOILE;
            else
                limiteHaute = Integer.parseInt(temp);
            temp = null;
        }
    }
    
    /**
     * Cette m�thode retourne la limite haute de la cardinalit�.
     * 
     * @return limite haute de la cardinalit�.
     */
    public int getLimiteHaute() { return limiteHaute; }
    
    /**
     * Cette m�thode retourne la limite basse de la cardinalit�.
     * 
     * @return limite basse de la cardinalit�.
     */
    public int getLimiteBasse() { return limiteBasse; }
    
    /**
     * Cette m�thode converti en String la cardinalit� afin qu'elle soit affichable.
     *
     * @return une String qui contient la cardinalit�. La String est de la forme
     *         "1", "0", "0..*", "0..1" etc.
     */
    public String toString() {
        String s = new String("");
        if(limiteBasse==ETOILE)
            s+="*";
        else
            s+=limiteBasse;
        if(limiteHaute!=NOT_DEFINED) {
            s+="..";
            if(limiteHaute==ETOILE)
                s+="*";
            else
                s+=limiteHaute;
            
        }
        return s;
    }
}
