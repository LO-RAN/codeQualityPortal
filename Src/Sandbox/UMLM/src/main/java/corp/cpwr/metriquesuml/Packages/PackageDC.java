package corp.cpwr.metriquesuml.Packages;

/*
 * PackageDC.java
 *
 * Created on 10 mai 2004, 17:42
 */

import java.io.*;
import java.util.*;
import corp.cpwr.metriquesuml.*;
import org.jdom.*;
import corp.cpwr.metriquesuml.MetriquesUMLExceptions.*;
import corp.cpwr.metriquesuml.Diagrammes.*;
import corp.cpwr.metriquesuml.Liens.*;
import corp.cpwr.metriquesuml.Objets.*;

/**
 * Cette classe permet la représentation et l'analyse d'un package appartenant à
 * un diagramme de classes.
 * @author  cwfr-dzysman
 */

public class PackageDC extends PackageUML{
    ArrayList classifierRoles = null;
    /**
     * Ce constructeur est <code>private</code> afin d'empêcher la création d'un
     * package sans les informations nécessaires.
     */
    private PackageDC(){}
    
    /**
     * Ce constructeur permet l'instanciation d'un package de diagramme de classes.
     * Il initialise toutes les variables et lance automatiquement l'analyse du package.
     *
     * @param e L'élément racine du sous-arbre définissant le package.
     * @param p Le package contenant ce package.
     * @param r Le package racine de l'arbre des packages dont celui-ci est un noeud (ou une feuille).
     */
    public PackageDC(Element e, PackageDC p, Modele m, Diagramme dgp) {
        super(e, XMIObject.PACKAGEDC, m, XMIObject.DIAGRAMME_CLASSE, (PackageUML)p, dgp);
        classifierRoles = new ArrayList();
        analysePackage(e);
    }
    
    /**
     * Cette méthode lance le calcul de la métrique correspondant à la
     * profondeur d'héritage pour les classes et les interfaces du packages, ainsi
     * que pour les packages fils.
     */
    public void calculerMetriques() {
        calculerComplexite();
        ArrayList vect = getModele().getAll(XMIObject.OBJECT_UML, true);
        ArrayList classes = getAll(XMIObject.CLASSEDC, false);
        classes.addAll(getAll(XMIObject.INTERFACEDC, false));
        for(Iterator e = classes.iterator(); e.hasNext(); ) {
            DC cdc = (DC)e.next();
            cdc.calculerProfondeurMaximale();
            cdc.calculerNombreDescendants();
            cdc.calculerNombreAncetres();
            cdc.calculerNombreFilles();
            cdc.calculerCouplages();
            cdc.calculerComplexiteCyclomatique();
            cdc.calculerTailleClasse();
            cdc.calculerNbInstances(vect);
            cdc.calculerNbInterfacesImplementees();
            cdc.calculerRFC();
        }
        
        for(Iterator e=getAll(XMIObject.PACKAGEDC, false).iterator(); e.hasNext(); ) {
            ((PackageDC)e.next()).calculerMetriques();
        }
    }
    
    public void calculerComplexite() {
        if(complexite!=0.0f) return;
        float nbClasses = getNbObjetsXMIContenusTotal();
        float nbLiens = getNbLiensContenusTotal();
        
        if(nbLiens!=0.0f) complexite = (float)(nbClasses/nbLiens);
    }
    
    public void fillAll() {
        fillClassifierRole();
        fillAssociation(XMIObject.ASSOCIATIONDC);
        fillGeneralization();
        fillDependency();
        fillObjetsUML();
        
        int taille = objets.size();
        for(int i=0; i<taille; i++) {
            XMIObject obj = (XMIObject)objets.get(i);
            if( obj.getTypeObjet()!=XMIObject.ASSOCIATIONDC) continue;
            AssociationDC a = (AssociationDC)obj;
            if(a.getAssociationClassId()!=null) {
                try {
                    Object o = packageRoot.getBy(a.getAssociationClassId(),true);
                    if(o!=null) {
                        if(o instanceof DC) a.setAssociationClassDC((DC)o);
                    }
                } catch(XMIObjectNotFoundException exc) {
                    continue;
                }
            }
        }
        
        for( Iterator e = getAll(XMIObject.PACKAGEDC, false).iterator(); e.hasNext(); ) {
            ((PackageDC)e.next()).fillAll();
        }
    }
    
