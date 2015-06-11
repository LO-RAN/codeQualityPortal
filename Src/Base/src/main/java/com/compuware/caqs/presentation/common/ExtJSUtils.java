package com.compuware.caqs.presentation.common;

import com.compuware.caqs.domain.dataschemas.ElementType;

public class ExtJSUtils {
	private static final String PRJ_CLS = "prj";
	private static final String DOMAIN_CLS = "domain";
	private static final String EA_CLS = "ea";
	private static final String SSP_CLS = "ssp";
	
	public static String getIconClassForTelt(String telt, boolean isSymbolicType) {
		String retour = "";
		
		if(ElementType.DOMAIN.equals(telt)) {
			retour = ExtJSUtils.DOMAIN_CLS;
		} else if(ElementType.EA.equals(telt)) {
			retour = ExtJSUtils.EA_CLS;
		} else if(ElementType.PRJ.equals(telt)) {
			retour = ExtJSUtils.PRJ_CLS;
		} else if(ElementType.SSP.equals(telt)) {
			retour = ExtJSUtils.SSP_CLS;
		}

        if(isSymbolicType) {
            retour += "-link";
        }
		
		return retour;
	}
	
}
