/*
 * CategoryDatasetEntry.java
 *
 * Created on 20 août 2004, 11:34
 */
package com.compuware.caqs.presentation.applets.evolution;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author  cwfr-fdubois
 */
public class CategoryDatasetEntry {
    
    private Comparable mKey;
    
    private Number mValue;
    
    /** Creates a new instance of CategoryDatasetEntry */
    public CategoryDatasetEntry(Comparable key, Number value) {
        this.mKey = key;
        this.mValue = value;
    }
    
    public void addValueFromRowKey(DefaultCategoryDataset ds, Comparable rowKey) {
        ds.addValue(this.mValue, rowKey, this.mKey);
    }
    
    public void addValueFromColumnKey(DefaultCategoryDataset ds, Comparable columnKey) {
        ds.addValue(this.mValue, this.mKey, columnKey);
    }
    
}
