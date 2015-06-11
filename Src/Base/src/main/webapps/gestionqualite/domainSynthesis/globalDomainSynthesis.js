Ext.ux.CaqsGlobalDomainSynthesisPanel = function(config) {
    Ext.ux.CaqsGlobalDomainSynthesisPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsGlobalDomainSynthesisPanel constructor


Ext.extend(Ext.ux.CaqsGlobalDomainSynthesisPanel, Ext.Panel, {
    layout:             'column',
    //autoHeight:         true,
    //height:             450,
    autoScroll:         true,
    border:             false,
    kiviatWidth:        0,
    kiviatUrl:          '/DomainSynthesisKiviat.do?',
    domainId:           undefined,
    treemap:            undefined,
    autoWidth:          true,
    kiviatAPCheckBox:   undefined,
    kiviatAPPriorityCB: undefined,
    kiviatAPPriorityCBStore: undefined,
    prioritiesLoaded:   false,
    nb:                 0,
//    anchor:             '100% 60%',
//    hideMode:           'offsets',
    
    reloadKiviat: function() {
        this.nb++;
        var kiviatDiv = this.findById('DomainSynthesisGlobalColumn1');
        if(kiviatDiv != null) {
            var child = undefined;
            if(kiviatDiv.items != undefined) {
                child = kiviatDiv.getComponent('domainImgKiviat');
                if(child != undefined) {
                    kiviatDiv.remove(child);
                }
            }
            var img = '<IMG id="globalDomainSynthesisKiviatImg" src="';
            img += requestContextPath + this.kiviatUrl+'nb='+this.nb;
            img += "&width="+(this.kiviatWidth - 10);
            img += "&height="+parseInt((this.kiviatWidth - 10)*0.5);
            img += '&displayActionPlan='+this.kiviatAPCheckBox.getValue();
            if(this.kiviatAPCheckBox.getValue()) {
                img += '&actionPlanPriority='+this.kiviatAPPriorityCB.getValue()
            }

            img += '" />';
            child = new Ext.Panel({
                border: false,
                id: 'domainImgKiviat',
                html:   img
            });
            kiviatDiv.insert(0, child);
            kiviatDiv.doLayout();
        }
    },

    initComponent : function(){
        Ext.ux.CaqsGlobalDomainSynthesisPanel.superclass.initComponent.call(this);

        this.kiviatAPPriorityCBStore = new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/RetrieveActionPlanPriorities.do',
            fields: ['id', 'lib']
        });

        this.kiviatAPPriorityCBStore.addListener('load', function(store, records, options){
            this.kiviatAPPriorityCB.setValue(records[0].data.id);
        }, this);

        this.kiviatAPPriorityCB = new Ext.form.ComboBox({
            name:       	'idGlobalDomainSynthesisActionPlanPriorityCB',
            id:         	'idGlobalDomainSynthesisActionPlanPriorityCB',
            displayField:	'lib',
            valueField: 	'id',
            hiddenName: 	'id',
            editable:		false,
            disabled:           true,
            store:		this.kiviatAPPriorityCBStore,
            triggerAction:	'all',
            autocomplete:	false,
            anchor:		'99%',
            style:              'margin-left: 5px;',
            mode:      		'local'
        });
        this.kiviatAPPriorityCB.addListener('select', function() {
            this.reloadKiviat();
        }, this);
        
        this.treemap = new Ext.ux.CaqsTreeMapPanel({
            columnWidth:    0.49,
            domainId:       this.domainId
        });
        this.kiviatAPCheckBox = new Ext.form.Checkbox({
            labelSeparator:     '',
            listeners: {
                scope:          this,
                check:          function(cb, checked) {
                    this.kiviatAPPriorityCB.setDisabled(!checked);
                    this.reloadKiviat();
                }
            }
        });
        var config = {
            items:  [
                this.treemap
                ,{//colonne 2
                    id:             'DomainSynthesisGlobalColumn1',
                    columnWidth:    .49,
                    border:         false,
                    listeners:      {
                                    'resize':  function(comp, adjWidth, adjHeight, rawWidth, rawHeight ) {
                                        this.kiviatWidth = adjWidth;
                                        this.reloadKiviat();
                                    },
                                    scope:  this
                    },
                    items: [
                        {
                            id:     'domainImgKiviat',
                            html:   '&nbsp;'
                        }
                        , {
                            id:     'comboBoxKiviat',
                            style:  'margin-left: 5px;',
                            labelWidth: 150,
                            border: false,
                            layout: 'table',
                            layoutConfig: {
                                columns: 3
                            },
                            items:   [
                                {
                                    border: false,
                                    width:  150,
                                    //style:  'margin-right: 5px;',
                                    html:   '<span class="smallerStaticField ux-form-smallerStaticField">'+getI18nResource('caqs.domainsynthese.kiviat.checkActionPlan')+'</span>'
                                }
                                , this.kiviatAPCheckBox
                                , this.kiviatAPPriorityCB
                            ]
                        }
                    ]
                }
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsGlobalDomainSynthesisPanel.superclass.initComponent.call(this);
    },

    refresh: function(domainId) {
        this.domainId = domainId;
        this.kiviatAPPriorityCBStore.load();
        this.reloadKiviat();
        this.treemap.refresh(this.domainId)
    }
});