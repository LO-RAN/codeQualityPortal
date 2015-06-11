/*
 * TransitionET.java
 *
 * Created on 1 juin 2004, 14:39
 */

package corp.cpwr.metriquesuml.Liens;

import corp.cpwr.metriquesuml.*;
import org.jdom.*;

/**
 * id INTEGER, id_xmi VARCHAR(100), nom VARCHAR(30), id_Source INTEGER, type_Source INTEGER, 
 *   id_Destination INTEGER, type_Destination INTEGER, id_diagramme INTEGER, 
 *   diagramme_Type INTEGER, version VARCHAR(10)
 * @author  cwfr-dzysman
 */
public class Transition extends LinkUML{
    /** La garde de la transition, s'il y en a une.*/
    private String garde = null;
    
    /**
     * Ce constructeur est private afin d'empecher la création de transitions sans
     * les informations nécessaires.
     */
    private Transition() { }
    
    /**
     * Ce constructeur permet la création de la transition avec toutes les informations
     * nécessaires.
     * @param e L'élément définissant, dans l'arbre généré par JDOM, la transition.
     */
    public Transition(Element e, int dt, Modele m) {
        super(e, XMIObject.TRANSITION, dt, "source", "target", m, null);
        //la garde semble être stockée, avec Entreprise Architect 3.60, dans un champ
        //TaggedValue dont le tag est privatedata1
        garde = FonctionsParser.getPropertyString(e, "privatedata1");
    }
}