    /**
     * Cette méthode met à jour les identifiants des packages en fonction de ce
     * qui a été lu dans les classifierRoles.
     */
    public void fillClassifierRole() {
        for(Iterator e= classifierRoles.iterator(); e.hasNext(); ) {
            String s1 = (String)e.next();
            String s2 = (String)e.next();
            try{
                XMIObject obj = packageRoot.getBy(s1,false);
                //il faut tester pour savoir si l'on a trouvé le classifierRole.
                //Si on ne l'a pas trouvé, cela signifie que c'est un objet qui n'a pas
                //de balises spécifiques en XMI et qui a donc par défaut une balise
                //ClassifierRole. Auquel cas, il faut l'ignorer.
                obj.setId(s2);
            }catch(XMIObjectNotFoundException exc) {
                continue;
            }
        }

        for(Iterator e = getAll(XMIObject.PACKAGEDC, false).iterator(); e.hasNext(); ) {
            ((PackageDC)e.next()).fillClassifierRole();
        }
    }
    
    /**
     * Cette méthode ajoute un classifierRole en vu d'une future analyse.
     * @param e L'élément définissant le classifierRole à ajouter.
     */
    public void addClassifierRole(Element e) {
        String test = FonctionsParser.getPropertyString(e, "ea_stype");
        if(test!=null) {
            if(test.equals("Object")) {
                objets.add(new ObjectUML(e, XMIObject.DIAGRAMME_CLASSE, getModele()));
                return;
            }
        }
        
        //Pas besoin de créer une nouvelle classe pour un classifierRole.
        //on place d'abord dans le ArrayList le nom de l'objet concerné, puis l'identifiant
        //qui lui correspond.
        String classifierName = e.getAttributeValue("name");
        //un classifierRole qui n'a pas de nom n'a que peu d'intêrets.
        if(classifierName==null) return;
        classifierRoles.add(classifierName);
        classifierRoles.add(e.getAttributeValue("xmi.id"));
    }
    
    /**
     * Cette méthode renvoie le nombre de classes ou interfaces dans le package.
     * Le nombre de classes est renvoyé si le paramètre est égal à XMIObject.CLASSEDC.
     * Si le paramètre vaut XMIObject.INTERFACEDC, alors c'est le nombre d'interfaces
     * qui est retourné.
     * @return le nombre de "DC" dans le package.
     */
    public int nbDC(int typeDC) {
        int retour = 0;
        for(Iterator e=getAll(typeDC, false).iterator(); e.hasNext(); ) {
            XMIObject o = (XMIObject)e.next();
            if(!((DC)o).getEAStype().equals("Association")) retour++;
        }
        return retour;
    }
    /**
     * Cette méthode renvoie le nombre de packages fils dans le package.
     * @return le nombre de packages fils dans le package.
     */
    public int nbPackages() {
        return getAll(XMIObject.PACKAGEDC, true).size();
    }
    
    /**
     * Cette méthode renvoie le nombre d'associations dans le package.
     * @return le nombre d'associations dans le package.
     */
    public int nbAssociationPackage() {
        return getAll(XMIObject.ASSOCIATIONDC, true).size();
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
        if(e.getName().equals("Package") && e!=getRootElement()) {
            if(! diagrammePere.isInBlackList(e))
                objets.add(new PackageDC(e, this, getModele(), diagrammePere));
            return;
        }
        
        if(e.getName().equals("Class")) {
            objets.add(new DC(e, this, XMIObject.CLASSEDC, getModele()));
            return;
        }
        
        if(e.getName().equals("Interface")) {
            objets.add(new DC(e, this, XMIObject.INTERFACEDC, getModele()));
            return;
        }
        
        if(e.getName().equals("Generalization")) {
            objets.add(new LinkUML(e, XMIObject.GENERALIZATION, XMIObject.DIAGRAMME_CLASSE, "subtype", "supertype", getModele(), (XMIObject)this));
            return;
        }
        
        if(e.getName().equals("ClassifierRole")) {
            addClassifierRole(e);
            return;
        }
        
        if(e.getName().equals("Association") || e.getName().equals("AssociationRole")) {
            objets.add(new AssociationDC(e, this, getModele()));
            return;
        }
        
        if(e.getName().equals("Actor")) {
            objets.add(new Actor(e, (XMIObject)this, XMIObject.DIAGRAMME_CLASSE, getModele()));
            return;
        }
        
        if(e.getName().equals("Dependency")) {
            objets.add(new LinkUML(e, XMIObject.DEPENDENCY, XMIObject.DIAGRAMME_CLASSE, "supplier", "client", getModele(), (XMIObject)this));
            return;
        }
        
        while(it.hasNext()) {
            Object o = it.next();
            if(o instanceof Element) {
                analysePackage((Element)o);
            }
        }
    }
    
