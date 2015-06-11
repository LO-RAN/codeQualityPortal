Ext.ux.CaqsModelEditorEditAggregationsWindow = function(config) {
    Ext.ux.CaqsModelEditorEditAggregationsWindow.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ux.CaqsModelEditorEditAggregationsWindow, Ext.Window, {
    layout:             'anchor',
    maximizable:        true,
    modelId:            undefined,
    critId:             undefined,
    bodyBorder:         false,
    border:             true,
    validateBtn:        undefined,
    parentPanel:        undefined,
    height:             600,
    width:              900,
    modal:              true,
    firstLoad:          true,
    autoScroll:         true,
    bodyStyle:          'margin-left: 5px;',
    childrenPanels:     undefined,


    setId: function(idUsa, idCrit) {
        this.modelId = idUsa;
        this.critId = idCrit;
        this.firstLoad = true;
    },

    removeAgregation: function(agregationPanel) {
        if(this.childrenPanels.length>1) {
            Ext.MessageBox.confirm(
                getI18nResource('caqs.confirmDelete'),
                getI18nResource('caqs.modeleditor.modelEdition.editAgregation.confirmRemove'),
                function(btn) {
                    if(btn == 'yes') {
                        this.remove(agregationPanel);
                        for(var i=0; i < this.childrenPanels.length; i++) {
                            if(this.childrenPanels[i]==agregationPanel) {
                                this.childrenPanels.splice(i,1);
                                break;
                            }
                        }
                    }
                }, this);
        } else {
            Ext.MessageBox.alert( '',
                getI18nResource('caqs.modeleditor.modelEdition.editAgregation.cannotRemove'));
        }
    },

    displayRetrievedAgregations: function(response) {
        if(response!=null && response.responseText!=null) {
            var json = Ext.decode(response.responseText);
            if(json) {
                var array = json.elements;
                if(array && array.length>0) {
                    for(var i=0; i<array.length; i++) {
                        var panel = new Ext.ux.CaqsModelEditorEditAggregation({
                            defaultValues:      array[i],
                            parentPanel:        this
                        });
                        this.childrenPanels[this.childrenPanels.length] = panel;
                        this.add(panel);
                    }
                }
                this.doLayout();
            }
        }
    },

    retrieveAgregations: function() {
        for(var i=0; i < this.childrenPanels.length; i++) {
            this.remove(this.childrenPanels[i]);
        }
        this.childrenPanels.length = 0;
        Ext.Ajax.request({
            url:        requestContextPath + '/RetrieveAgregationsForCriterionAndModel.do',
            success:    this.displayRetrievedAgregations,
            scope:      this,
            params: {
                modelId:                this.modelId,
                critId:                 this.critId
            }
        });
    },

    checkElementTypeForAgregation: function(elementType) {
        var retour = true;
        if(this.childrenPanels.length==1 && elementType!='ALL') {
            Ext.MessageBox.alert( '',
                getI18nResource('caqs.modeleditor.modelEdition.editAgregation.oneAgregAllElts'));
            retour = false;
        }
        return retour;
    },

    saveAgregations: function() {
        var agregations = new Array();
        for(var i=0; i < this.childrenPanels.length; i++) {
            agregations[i] = this.childrenPanels[i].getJSONAgregation();
        }
        Ext.Ajax.request({
            url:        requestContextPath + '/SaveAgregationsForCriterionAndModel.do',
            success:    this.displayRetrievedAgregations,
            scope:      this,
            params: {
                modelId:        this.modelId,
                critId:         this.critId,
                agregations:    Ext.util.JSON.encode(agregations)
            }
        });
        this.close();
    },

    onShowWnd: function() {
        this.setPosition(0,0);
        this.retrieveAgregations();
    },

    initComponent: function(){
        this.childrenPanels = new Array();
        var closeBtn = new Ext.Button({
            text:	getI18nResource("caqs.close"),
            icon: 	requestContextPath + '/images/cross.gif',
            cls: 	'x-btn-text-icon',
            width:      80,
            handler:    this.close,
            scope:      this
        });
        this.validateBtn = new Ext.Button({
            text:	getI18nResource("caqs.update"),
            cls:	'x-btn-text-icon',
            icon:	requestContextPath+'/images/database_save.gif',
            width:      80,
            handler:    this.saveAgregations,
            scope:      this
        });
        this.on('beforeclose', function(wnd) {
            this.hide();
            this.parentPanel.reloadGrid();
            return false;
        }, this);

        var config = {
            title:          getI18nResource('caqs.modeleditor.modelEdition.editAgregation.title'),
            buttons: [
                this.validateBtn
                , closeBtn
            ],
            tbar:   [
                new Ext.Toolbar.Button({
                    text:	getI18nResource("caqs.modeleditor.modelEdition.editAgregation.addAgregation"),
                    icon: 	requestContextPath + '/images/add.gif',
                    cls: 	'x-btn-text-icon',
                    width:      80,
                    handler:	function() {
                        this.addAgregation();
                    },
                    scope:      this
                })
            ]
        }
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsModelEditorEditAggregationsWindow.superclass.initComponent.apply(this, arguments);
    },

    addAgregation: function() {
        var panel = new Ext.ux.CaqsModelEditorEditAggregation({
            parentPanel:        this
        });
        this.childrenPanels[this.childrenPanels.length] = panel;
        this.add(panel);
        this.doLayout();
    }
});