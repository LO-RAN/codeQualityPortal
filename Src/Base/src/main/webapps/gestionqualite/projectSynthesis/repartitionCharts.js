Ext.ux.CaqsProjectSynthesisRepartitionChart = function(config) {
    // call parent constructor
    Ext.ux.CaqsProjectSynthesisRepartitionChart.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsDomainSynthesisRepartitionChart constructor


Ext.extend(Ext.ux.CaqsProjectSynthesisRepartitionChart, Ext.Panel, {
    autoScroll:         true,
    groups:             undefined,
    loadedGroups:       undefined,
    tpl:                undefined,
    idFac:              'ALL_FACTORS',
    border:             false,
    factorCB:           undefined,
    groupsPanel:        undefined,
    parentElement:      undefined,
    layout:             'anchor',
    nbColumns:          2,
    addedImages:        undefined,

    addGroups : function() {
        for(i=0; i<this.addedImages.length; i++) {
            var img = this.addedImages[i];
            img.parentColumn.remove(img);
        }
        this.addedImages.length = 0;
        for(i=0; i<this.groups.length; i++) {
            var elt = this.groups[i];
            var colIndex = (i%this.nbColumns);
            var col = this.groupsPanel.findById('projectRepartitionColumn'+colIndex);
            if(col != undefined) {
                var panel = new Ext.ux.ComponentImage({
                    id:     'imgRep'+elt.idLanguage,
                    parentColumn: col,
                    src:    requestContextPath + '/RepartitionSynthesisChart.do?' + 'idLanguage='+elt.idLanguage + '&libLanguage='+elt.title + '&idFac=ALL_FACTORS'
                });
                col.add(panel);
                this.addedImages[this.addedImages.length] = panel;
            }
        }
        this.groupsPanel.doLayout();
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
    },
 
    refreshImages: function() {
        for(i=0; i<this.groups.length; i++) {
            var idLanguage = this.groups[i].idLanguage;
            var libLangage = this.groups[i].title;
            var img = document.getElementById("imgRep"+idLanguage);
            if(img != undefined) {
                img.src = requestContextPath
                    + '/RepartitionSynthesisChart.do?idLanguage=' + idLanguage
                    + '&idFac=' + this.idFac + '&libLanguage='+ libLangage;
            }
        }
    },

    displayGroups: function(response) {
        if(response.responseText!='' && response.responseText!='[]') {
            this.groups = Ext.util.JSON.decode(response.responseText);
            this.groups = this.groups.dataArray;
            this.addGroups();
        }
    },

    initComponent : function(){
        Ext.ux.CaqsProjectSynthesisRepartitionChart.superclass.initComponent.call(this);
        this.addedImages = new Array();
        var widths = ((100 - this.nbColumns)/this.nbColumns)/100;
        var cols = new Array();
        for(var i = 0; i < this.nbColumns; i++) {
            cols[cols.length] = {
                columnWidth:	widths,
                id:		'projectRepartitionColumn'+i,
                autoScroll:     true,
                border:		false
            };
        }

        this.groupsPanel = new Ext.Panel({
            layout:         'column',
            border:         false,
            autoScroll:     true,
            anchor:         '95% 80%',
            layoutConfig:   {scrollOffset:25},
            items:          cols
        });

        var factorStore = new Ext.ux.CaqsJsonStore({
            url:    requestContextPath+'/ProjectSynthesisRetrieveTreeMapGoalsList.do',
            fields: ['idFac', 'libFac']
        });
        factorStore.addListener('load', this.afterLoadingGoalsCB, this);

        this.factorCB = new Ext.form.ComboBox({
            name:       	'projectRepartitionChartFactorCB',
            id:         	'projectRepartitionChartFactorCB',
            displayField:	'libFac',
            valueField: 	'idFac',
            hiddenName: 	'idFac',
            editable:		false,
            store:		factorStore,
            triggerAction:	'all',
            fieldLabel:         getI18nResource('caqs.domainsynthese.repartition.factors'),
            autocomplete:	false,
            anchor:		'95%',
            mode:      		'local'
        });
        this.factorCB.addEvents({'select' : true});
        this.factorCB.on('select', function(field, record, index) {
            var v = record.data.idFac;
            if(v!='' && v!==this.idFac) {
                this.idFac = v;
                this.refreshImages();
            }
        }, this);
        var fieldset = new Ext.form.FieldSet ({
            title:      getI18nResource('caqs.domainsynthese.repartition.setting'),
            collapsed:  false,
            collapsible:true,
            autoHeight: true,
            autoWidth:  true,
            anchor:     '95%',
            items :     this.factorCB
        });
        this.add(fieldset);
        this.add(this.groupsPanel);
    },

    afterLoadingGoalsCB: function(store, records, options) {
        options.scope.factorCB.setValue(records[0].data.idFac);
        options.scope.idFac = records[0].data.idFac;
        this.refreshGroups();
    },

    refreshGroups: function() {
        Ext.Ajax.request({
            url:		requestContextPath+'/RetrieveProjectLanguageGroupsList.do',
            scope:      this,
            success:	this.displayGroups
        });
    },

    refresh : function(cmpt){
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        this.factorCB.store.load({
            scope:  this,
            add:    false
	});
    }
});