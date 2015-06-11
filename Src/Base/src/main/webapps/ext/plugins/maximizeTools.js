var minimizeTool = {
    id:'minimize',
    hidden:true,
    qtip: 'Minimize Panel',
    handler: function(e, target, panel)
    {
        panel.maximized = false;
        panel.setSize(panel.originalSize);

        var parentItems = panel.ownerCt.ownerCt.items.items;
        for(var i=0; i<parentItems.length; i++) {
            var item = parentItems[i];
            item.columnWidth = item.originalColumnWidth;
            if(item.id!=panel.column) {
                item.show();
            }
        }

        if(panel.tools['toggle']) {
            panel.tools['toggle'].setVisible(true);
        }
        panel.tools['maximize'].setVisible(true);
        panel.tools['minimize'].setVisible(false);
        panel.ownerCt.ownerCt.doLayout(false);
    }
};

var maximizeTool = {
    id:'maximize',
    qtip: 'Maximize Panel',
    handler: function(e, target, panel)
    {
        panel.maximized = true;
        panel.originalSize = panel.getSize();
        
        var parentItems = panel.ownerCt.ownerCt.items.items;
        for(var i=0; i<parentItems.length; i++) {
            var item = parentItems[i];
            item.originalColumnWidth = item.columnWidth;
            if(item.id==panel.column) {
                item.columnWidth = 1.0;
            } else {
                item.columnWidth = 0.01;
                item.hide();
            }
        }

        // Set the column width to 1.0
        if(panel.tools['toggle']) {
            panel.tools['toggle'].setVisible(false);
        }
        panel.tools['maximize'].setVisible(false);
        panel.tools['minimize'].setVisible(true);
        
        panel.setWidth(panel.ownerCt.ownerCt.getSize().width);
        panel.setHeight(panel.ownerCt.ownerCt.getSize().height - 10);
        panel.ownerCt.ownerCt.doLayout(false);
    }
};