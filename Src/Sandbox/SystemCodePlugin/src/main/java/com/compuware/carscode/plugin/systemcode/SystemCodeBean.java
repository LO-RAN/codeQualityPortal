package com.compuware.carscode.plugin.systemcode;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by IntelliJ IDEA.
 * User: cwfr-fdubois
 * Date: 23 févr. 2006
 * Time: 12:36:25
 * To change this template use File | Settings | File Templates.
 */
public class SystemCodeBean {
    private String id;
    private String desc;
    private HashMap rules = new HashMap();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc.trim();
    }

    public HashMap getRules() {
        return rules;
    }

    public void addRule(SystemCodeRuleBean rule) {
        SystemCodeRuleBean existingRule = (SystemCodeRuleBean)this.rules.get(rule.getId());
        if (existingRule != null) {
            existingRule.setValue(existingRule.getValue() + 1);
        }
        else {
            this.rules.put(rule.getId(), rule);
        }
    }

    private static final String CSV_SEPARATOR = ";";

    public String toCsvLine(Collection ruleList) {
        StringBuffer result = new StringBuffer();
        result.append(getDesc()).append(CSV_SEPARATOR);
        Iterator i = ruleList.iterator();
        while (i.hasNext()) {
            String idRule = (String)i.next();
            SystemCodeRuleBean ruleBean = (SystemCodeRuleBean)rules.get(idRule);
            int value = 0;
            if (ruleBean != null) {
                value = ruleBean.getValue();
            }
            result.append(value).append(CSV_SEPARATOR);
        }
        if (result.length() > 0) {
            result.append("\n");
        }
        return result.toString();
    }
}
