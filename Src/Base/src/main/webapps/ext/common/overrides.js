//surcharge de treenode pour maj de tooltip
Ext.tree.TreeNode.override({
    updateqtip: function(newqtip, newqtitle){
        if(this.getUI().textNode.setAttributeNS) {
            this.getUI().textNode.setAttributeNS("ext","qtip", newqtip);
            if(newqtitle) {
                this.getUI().textNode.setAttributeNS("ext","qtitle", newqtitle);
            }
        } else {
            this.getUI().textNode.setAttribute("ext:qtip", newqtip);
            if(newqtitle) {
                this.getUI().textNode.setAttribute("ext:qtitle", newqtitle);
            }
        }
    }
});

//surcharge de Button pour pouvoir changer dynamiquement un tooltip d'un bouton
Ext.Button.override({
    setTooltip: function(qtipText) {
        var btnEl = this.getEl().child(this.buttonSelector)
        Ext.QuickTips.register({
            target: btnEl.id,
            text: qtipText
        });
    },
    setIcon: function(url){
        if (this.rendered){
            var btnEl = this.getEl().child(this.buttonSelector);
            btnEl.setStyle('background-image', 'url(' + url +')');
        }
    }
});

//surcharge de Ext.grid.GroupingView pour pouvoir définir un groupRenderer
Ext.grid.GroupingView.override({
    getGroupId : function(value){
        var gidPrefix = this.grid.getGridEl().id;
        var groupField = this.getGroupField();
        var colIndex = this.cm.findColumnIndex(groupField);
        var cfg = this.cm.config[colIndex];
        var groupRenderer = cfg.groupRenderer || cfg.renderer;
        var gtext = this.getGroup(value, {data:{}}, groupRenderer, 0, colIndex, this.ds);
        return gidPrefix + '-gp-' + groupField + '-' + gtext;
    }
});

Ext.EventObjectImpl.getTarget = function(selector, maxDepth, returnEl){
        	var retour = undefined;
            if(this.target!=undefined) {
                var t = Ext.get(this.target);
                retour = selector ? t.findParent(selector, maxDepth, returnEl) : (returnEl ? t : this.target)
            }
            return retour;
        };

Ext.Tip.override({
    showAt : function(xy){
        Ext.Tip.superclass.show.call(this);
        if(!this.initialConfig || typeof this.initialConfig.width != 'number'){
            var bw = this.body.getTextWidth();
            if(this.title){
                bw = Math.max(bw, this.header.child('span').getTextWidth(this.title));
            }
            bw += this.getFrameWidth() + (this.closable ? 20 : 0) + this.body.getPadding("lr");
			if(this.activeTarget!=null && this.activeTarget.maxForcedWidth != undefined) {
				this.setWidth(bw.constrain(this.minWidth, this.activeTarget.maxForcedWidth));
			} else {
				this.setWidth(bw.constrain(this.minWidth, this.maxWidth));
			}
        }
        if(this.constrainPosition){
            xy = this.el.adjustForConstraints(xy);
        }
        this.setPagePosition(xy[0], xy[1]);
    }
});

Ext.ToolTip.override({
	maxForcedWidth: 300
});

Ext.QuickTip.override({
    tagConfig : {
        namespace : "ext",
        attribute : "qtip",
        width : "qwidth",
		maxForcedWidth: "qmaxForcedWidth",
        target : "target",
        title : "qtitle",
        hide : "hide",
        cls : "qclass",
        align : "qalign"
    },
    onTargetOver : function(e){
        if(this.disabled){
            return;
        }
        this.targetXY = e.getXY();
        var t = e.getTarget();
        if(!t || t.nodeType !== 1 || t == document || t == document.body){
            return;
        }
        if(this.activeTarget && t == this.activeTarget.el){
            this.clearTimer('hide');
            this.show();
            return;
        }
        if(t && this.targets[t.id]){
            this.activeTarget = this.targets[t.id];
            this.activeTarget.el = t;
            this.delayShow();
            return;
        }
        var ttp, et = Ext.fly(t), cfg = this.tagConfig;
        var ns = cfg.namespace;
        if(this.interceptTitles && t.title){
            ttp = t.title;
            t.qtip = ttp;
            t.removeAttribute("title");
            e.preventDefault();
        } else{
            ttp = t.qtip || et.getAttributeNS(ns, cfg.attribute);
        }
        if(ttp){
            var autoHide = et.getAttributeNS(ns, cfg.hide);
            this.activeTarget = {
                el: t,
                text: ttp,
                width: et.getAttributeNS(ns, cfg.width),
                autoHide: autoHide != "user" && autoHide !== 'false',
                title: et.getAttributeNS(ns, cfg.title),
                cls: et.getAttributeNS(ns, cfg.cls),
                align: et.getAttributeNS(ns, cfg.align),
				maxForcedWidth: et.getAttributeNS(ns, cfg.maxForcedWidth)
            };
            this.delayShow();
        }
    }
});

