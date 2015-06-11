/*
 * CategoryDatasetEntryCollection.java
 *
 * Created on 20 août 2004, 11:41
 */
package com.compuware.caqs.presentation.applets.evolution;

import java.util.ArrayList;
import java.util.Iterator;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author  cwfr-fdubois
 */
public abstract class CategoryDatasetEntryCollection extends ArrayList {
    
    public static final String ROW = "ROW";
    public static final String COLUMN = "COLUMN";
    
    private String mType;
    
    protected Comparable mKey;
    
    /** Creates a new instance of CategoryDatasetEntryCollection */
    public CategoryDatasetEntryCollection(Comparable key, String type) {
        this.mKey = key;
        this.mType = type;
    }
    
    public void addCollection(DefaultCategoryDataset ds) {
        Iterator i = this.iterator();
        while (i.hasNext()) {
            CategoryDatasetEntry entry = (CategoryDatasetEntry)i.next();
            addEntry(ds, entry);
        }
    }
    
    public String getType() {
    	return this.mType;
    }
    
    protected abstract void addEntry(DefaultCategoryDataset ds, CategoryDatasetEntry entry);
    
}
