/**
 * 
 */
package com.compuware.caqs.domain.dataschemas.analysis;

/**
 * @author cwfr-fdubois
 *
 */
public class SystemCallResult {

	/** The system call result code. */
	int resultCode = 0;
	
	/** The system call trace. */
	String trace = null;

	/**
	 * @return the resultCode
	 */
	public int getResultCode() {
		return resultCode;
	}

	/**
	 * @param resultCode the resultCode to set
	 */
	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	/**
	 * @return the trace
	 */
	public String getTrace() {
		return trace;
	}

	/**
	 * @param trace the trace to set
	 */
	public void setTrace(String trace) {
		this.trace = trace;
	}

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("Result code=");
        result.append(resultCode);
        if (this.trace != null && trace.length() > 0) {
            result.append(",\nTrace=").append(this.trace);
        }
        return result.toString();
    }
	
}
