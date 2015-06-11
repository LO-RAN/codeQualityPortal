/*
 * DC.java
 *
 * Created on 12 mai 2004, 11:20
 */

package corp.cpwr.metriquesuml.Objets;

import java.io.*;
import java.util.*;
import org.jdom.*;
import corp.cpwr.metriquesuml.*;
import corp.cpwr.metriquesuml.Packages.*;
import corp.cpwr.metriquesuml.Liens.*;
import corp.cpwr.metriquesuml.Diagrammes.DiagrammeActivites;

/**
 * Cette classe "regroupe" les caract�ristiques communes aux classes InterfaceDC et
 * ClasseDC, qui, logiquement, h�ritent de celle-ci.
 * @author  cwfr-dzysman
 */
public class DC extends XMIObject{
    /** Le ea_stype de l'objet : Class, Interface ou Association */
    private String    ea_stype = null;
    /** La liste des classes ou interfaces qui h�ritent de celle-ci. */
    private ArrayList    classesFilles= null;
    /** La liste des classes ou interfaces donc celle-ci h�rite. */
    private ArrayList    classesMeres = null;
    /** Le package qui contient cette classe ou interface. */
    private PackageDC packagePere  = null;
    /** Indique si l'objet est abstrait ou non. */
    private boolean   isAbstract   = false;
    
    
    /******  DEBUT DES VARIABLES CONCERNANT LES METRIQUES ******/
    /** La profondeur d'h�ritage maximale de la classe. */
    private int       profondeurMaximale = -1;
    /** Le nombre de filles de la classe.*/
    private int       nombreFilles = -1;
    /** Le nombre de descendants de la classe. */
    private int       nombreDescendants = -1;
    /** Le nombre d'anc�tres de la classe. */
    private int       nombreAncetres = -1;
    /** Le couplage entrant */
    private int       couplageEntrant = -1;
    /** Le couplage sortant */
    private int       couplageSortant = -1;
    /** Compl�xit� cyclomatique */
    private int       complexiteCyclomatique = 0;
    /** ** ** la taille de la classe ** ** **/
    private int       nbAssociationsClasse    = 0;
    private int       nbAttributsClasse       = 0;
    private int       nbAttributsPublics      = 0;
    private int       nbOperationsClasse      = 0;
    private int       nbOperationsPubliques   = 0;
    private int       nbOperationsSurchargees = 0;
    private int       nbOperationsHeritees    = 0;
    /** ** ** fin la taille de la classe ** ** **/
    
    /** le nombre d'instances de la classe dans le mod�le **/
    private int       nbInstances             = 0;
    /** le nombre d'interfaces que la classe impl�mente **/
    private int       nbInterfacesImplementees= 0;
    /** la rfc de la classe */
    private int       rfc                     = 0;
    
    /**
     * Ce constructeur est protected afin d'�tre accessible par les classes h�ritant
     * de celle-ci. Il ne doit pas �tre accessible pour les autres objets car on
     * ne doit pas pouvoir cr�er de DC sans informations.
     */
    private DC() {}
    
