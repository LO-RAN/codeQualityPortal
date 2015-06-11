package com.compuware.caqs.domain.dataschemas;

import com.compuware.caqs.constants.Constants;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ElementLinked extends ElementBean implements Serializable {

    private static final long serialVersionUID = 7965717641251961720L;
    ElementBean father = null;
    List<ElementBean> children = new ArrayList<ElementBean>();
    String linkType = null;

    /**
     * @return Returns the pere.
     */
    public ElementBean getFather() {
        return father;
    }

    /**
     * @param pere The pere to set.
     */
    public void setFather(ElementBean pere) {
        this.father = pere;
    }

    /**
     * Get the child list.
     * @return the children
     */
    public List<ElementBean> getChildren() {
        return children;
    }

    /**
     * Set the child list.
     * @param children the children to set
     */
    public void setChildren(List<ElementBean> children) {
        this.children = children;
    }

    /**
     * Add a new child to the child list.
     * @param child the new child.
     */
    public void addChild(ElementBean child) {
        this.children.add(child);
    }

    public void setLinkType(String l) {
        this.linkType = l;
    }

    public String getLinkType() {
        return this.linkType;
    }

    public boolean isSymbolicLink() {
        return Constants.SYMBOLIC_LINK_TYPE.equals(this.linkType);
    }
}
