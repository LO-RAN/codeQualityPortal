/**
 * 
 */
package com.compuware.caqs.domain.calculation.rules.aggregation;

import java.util.Properties;

import com.compuware.caqs.domain.calculation.rules.Aggregable;
import com.compuware.caqs.domain.calculation.rules.constants.Constants;
import com.compuware.toolbox.io.PropertiesReader;

/**
 * @author cwfr-fdubois
 *
 */
public class AggregationFactory implements IAggregationFactory {

    protected Properties mProps = null;
    
    public AggregationFactory() {
        mProps = PropertiesReader.getProperties(Constants.AGGREG_CONFIG_FILE_PATH, this);
    }

	public Aggregation create() {
		return create(Aggregable.AVG);
	}
    
	public Aggregation create(String key) {
    	Aggregation result = null;
    	try {
	        String className = mProps.getProperty(key);
	        Class cls = Class.forName(className);
	        result = (Aggregation)cls.newInstance();
    	}
    	catch (ClassNotFoundException e) {
    	}
    	catch (InstantiationException e){
		}
    	catch (IllegalAccessException e) {
		}
        return result;
    }
    
}
