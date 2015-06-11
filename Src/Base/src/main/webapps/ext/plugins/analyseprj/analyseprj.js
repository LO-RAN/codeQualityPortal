/**
 *		!!! specifique au lancement d'analyse avec selection d'elements !!!
 **/

var localScope = undefined;
function remove(array, id) {
    for(i=0; i<array.length; i++) {
        if(array[i]==id) {
            array.splice(i,1);
            break;
        }
    }
}

function contains(array, id) {
    for(i=0; i<array.length; i++) {
        if(array[i]==id) {
            return true;
        }
    }
    return false;
}

function checkIt(recordId, check, recurse) {
    var scope = localScope;
    var record = scope.records[recordId];
    var eltExt = Ext.get('checkbox'+record.id);
    var checking = false;
    if(eltExt!=undefined) {
        if(check==undefined) {
            //undefined : appel depuis un clic sur noeud
            if(eltExt.hasClass('x-grid3-check-col-on')) {
                eltExt.removeClass('x-grid3-check-col-on');
                eltExt.addClass('x-grid3-check-col');
            } else {
                checking = true;
                eltExt.removeClass('x-grid3-check-col');
                eltExt.addClass('x-grid3-check-col-on');
            }
        } else {
            //not undefined : appel recursif
            checking = check;
            if(check && !eltExt.hasClass('x-grid3-check-col-on')) {
                eltExt.removeClass('x-grid3-check-col');
                eltExt.addClass('x-grid3-check-col-on');
            } else {
                //checking = true;
                if(!check && !eltExt.hasClass('x-grid3-check-col')) {
                    eltExt.removeClass('x-grid3-check-col-on');
                    eltExt.addClass('x-grid3-check-col');
                }
            }
        }
    }
    if('EA'==record.attributes) {
        if(checking) {
            if(!contains(scope.checkedEltsId, record.id)) {
                //pas contenu : on ajoute
                scope.checkedEltsId[scope.checkedEltsId.length] = record.id;
            }
        } else {
            remove(scope.checkedEltsId, record.id);
        }
    	
        if(scope.analysisWindowSubmitBtn!=undefined) {
            var disable = (scope.checkedEltsId.length==0);
            if(disable) {
                scope.analysisLaunchFormPanel.stopMonitoring();
            } else {
                scope.analysisLaunchFormPanel.startMonitoring();
            }
            scope.analysisWindowSubmitBtn.setDisabled(disable);
        }
        if(scope.manualAnalysisWindowSubmitBtn!=undefined) {
            scope.manualAnalysisWindowSubmitBtn.setDisabled(scope.checkedEltsId.length==0);
        }
        if(scope.generateBatchParametersBtn!=undefined) {
            scope.generateBatchParametersBtn.setDisabled(scope.checkedEltsId.length==0);
        }
    } else {
        if(recurse==undefined) {
            recurse = true;
        }
        //element pere vers fils
        if(recurse && record.children!=null) {
            for(var i=0; i<record.children.length; i++) {
                checkIt(record.children[i].id, checking);
            }
        }
    }
   	
    if(!checking) {
        //on decoche, on decoche aussi les parents
        if(record.parentId!='') {
            checkIt(record.parentId, false, false);
        }
    }
}


Ext.grid.CheckColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.CheckColumn.prototype ={
    
    init : function(grid){
    },
    
    onClick : function() {
        checkIt(this);
    },

    renderer : function(v, p, record){
        var scope = this.scope;
        //        p.on('click', this.onClick, record);
        if(record.checked && record.attributes=='EA' && !contains(scope.checkedEltsId, record.id)) {
            scope.checkedEltsId[scope.checkedEltsId.length] = record.id;
        }
        if(scope.analysisWindowSubmitBtn!=undefined) {
            var disable = (scope.checkedEltsId.length==0);
            if(disable) {
                scope.analysisLaunchFormPanel.stopMonitoring();
            } else {
                scope.analysisLaunchFormPanel.startMonitoring();
            }
            scope.analysisWindowSubmitBtn.setDisabled(disable);
        }
        if(scope.manualAnalysisWindowSubmitBtn!=undefined) {
            scope.manualAnalysisWindowSubmitBtn.setDisabled(scope.checkedEltsId.length==0);
        }
        if(scope.generateBatchParametersBtn!=undefined) {
            scope.generateBatchParametersBtn.setDisabled(scope.checkedEltsId.length==0);
        }
        scope.records[record.id] = record;
        localScope = scope;
        return '<div onClick="checkIt(\''+record.id+'\');" id="checkbox'+record.id+'" class="x-grid3-check-col'+(v?'-on':'')+'"></div>';
    }
};

Ext.grid.TextFieldColumn = function(config){
    Ext.apply(this, config);
    if(!this.id){
        this.id = Ext.id();
    }
    this.renderer = this.renderer.createDelegate(this);
};

Ext.grid.TextFieldColumn.prototype ={
    
    init : function(grid){
    },
    
    renderer : function(v, p, record){
        var retour = '';
        if(record.attributes=='EA') {
            retour = '<input class="x-form-text x-form-field" style="width:90px;height:20px;" width="90px" size="10" autocomplete="off" id="'+
            record.id+'OptionField" name="'+record.id+'OptionField" type="text">';
        }
        return retour;
    }
};