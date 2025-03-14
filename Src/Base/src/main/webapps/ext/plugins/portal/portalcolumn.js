/*
 * Ext JS Library 2.2.1
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 *
 * http://extjs.com/license
 */

Ext.ux.PortalColumn = Ext.extend(Ext.Container, {
    layout:         'anchor',
    autoEl:         'div',
    defaultType:    'portlet',
    cls:            'x-portal-column'
});
Ext.reg('portalcolumn', Ext.ux.PortalColumn);