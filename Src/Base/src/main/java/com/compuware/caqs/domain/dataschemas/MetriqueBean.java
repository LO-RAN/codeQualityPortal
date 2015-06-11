/*
 * MetriqueBean.java
 *
 * Created on 27 janvier 2004, 15:47
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author  cwfr-fdubois
 */
public class MetriqueBean extends MetriqueDefinitionBean implements Serializable {
    
    private static final long serialVersionUID = -7327881265113446913L;
	
	private double mValbrute = 0.0;
	private List lines = new ArrayList();
    
    /** Creates a new instance of MetriqueBean */
    public MetriqueBean() {
    }
    
    public double getValbrute() {
        return this.mValbrute;
    }
    
    public void setValbrute(double valbrute) {
        this.mValbrute = valbrute;
    }

	/**
	 * @return Returns the lines.
	 */
	public List getLines() {
		return lines;
	}

	/**
	 * @param lines The lines to set.
	 */
	public void setLines(String[] sLines) {
		if (sLines != null) {
			for (int i = 0; i < sLines.length; i++) {
				if (sLines[i] != null && sLines[i].length() > 0) {
					this.lines.add(new MetriqueLineBean(getId(), Integer.parseInt(sLines[i])));
				}
			}
		}
	}
	
	public void addLine(String line) {
		if (line != null && (line.length() > 0)) {
			lines.add(new MetriqueLineBean(getId(), Integer.parseInt(line)));
		}
	}
    
	public String getLinesAsString(char separator, int maxLength) {
		String result = null;
		StringBuffer buffer = new StringBuffer();
		if (lines != null) {
			MetriqueLineBean bean;
			int lastLine = 0;
			Iterator i = lines.iterator();
			while (i.hasNext()) {
				bean = (MetriqueLineBean)i.next();
				if (bean.getLine() != lastLine) {
					buffer.append(bean.getLine());
					if (i.hasNext()) {
						buffer.append(separator);
					}
				}
				lastLine = bean.getLine();
			}
			if (buffer.length() > maxLength) {
				result = buffer.substring(0, buffer.indexOf(""+separator, maxLength - 10));
			}
			else {
				result = buffer.toString();
			}
		}
		return result;
	}
	
}
