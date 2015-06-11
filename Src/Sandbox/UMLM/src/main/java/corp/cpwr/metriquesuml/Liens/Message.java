/*
 * Message.java
 *
 * Created on 3 juin 2004, 13:46
 */

package corp.cpwr.metriquesuml.Liens;

import org.jdom.*;
import corp.cpwr.metriquesuml.*;
import corp.cpwr.metriquesuml.Liens.LinkUML;
/**
 * id_client INTEGER, type_client INTEGER, id_supplier INTEGER, type_supplier INTEGER,
 *  synchronisation INTEGER, frequence INTEGER, life_cycle INTEGER,
 *  return_params VARCHAR(50), garde VARCHAR(30), is_return INTEGER, id_diagramme INTEGER
 * @author  cwfr-dzysman
 */
public class Message extends LinkUML {
    /** Synchronisation : Synchronous */
    public static final int SYNCHRONOUS = 1;
    /** Synchronisation : Asynchronous */
    public static final int ASYNCHRONOUS = 2;
    /** Synchronisation : Balking */
    public static final int BALKING = 3;
    /** Synchronisation : Simple */
    public static final int SIMPLE = 4;
    /** Synchronisation : Timeout */
    public static final int TIMEOUT = 5;
    
    /** Life Cycle : none */
    public static final int LIFE_NONE = 6;
    /** Life Cycle : new */
    public static final int LIFE_NEW = 7;
    /** Life Cycle : delete */
    public static final int LIFE_DELETE = 8;
    
    /** Frequency : aperiodic */
    public static final int APERIODIC = 9;
    /** Frequency : periodic */
    public static final int PERIODIC = 10;
    
    /** le type de la synchronisation */
    private int typeSynchronisation = XMIObject.NOT_DEFINED;
    /** le type de la fréquence */
    private int typeFrequence = XMIObject.NOT_DEFINED;
    /** le type du cycle de vie */
    private int lifeCycle = Message.LIFE_NONE;
    /** le type de retour et les paramètres, s'ils sont présents */
    private String returnParams = null;
    /** la condition, s'il y en a une */
    private String garde = null;
    /** la contrainte, s'il y en a une */
    private String contrainte = null;
    /** la valeur de la condition "is return" */
    private boolean isReturn = false;
    private int sequenceNumber = -1;
    
    /** LES METRIQUES **/
    private int clotureTransitive=0;
    private int messagesResultants=0;
    
    private float complexite=-1.0f;
    
    /**
     * Ce constructeur est private afin d'empecher la création de Messages sans
     * les informations nécessaires.
     */
    private Message() {super();}
    
    /**
     * Ce constructeur permet de créer des instances de Message.
     */
    public Message(Element e, int dt, Modele m) {
        super(e, XMIObject.MESSAGE, dt, "sender", "receiver", m, null);
        
        String temp = FonctionsParser.getPropertyString(e, "privatedata1");
        if(temp.equals("Synchronous")) typeSynchronisation = Message.SYNCHRONOUS;
        else if(temp.equals("Asynchronous")) typeSynchronisation = Message.ASYNCHRONOUS;
        else if(temp.equals("Balking")) typeSynchronisation = Message.BALKING;
        else if(temp.equals("Simple")) typeSynchronisation = Message.SIMPLE;
        else if(temp.equals("Timeout")) typeSynchronisation = Message.TIMEOUT;
        
        temp = FonctionsParser.getPropertyString(e, "privatedata3");
        if(temp.equals("Aperiodic")) typeFrequence = Message.APERIODIC;
        else if(temp.equals("Periodic")) typeFrequence = Message.PERIODIC;
        
        temp = FonctionsParser.getPropertyString(e, "subtype");
        if(temp.equals("Delete")) lifeCycle = Message.LIFE_DELETE;
        else if(temp.equals("New")) lifeCycle = Message.LIFE_NEW;
        
        contrainte = FonctionsParser.getPropertyString(e, "constraint");
        returnParams = FonctionsParser.getPropertyString(e, "privatedata2");
        garde = FonctionsParser.getPropertyString(e, "conditional");
        isReturn = (FonctionsParser.getPropertyString(e, "privatedata4").equals("1"));
        sequenceNumber = FonctionsParser.getPropertyInt(e, "seqno");
    }

    public void setClotureTransitive(int c) {
        clotureTransitive = c;
    }
    
    public void setMessagesResultants(int c) {
        messagesResultants = c;
    }
    
    public int getNumeroSequence() { return sequenceNumber;}
    
    public float getComplexite() {
        if(complexite==-1.0f) {
            int total = 0;
            if(!returnParams.equals("")){
                String returnValue = returnParams.substring(returnParams.indexOf('=')+1,returnParams.indexOf(';'));
                if(!returnValue.equals("void")) total++;
            }
            for(int i=0; i<returnParams.length(); i++) {
                if(returnParams.charAt(i)==':')//nouveau paramètre défini
                    total++;
            }
            if(!garde.equals("")) total++;
            if(!contrainte.equals("")) total+=2;
            complexite = ((float)total)/ 4.0f;
            if(clotureTransitive!=0)
                complexite = complexite + ((float)(((float)messagesResultants)/((float)(clotureTransitive))));
            complexite = complexite / 2.0f;
        }
        return complexite;
    }
}
