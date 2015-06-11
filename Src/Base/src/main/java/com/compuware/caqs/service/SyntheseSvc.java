package com.compuware.caqs.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.compuware.caqs.business.consult.Synthese;
import com.compuware.caqs.domain.dataschemas.ElementBean;
import com.compuware.caqs.domain.dataschemas.FactorBean;
import com.compuware.caqs.domain.dataschemas.calcul.Volumetry;
import com.compuware.caqs.domain.dataschemas.graph.GraphImageConfig;

public class SyntheseSvc {

    private static final SyntheseSvc instance = new SyntheseSvc();

    private SyntheseSvc() {
    }

    public static SyntheseSvc getInstance() {
        return instance;
    }
    private Synthese synthese = new Synthese();

    public List<FactorBean> retrieveKiviat(ElementBean eltBean) {
        return synthese.retrieveKiviat(eltBean);
    }

    public List<FactorBean> retrieveKiviat(ElementBean eltBean, String idBline) {
        return synthese.retrieveKiviat(eltBean, idBline);
    }

    public File retrieveKiviatImage(ElementBean eltBean, GraphImageConfig imgConfig) throws IOException {
        return synthese.retrieveKiviatImage(eltBean, imgConfig);
    }

    public File retrieveKiviatImage(ElementBean eltBean, String idBline, GraphImageConfig imgConfig) throws IOException {
        return synthese.retrieveKiviatImage(eltBean, idBline, imgConfig);
    }

    public File retrieveDashboardKiviatImage(ElementBean eltBean, String idBline, GraphImageConfig imgConfig) throws IOException {
        return synthese.retrieveDashboardKiviatImage(eltBean, idBline, imgConfig);
    }

    public Map<String, Double> retrieveVolumetryMetrics(ElementBean eltBean, String idBline) {
        return synthese.retrieveVolumetryMetrics(eltBean, idBline);
    }

    public List<Volumetry> retrieveVolumetry(ElementBean eltBean) {
        return synthese.retrieveVolumetry(eltBean);
    }
}
