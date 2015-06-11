/*
 * PackageCollaboration.java
 *
 * Created on 4 juin 2004, 11:52
 */

package corp.cpwr.metriquesuml.Packages;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;
import java.io.*;
import java.util.*;
import corp.cpwr.metriquesuml.Objets.*;
import corp.cpwr.metriquesuml.Diagrammes.*;
import corp.cpwr.metriquesuml.Liens.*;

/**
 *
 * @author  cwfr-dzysman
 */
public class PackageCollaboration extends PackageUML{
    /**
     * Ce constructeur est private afin d'empecher la création d'une instance de la
     * classe sans les informations nécessaires
     */
    private PackageCollaboration() {}
    
    public PackageCollaboration(Element e, PackageCollaboration p, Modele m, Diagramme dgp) {
        super(e, XMIObject.PACKAGE_COLL, m, XMIObject.DIAGRAMME_COLL, (PackageUML)p, dgp);
        
        analysePackage(e);
    }
    
    
    /**
     * Cette méthode analyse le package et extrait toutes les informations nécessaires.
     * Elle est récursive.
     *
     * @param e Le sous-arbre en cours d'analyse ou devant être analysé.
     */
    public void analysePackage(Element e) {
    /*Ici, on n'utilise pas FonctionsParser car il y a trop de balises à analyser
      et la perte d'efficacité serait trop importante. Il faudra parcourir 6 fois
     l'arbre du package au lieu d'une fois sans FonctionsParser.*/
        Iterator   it = e.getContent().iterator();
        //le premier package est volontairement ignoré car il ne contient pas d'informations essentielles.
        if(e.getName().equals("Package") && e!=elementRoot) {
            if(! diagrammePere.isInBlackList(e))
                objets.add(new PackageCollaboration(e, this, getModele(), diagrammePere));
            return;
        }
        
        if(e.getName().equals("ClassifierRole")) {
            String temp = FonctionsParser.getPropertyString(e, "ea_stype");
            if(temp.equals("Object")) {
                objets.add(new ObjectUML(e, XMIObject.DIAGRAMME_COLL, getModele()));
                return;
            }
        }
        
        if(e.getName().equals("Dependency")) {
            objets.add(new LinkUML(e, XMIObject.DEPENDENCY, XMIObject.DIAGRAMME_COLL, "supplier", "client", getModele(), (XMIObject)this));
            return;
        }
        
        if(e.getName().equals("Association") || e.getName().equals("AssociationRole")) {
            objets.add(new Association(e, this, getModele(), XMIObject.DIAGRAMME_COLL));
            return;
        }
        
        if(e.getName().equals("Actor")) {
            objets.add(new Actor(e, this, XMIObject.DIAGRAMME_COLL, getModele()));
            return;
        }
        
        while(it.hasNext()) {
            Object o = it.next();
            if(o instanceof Element) {
                analysePackage((Element)o);
            }
        }
    }
    
    public void fillAll() {
        fillDependency();
        fillAssociation(XMIObject.ASSOCIATION);
        fillObjetsUML();
        for(Iterator e=getAll(XMIObject.PACKAGE_COLL, false).iterator(); e.hasNext(); ) {
            ((PackageCollaboration)e.next()).fillAll();
        }
    }
    
    public void calculerMetriques() {
        calculerComplexite();
    }
    
    public void calculerComplexite() {
        ArrayList al = getAll(XMIObject.ACTOR, true);
        al.addAll(getAll(XMIObject.OBJECT_UML, true));
        
        ArrayList al2 = getAll(XMIObject.DEPENDENCY, true);
        al2.addAll(getAll(XMIObject.ASSOCIATION, true));
        if(al2.size()!=0)
            complexite = (((float)al.size()) / ((float)al2.size()));
    }
    
    public void writeCSV(String fic, String pere) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(fic+"PACKAGES.CSV", true));
            String line =  pere+"."+getName()+";"+complexite;
            bw.newLine();
            bw.write(line, 0, line.length());
            bw.close();
        } catch(java.io.IOException exc) {
        }
        
        for(Iterator it=getAll(XMIObject.PACKAGE_COLL, false).iterator(); it.hasNext(); ) {
            PackageCollaboration pkg = (PackageCollaboration)it.next();
            pkg.writeCSV(fic, pere);
        }
    }
    
}
