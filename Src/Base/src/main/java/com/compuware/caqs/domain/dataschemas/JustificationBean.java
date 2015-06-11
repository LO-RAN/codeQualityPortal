/*
 * BaselineBean.java
 *
 * Created on 23 janvier 2004, 11:27
 */

package com.compuware.caqs.domain.dataschemas;

import java.io.Serializable;
import java.sql.Timestamp;

import com.compuware.caqs.presentation.util.StringFormatUtil;
import com.compuware.caqs.util.IDCreator;

/**
 *
 * @author  cwfr-fdubois
 */
public class JustificationBean extends DefinitionBean implements Serializable {
    
    private static final long serialVersionUID = -5364655990207490415L;

	private double note = 0.0;
    
    private String statut = "DEMAND";
    private String user = null;
    
    private JustificationBean linkedJustificatif = null;
    
    /** Creates a new instance of BaselineBean */
    public JustificationBean() {
        this.id = IDCreator.getID();
        this.lib = "";
        this.desc = "";
    }
    
    public JustificationBean(double justNote, String libJust,
            String descJust, String statutJust, String cuserJust) {
    	this.id = IDCreator.getID();
        this.note = justNote;
        this.lib = libJust;
        this.desc = descJust;
        this.statut = statutJust;
        this.user = cuserJust;
    }
    
    /** Creates a new instance of Justificatif */
    public JustificationBean(String idJust, double justNote,
            String libJust, String descJust, String statutJust,
            String cuserJust, Timestamp dinstJust, Timestamp dmajJust) {
        this.id = idJust;
        this.note = justNote;
        this.lib = libJust;
        this.desc = descJust;
        this.statut = statutJust;
        this.user = cuserJust;
        this.dinst = dinstJust;
        this.dmaj = dmajJust;
    }

    /** Creates a new instance of Justificatif */
    public JustificationBean(String idJust,
            String libJust, String descJust, String statutJust,
            String cuserJust, Timestamp dinstJust, Timestamp dmajJust) {
        this.id = idJust;
        this.lib = libJust;
        this.desc = descJust;
        this.statut = statutJust;
        this.user = cuserJust;
        this.dinst = dinstJust;
        this.dmaj = dmajJust;
    }
    
    public void setNote(double note) {
        this.note = note;
    }
    
    public double getNote() {
        return this.note;
    }
    
    public void setStatut(String statut) {
        this.statut = statut;
    }
    
    public String getStatut() {
        return this.statut;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public String getEscapedUser(boolean quote, boolean bracket, boolean doubleQuote) {
    	return StringFormatUtil.escapeString(this.getUser(), quote, bracket, doubleQuote);
	}
	
    public String getUser() {
        return this.user;
    }
    
    public void setLinkedJustificatif(JustificationBean just) {
        this.linkedJustificatif = just;
    }
    
    public JustificationBean getLinkedJustificatif() {
        return this.linkedJustificatif;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[");
        buffer.append(super.toString());
        buffer.append(", NOTE="+this.note);
        buffer.append("]");
        return buffer.toString();
    }

    @Override
    public JustificationBean clone() {
    	JustificationBean jb = new JustificationBean();
		jb.setId(this.getId());
		jb.setDesc(this.getDesc());
		jb.setLib(this.getLib());
		jb.setNote(this.getNote());
		if(this.getLinkedJustificatif()!=null) {
			jb.setLinkedJustificatif(this.getLinkedJustificatif().clone());
		}
		return jb;
    }
}
