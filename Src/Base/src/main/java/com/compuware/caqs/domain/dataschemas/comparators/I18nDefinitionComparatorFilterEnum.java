/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.comparators;

/**
 * @author cwfr-fdubois
 *
 */
public enum I18nDefinitionComparatorFilterEnum {

	ID,
    LIB,
	DESC,
	COMPL;

    public static I18nDefinitionComparatorFilterEnum fromString(String s) {
        I18nDefinitionComparatorFilterEnum retour = I18nDefinitionComparatorFilterEnum.COMPL;
        if("id".equals(s)) {
            retour = I18nDefinitionComparatorFilterEnum.ID;
        } else if("lib".equals(s)) {
            retour = I18nDefinitionComparatorFilterEnum.LIB;
        } else if("desc".equals(s)) {
            retour = I18nDefinitionComparatorFilterEnum.DESC;
        }
        return retour;
    }
	
}
