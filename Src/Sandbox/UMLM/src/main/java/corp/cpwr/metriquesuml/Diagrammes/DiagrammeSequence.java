/*
 * DiagrammeSequence.java
 *
 * Created on 3 juin 2004, 11:30
 */

package corp.cpwr.metriquesuml.Diagrammes;

import corp.cpwr.metriquesuml.*;
import corp.cpwr.metriquesuml.Objets.*;
import corp.cpwr.metriquesuml.Liens.*;
import org.jdom.*;
import java.io.*;
import java.util.*;
import corp.cpwr.metriquesuml.MetriquesUMLExceptions.*;

/**
 *
 * @author  cwfr-dzysman
 */
public class DiagrammeSequence extends Diagramme {
    private int nbActeurs = 0;
    
    /**
     * Ce constructeur est private afin d'empecher la création de diagrammes de
     * séquence sans les informations nécessaires
     */
    private DiagrammeSequence() {}
    
    /**
     * Ce constructeur permet de créer un diagramme de séquence.
     * @param e l'élément racine du sous-arbre définissant le diagramme de séquence
     *          dans l'arbre généré par JDOM.
     * @param n le nom du diagramme.
     * @param m le modèle auquel appartient l'objet.
     */
    public DiagrammeSequence(Element e, String n, Modele m) {
        super(e, XMIObject.DIAGRAMME_SEQ, m);
        n = n.replace('.', '_');
        n = n.replace(' ', '_');
        name = n;
    }
    
