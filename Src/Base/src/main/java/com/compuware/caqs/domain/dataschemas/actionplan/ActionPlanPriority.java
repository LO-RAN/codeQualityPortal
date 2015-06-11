package com.compuware.caqs.domain.dataschemas.actionplan;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import com.compuware.caqs.presentation.util.RequestUtil;

public enum ActionPlanPriority {
	UNDEFINED(0),
        SHORT_TERM(1),
	MEDIUM_TERM(2),
	LONG_TERM(3);
        
        private int priority = 0;

        private ActionPlanPriority(int p) {
            this.priority = p;
        }
	
	public String toString(HttpServletRequest request) {
		ResourceBundle resources = RequestUtil.getCaqsResourceBundle(request);
		String msg = resources.getString("caqs.actionplan.priority."+this.toString());
		return msg;
	}

        public boolean hasSameOrMorePriority(ActionPlanPriority p) {
            return (this.priority <= p.priority);
        }

        public boolean equals(ActionPlanPriority p) {
            return (this.priority==p.priority);
        }

        public static ActionPlanPriority[] getValidValues() {
            return new ActionPlanPriority[]{
                SHORT_TERM,
                MEDIUM_TERM,
                LONG_TERM
            };
        }
}