//surcharges pour gérer les erreurs ajax

Ext.ux.CaqsJsonReader = function(config) {
    // call parent constructor
    Ext.ux.CaqsJsonReader.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsJsonReader constructor
Ext.extend(Ext.ux.CaqsJsonReader, Ext.data.JsonReader);

Ext.override(Ext.ux.CaqsJsonReader, {
    read : function(response){
        var json = response.responseText;
        var o = eval("("+json+")");
        if(!o) {
            throw {message: "JsonReader.read: Json object not found"};
        }
        if(o.success != undefined) {
            if(o.success == false) {
                return {
                    success : false,
                    msgReturnCode: o.msgReturnCode,
                    typeMsg: o.typeMsg
                };
            }
        }
        return this.readRecords(o.dataArray);
    }
});

Ext.ux.CaqsJsonStore = function(c){
    Ext.ux.CaqsJsonStore.superclass.constructor.call(this, Ext.apply(c, {
        proxy: !c.data ? new Ext.data.HttpProxy({url: c.url}) : undefined,
        reader: new Ext.ux.CaqsJsonReader(c, c.fields)
    }));
};
Ext.extend(Ext.ux.CaqsJsonStore, Ext.data.Store);


Ext.override(Ext.data.JsonReader, {
    read : function(response){
        var json = response.responseText;
        var o = eval("("+json+")");
        if(!o) {
            throw {message: "JsonReader.read: Json object not found"};
        }
        if(o.success != undefined) {
            if(o.success == false) {
                return {
                    success : false,
                    msgReturnCode: o.msgReturnCode,
                    typeMsg: o.typeMsg
                };
            }
        }
        return this.readRecords(o);
    }
});

Ext.override(Ext.data.Connection, {
    handleResponse : function(response){
        this.transId = false;
        var options = response.argument.options;
        response.argument = options ? options.argument : null;
        this.fireEvent("requestcomplete", this, response, options);
        var json = response.responseText;
        try {
            var o = eval("("+json+")");
            //begin edit
            if(o) {
                if(o.success != false) {
                    Ext.callback(options.success, options.scope, [response, options]);
                } else {
                    caqsHandleErrors(o);
                }
            }
            //end edit
        }catch(e){
            //alert(e);
            Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
        }
        Ext.callback(options.callback, options.scope, [options, true, response]);
    }
});

Ext.override(Ext.tree.TreeLoader, {

    processResponse : function(response, node, callback){
        var json = response.responseText;
        try {
            var o = eval("("+json+")");
            //begin edit
            if(o) {
                if(o.success != false) {
                    o = o.dataArray;
                    //end edit
                    node.beginUpdate();
                    for(var i = 0, len = o.length; i < len; i++){
                        var n = this.createNode(o[i]);
                        if(n){
                            node.appendChild(n);
                        }
                    }
                    node.endUpdate();
                    if(typeof callback == "function"){
                        callback(this, node);
                    }
                }
            }
        }catch(e){
            this.handleFailure(response);
        }
    }
});

function caqsHandleErrors(result) {
    if(result.msgReturnCode) {
        if(Caqs.Portal.hasMaskShown()) {
            Caqs.Portal.hideShownMask();
        }
        var text = getI18nResource(result.msgReturnCode,
            result.msgReturnCodeArg0,  result.msgReturnCodeArg1,
            result.msgReturnCodeArg2, result.msgReturnCodeArg3,
            result.msgReturnCodeArg4);
        var icon = undefined;
        if(result.typeMsg=='ERROR') {
            icon = Ext.MessageBox.ERROR;
        } else if(result.typeMsg=='WARNING') {
            icon = Ext.MessageBox.QUESTION;
        } else if(result.typeMsg=='INFO') {
            icon = Ext.MessageBox.WARNING;
        }
        Ext.Msg.show({
            maxWidth:       700,
            minWidth:       400,
            icon:           icon,
            title:          getI18nResource("caqs.errorOccured"),
            msg:            text,
            buttons:        Ext.Msg.OK
        });
    }
}

Ext.override(Ext.data.HttpProxy, {
	loadResponse : function(o, success, response) {
        delete this.activeRequest;
		if(!success){
			this.fireEvent("loadexception", this, o, response);
			o.request.callback.call(o.request.scope, null, o.request.arg, false);
			return;
		}
		var result;
		try {
			result = o.reader.read(response);
		}catch(e){
			this.fireEvent("loadexception", this, o, response, e);
			o.request.callback.call(o.request.scope, null, o.request.arg, false);
			return;
		}
		// BEGIN EDIT
		this.fireEvent("load", this, o, o.request.arg);
		if(result.success !== false) {
			o.request.callback.call(o.request.scope, result, o.request.arg, true);
		} else {
            caqsHandleErrors(result);
			o.request.callback.call(o.request.scope, response, o.request.arg, false);
		}
		// END EDIT
	}
});


//fin gestion des erreurs de requete


Ext.override(Ext.layout.TableLayout, {
    onLayout : function(ct, target){
        var cs = ct.items.items, len = cs.length, c, i;
        if(!this.table){
            target.addClass('x-table-layout-ct');

            this.table = target.createChild(
                {tag:'table', cls:'x-table-layout', cellspacing: 0, cn: {tag: 'tbody'}}, null, true);
        }
	this.renderAll(ct, target);//move out that can render items more than once.
    }
});

//overrides datepicker
Ext.override(Ext.DatePicker, {
    setValue : function(value){
        if(value instanceof Date) {
            this.value = value.clearTime(true);
            if(this.el){
                this.update(this.value);
            }
        }
    }
});

//surcharge permettant d'appliquer un style aux elements de cellules de tableaux
Ext.override(Ext.layout.TableLayout, {
	getNextCell : function(c){
		var cell = this.getNextNonSpan(this.currentColumn, this.currentRow);
		var curCol = this.currentColumn = cell[0], curRow = this.currentRow = cell[1];
		for(var rowIndex = curRow; rowIndex < curRow + (c.rowspan || 1); rowIndex++){
			if(!this.cells[rowIndex]){
				this.cells[rowIndex] = [];
			}
			for(var colIndex = curCol; colIndex < curCol + (c.colspan || 1); colIndex++){
				this.cells[rowIndex][colIndex] = true;
			}
		}
		var td = document.createElement('td');
		if(c.cellId){
			td.id = c.cellId;
		}
		var cls = 'x-table-layout-cell';
		if(c.cellCls){
			cls += ' ' + c.cellCls;
		}
		td.className = cls;
		if(c.cellStyle){
			Ext.DomHelper.applyStyles(td, c.cellStyle);
		}
		if(c.cellAlign){
			td.align = c.cellAlign;
		}
		if(c.cellVAlign){
			td.vAlign = c.cellVAlign;
		}
		if(c.colspan){
			td.colSpan = c.colspan;
		}
		if(c.rowspan){
			td.rowSpan = c.rowspan;
		}
		this.getRow(curRow).appendChild(td);
		return td;
	}
});

//override to hide a field and set its height to 0
Ext.override(Ext.layout.FormLayout, {
	renderItem : function(c, position, target){
		if(c && !c.rendered && c.isFormField && c.inputType != 'hidden'){
			var args = [
				   c.id, c.fieldLabel,
				   c.labelStyle||this.labelStyle||'',
				   this.elementStyle||'',
				   typeof c.labelSeparator == 'undefined' ? this.labelSeparator : c.labelSeparator,
				   (c.itemCls||this.container.itemCls||'') + (c.hideLabel ? ' x-hide-label' : ''),
				   c.clearCls || 'x-form-clear-left'
			];
			if(typeof position == 'number'){
				position = target.dom.childNodes[position] || null;
			}
			if(position){
				c.formItem = this.fieldTpl.insertBefore(position, args, true);
			}else{
				c.formItem = this.fieldTpl.append(target, args, true);
			}
			c.actionMode = 'formItem';
			c.render('x-form-el-'+c.id);
			c.container = c.formItem;
			c.actionMode = 'container';
		}else {
			Ext.layout.FormLayout.superclass.renderItem.apply(this, arguments);
		}
	}
});
Ext.override(Ext.form.TriggerField, {
	actionMode: 'wrap',
	onShow: Ext.form.TriggerField.superclass.onShow,
	onHide: Ext.form.TriggerField.superclass.onHide
});

//override pour JIT (treemap generator)
rgbToHex = function(srcArray, array){
    if (srcArray.length < 3) return null;
    if (srcArray.length == 4 && srcArray[3] == 0 && !array) return 'transparent';
    var hex = [];
    for (var i = 0; i < 3; i++){
        var bit = (srcArray[i] - 0).toString(16);
        hex.push((bit.length == 1) ? '0' + bit : bit);
    }
    return (array) ? hex : '#' + hex.join('');
};

//override pour avoir un context dans le renderer d'une colonne
Ext.override(Ext.grid.GridView, {
    getColumnData : function(){
        var cs = [], cm = this.cm, colCount = cm.getColumnCount();
        for(var i = 0; i < colCount; i++){
            var name = cm.getDataIndex(i);
            cs[i] = {
                name : (typeof name == 'undefined' ? this.ds.fields.get(i).name : name),
                renderer : cm.getRenderer(i),
                id : cm.getColumnId(i),
                style : this.getColumnStyle(i),
                grid : this.grid,
                view : this,
                colModel : cm
            };
        }
        return cs;
    }
});

Ext.override(Ext.Toolbar.TextItem, {
    setText : function (text) {
            this.el.innerHTML = text;
    }
});

//pour le cas de NaN sous ie
Ext.Element.prototype.addUnits = function (size) {
        if (isNaN(size)) return "auto";
        return Ext.Element.addUnits(size, this.defaultUnit);
};