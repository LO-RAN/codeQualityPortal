/*
 * CategoryDatasetRow.java
 *
 * Created on 20 août 2004, 11:49
 */
package com.compuware.caqs.presentation.applets.evolution;

import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author cwfr-fdubois
 */
public class CategoryDatasetRow extends CategoryDatasetEntryCollection {
    
    private static final long serialVersionUID = -8354816090314854050L;

	/** Creates a new instance of CategoryDatasetRow */
    public CategoryDatasetRow(Comparable key) {
        super(key, CategoryDatasetEntryCollection.ROW);
    }
    
    protected void addEntry(DefaultCategoryDataset ds, CategoryDatasetEntry categoryDatasetEntry) {
        categoryDatasetEntry.addValueFromRowKey(ds, this.mKey);
    }
    
}
