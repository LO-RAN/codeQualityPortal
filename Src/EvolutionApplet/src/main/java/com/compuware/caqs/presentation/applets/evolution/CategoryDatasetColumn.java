/*
 * CategoryDatasetColumn.java
 *
 * Created on 20 août 2004, 11:49
 */
package com.compuware.caqs.presentation.applets.evolution;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author cwfr-fdubois
 */
public class CategoryDatasetColumn extends CategoryDatasetEntryCollection {
    
    private static final long serialVersionUID = 2523656422526118697L;

	/** Creates a new instance of CategoryDatasetRow */
    public CategoryDatasetColumn(Comparable key) {
        super(key, CategoryDatasetEntryCollection.COLUMN);
    }
    
    protected void addEntry(DefaultCategoryDataset ds, CategoryDatasetEntry categoryDatasetEntry) {
        categoryDatasetEntry.addValueFromColumnKey(ds, this.mKey);
    }
    
}
