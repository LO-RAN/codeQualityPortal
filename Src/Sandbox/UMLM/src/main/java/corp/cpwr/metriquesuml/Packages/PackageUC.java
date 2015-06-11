/*
 * PackageUC.java
 *
 * Created on 19 mai 2004, 10:21
 */

package corp.cpwr.metriquesuml.Packages;
import java.io.*;
import java.util.*;
import corp.cpwr.metriquesuml.*;
import corp.cpwr.metriquesuml.Diagrammes.*;
import org.jdom.*;
import corp.cpwr.metriquesuml.Objets.*;
import corp.cpwr.metriquesuml.Liens.*;

/**
 * Cette classe permet la représentation et l'analyse d'un package appartenant à
 * un diagramme de cas d'utilisations.
 *
 * @author  cwfr-dzysman
 */
public class PackageUC extends PackageUML{
    
    /** Ce package est private afin d'empêcher la création de package sans les informations requises. */
    private PackageUC() {}
    
    /**
     * Ce constructeur permet l'instanciation d'un package de diagramme de cas d'utilisation.
     * Il initialise toutes les variables et lance automatiquement l'analyse du package.
     *
     * @param e L'élément racine du sous-arbre définissant le package.
     * @param p Le package contenant ce package.
     * @param r Le package root du diagramme.
     * @param m le modèle auquel appartient l'objet.
     */
    public PackageUC(Element e, PackageUC p, Modele m, Diagramme pere) {
        super(e, XMIObject.PACKAGEUC, m, XMIObject.DIAGRAMME_UC, (PackageUML)p, pere);
        
        analysePackage(e);
    }
    
    /**
     * Cette méthode permet de remplir tous les "trous" consécutifs à l'analyse
     * en deux temps du flux XMI.
     */
    public void fillAll() {
        fillAssociation(XMIObject.ASSOCIATIONUC);
        fillGeneralization();
        for(Iterator e=getAll(XMIObject.PACKAGEUC, false).iterator(); e.hasNext(); ) {
            ((PackageUC)e.next()).fillAll();
        }
    }
    
    /**
     * Cette méthode analyse le package et extrait toutes les informations nécessaires.
     * Elle est récursive.
     *
     * @param e Le sous-arbre en cours d'analyse ou devant être analysé.
     */
    public void analysePackage(Element e) {
        Iterator   it = e.getContent().iterator();
        //le premier package est ignoré car il ne contient pas d'informations essentielles.
        if(e.getName().equals("Package") && e!=elementRoot) {
            if(! diagrammePere.isInBlackList(e))
                objets.add(new PackageUC(e, this, getModele(), diagrammePere));
            return;
        }
        
        if(e.getName().equals("Actor")) {
            objets.add(new Actor(e, (XMIObject)this, XMIObject.DIAGRAMME_UC, getModele()));
            return;
        }
        
        if(e.getName().equals("Association")) {
            //objets.add(new AssociationUC(e, this, getModele()));
            objets.add(new Association(e, (XMIObject)this, getModele(), XMIObject.ASSOCIATIONUC, XMIObject.DIAGRAMME_UC));
            return;
        }
        
        if(e.getName().equals("Generalization")) {
            objets.add(new LinkUML(e, XMIObject.GENERALIZATION, XMIObject.DIAGRAMME_UC, "subtype", "supertype", getModele(), (XMIObject)this));
            return;
        }
        
        if(e.getName().equals("UseCase")) {
            String temp = FonctionsParser.getPropertyString(e, "ea_stype");
            if(temp.equals("UseCase"))  objets.add(new UseCase(e, this, getModele(), XMIObject.USECASE));
            else if(temp.equals("Collaboration")) objets.add(new UseCase(e, this, getModele(), XMIObject.COLLABORATIONUC));
            return;
        }
        
        while(it.hasNext()) {
            Object o = it.next();
            if(o instanceof Element) {
                analysePackage((Element)o);
            }
        }
    }
    
    public void calculerMetriques() {
        //le nombre d'acteurs liés à un useCase
        for(Iterator e=getAll(XMIObject.USECASE, false).iterator(); e.hasNext(); ) {
            UseCase uc = (UseCase)e.next();
            int nb_act = 0;
            int nbMess = 0;
            for(Iterator e2=getAll(XMIObject.ASSOCIATIONUC, false).iterator(); e2.hasNext(); ) {
                Association a = (Association)e2.next();
                if(a.isExtremite(uc)) {
                    nbMess++;
                    XMIObject obj = a.getAutreExtremite(uc);
                    if(obj.getTypeObjet()==XMIObject.ACTOR) {
                        nb_act++;
                    }
                }
            }
            uc.setNbActeursAssocies(nb_act);
            uc.setNbMessagesAssocies(nbMess);
        }
        
        calculerComplexite();
        
        for(Iterator it = getAll(XMIObject.PACKAGEUC, false).iterator(); it.hasNext(); ) {
            ((PackageUML)it.next()).calculerMetriques();
        }
    }
    
    public void calculerComplexite() {
        ArrayList actors = getAll(XMIObject.ACTOR, false);
        ArrayList useCases = getAll(XMIObject.USECASE, false);
        ArrayList collaborations = getAll(XMIObject.COLLABORATIONUC, false);
        ArrayList associations = getAll(XMIObject.ASSOCIATIONUC, false);
        ArrayList generalizations = getAll(XMIObject.GENERALIZATION, false);
        
        float num = ((float)(actors.size()+useCases.size()+collaborations.size()));
        float div = ((float)(associations.size()+generalizations.size()));
        float p1 = 0.0f;
        if(div!=0.0f) p1 = num / div;
        
        ArrayList al = getAll(XMIObject.USECASE, true);
        al.addAll(getAll(XMIObject.COLLABORATIONUC, true));
        
        float moyenneActeurs = 0.0f;
        float moyenneMessages = 0.0f;
        for(Iterator it=al.iterator(); it.hasNext(); ) {
            UseCase uc = (UseCase)it.next();
            moyenneActeurs += uc.getNbActeursAssocies();
            moyenneMessages += uc.getNbMessagesAssocies();
        }
        
        moyenneActeurs = (al.size()!=0)?(moyenneActeurs / al.size()):0.0f;
        moyenneMessages = (al.size()!=0)?(moyenneMessages / al.size()):0.0f;
        complexite = (p1 + 2.0f*moyenneActeurs + 2.0f*moyenneMessages) / 5.0f;
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
        
        for(Iterator it=getAll(XMIObject.PACKAGEUC, false).iterator(); it.hasNext(); ) {
            PackageUC pkg = (PackageUC)it.next();
            pkg.writeCSV(fic, pere);
        }
    }
    
    
}
