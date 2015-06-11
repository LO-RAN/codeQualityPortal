package corp.cpwr.metriquesuml.Liens;

/*
 * Cardinalite.java
 *
 * Created on 11 mai 2004, 15:38
 */

/**
 * Cette classe permet la représentation des cardinalités d'une AssociationEnd.
 * @see AssociationEndDC
 * @author  cwfr-dzysman
 */
public class Cardinalite {
    /**
     * La limite de la cardinalité n'est pas définie.
     */
    public static final int NOT_DEFINED = -2;
    /**
     * La limite de la cardinalité est l'étoile (*).
     */
    public static final int ETOILE = -1;
    /**
     * La limite haute de la cardinalité.
     */
    private int limiteHaute = Cardinalite.NOT_DEFINED;
    /**
     * La limite basse de la cardinalité.
     */
    private int limiteBasse = Cardinalite.NOT_DEFINED;
    
    /**
     * Ce constructeur de classe est <code>private</code> afin d'empêcher la création
     * d'instance de Cardinalite sans les informations nécessaires.
     */
    private Cardinalite() {  }
    
    /**
     * Ce constructeur de classe permet la création d'une instance de Cardinalite.
     * Il prend en paramètre une String qui contient la définition de la cardinalité.
     * La String peut être de la forme :
     *  <ul>
     *  <li>0
     *  <li> 1
     *  <li> 0..*
     *  <li> 0........* (autant de . que nécessaire)
     *  <li> etc
     *  </ul>
     * @param c la String définissant la cardinalité.
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
     * Cette méthode retourne la limite haute de la cardinalité.
     * 
     * @return limite haute de la cardinalité.
     */
    public int getLimiteHaute() { return limiteHaute; }
    
    /**
     * Cette méthode retourne la limite basse de la cardinalité.
     * 
     * @return limite basse de la cardinalité.
     */
    public int getLimiteBasse() { return limiteBasse; }
    
    /**
     * Cette méthode converti en String la cardinalité afin qu'elle soit affichable.
     *
     * @return une String qui contient la cardinalité. La String est de la forme
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
