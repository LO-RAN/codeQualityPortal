package com.compuware.caqs.service;

import java.util.ResourceBundle;

import com.compuware.caqs.business.analysis.StaticAnalysis;
import com.compuware.caqs.business.analysis.StaticAnalysisConfig;
import com.compuware.caqs.domain.dataschemas.AnalysisResult;
import com.compuware.caqs.service.delegationsvc.BusinessFactory;

public class StaticAnalysisSvc {
	private static final StaticAnalysisSvc instance = new StaticAnalysisSvc();
	
	private StaticAnalysisSvc() {
	}
	
	public static StaticAnalysisSvc getInstance() {
		return instance;
	}
	
	public AnalysisResult execute(StaticAnalysisConfig config, ResourceBundle resources) {
        AnalysisResult result = null;
        BusinessFactory factory = BusinessFactory.getInstance();
		StaticAnalysis analysis = (StaticAnalysis)factory.getBean("parser." + config.getToolId());
        analysis.init(config);
        if (analysis != null) {
    		result = analysis.execute(config.getModuleArray(), resources);
        }
        else {
            result = new AnalysisResult();
            result.setSuccess(false);
            result.setMessage("Unknown tool for analysis");
        }
        return result;
	}
	
}
