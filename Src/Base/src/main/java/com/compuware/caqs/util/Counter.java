/**
 * 
 */
package com.compuware.caqs.util;

/**
 * @author cwfr-fdubois
 *
 */
public class Counter {

	private int value = 0;
	
	public int getValue() {
		return value;
	}
	
	public void inc() {
		this.value++;
	}
	
	public void inc(int n) {
		this.value += n;
	}
	
	public void dec() {
		this.value--;
	}
}