    /**
     * Cette méthode analyse le diagramme de séquence.
     */
    public void analyseDiagramme() {
        //on commence par récupérer tous les états.
        ArrayList vect = FonctionsParser.findAllBalises(getRootElement(), "Actor");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new Actor((Element)it.next(), null, XMIObject.DIAGRAMME_SEQ, getModele()));
        }
        
        vect = FonctionsParser.findAllBalises(getRootElement(), "Message");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            objets.add(new Message((Element)it.next(), XMIObject.DIAGRAMME_SEQ, getModele()));
        }
        
        //enfin tous les classifierRole qui sont utiles (pour les objets)
        vect = FonctionsParser.findAllBalises(getRootElement(), "ClassifierRole");
        for(Iterator it = vect.iterator(); it.hasNext(); ) {
            Element elt = (Element)it.next();
            if(FonctionsParser.getPropertyString(elt, "ea_stype").equals("Sequence")) {
                objets.add(new ObjectUML(elt, XMIObject.DIAGRAMME_SEQ, getModele()));
            }
        }
    }
    /**
     * Cette méthode remplit les champs manquants des messages.
     */
    public void fillAll() {
        for(Iterator it=objets.iterator(); it.hasNext(); ) {
            XMIObject obj = (XMIObject) it.next();
            switch(obj.getTypeObjet()) {
                case XMIObject.MESSAGE:
                    XMIObject src = null;
                    XMIObject dest = null;
                    try {
                        src = getBy(((Message)obj).getSourceId(),true);
                    } catch(XMIObjectNotFoundException exc) {
                        try {
                            src = getByInModele(((Message)obj).getSourceId(),true);
                        } catch(XMIObjectNotFoundException exc2) {}
                    }
                    try {
                        dest = getBy(((Message)obj).getDestinationId(),true);
                    } catch(XMIObjectNotFoundException exc) {
                        try {
                            dest = getByInModele(((Message)obj).getDestinationId(),true);
                        } catch(XMIObjectNotFoundException exc2) {}
                    }
                    ((Message)obj).setSource(src);
                    ((Message)obj).setDestination(dest);
                    break;
                case XMIObject.OBJECT_UML:
                    ((ObjectUML)obj).fillIt();
                    break;
            }
        }
    }
    
    public void calculerMetriques() {
        //on calcule le nombre de messages envoyés après un message.
        ArrayList messages = getAll(XMIObject.MESSAGE, true);
        int nb_messages = messages.size();
        for(Iterator e=messages.iterator(); e.hasNext(); ) {
            Message mess = (Message)e.next();
            mess.setMessagesResultants(nb_messages-mess.getNumeroSequence());
            
            ArrayList temp = getAllMessagesAfter(mess);
            mess.setClotureTransitive(temp.size());
        }
        
        ArrayList objetsUML = getAll(XMIObject.OBJECT_UML, true);
        for(Iterator e=objetsUML.iterator(); e.hasNext(); ) {
            XMIObject obj = (XMIObject) e.next();
            ArrayList sources = getAllMessages(obj, 1);
            ArrayList destinations = getAllMessages(obj, 2);
            ((ObjectUML)obj).setMessagesLies(sources.size(), destinations.size());
        }
        complexite=0.0f;
        for(Iterator e=messages.iterator(); e.hasNext(); ) {
            Message mess = (Message)e.next();
            complexite += mess.getComplexite();
        }
        
        ArrayList al = this.getAll(XMIObject.ACTOR, true);
        nbActeurs = al.size();
    }
    
    /**
     * Cette méthode renvoie une liste des messages dont l'objet en premier paramètre est :
     * <br>
     * <ul>
     * <li> La source si le paramètre type égale 1.
     * <li> La destination si le paramètre type égale 2.
     * </ul>
     * @param obj L'objet qui doit être source ou destination des messages recherchés.
     * @param type Indique si l'objet donné en paramètre doit être source ou destination des messages recherchés.
     * @return la liste des messages correspondants aux critères recherchés.
     */
    public ArrayList getAllMessages(XMIObject objPar, int type) {
        ArrayList retour = new ArrayList();

        for(Iterator e=objets.iterator(); e.hasNext(); ) {
            XMIObject obj = (XMIObject)e.next();
            if(obj.getTypeObjet()!=XMIObject.MESSAGE) continue;
            Message m = (Message)obj;
            if(type==1) {
                if(m.getSource()==objPar)
                    retour.add(m);
            } else if(type==2) {
                if(m.getDestination()==objPar)
                    retour.add(m);
            }
        }
        return retour;
    }
    
    /**
     * Cette méthode renvoie un ArrayList contenant tous les messages suivants le
     * message envoyé en paramètre. Ces messages ne sont que ceux accessibles
     * depuis le message envoyé en paramètre.
     * @param mess le message de départ.
     * @return Un ArrayList contenant tous les messages sélectionnés.
     */
    
    public ArrayList getAllMessagesAfter(Message mess) {
        ArrayList dejaVisites = new ArrayList();
        ArrayList compte = new ArrayList();
        ArrayList pile = new ArrayList();
        
        XMIObject objetCourant = mess.getDestination();
        pile.add(objetCourant);
        dejaVisites.add(objetCourant);
        while( !pile.isEmpty() ) {
            objetCourant = (XMIObject) pile.remove(0);
            ArrayList messages = getAllMessages(objetCourant, 1);
            for(Iterator it = objets.iterator(); it.hasNext(); ) {
                XMIObject obj = (XMIObject) it.next();
                if(obj.getTypeObjet()!=XMIObject.MESSAGE) continue;
                Message m = (Message) obj;
                if(m.getNumeroSequence()>mess.getNumeroSequence()) {
                    compte.add(m);
                    if(! dejaVisites.contains(m.getDestination()) ){
                        dejaVisites.add(m.getDestination());
                        if(objetCourant!=m.getDestination())
                            pile.add(m.getDestination());
                    }
                }
            }
        }
        return compte;
    }
    
    public void writeCSV(String fic, String pere) {
        try{
            BufferedWriter bw = new BufferedWriter(new FileWriter(fic+"DIAGRAMMES.CSV", true));
            String line = pere+"."+getName()+";"+complexite+";-1.0;"+nbActeurs+";-1";
            bw.newLine();
            bw.write(line, 0, line.length());
            bw.close();
            
        } catch(java.io.IOException exc) {
        }
    }
}