    /** Cette méthode retourne le nombre de classes et d'objets contenues dans ce package, en
     * incluant le nombre de classes contenues dans les packages fils.
     * @return Le nombre de classes contenues dans ce package.
     */
    public int getNbObjetsXMIContenusTotal() {
        int retour = getAll(XMIObject.CLASSEDC, false).size()+getAll(XMIObject.INTERFACEDC, false).size()+ getAll(XMIObject.OBJECT_UML, false).size();
        for(Iterator e=getAll(XMIObject.PACKAGEDC, false).iterator(); e.hasNext(); ){
            PackageDC pkg = (PackageDC) e.next();
            retour += pkg.getNbObjetsXMIContenusTotal();
        }
        return retour;
    }
    
    /**
     * Cette méthode retourne le nombre de liens (associations, dépendances, héritages) contenus dans ce package, en
     * incluant le nombre de liens contenus dans les packages fils.
     * @return Le nombre de liens contenus dans ce package.
     */
    public int getNbLiensContenusTotal() {
        int retour = getAll(XMIObject.ASSOCIATIONDC, false).size()+getAll(XMIObject.DEPENDENCY, false).size()+
        getAll(XMIObject.GENERALIZATION,false).size();
        for(Iterator e=getAll(XMIObject.PACKAGEDC, false).iterator(); e.hasNext(); ){
            PackageDC pkg = (PackageDC) e.next();
            retour += pkg.getNbLiensContenusTotal();
        }
        return retour;
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
        
        for(Iterator it=getAll(XMIObject.CLASSEDC, true).iterator(); it.hasNext(); ) {
            DC cdc = (DC)it.next();
            cdc.writeCSV(fic, pere+"."+getName());
        }
        for(Iterator it=getAll(XMIObject.INTERFACEDC, true).iterator(); it.hasNext(); ) {
            DC cdc = (DC)it.next();
            cdc.writeCSV(fic, pere+"."+getName());
        }
        
        for(Iterator it=getAll(XMIObject.PACKAGEDC, true).iterator(); it.hasNext(); ) {
            PackageDC pkg = (PackageDC)it.next();
            pkg.writeCSV(fic, pere);
        }
    }
    
    public ArrayList getDependenciesWhereClient(XMIObject obj) {
        ArrayList al = new ArrayList();
        for(Iterator it=getAll(XMIObject.DEPENDENCY, true).iterator(); it.hasNext(); ) {
            LinkUML d = (LinkUML) it.next();
            if(obj==d.getDestination()) al.add(d);
        }
        return al;
    }
    
    public ArrayList getDependenciesWhereSupplier(XMIObject obj) {
        ArrayList al = new ArrayList();
        for(Iterator it=getAll(XMIObject.DEPENDENCY, true).iterator(); it.hasNext(); ) {
            LinkUML d = (LinkUML) it.next();
            if(obj==d.getSource()) al.add(d);
        }
        return al;
    }
    
    public ArrayList getLinkedDependencies(XMIObject obj) {
        ArrayList al = new ArrayList();
        for(Iterator it=getAll(XMIObject.DEPENDENCY, true).iterator(); it.hasNext(); ) {
            LinkUML d = (LinkUML) it.next();
            if( (obj==d.getDestination()) || (obj==d.getSource()) )al.add(d);
        }
        return al;
    }
    
    public ArrayList getLinkedAssociations(XMIObject obj) {
        ArrayList al = new ArrayList();
        for(Iterator it=getAll(XMIObject.ASSOCIATIONDC, true).iterator(); it.hasNext(); ) {
            AssociationDC d = (AssociationDC) it.next();
            if( d.isExtremite(obj) ) al.add(d);
        }
        return al;
    }
}
