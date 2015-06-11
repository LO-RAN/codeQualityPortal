/*
 * DiagrammeClasse.java
 *
 * Created on 10 mai 2004, 17:11
 */

package corp.cpwr.metriquesuml.Diagrammes;

import corp.cpwr.metriquesuml.*;
import corp.cpwr.metriquesuml.Packages.*;
import org.jdom.*;
import java.io.*;
import java.util.*;

/**
 * Cette classe permet la gestion et l'analyse d'un diagramme de classes. Un diagramme
 * de classes est principalement constitu�, outre les caract�ristiques communes
 * � l'ensemble des diagrammes telles que le nom, d'un package de type PackageDC.
 * @see PackageDC
 * @author  cwfr-dzysman
 */

public class DiagrammeCUC extends Diagramme{
    private int nbActeurs = -1;
    private int nbUseCases = -1;
    
    /**
     * Ce constructeur est <code>private</code> car il ne doit pas �tre possible
     * de cr�er de diagramme de classes sans toutes les informations n�cessaires.
     */
    private DiagrammeCUC() {}
    
    /**
     * Ce constructeur de classe permet la cr�ation d'un diagramme de classes.
     * Il ne lance pas automatiquement l'analyse de ce diagramme.
     *
     * @param e objet de type Element �tant la racine du sous-arbre repr�sentant
     *          le diagramme de classes dans l'arbre g�n�r� par JDOM.
     * @param n le nom du diagramme de classes.
     */
    public DiagrammeCUC(Element e, String n, Modele m, int typeD){
        super(e, typeD, m);
        n = n.replace('.', '_');
        n = n.replace(' ', '_');
        name = n;
    }
    
    /**
     * Cette m�thode lance l'analyse du diagramme de classes en cherchant toutes
     * les balises "Package" � partir de l'�l�ment racine du diagramme et en
     * les rajoutant dans le diagramme.
     * Le nouveau package racine est le premier package trouv�.
     */
    public void analyseDiagramme() {
        switch(getTypeObjet()){
            case XMIObject.DIAGRAMME_CLASSE:
                objets.add(new PackageDC(getRootElement(),null, getModele(), (Diagramme)this));
                break;
            case XMIObject.DIAGRAMME_UC:
                objets.add(new PackageUC(getRootElement(),null, getModele(), (Diagramme)this));
                break;
            case XMIObject.DIAGRAMME_COLL:
                objets.add(new PackageCollaboration(getRootElement(),null, getModele(), (Diagramme)this));
                break;
        }
    }
    
    /**
     * Cette m�thode est automatiquement appel�e une fois l'ensemble du diagramme
     * analys� depuis le flux XMI. Elle lance une deuxi�me analyse concernant les
     * associations, relations d'h�ritage et d�pendances. En effet, ces relations
     * se font entre deux classes et il est possible qu'une des deux classes ne soit
     * pas encore "connue" au moment de l'analyse de la relation.
     *<br>Il faut donc lancer une deuxi�me analyse afin de compl�ter les informations
     * manquantes.
     *<p>
     *Elle lance ensuite le calcul des m�triques.
     *<p>
     *Cette m�thode fini en ex�cutant le remplissage de la base de donn�es.
     */
    public void fillAll() {
        PackageUML pkg = getPackageRoot();
        if(pkg!=null)   pkg.fillAll();
        //ici, nous avons fini d'analyser le diagramme.
    }
    
    /**
     * Cette m�thode lance le calcul des m�triques.
     */
    public void calculerMetriques() {
        ArrayList al = null;
        PackageUML pkg = getPackageRoot();
        if(pkg==null) return;
        pkg.calculerMetriques();
        //on r�cup�re le nombre d'acteurs en fonction du type de diagrammes:
        switch(getTypeObjet()) {
            case XMIObject.DIAGRAMME_CLASSE:
            case XMIObject.DIAGRAMME_UC:
                al = this.getAll(XMIObject.ACTOR, true);
                nbActeurs = al.size();
                al = this.getAll(XMIObject.USECASE, true);
                nbUseCases = al.size();
                break;
        }
        
        //maintenant, on calcule la complexit�.
        switch(getTypeObjet()) {
            case XMIObject.DIAGRAMME_CLASSE:
                al = pkg.getAll(XMIObject.PACKAGEDC, true);
                break;
            case XMIObject.DIAGRAMME_UC:
                al = pkg.getAll(XMIObject.PACKAGEUC, true);
                break;
            case XMIObject.DIAGRAMME_COLL:
                al = pkg.getAll(XMIObject.PACKAGE_COLL, true);
                break;
        }
        float total = 0.0f;
        for(Iterator it=al.iterator(); it.hasNext(); ) {
            total += ((PackageUML)it.next()).getComplexite();
        }
        if(al.size()!=0.0f)
        {
            complexite = total / ((float)al.size());
        }
    }
    
    /**
     * Cette m�thode renvoie la package racine du diagramme.
     *
     * @return Le package racine du diagramme.
     */
    public PackageUML getPackageRoot() {
        PackageUML retour = null;
        for(Iterator it=objets.iterator(); it.hasNext(); ) {
            XMIObject obj=(XMIObject) it.next();
            if( (obj.getTypeObjet()==XMIObject.PACKAGEDC) || (obj.getTypeObjet()==XMIObject.PACKAGEUC)
            ||  (obj.getTypeObjet()==XMIObject.PACKAGE_COLL)) {
                retour = (PackageUML)obj;
                break;
            }
        }
        return retour;
    }
    
    public void writeCSV(String fic, String pere) {
        //on commence par les classes
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(fic+"DIAGRAMMES.CSV", true));
            String line = pere+"."+getName()+";"+complexite+";-1.0;"+nbActeurs+";"+nbUseCases;
            bw.newLine();
            bw.write(line, 0, line.length());
            bw.close();
            PackageUML pkg = getPackageRoot();
            if(pkg!=null) pkg.writeCSV(fic,pere+"."+getName());
            
        } catch(java.io.IOException exc) {
        }
        
    }
}
