/* Generated By:JJTree: Do not edit this line. ASTDoctypeExternalId.java */

package com.compuware.caqs.pmd.jsp.ast;

public class ASTDoctypeExternalId extends SimpleNode {

/* BEGIN CUSTOM CODE */

    /**
     * URI of the external entity. Cannot be null.
     */
    private String uri;

    /**
     * Public ID of the external entity. This is optional.
     */
    private String publicId;

    public boolean isHasPublicId() {
        return (null != publicId);
    }

    /**
     * @return Returns the name.
     */
    public String getUri() {
        return uri;
    }

    /**
     * @param name The name to set.
     */
    public void setUri(String name) {
        this.uri = name;
    }

    /**
     * @return Returns the publicId (or an empty string if there is none
     *         for this external entity id).
     */
    public String getPublicId() {
        return (null == publicId ? "" : publicId);
    }

    /**
     * @param publicId The publicId to set.
     */
    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    /* (non-Javadoc)
     * @see com.applicationengineers.pmd4jsp.ast.SimpleNode#toString(java.lang.String)
     */
    public String toString(String prefix) {
        return
                super.toString(prefix)
                + " uri=[" + uri + "] "
                + (null == publicId ? "" : "publicId=[" + publicId + "] ");
    }
	
/* END CUSTOM CODE */


    public ASTDoctypeExternalId(int id) {
        super(id);
    }

    public ASTDoctypeExternalId(JspParser p, int id) {
        super(p, id);
    }


    /**
     * Accept the visitor. *
     */
    public Object jjtAccept(JspParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }
}
