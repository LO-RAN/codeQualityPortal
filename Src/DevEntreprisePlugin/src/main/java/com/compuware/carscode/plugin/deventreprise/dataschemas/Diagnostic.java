package com.compuware.carscode.plugin.deventreprise.dataschemas;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.compuware.carscode.plugin.deventreprise.util.CsvReader;

public class Diagnostic {
	private Map<String, DiagnosticViolation> violations = null;
	
	public Diagnostic(CsvReader diag, String programName) {
		violations = new HashMap<String, DiagnosticViolation>();
		if(diag!=null) {
			List<Map<String, String>> lines = diag.getAllLinesFor("Program", programName+".CBL");
			if(lines!=null && !lines.isEmpty()) {
				for(Iterator<Map<String, String>> it = lines.iterator(); it.hasNext(); ) {
					Map<String, String> line = it.next();
					String code = line.get("Code");
					if(code!=null) {
						DiagnosticViolation v = this.violations.get(code);
						if(v==null) {
							v = new DiagnosticViolation(code);
							this.violations.put(code, v);
						}
						v.addViolation(line.get("Source_Line"), line.get("File_Line"));
					}
				}
			}
		}
	}
	
	public String toString() {
		StringBuffer retour = new StringBuffer();
		DiagnosticViolation dv = violations.get("W0004");
		if(dv!=null && !dv.getFileLine().isEmpty()) {
			retour.append(dv.toString("UNKNOWN-DATA-NAME"));
			violations.remove("W0004");
		}
		dv = violations.get("E002");
		if(dv!=null && !dv.getFileLine().isEmpty()) {
			retour.append(dv.toString("UNKNOWN-PROC-NAME"));
			violations.remove("E002");
		}
		dv = violations.get("I008");
		if(dv!=null && !dv.getFileLine().isEmpty()) {
			retour.append(dv.toString("GOTO-OUT-OF-PERFORM"));
			violations.remove("I008");
		}
		dv = violations.get("I005");
		if(dv!=null && !dv.getFileLine().isEmpty()) {
			retour.append(dv.toString("INEXECUTABLE-STATEMENT"));
			violations.remove("I005");
		}
		dv = violations.get("I006");
		if(dv!=null && !dv.getFileLine().isEmpty()) {
			retour.append(dv.toString("UNREACHABLE-PARAGRAPH"));
			violations.remove("I006");
		}
		dv = violations.get("I010");
		if(dv!=null && !dv.getFileLine().isEmpty()) {
			dv.addAll(violations.get("I011"));
			dv.addAll(violations.get("I012"));
			retour.append(dv.toString("PARAGRAPH-FALLS-THROUGH"));
			violations.remove("I010");
		}
		for(Iterator<Map.Entry<String, DiagnosticViolation>> it = this.violations.entrySet().iterator(); it.hasNext(); ) {
			Map.Entry<String, DiagnosticViolation> violation = it.next();
			dv = violation.getValue();
			if(dv!=null && !dv.getFileLine().isEmpty()) {
				retour.append(dv.toString(violation.getKey()));
			}
		}
		
		return retour.toString();
	}
}