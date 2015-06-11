package com.compuware.caqs.domain.dataschemas.modelmanager;

import com.compuware.caqs.domain.dataschemas.ElementType;


/**
 * Form bean FormuleForm.
 */

/* include your imports here */
public class IFPUGFormulaForm extends FormuleForm {
    private ElementType elementType;

    public IFPUGFormulaForm() {
        super();
    }

    public ElementType getElementType() {
        return elementType;
    }

    public void setElementType(ElementType elementType) {
        this.elementType = elementType;
    }
} 
