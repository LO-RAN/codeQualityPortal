/*
 * Ext JS Library 2.2.1
 * Copyright(c) 2006-2009, Ext JS, LLC.
 * licensing@extjs.com
 *
 * http://extjs.com/license
 */

Ext.ux.Portlet = Ext.extend(Ext.Panel, {
    anchor:         '100%',
    frame:          true,
    collapsible:    true,
    draggable:      false,
    cls:            'x-portlet'
});
Ext.reg('portlet', Ext.ux.Portlet);