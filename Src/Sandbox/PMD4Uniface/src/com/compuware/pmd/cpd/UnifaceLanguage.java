/*
 * @author laurent IZAC laurent.izac@compuware.com
*/
package com.compuware.pmd.cpd;

import net.sourceforge.pmd.cpd.AbstractLanguage;

public class UnifaceLanguage extends AbstractLanguage {
	public UnifaceLanguage() {
		super(new UnifaceTokenizer(), ".uniface");
	}
}
