package com.compuware.carscode.migration.bean;


public class ClasseBean extends ElementBean {

	public String getPackageDesc() {
		String result = null;
		if (this.getDesc() != null && this.getDesc().lastIndexOf('.') > 0) {
			result = this.getDesc().substring(0, this.getDesc().lastIndexOf('.'));
		}
		return result;
	}
	
}
