/**
 * @class Ext.ux.ClickableTabPanel
 * @extends Ext.TabPanel
 * Clickable tab panel
 * @constructor
 * @param {Object} config Configuration options
 */
Ext.namespace('Ext.ux.form');

Ext.ux.ClickableTabPanel = function(config){
    this.name = config.name || config.id;
    Ext.ux.ClickableTabPanel.superclass.constructor.call(this, config);
};

Ext.extend(Ext.ux.ClickableTabPanel, Ext.TabPanel,  {
    
    /**
     * Returns the name attribute of the field if available
     * @return {String} name The field name
     */
    getName: function(){
        return this.name;
    },
	
    setActiveTab : function(item){
        if((item == null) && (this.activeTab != null)) {
            var oldEl = this.getTabEl(this.activeTab);
            if(oldEl){
                Ext.fly(oldEl).removeClass('x-tab-strip-active');
            }
            this.activeTab.fireEvent('deactivate', this.activeTab);
            this.activeTab = null;
            return;
        }
        if(item != null) {
            item = this.getComponent(item);
            if(!item || this.fireEvent('beforetabchange', this, item, this.activeTab) === false){
                //this.fireEvent('tabchange', this, item);
                return;
            }
            if(!this.rendered){
                this.activeTab = item;
                return;
            }
            if(this.activeTab != item){
                if(this.activeTab){
                    var oldEl = this.getTabEl(this.activeTab);
                    if(oldEl){
                        Ext.fly(oldEl).removeClass('x-tab-strip-active');
                    }
                    this.activeTab.fireEvent('deactivate', this.activeTab);
                }
                var el = this.getTabEl(item);
                Ext.fly(el).addClass('x-tab-strip-active');
                this.activeTab = item;
                this.stack.add(item);

                this.layout.setActiveItem(item);
                if(this.layoutOnTabChange && item.doLayout){
                    item.doLayout();
                }
                if(this.scrolling){
                    this.scrollToTab(item, this.animScroll);
                }

                item.fireEvent('activate', item);
            }
            this.fireEvent('tabchange', this, item);
        }
    },
	
    onStripMouseDown : function(e){
        e.preventDefault();
        if(e.button != 0){
            return;
        }
        var t = this.findTargets(e);
        if(t.close){
            this.remove(t.item);
            return;
        }
		
        if(t.item){
            this.setActiveTab(t.item);
        }
    }
});

Ext.reg('clickabletabpanel', Ext.ux.ClickableTabPanel);