package com.compuware.caqs.domain.dataschemas;

import java.util.Locale;

import com.compuware.caqs.domain.dataschemas.i18n.I18nUtil;
import com.compuware.toolbox.util.resources.DbmsResourceBundle;
import com.compuware.toolbox.util.resources.Internationalizable;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 1 dec. 2005
 * Time: 17:08:17
 * To change this template use File | Settings | File Templates.
 */
public class ElementType extends DefinitionBean implements Internationalizable {

    private static final long serialVersionUID = 5636279805666407241L;
    /** Identifie un domaine. */
    public static final String DOMAIN = "DOMAIN";
    /** Identifie un projet. */
    public static final String PRJ = "PRJ";
    /** Identifie un sous-projet. */
    public static final String SSP = "SSP";
    /** Identifie une entite applicative. */
    public static final String EA = "EA";
    /** Identifie une classe. */
    public static final String CLS = "CLS";
    /** Identifie une methode. */
    public static final String MET = "MET";
    /** Identifie le point d'entree */
    public static final String ENTRYPOINT = "ENTRYPOINT";
    /** Identifie n'importe quel type. */
    public static final String ALL = "ALL";
    /** Indentifie un programme cobol */
    public static final String PROGRAM = "FILE";
    /** Indentifie une procedure cobol */
    public static final String PROCEDURE = "PROCEDURE";
    private boolean isFile = false;
    private boolean hasSource = false;

    public ElementType() {
    }

    public ElementType(String id) {
        this.id = id;
    }

    public String getTextKey(String columnName) {
        return this.getBundleName() + Internationalizable.KEY_SEPARATOR +
                columnName + Internationalizable.KEY_SEPARATOR + getId();
    }

    public String getLib(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.LIB_PROPERTY_KEY, loc);
    }

    public String getDesc(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.DESC_PROPERTY_KEY, loc);
    }

    public String getComplement(Locale loc) {
        return DbmsResourceBundle.getString(this, this.getBundleName(), Internationalizable.COMPL_PROPERTY_KEY, loc, "");
    }

    public boolean hasSource() {
        return hasSource;
    }

    public void setHasSource(boolean hasSource) {
        this.hasSource = hasSource;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setIsFile(boolean isFile) {
        this.isFile = isFile;
    }

    public String getBundleName() {
        return I18nUtil.ELEMENT_TYPE_BUNDLE_NAME;
    }
}
