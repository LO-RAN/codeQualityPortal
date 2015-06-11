/*
 * EventAct.java
 *
 * Created on 2 juin 2004, 10:25
 */

package corp.cpwr.metriquesuml.Objets;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;

/**
 * id INTEGER, id_xmi VARCHAR(100), nom VARCHAR(30), stereotype VARCHAR(30), id_diagramme INTEGER,
 *  diagramme_type INTEGER, type_Event INTEGER, version VARCHAR(10)
 *
 * @author  cwfr-dzysman
 */
public class EventAct extends XMIObject{
    /** Type d'évènement : NOT_DEFINED */
    public static final int NOT_DEFINED = -1;
    /** Type d'évènement : SEND */
    public static final int SEND_EVENT = 0;
    /** Type d'évènement : RECEIVE */
    public static final int RECEIVE_EVENT = 1;
    
    /** Le type de l'event */
    private int typeEvent = EventAct.NOT_DEFINED;
    /**
     * Ce constructeur est private afin d'empêcher la création d'Event sans les
     * informations nécessaires.
     */
    private EventAct() {}
    
    public EventAct(Element e, int dt, Modele m) {
        super(e, XMIObject.EVENT_ACT, m, dt, false);
        typeEvent = FonctionsParser.getPropertyInt(e, "ea_ntype");
    }
}
