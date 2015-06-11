/*
 * PseudoState.java
 *
 * Created on 1 juin 2004, 11:32
 */

package corp.cpwr.metriquesuml.Objets;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;

/**
 * Un PseudoState est un �tat particulier du diagramme d'�tats-transitions. C'est
 * un �tat qui est soit initial, soit final. Ce peut aussi �tre un �tat de
 * synchronisation.
 * id INTEGER, id_xmi VARCHAR(100), nom VARCHAR(30), id_Pere INTEGER,
 *  type_State INTEGER, id_diagramme INTEGER, diagramme_Type INTEGER, version VARCHAR(10)
 * @author  cwfr-dzysman
 */
public class PseudoState extends State{
    
    
    
    /**
     * Ce constructeur est private afin d'empecher la construction de pseudo-�tats
     * dont des informations manqueraient.
     */
    private PseudoState() {}
    
    /**
     * Ce constructeur permet d'instancier un objet de type SimpleState.
     * @param e l'�l�ment dans l'arbre g�n�r� par JDOM repr�sentant l'objet.
     * @param dt le type du diagramme qui contient ce pseudoState.
     */
    public PseudoState(Element e, int dt, Modele m) {
        super(e, XMIObject.PSEUDO_STATE, dt, m);
        
        String ea_stype = FonctionsParser.getPropertyString(e, "ea_stype");
        if(ea_stype!=null) {
            if(ea_stype.equals("StateNode")) {
                String init_final = e.getAttributeValue("kind");
                if(init_final!=null) {
                    if(init_final.equals("initial")) typeState = State.INITIAL;
                    else if(init_final.equals("final")) typeState = State.FINAL;
                }
                else {
                    //c'est un pseudostate mais sans attribut "kind", donc, d'apr�s
                    //entreprise architect, c'est soit un "history", soit un "synchronisation"
                    String s_ntype=null;
                    int ntype = FonctionsParser.getPropertyInt(e, "ea_ntype");
                    switch(ntype) {
                        case 5:
                            typeState = State.HISTORY;
                            break;
                        case 6:
                            typeState = State.SYNCHRONISATION;
                            break;
                    }
                }
            }
            else if(ea_stype.equals("Decision")) {
                typeState = State.DECISION;
            }
            else if(ea_stype.equals("Synchronisation")) typeState = State.SYNCHRONISATION;
        }
        
    }
    
}
