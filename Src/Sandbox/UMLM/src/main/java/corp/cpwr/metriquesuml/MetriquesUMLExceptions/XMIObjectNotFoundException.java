/*
 * XMIObjectNotFoundException.java
 *
 * Created on 10 juin 2004, 11:46
 */

package corp.cpwr.metriquesuml.MetriquesUMLExceptions;

/**
 *
 * @author  cwfr-dzysman
 */
public class XMIObjectNotFoundException extends java.lang.Throwable{
    
    /** Creates a new instance of XMIObjectNotFoundException */
    public XMIObjectNotFoundException() {
        super("L'objet XMIObject recherché n'existe pas.");
    }
    
}
