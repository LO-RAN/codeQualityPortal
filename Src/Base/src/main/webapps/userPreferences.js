Ext.ux.CaqsPreferencesPanel = function(config) {
    Ext.ux.CaqsPreferencesPanel.superclass.constructor.call(this,  config);
}; // end of Ext.ux.CaqsPreferencesPanel constructor


Ext.extend(Ext.ux.CaqsPreferencesPanel, Ext.form.FormPanel, {
    themeCB:                    undefined,
    messagesLocationCB:         undefined,
    startingPageCB:             undefined,
    dashboardGlobalBaselinesCheck:undefined,
    dashboardAccessConnectionCheck:undefined,
    dashboardConnectionCheck:   undefined,
    dashboardDefaultDomainCB:   undefined,
    updateButtonPreferences:    undefined,
    nbDaysAnalysisWarningTF:    undefined,
    hideMode:                   'offsets',
    themesStore:                undefined,
    msgLocStore:                undefined,
    startingPageStore:          undefined,
    dashboardDefaultDomainStore:undefined,
    dashboardDefaultDomain:     undefined,
    lazyLoaded:                 false,
    nbLoadedValues:             0,
    maxValuesToLoad:            5,
    panelMask:                  undefined,
    
    initComponent : function(){
        Ext.ux.CaqsPreferencesPanel.superclass.initComponent.call(this);
        this.updateButtonPreferences = new Ext.Button({
            text:       getI18nResource("caqs.update"),
            name:       'caqs.admin.update',
            handler:    this.updateFunctionPreferences,
            cls:	'x-btn-text-icon',
            icon:	requestContextPath+'/images/database_save.gif',
            scope:      this
        });

        this.themesStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/GetPreferencesListAction.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root:           'prefs',
                totalProperty:  'totalCount',
                id:             'idSetting',
                fields: [
                'idSetting', 'valueSetting', 'defaultValue'
                ]
            })
        });
        this.themesStore.on('beforeload', this.beforeLoadingThemes, this);
        this.themesStore.on('load', this.afterLoadingThemes, this);
        this.themeCB = new Ext.form.ComboBox({
            xtype     : 		'combo',
            fieldLabel: 		getI18nResource("caqs.userPreferencesTheme"),
            name:       		'idThemeCB',
            id:         		'idThemeCB',
            store:      		this.themesStore,
            displayField:		'valueSetting',
            valueField: 		'idSetting',
            hiddenName: 		'idTheme',
            width:			220,
            editable:			false,
            forceSelection:		true,
            triggerAction:		'all',
            autocomplete:		false
        });

        this.msgLocStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/GetPreferencesListAction.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root:           'prefs',
                totalProperty:  'totalCount',
                id:             'idSetting',
                fields: [
                'idSetting', 'valueSetting', 'defaultValue'
                ]
            }),
            remoteSort: true
        });
        this.msgLocStore.on('beforeload', this.beforeLoadingMsgLoc, this);
        this.msgLocStore.on('load', this.afterLoadingMsgLoc, this);
        this.messagesLocationCB = new Ext.form.ComboBox({
            xtype     : 		'combo',
            fieldLabel: 		getI18nResource("caqs.userPreferences.msgLoc"),
            name:       		'idMsgLocCB',
            id:         		'idMsgLocCB',
            store:      		this.msgLocStore,
            displayField:		'valueSetting',
            valueField: 		'idSetting',
            hiddenName: 		'idMsgLoc',
            width:			220,
            editable:			false,
            forceSelection:		true,
            triggerAction:		'all',
            autocomplete:		false
        });

        this.startingPageStore = new Ext.data.Store({
            proxy: new Ext.data.HttpProxy({
                url: requestContextPath+'/GetPreferencesListAction.do'
            }),

            // create reader that reads the Topic records
            reader: new Ext.data.JsonReader({
                root:           'prefs',
                totalProperty:  'totalCount',
                id:             'idSetting',
                fields: [
                'idSetting', 'valueSetting', 'defaultValue'
                ]
            }),
            remoteSort: true
        });
        this.startingPageStore.on('beforeload', this.beforeLoadingStartingPage, this);
        this.startingPageStore.on('load', this.afterLoadingStartingPage, this);
        this.startingPageCB = new Ext.form.ComboBox({
            xtype     : 		'combo',
            fieldLabel: 		getI18nResource("caqs.userPreferences.startingPage"),
            name:       		'idStartingPageCB',
            id:         		'idStartingPageCB',
            store:      		this.startingPageStore,
            displayField:		'valueSetting',
            valueField: 		'idSetting',
            hiddenName: 		'idStartingPage',
            width:			220,
            editable:			false,
            forceSelection:		true,
            triggerAction:		'all',
            autocomplete:		false
        });

        var dashboardItems = new Array();


        this.dashboardDefaultDomainStore = new Ext.ux.CaqsJsonStore({
            url: requestContextPath + '/RetrieveDomainsListForUser.do',
            fields: ['id', 'lib']
        });
        this.dashboardDefaultDomainStore.on('load', this.afterLoadingDefaultDomain, this);
        this.dashboardDefaultDomainCB = new Ext.form.ComboBox({
            xtype     : 	'combo',
            fieldLabel: 	getI18nResource("caqs.userPreferences.dashboardDefaultDomain"),
            name:       	'idDashboardDefaultDomainCB',
            id:         	'idDashboardDefaultDomainCB',
            store:      	this.dashboardDefaultDomainStore,
            displayField:	'lib',
            valueField: 	'id',
            hiddenName: 	'idDashboardDefaultDomain',
            width:		420,
            editable:		false,
            forceSelection:	true,
            triggerAction:	'all',
            autocomplete:	false,
            mode:      		'local'
        });
        dashboardItems[dashboardItems.length] = this.dashboardDefaultDomainCB;

        this.nbDaysAnalysisWarningTF = new Ext.form.NumberField({
            allowDecimals:      false,
            allowNegative:      false,
            fieldLabel:         getI18nResource("caqs.userPreferences.nbDaysAnalysisWarning"),
            labelWidth:         300
        });
        dashboardItems[dashboardItems.length] = this.nbDaysAnalysisWarningTF;

        this.dashboardGlobalBaselinesCheck = new Ext.form.Checkbox({
            allowDecimals:      false,
            allowNegative:      false,
            fieldLabel:         getI18nResource("caqs.userPreferences.displayGlobalBaselines"),
            labelWidth:         300
        });
        dashboardItems[dashboardItems.length] = this.dashboardGlobalBaselinesCheck;

        if(this.dashboardAccessConnectionCheck) {
            this.dashboardConnectionCheck = new Ext.form.Checkbox({
                allowDecimals:      false,
                allowNegative:      false,
                fieldLabel:         getI18nResource("caqs.userPreferences.displayConnections"),
                labelWidth:         300
            });
            dashboardItems[dashboardItems.length] = this.dashboardConnectionCheck;
        }

        var config = {
            id:                 'userPreferencesFormPanel',
            border:		false,
            bodyStyle:		'padding-left:5px; padding-top:5px; padding-right:5px;',
            labelWidth:		250,
            items: [
            new Ext.form.FieldSet({
                title:      getI18nResource("caqs.userPreferencesTheme"),
                autoHeight: true,
                items: [
                this.themeCB
                ]
            }),
            new Ext.form.FieldSet({
                title:      getI18nResource("caqs.userPreferences.ihmTitle"),
                autoHeight: true,
                items: [
                this.messagesLocationCB,
                this.startingPageCB
                ]
            }),
            new Ext.form.FieldSet({
                title:      getI18nResource("caqsPortal.dashboard"),
                autoHeight: true,
                items:      dashboardItems
            })
            ],
            buttons: [
            this.updateButtonPreferences
            ]
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsPreferencesPanel.superclass.initComponent.call(this);
        this.on('render', function() {
            this.panelMask = new Ext.LoadMask(Ext.getBody());
        }, this);
    },

    showMask: function() {
        if(this.panelMask) {
            this.panelMask.show();
        }
    },

    hideMask: function() {
        if(this.panelMask) {
            this.panelMask.hide();
        }
    },

    testLazyLoadingFinished: function() {
        if(this.nbLoadedValues==this.maxValuesToLoad) {
            this.hideMask();
        }
    },

    lazyLoad: function() {
        Caqs.Portal.setCurrentScreen('userpreferences');
        if(!this.lazyLoaded) {
            this.showMask();
            this.themesStore.load();
            this.msgLocStore.load();
            this.startingPageStore.load();
            this.dashboardDefaultDomainStore.load();
            this.loadNonListPreferences();
            this.lazyLoaded = true;
        }
    },

    loadNonListPreferences: function() {
        Ext.Ajax.request({
            url:	requestContextPath+'/UserPreferencesNonListLoad.do',
            success:	this.setNonListPreferences,
            scope:      this
        });
    },

    setNonListPreferences: function(response) {
        if(response.responseText!='' && response.responseText != []) {
            var values = Ext.util.JSON.decode(response.responseText);
            this.nbDaysAnalysisWarningTF.setValue(values.nbDaysWarning);
            this.dashboardGlobalBaselinesCheck.setValue(values.displayGlobalBaselines);
            if(this.dashboardConnectionCheck) {
                this.dashboardConnectionCheck.setValue(values.displayConnections);
            }
        }
        this.nbLoadedValues++;
        this.testLazyLoadingFinished();
    },

    beforeLoadingThemes: function(store, options) {
        store.baseParams.settingId = 'COLOR_THEME';
    },

    beforeLoadingMsgLoc: function(store, options) {
        store.baseParams.settingId = 'MESSAGE_LOCATION';
    },

    beforeLoadingStartingPage: function(store, options) {
        store.baseParams.settingId = 'STARTING_PAGE';
    },

    afterLoadingThemes: function(store, records, options) {
        this.themeCB.setValue(records[0].data.defaultValue);
        this.nbLoadedValues++;
        this.testLazyLoadingFinished();
    }, 

    afterLoadingMsgLoc: function(store, records, options) {
        this.messagesLocationCB.setValue(records[0].data.defaultValue);
        this.nbLoadedValues++;
        this.testLazyLoadingFinished();
    },

    afterLoadingStartingPage: function(store, records, options) {
        this.startingPageCB.setValue(records[0].data.defaultValue);
        this.nbLoadedValues++;
        this.testLazyLoadingFinished();
    },


    afterLoadingDefaultDomain: function(store, records, options) {
        this.dashboardDefaultDomainCB.setValue(this.dashboardDefaultDomain);
        this.nbLoadedValues++;
        this.testLazyLoadingFinished();
    },

    retrieveResponseFromSaving: function(response) {
        if(response.responseText!=''
            && response.responseText!='[]') {
            var retour = Ext.util.JSON.decode(response.responseText);
            var newTheme = retour.newTheme;
            if(newTheme) {
                Ext.util.CSS.swapStyleSheet("extJSTheme", requestContextPath+'/ext/resources/css/x'+newTheme+'.css');
            }
            if(retour.success) {
                Ext.Msg.alert('', getI18nResource("caqs.userPreferences.saveDone"));
            }
        }
    },

    submitFormPreferences: function() {
        Ext.Ajax.request({
            url:		requestContextPath+'/UserPreferencesSave.do',
            params:	{
                'idTheme' :                 this.themeCB.getValue(),
                'msgLoc' :                  this.messagesLocationCB.getValue(),
                'startingPage' :            this.startingPageCB.getValue(),
                'nbDaysAnalysisWarning' :   this.nbDaysAnalysisWarningTF.getValue(),
                'displayConnections':       (this.dashboardConnectionCheck)?this.dashboardConnectionCheck.getValue():'false',
                'displayGlobalBaselines':   this.dashboardGlobalBaselinesCheck.getValue(),
                'dashboardDefaultCB':       this.dashboardDefaultDomainCB.getValue()
            },
            success:	this.retrieveResponseFromSaving,
            scope:      this
        });
    },

    updateFunctionPreferences: function() {
        this.submitFormPreferences();
    }
});
