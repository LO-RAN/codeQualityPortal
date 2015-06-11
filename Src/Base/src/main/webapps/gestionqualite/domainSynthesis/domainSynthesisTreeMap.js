
Ext.ux.CaqsTreeMapPanel = function(config) {
    // call parent constructor
    Ext.ux.CaqsTreeMapPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsTreeMapPanel, Ext.Panel, {
    layout:             'anchor',
    treemap:            undefined,
    domainId:           undefined,
    border:             false,
    tmmCB:              undefined,
    couleurCB:          undefined,
    selectedTMM:        undefined,
    selectedColor:      undefined,
    tmOneLevelCB:       undefined,
    lastLoadedJSONResponse:undefined,

    treeMapGoUp:        function() {
        if(this.treemap != undefined) {
            this.treemap.out();
        }
    },

    initComponent : function(){
        Ext.ux.CaqsTreeMapPanel.superclass.initComponent.call(this);

        var tmmStore = new Ext.ux.CaqsJsonStore({
            url:    requestContextPath+'/RetrieveTreeMapMetrics.do',
            fields: ['idTMM', 'libTMM']
        });
        tmmStore.addListener('load', this.afterLoadingTreeMapMetrics);

        this.tmmCB = new Ext.form.ComboBox({
            name:       	'projectTmmCB',
            id:         	'projectTmmCB',
            displayField:	'libTMM',
            valueField: 	'idTMM',
            hiddenName: 	'idTMM',
            editable:		false,
            store:		tmmStore,
            triggerAction:	'all',
            fieldLabel:         getI18nResource('caqs.domainsynthese.treemap.metrics'),
            autocomplete:	false,
            anchor:		'95%',
            mode:      		'local'
        });

        this.tmmCB.addEvents({
            'select' : true
        });
        this.tmmCB.on('select', function(field, record, index) {
            var v = record.data.idTMM;
            if(v!='' && v!==this.selectedTMM) {
                this.selectedTMM = v;
                this.askValues();
            }
        }, this);

        var couleurStore = new Ext.ux.CaqsJsonStore({
            url:    requestContextPath+'/RetrieveTreeGoalsList.do',
            fields: ['idFac', 'libFac']
        });
        couleurStore.addListener('load', this.afterLoadingGoalsCB);
        couleurStore.on('beforeload', function() {
            couleurStore.baseParams.idDomain = this.domainId;
        }, this);

        this.couleurCB = new Ext.form.ComboBox({
            name:       	'projectCouleurCB',
            id:         	'projectCouleurCB',
            displayField:	'libFac',
            valueField: 	'idFac',
            hiddenName: 	'idFac',
            editable:		false,
            store:		couleurStore,
            triggerAction:	'all',
            fieldLabel:         getI18nResource('caqs.domainsynthese.treemap.factors'),
            autocomplete:	false,
            anchor:		'95%',
            mode:      		'local'
        });
        this.couleurCB.addEvents({
            'select' : true
        });
        this.couleurCB.on('select', function(field, record, index) {
            var v = record.data.idFac;
            if(v!='' && v!==this.selectedColor) {
                this.selectedColor = v;
                this.askValues();
            }
        }, this);
        var fieldset = new Ext.form.FieldSet ({
            title:      getI18nResource('caqs.domainsynthese.treemap.setting'),
            collapsed:  true,
            collapsible:true,
            autoHeight: true,
            anchor:     '100%',
            items : [
            this.tmmCB,
            this.couleurCB
            ]
        });

        this.add(fieldset);
        this.add(new Ext.Panel({
            border:     false,
            html:       '<div style="margin-top:10px; text-align:center;"><div class="infovis" id="domainInfovis"></div></div>'
        }));
        this.tmOneLevelCB = new Ext.form.Checkbox({
            style:          'margin-top: 5px;',
            boxLabel:       getI18nResource('caqs.domainsynthese.treemap.displayOneLevel'),
            listeners: {
                scope:          this,
                check:          function(cb, checked) {
                    this.initTreeMap(this.lastLoadedJSONResponse);
                }
            }
        });
        this.add(this.tmOneLevelCB);
    },

    // private
    refresh : function(domainId){
        this.domainId = domainId;
        this.tmmCB.store.load({
            scope:  this,
            add:    false
        });
    },

    loadFromResponse: function(response) {
        var rs = response.responseText;
        if(rs==null || rs=='[]') {
            rs = '{\
                        "scoreLabel" : "",\
                        "data": {$color:"", $area:""},\
                        "valueLabel":"",\
                        "name":"'+getI18nResource('caqs.domainsynthese.treemap.nodata')+'",\
                        "children":[],	"id":""}';
        }
        this.lastLoadedJSONResponse = rs;
        this.initTreeMap(rs);
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
    },

    askValues: function() {
        var tmmId = this.selectedTMM;
        var color = this.selectedColor;
        Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
        Ext.Ajax.request({
            url:        requestContextPath+'/DomainTreeMap.do?domainId='
            + this.domainId + '&met='+tmmId + '&idFac='+color,
            scope:      this,
            success:    this.loadFromResponse
        });
    },

    afterLoadingGoalsCB: function(store, records, options) {
        options.scope.couleurCB.setValue(records[0].data.idFac);
        options.scope.selectedColor = records[0].data.idFac;
        options.scope.askValues();
    },

    afterLoadingTreeMapMetrics: function(store, records, options) {
        options.scope.tmmCB.setValue(records[0].data.idTMM);
        options.scope.selectedTMM = records[0].data.idTMM;
        options.scope.couleurCB.store.load({
            scope:  options.scope,
            add:    false
        });
    },

    initTreeMap : function(response) {
        TM.Squarified.implement({
            'setColor': function(json) {
                var x = json.data.$color;
                var newColor = new Array();
                if(x < 2.0) {
                    newColor[0] = 255;
                    newColor[1] = 0;
                    newColor[2] = 0;
                } else if(x < 3.0) {
                    newColor[0] = 252;
                    newColor[1] = 171;
                    newColor[2] = 3;
                } else if(x < 4.0) {
                    newColor[0] = 0;
                    newColor[1] = 128;
                    newColor[2] = 0;
                } else {
                    newColor[0] = 0;
                    newColor[1] = 255;
                    newColor[2] = 0;
                }
                return rgbToHex(newColor);
            }
        });

        //var localJSON = Ext.util.JSON.decode(response);
        var subtree = Ext.util.JSON.decode(response);

        var checked = this.tmOneLevelCB.getValue();
        var lvlToShow = (checked)?1:null;

        var tm = new TM.Squarified({
            rootId: 'domainInfovis',
            //Add click handlers for
            //zooming the Treemap in and out
            addLeftClickHandler: true,
            addRightClickHandler: true,

            //When hovering a node highlight the nodes
            //between the root node and the hovered node. This
            //is done by adding the 'in-path' CSS class to each node.
            selectPathOnHover: true,

            levelsToShow: lvlToShow,

            Color: {
                //Allow coloring
                allow: true,
                //Set min value and max value constraints
                //for the *$color* property value.
                //Default's to -100 and 100.
                minValue: 1,
                maxValue: 4
            },

            //Allow tips
            Tips: {
                allow:    true,
                //add positioning offsets
                offsetX:  20,
                offsetY:  20,
                //implement the onShow method to
                //add content to the tooltip when a node
                //is hovered
                onShow: function(tip, node, isLeaf, domElement) {
                    tip.innerHTML = "<div class=\"tip-title\">" + node.name + "</div>" +
                    "<div class=\"tip-text\">" + this.makeHTMLFromData(node.data) + "</div>";
                },

                //Build the tooltip inner html by taking each node data property
                makeHTMLFromData: function(data){
                    var html = '';
                    var value0 = new Number(data.$area).numberFormat(getI18nResource('caqs.integerFormat.js'));
                    var value1 = new Number(data.$color - 0.005).toFixed(2);
                    html += data.areaLabel + ': ' + value0 + '<br />';
                    html += data.scoreLabel + ': ' + value1;
                    return html;
                }
            },

            //Implement this method for retrieving a requested
            //subtree that has as root a node with id = nodeId,
            //and level as depth. This method could also make a server-side
            //call for the requested subtree. When completed, the onComplete
            //callback method should be called.
            request: function(nodeId, level, onComplete){
                //if(this.lastLoadedJSON) {
                var localJSON = Ext.util.JSON.decode(response);
                var thissubtree = TreeUtil.getSubtree(localJSON, nodeId);
                TreeUtil.prune(thissubtree, 1);
                onComplete.onComplete(nodeId, thissubtree);
            //}
            },

            //Remove all element events before destroying it.
            onDestroyElement: function(content, tree, isLeaf, leaf){
                if(leaf.clearAttributes) leaf.clearAttributes();
            }
        });
        if(checked) {
            TreeUtil.prune(subtree, 1);
        }
        tm.loadJSON(subtree);
    }
});
