package com.compuware.caqs.JavaStyle;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

public class CallTreeConstruct extends Check{

	public int[] getDefaultTokens() {
		return new int[] {
	            TokenTypes.CLASS_DEF,
	            
	        };
	}

}
