Ext.ux.CaqsFlotPropertyGrid = Ext.extend(Ext.ux.FlotPropertyGrid, {
    columnHeaders:  undefined,

    setRenderers: function(renderers, defaultRenderer) {
        var columnModel = this.getColumnModel();
        var nbCols = columnModel.getColumnCount();
        for(var i=0; i<nbCols; i++) {
            if(renderers && renderers[i]) {
                columnModel.setRenderer(i, renderers[i], this);
            } else if(defaultRenderer) {
                columnModel.setRenderer(i, defaultRenderer, this);
            }
        }
    },

    setHeaders: function(columnHeaders) {
        var columnModel = this.getColumnModel();
        if(columnHeaders) {
            for(var i=0; i<columnHeaders.length; i++) {
                if(columnHeaders[i]) {
                    columnModel.setColumnHeader(i, columnHeaders[i]);
                }
            }
        }
    },
  // private
  initComponent: function() {
        Ext.ux.CaqsFlotPropertyGrid.superclass.initComponent.apply(this, arguments);
  }
});
Ext.reg('caqsflotpropertygrid', Ext.ux.CaqsFlotPropertyGrid);
