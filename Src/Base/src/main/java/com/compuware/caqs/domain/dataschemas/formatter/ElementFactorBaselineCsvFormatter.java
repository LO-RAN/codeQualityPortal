/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.formatter;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.struts.util.MessageResources;

import com.compuware.caqs.domain.dataschemas.BaselineBean;
import com.compuware.caqs.domain.dataschemas.ElementFactorBaseline;
import com.compuware.caqs.domain.dataschemas.FactorBean;

/**
 * @author cwfr-fdubois
 *
 */
public class ElementFactorBaselineCsvFormatter implements Formatter {

	private List baselineList = null;
	
	public ElementFactorBaselineCsvFormatter(List baselineList) {
		this.baselineList = baselineList;
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.util.formater.Formater#format(java.util.Collection, java.io.PrintWriter, org.apache.struts.util.MessageResources)
	 */
	public void format(Collection allEltFactBaseline, PrintWriter writer, MessageResources resources, Locale loc) {
		int maxDeep = getMaxDeep(allEltFactBaseline);
		printHeader(maxDeep, writer);
		printBody(allEltFactBaseline, maxDeep, writer);
		writer.flush();
	}
	
	/* (non-Javadoc)
	 * @see com.compuware.caqs.util.formater.Formater#format(java.lang.Object, java.io.PrintWriter, org.apache.struts.util.MessageResources)
	 */
	public void format(Object allEltFactBaseline, PrintWriter writer, MessageResources resources, Locale loc) {
		// Unused
	}
	
	private void printHeader(int maxDeep, PrintWriter writer) {
		writer.write("Nom;");
		for (int i = 0; i < maxDeep; i++) {
			writer.append(';');
		}
		Iterator bIter = baselineList.iterator();
		BaselineBean baselineBean = null;
		while (bIter.hasNext()) {
			baselineBean = (BaselineBean)bIter.next();
			writer.append(baselineBean.getLib());
			writer.append(';');
		}
		writer.println();
	}
	
	private void printBody(Collection allEltFactBaseline, int maxDeep, PrintWriter writer) {
		Iterator eIter = allEltFactBaseline.iterator();
		ElementFactorBaseline elementBean = null;
		Collection factorList = null;
		while (eIter.hasNext()) {
			elementBean = (ElementFactorBaseline)eIter.next();
			factorList = elementBean.getFactorList();
			if (factorList != null) {
				Iterator fIter = factorList.iterator();
				FactorBean fBean = null;
				while (fIter.hasNext()) {
					fBean = (FactorBean)fIter.next();
					printName(elementBean, maxDeep, writer);
					printValues(elementBean, fBean, writer);
					writer.println();
				}
			}			
		}
		writer.println();
	}
	
	private void printName(ElementFactorBaseline elementBean, int maxDeep, PrintWriter writer) {
		int position = elementBean.getPosition() - 1;
		for (int i = 0; i < position; i++) {
			writer.append(';');
		}
		writer.write(elementBean.getLib());
		for (int i = 0; i < maxDeep - position; i++) {
			writer.append(';');
		}
	}	
	
	private void printValues(ElementFactorBaseline elementBean, FactorBean fBean, PrintWriter writer) {
		Iterator i = baselineList.iterator();
		BaselineBean baselineBean = null;
		FactorBean currentFactor = null;
		FactorBean lastFactor = null;
		writer.append(fBean.getLib()).append(';');
		while (i.hasNext()) {
			baselineBean = (BaselineBean)i.next();
			currentFactor = elementBean.getFactor(fBean.getId(), baselineBean.getId());
			if (currentFactor != null) {
				lastFactor = currentFactor;
			}
			if (lastFactor != null) {
				writer.print(lastFactor.getNote());
			}
			writer.append(';');
		}
	}
	
	private int getMaxDeep(Collection allEltFactBaseline) {
		int result = 0;
		Iterator i = allEltFactBaseline.iterator();
		ElementFactorBaseline son = null;
		while (i.hasNext()) {
			son = (ElementFactorBaseline)i.next();
			if (son.getDeep() > result) {
				result = son.getDeep();
			}
		}
		return result;
	}

}
