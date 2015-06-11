//Extending the BoxComponent to create a LinkButton
Ext.LinkButton = Ext.extend(Ext.BoxComponent, {
    constructor: function(config) {
        config = config || {};
        config.tooltipType = 'qtip',
        config.xtype = 'box';
        config.autoEl = {
            tag: 'a',
            html: config.text,
            href: '#'
        };
        Ext.LinkButton.superclass.constructor.apply(this, arguments); //Adding events
        this.addEvents({
            "click": true,
            "mouseover": true,
            "blur": true
        });
    },

    setText: function(t) {
        this.el.dom.innerHTML = t;
    },

    //Overriding the 'OnRender' method to attach the events
    onRender: function() {
        theLnk = this;
        this.constructor.superclass.onRender.apply(this, arguments);
        if (this.tooltip) {
            if (typeof this.tooltip == 'object') {
                Ext.QuickTips.register(Ext.apply({
                    target: this.id
                },
                this.tooltip));
            } else
            {
                //this does not work
                this.el.tooltip = this.tooltip;
            }
        }
        if (!theLnk.disabled) {
            this.el.on('blur',
            function(e) {
                theLnk.fireEvent('blur');
            });
            this.el.on('click',
            function(e) {
                theLnk.fireEvent('click');
            });
            this.el.on('mouseover',
            function(e) {
                theLnk.fireEvent('mouseover');
            });
        }
    }
});