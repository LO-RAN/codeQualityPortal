Ext.grid.ActionPlanCheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};


Ext.grid.ActionPlanCheckColumn.prototype ={
    thisScope: undefined,
    totalCostId:    undefined,
    totalWostWorkUnitId: undefined,

    init : function(grid){
        this.grid = grid;
        this.grid.on('render', function(){
            var view = this.grid.getView();
            view.mainBody.on('mousedown', this.onMouseDown, this);
        }, this);
    },

    updateRecord: function(record) {
        record.set(this.dataIndex, !record.data[this.dataIndex]);
        if(record.data.cost != undefined) {
            //dans le cas contraire, nous sommes niveau prj ou sprj
            var costSpan = document.getElementById(this.totalCostId);
            var thisCost = record.data.cost;
            thisCost = thisCost.replace(",",".");
            thisCost = parseFloat(thisCost);
            if(record.data.selected===true) {
                //on ajoute le cout
                this.thisScope.cost += thisCost;
            } else {
                //on retire le cout
                this.thisScope.cost -= thisCost;
            }
            costSpan.innerHTML = new Number(this.thisScope.cost).numberFormat(this.thisScope.numberFormatConfig);
            var costWorkUnitSpan = document.getElementById(this.totalWostWorkUnitId);
            var apu = this.thisScope.selectedAPULib;
            costWorkUnitSpan.innerHTML = new Number(this.thisScope.cost).numberFormat(this.thisScope.numberFormatConfig) + ' ' + apu;
        }
    },

    saveRecordToDB: function(record) {
        Ext.Ajax.request({
            url: requestContextPath+'/ActionPlanManageSavesAction.do',
            params: {
                'id_elt':  record.data.id,
                'selected': record.data.selected
            },
            scope: this.thisScope,
            success: function() {
                this.reloadImages();
            }
        });
    },

    onMouseDown : function(e, t){
        if(t.className && t.className.indexOf('x-grid3-cc-'+this.id) != -1){
            e.stopEvent();
            var index = this.grid.getView().findRowIndex(t);
            var record = this.grid.store.getAt(index);
            if((record.data.elementMaster=='') || (record.data.elementMaster == record.data.elementBeanId)) {
                //cet element est son propre maitre
                this.updateRecord(record);
                this.saveRecordToDB(record);
            } else {
                //cet element vient d'un plan d'actions maitre
                if(record.data.comment == '' || record.data.comment == undefined) {
                    //il n'a pas de commentaire, on force a en placer un
                    //comme il n'a pas de commentaire, c'est forcement un critere inclus par un plan d'actions maitre
                    //que l'on deselectionne.
                    var wnd = new Ext.ux.CaqsCommentEditWindow({
                        parentElement:  this,
                        title:          getI18nResource("caqs.actionplan.commentTitle"),
                        additionalExplication: getI18nResource("caqs.actionplan.higherAP.explanation"),
                        commentMandatory:   true,
                        updateValue: function(val) {
                            var newValue = val;
                            record.data.comment = newValue;
                            record.data.iconImgClass = (newValue=='')?'icon-comment-edit':'icon-has-comment';
                            this.parentElement.thisScope.grid.getView().refresh();
                            this.parentElement.updateRecord(record);
                            Ext.Ajax.request({
                                url:    requestContextPath+'/ActionPlanManageSavesAction.do',
                                params: {
                                    'id_elt':           record.id,
                                    'commentForDeselectMaster': true,
                                    'criterionComment': val
                                },
                                scope: this.parentElement.thisScope,
                                success: function() {
                                    this.reloadImages();
                                }
                            });
                        }
                    });
                    wnd.show();
                } else {
                    //il y a deja un commentaire. on deselectionne
                    this.updateRecord(record);
                    this.saveRecordToDB(record);
                }
            }
        }
    },

    renderer : function(v, p, record){
        p.css += ' x-grid3-check-col-td';
        return '<div class="x-grid3-check-col'+(v?'-on':'')+' x-grid3-cc-'+this.id+'">&#160;</div>';
    }
};