    /**
     * Ce constructeur de classe permet la cr�ation d'instances de DC car il prend
     * en param�tre toutes les informations n�cessaires.
     * <p> Il ne lance pas l'analyse de l'�l�ment donn� en param�tre.
     *
     * @param  e L'�l�ment extrait de l'arbre cr�� par JDOM et symbolisant cette
     *           instance de l'objet.
     * @param  p le package contenant cette instance de l'objet.
     * @param  tableDC la table dans la base de donn�es qui va contenir les informations
     *                sur l'objet (peut �tre SolidDB.table_classes ou SolidDB.table_interfaces).
     * @param  type Le type de l'objet (Ici soit XMIObject.CLASSEDC, soit XMIObject.INTERFACEDC.
     */
    public DC(Element e, PackageDC p, int type, Modele m) {
        super(e, type, m, XMIObject.DIAGRAMME_CLASSE, true);
        classesFilles = new ArrayList();
        classesMeres = new ArrayList();
        packagePere = p;
        
        ArrayList vect = FonctionsParser.findAllBalises(e, "Attribute");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new Attribut((Element)it.next(), this, getModele(), XMIObject.ATTRIBUT));
        }
        vect = FonctionsParser.findAllBalises(e, "Operation");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new Methode((Element)it.next(), this, getModele()));
        }
        
        isAbstract = e.getAttributeValue("isAbstract").equals("true");
        ea_stype = FonctionsParser.getPropertyString(e, "ea_stype");
    }
    
    /**
     * Cette m�thode calcule la profondeur de la classe dans l'arbre d'h�ritage. Si une des
     * classes m�res de la classe ne conna�t pas sa profondeur, alors celle-ci est
     * calcul�e.
     *<p> La valeur de la m�trique est sauvegard�e dans la variable d'instance
     * <code>profondeurMaximale</code>.
     */
    public void calculerProfondeurMaximale() {
        if( classesMeres.size()==0 ) {
            profondeurMaximale = 0;
            return;
        }
        
        for(Iterator e=classesMeres.iterator(); e.hasNext(); ) {
            try {
                int pm = ((DC)e.next()).getProfondeurMaximale();
                if(pm>profondeurMaximale)
                    profondeurMaximale = pm;
            } catch(java.lang.NullPointerException exc) {
            }
        }
        profondeurMaximale++;
    }
    
    /**
     * Cette m�thode calcule le nombre de classes filles de la classe. Ce sont les
     * "filles" directes et non les descendants de la classe.
     * <p>
     * La valeur de la m�trique est sauvegard�e dans la variable d'instance
     * <code>nombreFilles</code>.
     */
    public void calculerNombreFilles() {
        nombreFilles = classesFilles.size();
    }
    
    /**
     * Cette m�thode calcule le nombre de descendants de la classe.
     */
    public void calculerNombreDescendants() {
        if(nombreDescendants!=-1) return;
        nombreDescendants = classesFilles.size();
        for(Iterator e=classesFilles.iterator(); e.hasNext(); ){
            nombreDescendants += ((DC)e.next()).getNombreDescendants();
        }
    }
    
    /**
     * Cette m�thode calcule le nombre d'anc�tres de la classe.
     */
    public void calculerNombreAncetres() {
        if(nombreAncetres!=-1) return;
        nombreAncetres = classesMeres.size();
        for(Iterator e=classesMeres.iterator(); e.hasNext(); ){
            nombreAncetres += ((DC)e.next()).getNombreAncetres();
        }
    }
    
    public PackageDC getPackagePere() {return packagePere;}
    
    /**
     * Cette m�thode calcule la Response For a Class (RFC) de la classe ou interface.
     * Ce sont toutes les m�thodes qui peuvent �tre potentiellement appel�es en r�ponse
     * � un message, c'est � dire, les m�thodes de la classe plus les m�thodes des
     * classes associ�es qui sont accessibles (donc, dans le cas d'association, les m�thodes
     * publiques et dans le cadre de relation d'h�ritage, les m�thodes proteg�es et publiques).
     */
    public void calculerRFC() {
        ArrayList pile = new ArrayList();
        ArrayList dejaVisite = new ArrayList();
        dejaVisite.add(this);
        
        pile.add(this);
        
        ArrayList methodes = getAll(XMIObject.METHODE, false);
        
        rfc = methodes.size();
        while(pile.size()!=0) {
            DC courant = (DC)pile.remove(0);
            //on commence par r�cup�rer toutes les classes associ�es.
            for(Iterator it=(courant.getPackagePere()).getLinkedAssociations(courant).iterator(); it.hasNext(); ) {
                AssociationDC adc = (AssociationDC)it.next();
                
                XMIObject autreExtremite = adc.getAutreExtremite(courant);
                if(! (autreExtremite instanceof DC)) continue;
                AssociationEnd ae = (AssociationEnd)adc.getExtremite(courant);
                if(ae.isNavigable()) {
                    if(autreExtremite!=null && autreExtremite!=courant) {
                        if(! dejaVisite.contains(autreExtremite) ) {
                            pile.add(autreExtremite);
                            dejaVisite.add(autreExtremite);
                            ArrayList temp = ((DC)autreExtremite).getMethodes(XMIObject.NOT_DEFINED);
                            rfc += temp.size();
                        }
                    }
                }
            }
            
            //pour on r�cup�re les classes directement h�rit�es.
            for(Iterator it=courant.getClassesMeres().iterator(); it.hasNext(); ) {
                DC cdc = (DC)it.next();
                if(cdc!=null && cdc!=courant) {
                    if(! dejaVisite.contains(cdc) ) {
                        pile.add(cdc);
                        dejaVisite.add(cdc);
                        ArrayList temp = cdc.getMethodes(XMIObject.NOT_DEFINED);
                        rfc += temp.size();
                    }
                }
            }
        }
    }
    
    public int getRFC() {
        if(rfc==-1) calculerRFC();
        return rfc;
    }
    
    /**
     * Cette m�thode renvoie le ea_stype de l'objet.
     * @return le param�tre "ea_stype" de l'objet.
     */
    public String getEaSType() {return ea_stype;}
    
    public void calculerCouplages() {
        if(couplageEntrant!=-1 && couplageSortant!=-1) return;
        couplageEntrant = 0;
        couplageSortant = 0;
        //on commence par calculer le couplage entrant avec les associations.
        for(Iterator e=packagePere.getLinkedAssociations(this).iterator(); e.hasNext(); ) {
            //Pour chaque association li�es � la classe.
            //il faut parcourir toutes les associationEnds.
            AssociationDC adc = (AssociationDC)e.next();
            XMIObject obj = adc.getAutreExtremite(this);
            AssociationEnd aeEntrant = adc.getExtremite(this);
            AssociationEnd aeSortant = adc.getExtremite(obj);
            //Si l'autre "c�t�" de l'association n'est pas navigable,
            //elle ne "rentre" pas dans le cadre du couplage entrant, donc
            //on l'ignore.
            boolean entrant = true;
            boolean sortant = true;
            if(obj!=this) {
                if( !aeEntrant.isNavigable() ) entrant=false;
                if( !aeSortant.isNavigable() ) sortant=false;
            }
            //On r�cup�re l'autre extr�mit� de l'association.
            if(entrant || sortant) {
                try{
                    int tempo = 0;
                    if(obj.getTypeObjet()==XMIObject.CLASSEDC || obj.getTypeObjet()==XMIObject.INTERFACEDC)
                        if(((DC)obj).getEaSType().equals("Association")) {
                            //Nous avons trouv� une association multiple.
                            //comme entreprise architect "permet" de cr�er une association
                            //qui boucle sur une classe "Association" (donc une association multiple),
                            //il faut g�rer ce cas.
                            if(obj!=this) {//boucle sinon
                                tempo = ((DC)obj).getPackagePere().getLinkedAssociations(this).size()-1;
                            }
                        }
                        else {
                            tempo = 1;
                        }
                    if(entrant) couplageEntrant+=tempo;
                    if(sortant) couplageSortant+=tempo;
                }catch(java.lang.NullPointerException exc) {
                }
            }
        }
        //puis avec les d�pendances.
        couplageEntrant += packagePere.getDependenciesWhereSupplier(this).size();
        couplageSortant += packagePere.getDependenciesWhereClient(this).size();
    }
    
    /**
     * Cette m�thode ajoute un objet de type DC dans la liste des "filles" de cette
     * instance.
     *
     * @param c la classe fille de cette classe.
     */
    public void addClasseFille(DC c) {classesFilles.add(c);}
    
    /**
     * Cette m�thode ajoute un objet de type DC dans la liste des "m�res" de cette
     * instance.
     *
     * @param c la classe m�re de cette classe.
     */
    public void addClasseMere(DC c)  {classesMeres.add(c);}
    
    /**
     * Cette m�thode calcule la compl�xit� cyclomatique de chaque m�thode.
     */
    public void calculerComplexiteCyclomatique() {
        ArrayList methodes = getAll(XMIObject.METHODE, false);
        for(Iterator e=methodes.iterator(); e.hasNext(); ) {
            Methode m = (Methode)e.next();
            String  nom = m.getName();
            DiagrammeActivites da = getModele().searchActivityDiagram(nom);
            int r = 1;
            if(da!=null) {
                r = da.getComplexiteCyclomatique();
                m.setComplexiteCyclomatique(r);
                m.setHasActivityDiagram(true);
            }
            complexiteCyclomatique+=r;
        }
    }
    
    /**
     * Cette m�thode calcule la taille de la classe.
     */
    public void calculerTailleClasse() {
        //nbAssociationsClasse
        nbAssociationsClasse = packagePere.getLinkedAssociations(this).size()+packagePere.getLinkedDependencies(this).size();
        //nbAttributsClasse
        ArrayList al = getAll(XMIObject.ATTRIBUT, false);
        nbAttributsClasse = al.size();
        for(Iterator it = al.iterator(); it.hasNext(); ) {
            Attribut att = (Attribut)it.next();
            if(att.getVisibility()==XMIObject.PUBLIC) nbAttributsPublics++;
        }
        //nbOperationsClasse
        ArrayList methodes = getAll(XMIObject.METHODE, false);
        nbOperationsClasse = methodes.size();
        //nbOperationsSurchargees
        for(Iterator e1=methodes.iterator(); e1.hasNext(); ) {
            Methode m = (Methode)e1.next();
            if(m.getVisibility()==XMIObject.PUBLIC) nbOperationsPubliques++;
            for(Iterator e=classesMeres.iterator(); e.hasNext(); ) {
                if(((DC)e.next()).surcharge(m)) {
                    nbOperationsSurchargees++;
                }
            }
        }
        
        //nbOperationsHeritees
        ArrayList vect = methodesHeritees();
        nbOperationsHeritees = vect.size()-methodes.size();
    }
    
    public int getNbAssociationsLiees() {return nbAssociationsClasse;}
    public int getNbOperationsClasses() {return nbOperationsClasse;}
    public int getNbOperationsHeritees() {return nbOperationsHeritees;}
    public int getNbInterfacesImplementees() {return nbInterfacesImplementees;}
    public int getNombreMethodesSurchargees() { return nbOperationsSurchargees;}
    public int getNbInstances() {return nbInstances;}
    /**
     * Cette m�thode renvoie la liste des classes directement h�rit�es li�es � cet objet.
     * @return La liste des classes directement h�rit�es li�es � cet objet.
     */
    public ArrayList getClassesMeres() {return classesMeres;}
    /**
     * Cette m�thode renvoie la valeur de la variable d'instance <code>profondeurMaximale
     * </code>. Si la profondeur maximale n'a pas encore �t� calcul�e, alors le calcul
     * est lanc�.
     *
     * @return la valeur de la variable d'instance <code>profondeurMaximale
     * </code>. Cette valeur repr�sente la valeur de la m�trique sur la
     * profondeur d'h�ritage de la classe.
     */
    public int getProfondeurMaximale() {
        if(profondeurMaximale==-1) calculerProfondeurMaximale();
        return profondeurMaximale;
    }
    
    /**
     * Cette m�thode retourne le nombre de descendants de la classe. Si ce
     * nombre n'est pas encore calcul� (�gal � -1), alors il est
     * automatiquement calcul�.
     *
     * @return le nombre de descendants de la classe.
     */
    public int getNombreDescendants() {
        if(nombreDescendants==-1) calculerNombreDescendants();
        return nombreDescendants;
    }
    
    /**
     * Cette m�thode retourne le nombre d'anc�tres de la classe. Si ce
     * nombre n'est pas encore calcul� (�gal � -1), alors il est
     * automatiquement calcul�.
     *
     * @return le nombre d'anc�tres de la classe.
     */
    public int getNombreAncetres() {
        if(nombreAncetres==-1) calculerNombreAncetres();
        return nombreAncetres;
    }
    
    /**
     * Cette m�thode retourne le couplage entrant de la classe ou interface.
     * Si cette m�trique n'a pas encore �t� calcul�e, alors elle l'est.
     * @return le couplage entrant de la classe ou interface.
     */
    public int getCouplageEntrant() {
        if(couplageEntrant==-1) calculerCouplages();
        return couplageEntrant;
    }
    public int getCouplageSortant() {
        if(couplageSortant==-1) calculerCouplages();
        return couplageSortant;
    }
    
    /**
     * Cette m�thode retourne le nombre de filles de la classe. Si ce
     * nombre n'est pas encore calcul� (�gal � -1), alors il est
     * automatiquement calcul�.
     *
     * @return le nombre de descendants de la classe.
     */
    public int getNombreFilles() {
        if(nombreFilles==-1) calculerNombreFilles();
        return nombreFilles;
    }
    
    /**
     * Cette m�thode permet de calculer le nombre d'instances de la classe.
     */
    public void calculerNbInstances(ArrayList vect) {
        if(getModele()==null) return;
        for(Iterator it=vect.iterator(); it.hasNext(); ) {
            ObjectUML obj = (ObjectUML)it.next();
            if(obj.isInstance())
                if(obj.getInstanceOf()==this)
                    nbInstances++;
        }
    }
    
    /**
     * Cette m�thode calcule le nombre d'interfaces impl�ment�es.
     */
    public void calculerNbInterfacesImplementees() {
        for(Iterator it = packagePere.getDependenciesWhereClient(this).iterator(); it.hasNext(); ) {
            XMIObject obj = (XMIObject)it.next();
            if(obj.getTypeObjet()==XMIObject.INTERFACEDC) nbInterfacesImplementees++;
        }
    }
    
    /**
     * Cette m�thode renvoie un bool�en indiquant si le DC a, ou non, une m�thode
     * ayant la m�me signature que celle donn�e en param�tre.
     */
    public boolean hasMethode(Methode m) {
        for(Iterator e=getAll(XMIObject.METHODE, false).iterator(); e.hasNext(); ) {
            if(((Methode)e.next()).equals(m)) return true;
        }
        return false;
    }
    
    /**
     * Cette m�thode indique si la m�thode donn�e en param�tre est une m�thode h�rit�e ou non.
     * @return bool�en valant "true" si la m�thode surcharge une autre m�thode, "false" sinon.
     */
    public boolean surcharge(Methode met) {
        if(hasMethode(met)) return true;
        
        for(Iterator e=classesMeres.iterator(); e.hasNext(); ) {
            if(((DC)e.next()).surcharge(met)) return true;
        }
        
        return false;
    }
    
    public ArrayList methodesHeritees() {
        ArrayList retour = new ArrayList();
        ArrayList dejaVisite = new ArrayList();
        dejaVisite.add(this);
        methodesHeriteesPrivate(retour, true, new ArrayList());
        return retour;
    }
    
    /**
     * Cette m�thode ajoute dans le ArrayList donn� en param�tre les m�thodes qui
     * sont h�rit�es.
     */
    private void methodesHeriteesPrivate(ArrayList vect, boolean isFeuille, ArrayList dejaVisitees) {
        for(Iterator e=classesMeres.iterator(); e.hasNext(); ) {
            DC cdc = (DC)e.next();
            if(!dejaVisitees.contains(cdc)) {
                dejaVisitees.add(cdc);
                cdc.methodesHeriteesPrivate(vect, isFeuille, dejaVisitees);
            }
        }
        ArrayList methodes = getAll(XMIObject.METHODE, false);
        for(Iterator e=methodes.iterator(); e.hasNext(); ){
            boolean insere = false;
            Methode methode = (Methode)e.next();
            int vis = methode.getVisibility();
            if(vis==XMIObject.NOT_DEFINED || vis==XMIObject.PRIVATE) continue;
            for(Iterator e2=vect.iterator(); e2.hasNext() && !insere; ) {
                Methode m = (Methode)e2.next();
                if(methode.equals(m)) {
                    insere = true;
                    if(m.getVisibility()<=methode.getVisibility() || isFeuille) {
                        vect.remove(m);
                        vect.add(methode);
                    }
                }
            }
            if(!insere) vect.add(methode);
        }
    }
    
    /**
     * Cette m�thode renvoie toutes les m�thodes qui ont comme visibilit� celle envoy�e en param�tre.
     * Si la visibilit� de la m�thode envoy�e en param�tre est non d�finie (<code>XMIObject.NOT_DEFINED</code>),
     * alors, toutes les m�thodes sont renvoy�es.
     * C'est une copie du ArrayList qui est renvoy�. Il peut donc �tre manipul� sans risque. Par contre, ce ne sont
     * pas des copies des m�thodes qui sont renvoy�es dans le ArrayList. Si ces m�thodes sont modifi�es,
     * alors elles le seront pour le mod�le.
     * @param type Le type de visibilit� demand�.
     * @return la liste des m�thodes ayant la visibilit� demand�e.
     */
    public ArrayList getMethodes(int type) {
        ArrayList retour = new ArrayList();
        for(Iterator it=getAll(XMIObject.METHODE, false).iterator(); it.hasNext(); ){
            Methode m = (Methode)it.next();
            if(m.getVisibility()==type || type==XMIObject.NOT_DEFINED) {
                retour.add(m);
            }
        }
        return retour;
    }
    
    public boolean heriteDe(DC cdc) {
        for(Iterator it = classesMeres.iterator(); it.hasNext(); ){
            DC itDC = (DC)it.next();
            if(itDC==cdc) return true;
            if(itDC.heriteDe(cdc)) return true;
        }
        return false;
    }
    
    public ArrayList getAllClassesMeres() {
        ArrayList retour = new ArrayList();
        getAllClassesMeresPrivate(retour);
        return retour;
    }
    
    private void getAllClassesMeresPrivate(ArrayList vect) {
        for(Iterator it = classesMeres.iterator(); it.hasNext(); ) {
            DC cdc = (DC)it.next();
            if(!vect.contains(cdc)) {
                vect.add(cdc);
                getAllClassesMeresPrivate(vect);
            }
        }
    }
    
    public void writeCSV(String fic, String pere) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fic+"CLASSES.CSV", true));
            String line =  pere+"."+getName()+";"+profondeurMaximale+";"+nombreFilles+";"+nombreDescendants+";"+nombreAncetres+";"+couplageEntrant+";"+couplageSortant+";"+
            complexiteCyclomatique+";"+nbAssociationsClasse+";"+nbAttributsClasse+";"+nbOperationsClasse+";"+nbOperationsSurchargees+";"+
            nbOperationsHeritees+";"+nbInstances+";"+nbInterfacesImplementees+";"+rfc+";"+nbAttributsPublics+";"+nbOperationsPubliques+";"+classesMeres.size();
            bw.newLine();
            bw.write(line, 0, line.length());
            bw.close();
            
        } catch(java.io.IOException exc) {
        }
        
    }
    
    public String getEAStype() {return ea_stype;}
    
}
