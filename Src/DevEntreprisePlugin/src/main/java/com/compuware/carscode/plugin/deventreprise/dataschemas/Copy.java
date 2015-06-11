package com.compuware.carscode.plugin.deventreprise.dataschemas;

import java.util.ArrayList;
import java.util.List;

import com.compuware.carscode.plugin.deventreprise.dao.DaoFactory;
import com.compuware.carscode.plugin.deventreprise.dao.DevEntrepriseDao;
import com.compuware.carscode.plugin.deventreprise.util.CsvReader;
import java.util.Map;

public class Copy extends CobolSource {
	
	public Copy(String name, String collection, CobolPackage cp) {
		super(name, collection, cp);
		this.likeName = "%."+name;
	}
	
	private int nbIO=0;
	public int getNbIO() {
		return nbIO;
	}
	public void setNbIO(int nbIO) {
		this.nbIO = nbIO;
	}
	
	public String getName() {
		return name;
	}
	
	public String getElementType() {
		return "FILE";//"COPY";
	}
	
	public void fillProperties(CsvReader halstead, CsvReader mccabe, CsvReader diagnostic, List<Copy> copies) {
		DevEntrepriseDao dao = DaoFactory.getInstance().getDevPartnerDao();
		this.id = dao.getIdFromDatabase(this.name, this.collection);
		this.variables = dao.getNumericVariablesMore18Digits(this.id, this.getLikeName());
	}
	
	public String toString() {
		StringBuffer bf = new StringBuffer();
		bf.append("  <elt type=\""+this.getElementType()+"\" name=\""+
                this.getCobolPackage().getCobolPackage()+"."+this.name+"\" filepath=\""+
                this.getCobolPackage().getCobolPackagePath()+"/"+this.getElementName()+".CBL\">\n");
		bf.append(super.toStringBuffer());
		if(this.variables.size()>0) {
			List<Variable> l = new ArrayList<Variable>();
			for(Variable v : this.variables) {
				if(v.isMoreThan18Digits()) {
					l.add(v);
				}
			}
			if(!l.isEmpty()) {
				bf.append("    <metric id=\"NUMERIC-VAR-TOO-BIG\" value=\""+l.size()+"\" lines=\"");
				boolean first = true;
				for(Variable v : l) {
					if(!first) {
						bf.append(",");
					}
					bf.append(v.getLine());
					first = false;
				}
				bf.append("\" />\n");
			}
		}
		bf.append("  </elt>\n");
		return bf.toString();
	}

    @Override
    public Map<String, Object> getGlobalParams() {
        Map<String, Object> retour = super.getCobolSourceGlobalParams();
        return retour;
    }
}
