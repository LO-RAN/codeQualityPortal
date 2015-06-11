/*
 * Package.java
 *
 * Created on 8 juin 2004, 16:11
 */

package corp.cpwr.metriquesuml.Packages;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;
import java.util.*;
import corp.cpwr.metriquesuml.MetriquesUMLExceptions.*;
import corp.cpwr.metriquesuml.Diagrammes.*;
import corp.cpwr.metriquesuml.Liens.*;
import corp.cpwr.metriquesuml.Objets.*;

/**
 *
 * @author  cwfr-dzysman
 */
public abstract class PackageUML extends XMIObject{
    protected PackageUML        packageRoot=null;
    /** Le package père */
    protected PackageUML        packagePere    = null;
    /** Le diagramme père */
    protected Diagramme         diagrammePere = null;
    protected float             complexite    = 0.0f;
    
    /** Creates a new instance of Package */
    protected PackageUML() { }
    
    public PackageUML(Element e, int typePackage, Modele m, int typeDiag, PackageUML pere, Diagramme dgp) {
        super(e, typePackage, m, typeDiag, true);
        
        diagrammePere = dgp;
        packagePere = pere;
        packageRoot = (pere==null)?this:packagePere.getPackageRoot();
    }
    
    public abstract void analysePackage(Element e);
    public abstract void fillAll();
    public abstract void calculerMetriques();
    public abstract void writeCSV(String fic, String pere);
    
    public float getComplexite() {  return complexite; }
    public Diagramme getDiagrammePere() {return diagrammePere;}
    public PackageUML getPackageRoot() {return packageRoot;}
    
    public void fillDependency() {
        int taille = objets.size();
        for(int i=0; i<taille; i++ ) {
            XMIObject xmiObj = (XMIObject)objets.get(i);
            if(xmiObj.getTypeObjet()!=XMIObject.DEPENDENCY) continue;
            LinkUML m = (LinkUML)xmiObj;
            XMIObject src = null;
            XMIObject dest = null;
            try {
                src = getBy(m.getSourceId(),true);
            } catch(XMIObjectNotFoundException exc) {
                try {
                    src = getByInModele(m.getSourceId(),true);
                } catch(XMIObjectNotFoundException exc2) {
                    objets.remove(i);
                    taille--; i--;
                    continue;
                }
            }
            
            try {
                dest = getBy(m.getDestinationId(),true);
            } catch(XMIObjectNotFoundException exc) {
                try {
                    dest = getByInModele(m.getDestinationId(),true);
                } catch(XMIObjectNotFoundException exc2) {
                    objets.remove(i);
                    taille--; i--;
                    continue;
                }
            }
            m.setSource(src);
            m.setDestination(dest);
        }
    }
    
    /**
     * Cette méthode permet de remplir les champs "vides" des relations d'héritage,
     * notamment les "pointeurs" vers les objets représentants les
     * cas héritants et hérités.
     */
    public void fillGeneralization() {
        int taille = objets.size();
        for(int i=0; i<taille; i++) {
            XMIObject xmiObj = (XMIObject)objets.get(i);
            if(xmiObj.getTypeObjet()!=XMIObject.GENERALIZATION) continue;
            LinkUML gen = (LinkUML)xmiObj;
            XMIObject o1=null;
            XMIObject o2=null;
            try {
                o1 = packageRoot.getBy(gen.getSourceId(),true);
                o2 = packageRoot.getBy(gen.getDestinationId(),true);
                switch(getTypeObjet()) {
                    case XMIObject.PACKAGEUC:
                        if( (o1.getTypeObjet()!=o2.getTypeObjet()) && ((o1.getTypeObjet()!=XMIObject.ACTOR) || (o1.getTypeObjet()!=XMIObject.USECASE) ) ) {
                            objets.remove(i);
                            i--;taille--;
                            continue;
                        }
                        break;
                    case XMIObject.PACKAGEDC:
                        if( (o1.getTypeObjet()==XMIObject.PACKAGEDC) || (o2.getTypeObjet()==XMIObject.PACKAGEDC) ) {
                            objets.remove(i);
                            taille--; i--;
                            continue;
                        }
                        break;
                }
                switch(o1.getTypeObjet()) {
                    case XMIObject.CLASSEDC:
                    case XMIObject.INTERFACEDC:
                        ((DC)o1).addClasseMere((DC)o2);
                        ((DC)o2).addClasseFille((DC)o1);
                        break;
                    case XMIObject.ACTOR:
                        ((Actor)o2).addHeritant((Actor)o1);
                        ((Actor)o1).addHerite((Actor)o2);
                        break;
                }
                gen.setSource(o1);
                gen.setDestination(o2);
            }
            catch (XMIObjectNotFoundException exc) {
                objets.remove(i);
                i--;taille--;
            }
        }
    }
    
    public void fillAssociation(int tA) {
        int taille = objets.size();
        for(int i=0; i<taille; i++) {
            XMIObject obj = (XMIObject)objets.get(i);
            if(obj.getTypeObjet()!=tA) continue;
            Association a = (Association)obj;
            ArrayList objs = new ArrayList();
            boolean retire = false;
            for(Iterator e2 = a.getAssociationEnds().iterator(); e2.hasNext() && !retire; ){
                AssociationEnd ae = ((AssociationEnd) e2.next());
                try {
                    XMIObject objExtr = packageRoot.getBy(ae.getIdExtremite(),true);
                    objs.add(objExtr);
                } catch(XMIObjectNotFoundException exc) {
                    try {
                        XMIObject objExtr = getByInModele(ae.getIdExtremite(),true);
                        objs.add(objExtr);
                    } catch(XMIObjectNotFoundException exc2) {
                        retire=true;
                    }
                }
            }
            if(retire) {
                objets.remove(i);
                taille--; i--;
                continue;
            }
            
            //on remplit après pour éviter de remplir une association dont une des extrémités serait à null.
            int taille_ae = objs.size();
            for(int j=0; j<taille_ae; j++) {
                AssociationEnd ae = (AssociationEnd) a.getAssociationEnds().get(j);
                ae.setEnd((XMIObject)objs.get(j));
            }
        }
    }
    
    public void fillObjetsUML() {
        for(Iterator e=objets.iterator(); e.hasNext(); ) {
            XMIObject xmiObj = (XMIObject) e.next();
            if(xmiObj.getTypeObjet()!=XMIObject.OBJECT_UML) continue;
            ((ObjectUML)xmiObj).fillIt();
        }
    }
}
