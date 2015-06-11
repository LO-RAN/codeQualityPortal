Ext.ux.CaqsCommentEditWindow = function(config) {
    // call parent constructor
    Ext.ux.CaqsCommentEditWindow.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.CaqsCommentEditWindow, Ext.Window, {
    modal:          true,
    maximizable:    true,
    resizable:      true,
    minimizable:    false,
    closable:       false,
    width:          550,
    height:         200,
    shadow:         false,
    plain:          true,
    x:              20,
    y:              20,
    apCommentTextArea:  undefined,
    parentElement:  undefined,
    additionalExplication: undefined,
    commentMandatory:   false,

    updateValue: function(val){

    },

    setComment: function(val) {
        this.apCommentTextArea.setValue(val);
    },

    initComponent : function(){
        this.apCommentTextArea = new Ext.form.TextArea ({
            anchor:     '100% 70%',
            allowBlank: this.commentMandatory,
            value:	''
        });

        var thoseitems = new Array();
        var layout = 'fit';
        if(this.additionalExplication!=undefined) {
            layout = 'anchor',
            thoseitems[thoseitems.length] = new Ext.Panel({
                anchor: '100% 30%',
                layout: 'fit',
                border: false,
                items: [
                    {
                        border: false,
                        html:   this.additionalExplication
                    }
                ]
            });
        }
        thoseitems[thoseitems.length] = this.apCommentTextArea;

        var config = {
            items:	        thoseitems,
            layout:             layout,
            buttons: [
            {
                text: getI18nResource("caqs.finir"),
                scope: this,
                handler: function(){
                    var val = this.apCommentTextArea.getValue();
                    if((this.commentMandatory && val!='') || !this.commentMandatory) {
                        this.updateValue(val);
                        this.close();
                    }
                }
            }
            , {
                text: getI18nResource("caqs.annuler"),
                scope: this,
                handler: function(){
                    this.close();
                }
            }
            ]
        };

        // apply config
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.CaqsCommentEditWindow.superclass.initComponent.apply(this, arguments);
        this.on('beforeclose', this.onBeforeClose, this);
    },


    onBeforeClose: function(wnd) {
        wnd.hide();
        return false;
    }
});