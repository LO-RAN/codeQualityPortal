package com.compuware.carscode.plugin.systemcode;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 23 févr. 2006
 * Time: 12:38:43
 * To change this template use File | Settings | File Templates.
 */
public class SystemCodeRuleBean {
    private String id;
    private int value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id.trim();
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
