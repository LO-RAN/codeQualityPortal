package com.compuware.toolbox.io;

import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import com.compuware.toolbox.constants.Constants;

/** Classe utilitare d'accès aux annuaires JNDI.
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 30 nov. 2005
 * Time: 11:11:20
 */
public class JndiReader {

    /** The JndiReader logger. */
    static protected Logger logger = com.compuware.toolbox.util.logging.LoggerManager.getLogger(Constants.LOGGER_NAME);

    /** The java:comp/env prefix for JNDI access. */
    private static final String JAVA_COMP_ENV_PREFIX = "java:comp/env/";
    /** The java: prefix for JNDI access. */
    private static final String JAVA_PREFIX = "java:";
    /** No prefix for JNDI access. */
    private static final String EMPTY_PREFIX = "";

    /** A protected Constructor. */
    protected JndiReader(){}

    /**
     * Recherche la valeur associée à la clé JNDI.
     * Recherche de préférence dans java:comp/env/CLE_JNDI
     * Puis dans java:CLE_JNDI
     * Puis dans CLE_JNDI
     * @param lookupName la clé JNDI.
     * @return la valeur associée à la clé JNDI.
     */
    public static Object getValue(String lookupName){
        Object result = null;
        InitialContext initialContext = null;
        try {
            initialContext = new InitialContext();
            result = getValue(JAVA_COMP_ENV_PREFIX, lookupName, initialContext);
            if (result == null) {
                result = getValue(JAVA_PREFIX, lookupName, initialContext);
            }
            if (result == null) {
                result = getValue(EMPTY_PREFIX, lookupName, initialContext);
            }
        } catch (NamingException ne) {
            result = null;
        }
        return result;
    }

    /** Retrieve the value from a context, a prefix and a lookup name.
     * @param preLookupName the prefix used for JNDI access.
     * @param lookupName the lookup name.
     * @param initialContext the context used for JNDI access.
     * @return the object found if exist, null otherwise.
     */
    private static Object getValue(String preLookupName, String lookupName, InitialContext initialContext) {
        Object objref = null;
        try{
            objref = initialContext.lookup(preLookupName + lookupName);
        } catch(javax.naming.NamingException ne){
            objref = null;
        }
        return objref;
    }

    /**
     * Recherche la valeur associée à la clé JNDI.
     * Recherche de préférence dans java:comp/env/CLE_JNDI
     * Puis dans java:CLE_JNDI
     * Puis dans CLE_JNDI
     * Si aucune valeur n'est trouvée, retourne la valeur par défaut.
     * @param lookupName la clé JNDI.
     * @param defaultValue la valeur par défaut.
     * @return la valeur associée à la clé JNDI.
     */
    public static Object getValue(String lookupName, Object defaultValue){
        Object result = getValue(lookupName);
        if (result == null) {
            result = defaultValue;
        }
        return result;
    }

}
