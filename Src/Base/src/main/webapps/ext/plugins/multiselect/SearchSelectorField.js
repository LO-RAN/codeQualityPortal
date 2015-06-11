Ext.ux.SearchSelectorField = Ext.extend(Ext.form.TwinTriggerField, {
    initComponent: function(){
        Ext.ux.SearchSelectorField.superclass.initComponent.call(this);
        this.on('specialkey', function(f, e){
            if(e.getKey() == e.ENTER){
                this.onTrigger2Click();
            }
        }, this);
    },

    validationEvent:false,
    validateOnBlur:false,
    trigger1Class:'x-form-clear-trigger',
    trigger2Class:'x-form-search-trigger',
    hideTrigger1:true,
	autoWidth:false,
    hasSearch: false,
    paramName: 'query',
    onTrigger1Click: function(){
        if(this.hasSearch){
            //var o = {start: 0};
            //o[this.paramName] = '';
            //this.store.reload({params:o});
            this.store.filter('lib','');
			this.el.dom.value = '';
            this.triggers[0].hide();
            this.hasSearch = false;
        }
    },

    onTrigger2Click: function(){
        var v = this.getRawValue();
        if(v.length < 1){
            this.onTrigger1Click();
            return;
        }
        //var o = {start: 0};
        //o[this.paramName] = v;
		//this.store.filter({params:o});
		this.store.filter('lib',v,true);
        this.hasSearch = true;
        this.triggers[0].show();
    }
});
