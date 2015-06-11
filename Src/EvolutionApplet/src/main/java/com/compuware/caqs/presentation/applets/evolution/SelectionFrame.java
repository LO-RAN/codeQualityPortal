/*
 * SelectionFrame.java
 *
 * Created on 20 août 2004, 14:34
 */
package com.compuware.caqs.presentation.applets.evolution;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;


/**
 *
 * @author cwfr-fdubois
 */
public class SelectionFrame extends javax.swing.JFrame {
    
    private static final long serialVersionUID = -8249287705802329511L;
	
	List<String> mKeys = new ArrayList<String>();
    List<JCheckBox> mCheckboxes = null;
    JCheckBox mAll = new JCheckBox("Tout", true);
    JPanel globalPanel = null;
    
    EntryModifiable mModifiable = null;
    
    /** Creates a new instance of SelectionFrame */
    public SelectionFrame() {
    }

    public void setKeys(List<String> keys) {
        this.mKeys = keys;
        this.globalPanel.removeAll();
        this.globalPanel.add(mAll);
        mCheckboxes = new ArrayList<JCheckBox>();
        Iterator<String> i = this.mKeys.iterator();
        while (i.hasNext()) {
            String key = (String)i.next();
            JCheckBox cb = new JCheckBox(key, true);
            cb.addActionListener(new ActionListener() {
            	public void actionPerformed(ActionEvent ae) {
            		Object o = ae.getSource();
            		if(o instanceof JCheckBox) {
            			JCheckBox check = (JCheckBox)o;
            			String key = check.getText();
            			if(!check.isSelected()) {
            				mAll.setSelected(false);
            				mModifiable.removeEntry(key);
            	        } else {
            	            mModifiable.addEntry(key);
            	        }
            		}
            	}
            });
            mCheckboxes.add(cb);
            this.globalPanel.add(cb);
        }
        this.pack();
        this.globalPanel.paintImmediately(0,0,
        		this.globalPanel.getWidth(), 
        		this.globalPanel.getHeight());
    }
    
    public void setEntryModifiable(EntryModifiable modifiable) {
        mModifiable = modifiable;
    }
    
    public void init() {
        this.globalPanel = new JPanel(new GridLayout(0, 1));
        JScrollPane sp = new JScrollPane(this.globalPanel);
        this.getContentPane().add(sp);
        this.globalPanel.add(mAll);
        mAll.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent ae) {
        		if(!mAll.isSelected()) {
        			deselectAll();
        		} else {
        			selectAll();
        		}
        	}
        });
    }
    
    private void selectAll() {
        Iterator<JCheckBox> i = this.mCheckboxes.iterator();
        while (i.hasNext()) {
        	JCheckBox cb = i.next();
            if (!cb.isSelected()) {
                cb.setSelected(true);
                String key = cb.getText();
                if(key!=null) {
                	this.mModifiable.addEntry(key);
                }
            }
        }
    }
    
    boolean deselectingAll = false;
    private void deselectAll() {
    	deselectingAll = true;
    	Iterator<JCheckBox> i = this.mCheckboxes.iterator();
    	while (i.hasNext()) {
    		JCheckBox cb = i.next();
    		if (cb.isSelected()) {
    			cb.setSelected(false);
    			String key = cb.getText();
    			if(key!=null) {
    				this.mModifiable.removeEntry(key);
    			}
    		}
    	}
    }
    
}
