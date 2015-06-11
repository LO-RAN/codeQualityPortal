/*
 * DiagrammeActET.java
 *
 * Created on 2 août 2004, 15:26
 */

package corp.cpwr.metriquesuml.Diagrammes;
import corp.cpwr.metriquesuml.*;
import org.jdom.*;
import java.io.*;
import java.util.*;
import corp.cpwr.metriquesuml.MetriquesUMLExceptions.*;
import corp.cpwr.metriquesuml.Objets.*;
import corp.cpwr.metriquesuml.Liens.*;
/**
 *
 * @author  cwfr-dzysman
 */
public abstract class DiagrammeActET extends Diagramme{
    //indique si le diagramme est bien formé ou non, i.e. si chaque sous diagramme a bien
    //un état initial et un état final minimum.
    private float ratioMalForme = 0;
    
    
    /** Creates a new instance of DiagrammeActET */
    protected DiagrammeActET() { }
    
    public DiagrammeActET(Element e, String n, Modele m, int type) {
        super(e, type, m);
        n = n.replace('.', '_');
        n = n.replace(' ', '_');
        name = n;
    }
    
    public void analyseDiagramme() {
        //on récupère ensuite tous les pseudoStates (états initiaux, terminaux, etc)
        ArrayList vect = FonctionsParser.findAllBalises(getRootElement(), "PseudoState");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new PseudoState((Element)it.next(), XMIObject.DIAGRAMME_ET, getModele()));
        }
        
        //puis on récupère toutes les transitions
        vect = FonctionsParser.findAllBalises(getRootElement(), "Transition");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new Transition((Element)it.next(), XMIObject.DIAGRAMME_ET, getModele()));
        }
        
        //enfin tous les classifierRole qui sont utiles (pour les objets)
        vect = FonctionsParser.findAllBalises(getRootElement(), "ClassifierRole");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            Element elt = (Element)it.next();
            String temp = FonctionsParser.getPropertyString(elt, "ea_stype");
            if(temp.equals("Object")) objets.add(new ObjectUML(elt, XMIObject.DIAGRAMME_ET, getModele()));
        }
    }
    
    public void fillAll() {
        int taille = objets.size();
        for(int i=0; i<taille; i++){
            XMIObject obj = (XMIObject) objets.get(i);
            switch(obj.getTypeObjet()) {
                case XMIObject.TRANSITION:
                    try {
                        String src = ((Transition)obj).getSourceId();
                        String dest = ((Transition)obj).getDestinationId();
                        ((Transition)obj).setDestination(getBy(dest,true));
                        ((Transition)obj).setSource(getBy(src,true));
                    } catch(XMIObjectNotFoundException exc) {
                        objets.remove(obj);
                        i--; taille--;
                        continue;
                    }
                    break;
                case XMIObject.PSEUDO_STATE:
                case XMIObject.SIMPLE_STATE:
                    try {
                        String p = ((State)obj).getPereId();
                        if(!p.equals("")) {
                            XMIObject objPere = getBy(p,true);
                            if(objPere.getTypeObjet()==XMIObject.SIMPLE_STATE)
                                ((State)obj).setEtatPere((SimpleState)objPere);
                        }
                    } catch(XMIObjectNotFoundException exc) {
                        continue;
                    }
                    break;
            }
        }
    }

    public ArrayList getSousDiagramme(State sp) {
        ArrayList retour = new ArrayList();
        
        for(Iterator it = objets.iterator(); it.hasNext(); ) {
            XMIObject obj = (XMIObject)it.next();
            if( (obj.getTypeObjet()==XMIObject.PSEUDO_STATE) || (obj.getTypeObjet()==XMIObject.SIMPLE_STATE) ) {
                SimpleState p = ((State)obj).getEtatPere();
                if(p==sp) retour.add(obj);
            }
        }
        return retour;
    }
    
    public boolean testFormeDiagramme(ArrayList al) {
        boolean hasInitialState = false;
        boolean hasFinalState = false;
        for(Iterator it=al.iterator(); it.hasNext() && (!hasInitialState || !hasFinalState); ) {
            State st = (State)it.next();
            if(st.getTypeState()==State.INITIAL) hasInitialState=true;
            if(st.getTypeState()==State.FINAL) hasFinalState=true;
        }
        return (hasInitialState && hasFinalState);
    }
    
    public void calculerBienForme() {
        int nbSousDiag = 1;
        //on doit commencer par "pere==null" avant de prendre les sous-machines.
        ArrayList sousDiag = getSousDiagramme(null);
        if(!testFormeDiagramme(sousDiag)) ratioMalForme++;
        
        for(Iterator it=objets.iterator(); it.hasNext(); ) {
            XMIObject obj = (XMIObject) it.next();
            if( obj.getTypeObjet()==XMIObject.SIMPLE_STATE ) {
                SimpleState st = (SimpleState)obj;
                if(st.isSubMachine()) {
                    sousDiag = getSousDiagramme(st);
                    if(!testFormeDiagramme(sousDiag)) ratioMalForme++;
                    nbSousDiag++;
                }
            }
        }
        ratioMalForme = ratioMalForme / ((float)nbSousDiag);
    }
    
    public void writeCSV(String fic, String pere) {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(fic+"DIAGRAMMES.CSV", true));
            String line = pere+"."+getName()+";"+complexite+";"+ratioMalForme+";-1;-1";
            bw.newLine();
            bw.write(line, 0, line.length());
            bw.close();
            
        } catch(java.io.IOException exc) {
        }
    }
    
    public abstract void calculerMetriques();
    
}
