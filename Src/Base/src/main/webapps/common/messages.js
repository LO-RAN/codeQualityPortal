
Ext.ux.MessagesPanel = function(config) {
    // call parent constructor
    Ext.ux.MessagesPanel.superclass.constructor.call(this, config);
}; // end of Ext.ux.CaqsTreeMapPanel constructor


Ext.extend(Ext.ux.MessagesPanel, Ext.Panel, {
    id:             'extNorthPanel',
    nbMessages:     0,
    oldNbRunning:   0,
    oldNbSuccess:   0,
    oldNbFailed:    0,
    progressBars:   undefined,
    messagesToRender:   undefined,
    pageLoading:        true,
    northPanelVisible:  false,
    messagesAlreadyRendered:    undefined,
    NO_CHANGE:          0,
    NOT_FOUND:          1,
    STATUS_CHANGE:        2,
    PERCENT_CHANGE:     3,
    messagesZone:       undefined,
    stateful:           false,
    mask:               undefined,
    layout:             'anchor',


    messageStatus: function(message) {
        var retour = this.NOT_FOUND;
        var found = false;
        for(var i=0; i<this.messagesAlreadyRendered.length; i++) {
            if(this.messagesAlreadyRendered[i].id==message.id) {
                if(this.messagesAlreadyRendered[i].status==message.status) {
                    if(this.messagesAlreadyRendered[i].percentage!=message.percentage
                        || this.messagesAlreadyRendered[i].step!=message.step) {
                        retour = this.PERCENT_CHANGE;
                        this.messagesAlreadyRendered[i].percentage = message.percentage;
                        this.messagesAlreadyRendered[i].step = message.step;
                    } else {
                        retour = this.NO_CHANGE;
                    }
                } else {
                    retour = this.STATUS_CHANGE;
                }
                found = true;
            }
        }
        if(!found) {
            for(var i=0; i<this.messagesToRender.length; i++) {
                if(this.messagesToRender[i].id==message.id) {
                    if(this.messagesToRender[i].status==message.status) {
                        if(this.messagesToRender[i].percentage!=message.percentage
                            || this.messagesToRender[i].step!=message.step) {
                            retour = this.PERCENT_CHANGE;
                            this.messagesToRender[i].percentage = message.percentage;
                            this.messagesToRender[i].step = message.step;
                        }else {
                            retour = this.NO_CHANGE;
                        }
                    } else {
                        retour = this.STATUS_CHANGE;
                    }
                    found = true;
                }
            }
        }

        return retour;
    },

    searchForMessagesToDelete: function(messages) {
        var drawnEltDeleted = false;
        for(var i=0; i<this.messagesAlreadyRendered.length; i++) {
            var found = false;
            for(var j=0; j<messages.length; j++) {
                if(messages[j].id==this.messagesAlreadyRendered[i].id) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                //message a supprimer
                this.remove(this.messagesAlreadyRendered[i].id);
                drawnEltDeleted = true;
                this.messagesAlreadyRendered.splice(i,1);
                i--;
            }
        }
        for(var i=0; i<this.messagesToRender.length; i++) {
            var found = false;
            for(var j=0; j<messages.length; j++) {
                if(messages[j].id==this.messagesToRender[i].id) {
                    found = true;
                    break;
                }
            }
            if(!found) {
                //message a supprimer
                this.messagesToRender.splice(i,1);
                i--;
            }
        }
        return drawnEltDeleted;
    },

    setMessageAsSeen: function(id) {
        Ext.Ajax.request({
            url:		requestContextPath + '/SetMessageAsSeen.do?id_mess='+id,
            success:	this.messagesRenderer,
            scope:      this
        });
    },

    setAllMessagesAsSeen: function() {
        Ext.Ajax.request({
            url:		requestContextPath + '/SetMessageAsSeen.do',
            params: {
                'allMessages': 'true'
            },
            success:	this.messagesRenderer,
            scope:      this
        });
    },

    showRemoveButton: function(child, show) {
        var backgroundColor = (show) ? '#EFEFEF' : 'transparent';
        var check = child.getComponent(child.nbChildElts - 1);
        if(check != null) {
            check.setVisible(show);
        }
        var elt = Ext.get(child.id);
        if(elt != null) {
            elt.setStyle('backgroundColor', backgroundColor);
        }
    },

    //creation d'une ligne de message. 3 colonnes : icone, middleElt, bouton de suppression
    createExtLine: function(ligne, middleElt) {
        var iconPath = '';
        if( (ligne.type=='INFO' || ligne.type=='PROGRESS') &&  ligne.status!='FAILED') {
            iconPath = 'information.gif';
        } else if(ligne.type=='WARNING') {
            iconPath = 'warning.gif';
        } else if(ligne.type=='ERROR' || ligne.status=='FAILED') {
            iconPath = 'exclamation.gif';
        }


        var thoseitems = new Array();
        thoseitems[thoseitems.length] = new Ext.ux.ComponentImage({
            src:    requestContextPath + '/images/' + iconPath,
            style:  'width:16px; height:16px; padding-right: 5px; padding-bottom: 7px;'
        });
        
        thoseitems[thoseitems.length] = middleElt;
        
        if(ligne.specialAction!=undefined && ligne.specialAction == 'true') {
            var specialActionImg = new Ext.ux.ComponentImage({
                id:     ligne.specialActionId,
                style:  'cursor:pointer; width:16px; height:16px; padding-right: 5px; padding-bottom: 7px;',
                src:    requestContextPath + ligne.specialActionImg
            });
            specialActionImg.on('click', function(c) {
                if(ligne.specialActionType == 'url') {
                    location.href = ligne.specialActionHref;
                } else if(ligne.specialActionType == 'popup') {
                    this.displayPopupForAnalysisFailed(ligne.specialActionHref);
                }
            }, this);
            thoseitems[thoseitems.length] = specialActionImg;
            addTooltip(ligne.specialActionId, '', ligne.specialActionTooltip);
        }

        var markasreadImg = new Ext.ux.ComponentImage({
            id:     'removeLink'+ligne.id,
            style:  'cursor:pointer; width:16px; height:16px; padding-bottom: 7px;',
            src:    requestContextPath + '/images/tick.gif'
        });
        markasreadImg.on('click', function(c) {
            this.setMessageAsSeen(ligne.id);
        }, this);
        markasreadImg.on('render', function(cmp) {
            cmp.setVisible(false);
        }, this);
        addTooltip('removeLink'+ligne.id, '', getI18nResource("caqs.messages.setAsSeen"));
        thoseitems[thoseitems.length] = markasreadImg;
        
        var retour = new Ext.Panel({
            baseCls: 		'messageDivCls',
            id: 			ligne.id,
            name: 			ligne.id,
            border:			false,
            anchor:         '98%',
            layout:			'table',
            height:         25,
            nbChildElts:    thoseitems.length,
            layoutConfig: {
                columns:	thoseitems.length
            },
            items :         thoseitems
        });

        return retour;
    },

    responseDisplayPopup: function(response) {
        Caqs.Portal.hideGlobalLoadingMask();
        var json = Ext.util.JSON.decode(response.responseText);
        if(json.datas == null || json.datas == '') {
            json.datas = getI18nResource('caqs.noerroravailable');
        }
        var wnd = new Ext.Window({
            height:         400,
            width:          600,
            maximizable:    true,
            modal:          true,
            layout:         'fit',
            items: [
            {
                autoScroll: true,
                border:     false,
                html:       '<PRE>'+json.datas+'</PRE>'
            }
            ]
        });
        wnd.show(Ext.getBody());
    },

    displayPopupForAnalysisFailed: function(href) {
        Caqs.Portal.showGlobalLoadingMask();
        Ext.Ajax.request({
            url:		requestContextPath + href,
            success:	this.responseDisplayPopup,
            scope:      this,
            failure:    function() {
                Caqs.Portal.hideGlobalLoadingMask();
            }
        });
    },

    getElementCompleteLib: function(ligne) {
        var retour = '';
        if(ligne.eltLib != null && ligne.prjLib!=null) {
            retour = ligne.eltLib+' <i>('+TYPE_PRJ_LIB+' : '+ligne.prjLib+')</i>';
        } else if(ligne.prjLib!=null) {
            retour = TYPE_PRJ_LIB+' '+ligne.prjLib;
        } else if(ligne.eltLib!=null) {
            retour = ligne.eltLib;
        }
        return retour;
    },


    manageProgressBar: function(ligne) {
        var middleElt = undefined;
        if(ligne.status=='IN_PROGRESS') {
            var progressBar = undefined;
            var completeLib = this.getElementCompleteLib(ligne);
            var txt =  '';
            if(completeLib != '') {
                txt += completeLib + ' : ';
            }
            txt += ligne.statusLib;
            if(ligne.percentage==-1) {
                progressBar = new Ext.ProgressBar({
                    id: 		ligne.id+'progressBar',
                    width: 		700,
                    hideMode: 	'offsets',
                    text: 		txt
                });
                progressBar.on('render', function() {
                    progressBar.wait({
                        interval:	1000,
                        increment:	15
                    });
                });
            } else {
                var t = txt;
                t += ' ('+ligne.percentage+'%)';
                if(ligne.step!=null && ligne.step!='' && ligne.status!='COMPLETED') {
                    t += ' : '+ligne.step;
                }
                progressBar = new Ext.ProgressBar({
                    id: 		ligne.id+'progressBar',
                    width: 		700,
                    hideMode: 	'offsets',
                    text: 		t
                });
            }
            this.progressBars[ligne.id+'progressBar'] = progressBar;
            progressBar.on('show', function(cmpt) {
                cmpt.setHeight(25);
            });
            middleElt = progressBar;
        } else {
            middleElt = this.createTextMessage(ligne);
        }
        return this.createExtLine(ligne, middleElt);
    },

    createTextMessage: function(ligne) {
        var middleEltHTML = '<span class="txtInfoMsg">';
        var completeElt = this.getElementCompleteLib(ligne);
        if(completeElt != '') {
            middleEltHTML += completeElt + ' : ';
        }
        middleEltHTML += ' '+ligne.statusLib;
        if(ligne.endDate!=undefined) {
            middleEltHTML += ' '+getI18nResource("caqs.message.showEndDate")+' '+ligne.endDate;
        }

        if(ligne.status=='FAILED') {
            if(ligne.step!=null && ligne.step!='') {
                middleEltHTML += ' : '+ligne.step;
            }
        }

        middleEltHTML += '</span>';

        var retour = {
            html: 			middleEltHTML,
            baseCls:		'transparentPanel',
            style:          'height: 25px;',
            border:			false
        };
        return retour;
    },

    renderMessageFunction: function(ligne) {
        var child = undefined;
        if(ligne.type==='PROGRESS') {
            child = this.manageProgressBar(ligne);
        } else {
            var middleElt = this.createTextMessage(ligne);
            child =  this.createExtLine(ligne, middleElt);
        }
        if(child!=undefined) {
            this.insert(0, child);
            //this.doLayout();
            child.on('render', function() {
                child.getEl().on('mouseover', function(comp) {
                    this.showRemoveButton(child, true);
                }, this);
                child.getEl().on('mouseout', function(comp) {
                    this.showRemoveButton(child, false);
                }, this);
            }, this);
        }
        if(ligne.type==='PROGRESS' && this.progressBars[ligne.id+'progressBar']!=undefined) {
            this.doLayout();//obligé pour que la progressbar ait un rendu correct
            var t = this.progressBars[ligne.id+'progressBar'].text;
            this.progressBars[ligne.id+'progressBar'].updateProgress(ligne.percentage/100, t);
        }
    },

    manageCompletedFailedMessages: function(ligne) {
        if(ligne.status=='COMPLETED' || ligne.status=='FAILED') {
            var iconCls = '';
            var width = 300;
            if(ligne.status=='COMPLETED') {
                iconCls = 'information';
            } else {
                iconCls = 'error';
            }
            if(ligne.taskId=='COMPUTING') {
                //un calcul
                var thiscaqsportal = Caqs.Portal.getCaqsPortal();
                if(thiscaqsportal.gestionQualitePanel.treesPanel.selectedElementId==ligne.eltId) {
                    //nous sommes toujours sur le meme element. on simule un clic sur l'arbre
                    thiscaqsportal.gestionQualitePanel.treesPanel.selectElementOnProjectTree(thiscaqsportal.gestionQualitePanel.treesPanel.selectedElementId, true);
                }
            } else if(ligne.taskId=='GENERATING_REPORT') {
                Caqs.Portal.getCaqsPortal().gestionQualitePanel.eaSynthesisPanel.updateReportMenu(ligne.eltId, false, (ligne.status=='COMPLETED'));
            }
            if(ligne.toastMsg!=undefined && ligne.toastMsg.length>0) {
                var ttl = ligne.toastTitle + ' ' + this.getElementCompleteLib(ligne);
                new Ext.ux.Notification({
                    title: 			ttl,
                    html: 			ligne.toastMsg,
                    autoDestroy: 	true,
                    width:			width,
                    hideDelay:  	5000,
                    iconCls: 		iconCls
                }).show(document);
            }
        }
    },

    messagesRenderer: function(response) {
        var nbRunning = 0;
        var nbSuccess = 0;
        var nbFailed  = 0;
        var messages = new Array();
        var redraw = false;
        if(response.responseText != '') {
            var tmpMessages = Ext.util.JSON.decode(response.responseText);
            messages = tmpMessages.dataArray;
            if(tmpMessages.tooManyMessages)
                {
                    new Ext.ux.Notification({
                    title: getI18nResource("caqs.messages.toomanymessages"),
                    html:  getI18nResource("caqs.messages.onlyfirstmessagesdisplayed",messagesDisplayLimit),
                    autoDestroy: 	true,
                    hideDelay:  	5000,
                    iconCls: 		'warning'
                }).show(document);
                }
               nbRunning=tmpMessages.nbRunning;
               nbSuccess=tmpMessages.nbSuccess;
               nbFailed=tmpMessages.nbFailed;
        }
        if(messages.length == 0) {
            if(this.messagesAlreadyRendered.length>0) {
                for(var i=0; i<this.messagesAlreadyRendered.length; i++) {
                    this.remove(this.messagesAlreadyRendered[i].id);
                }
                this.messagesAlreadyRendered.length=0;
            }
            this.messagesToRender.length = 0;
        } else {
            for(var i=0; i<messages.length; i++) {
                var ligne = messages[i];

                var status = this.messageStatus(ligne);
                switch(status) {
                    case this.NO_CHANGE :
                        //manageCompletedFailedMessages(ligne);
                        break;
                    case this.PERCENT_CHANGE :
                        if(this.progressBars[ligne.id+'progressBar']!=undefined) {
                            var t = this.progressBars[ligne.id+'progressBar'].text;
                            t = t.substring(0, t.lastIndexOf('('));
                            t += ' ('+ligne.percentage+'%)';
                            if(ligne.step!='' && ligne.status!='COMPLETED') {
                                t += ' : '+ligne.step;
                            }
                            this.progressBars[ligne.id+'progressBar'].updateProgress(ligne.percentage/100, t);
                        }
                        break;
                    case this.STATUS_CHANGE ://on retire l'ancien et on cree le nouveau
                        //cas specifiques
                        if(this.progressBars[ligne.id+'progressBar']!=undefined) {
                            this.progressBars[ligne.id+'progressBar'].reset();
                            this.progressBars[ligne.id+'progressBar'] = undefined;
                        }
                        var thisfound = false;
                        for(var j=0; j < this.messagesAlreadyRendered.length; j++) {
                            if(ligne.id==this.messagesAlreadyRendered[j].id) {
                                this.messagesAlreadyRendered.splice(j,1);
                                thisfound = true;
                                this.remove(ligne.id);
                                redraw = true;
                                break;
                            }
                        }
                        if(!thisfound) {
                            for(var k=0; k<this.messagesToRender.length; k++) {
                                if(ligne.id==this.messagesToRender[k].id) {
                                    this.messagesToRender.splice(k,1);
                                    break;
                                }
                            }
                        }
                    //on cree le nouveau, donc pas de break
                    case this.NOT_FOUND :
                        if(this.northPanelVisible) {
                            redraw = true;
                            this.renderMessageFunction(ligne);
                            this.messagesAlreadyRendered[this.messagesAlreadyRendered.length] = ligne;
                        } else {
                            this.messagesToRender[this.messagesToRender.length] = ligne;
                        }
                        if(!this.pageLoading) {
                            this.manageCompletedFailedMessages(ligne);
                        }
                        break;
                }
            }
            redraw = (this.searchForMessagesToDelete(messages))? true : redraw;
        }
        if(redraw) {
            this.doLayout();
        }
        var messagesLength = this.messagesAlreadyRendered.length + this.messagesToRender.length;
        if( this.nbMessages != messagesLength || nbRunning!=this.oldNbRunning || nbSuccess!=this.oldNbSuccess || nbFailed!=this.oldNbFailed ) {
            var newMessages = false;
            if( this.messagesToRender.length>0 ) {
                newMessages = true;
            }
            this.nbMessages = nbFailed+nbSuccess+nbRunning;

            this.oldNbFailed = nbFailed;
            this.oldNbSuccess = nbSuccess;
            this.oldNbRunning = nbRunning;

            var titre = this.getPanelTitle();
            titre += '<span id="newMessagesEnveloppe">';
            if(newMessages && !this.northPanelVisible && !this.pageLoading) {
                var inner = '&nbsp;<img id="newMessagesImg" src="' +
                requestContextPath+'/images/email_add.gif" />&nbsp;&nbsp;';
                titre += inner;
                addTooltip("newMessagesImg", '', getI18nResource("caqs.messages.newmessages"));
            }
            titre += '</span>';
            this.pageLoading = false;

            this.setPanelTitle(titre);
            putTooltips();
        }
    },

    getAllMessages: function(type) {
        var retour = new Array();
        for(var i=0; i<this.messagesToRender.length; i++) {
            var m = this.messagesToRender[i];
            if(m.taskId==type) {
                retour[retour.length] = m;
            }
        }
        for(var i=0; i<this.messagesAlreadyRendered.length; i++) {
            var m = this.messagesAlreadyRendered[i];
            if(m.taskId==type) {
                retour[retour.length] = m;
            }
        }
        return retour;
    },

    getPanelTitle: function() {
        var titre = getI18nResource("caqs.messages.nbMessTitle") +' '+this.nbMessages;
        titre += this.getMessagesTypeTitle();
        return titre;
    },

    setPanelTitle: function(titre) {
        this.setTitle(titre);
        var northPanelCollapsedTitle = document.getElementById('northCollapsedTitle');
        if(northPanelCollapsedTitle!=undefined) {
            northPanelCollapsedTitle.innerHTML = titre;
        }
        var northPanelTitleElt = Ext.select('span[@class=x-panel-header-text]', true, this.getEl().dom);
        var northPanelTitleSpan = undefined;
        if(northPanelTitleElt.elements.length > 0) {
            northPanelTitleSpan = northPanelTitleElt.elements[0].dom;
        }
        if(this.nbMessages > 0) {
            if(northPanelCollapsedTitle!=undefined) {
                var img = document.createElement('IMG');
                img.setAttribute('src', requestContextPath + '/images/tick.gif');
                img.setAttribute('class', 'imageAction');
                img.setAttribute('className', 'imageAction');
                img.setAttribute('id', 'markAllAsReadImg');
                northPanelCollapsedTitle.appendChild(img);
                var el = Ext.get(img);
                el.on('click', this.setAllMessagesAsSeen, this);
                addTooltip('markAllAsReadImg', '', getI18nResource("caqs.messages.setAllAsSeen"));
            }
            if(northPanelTitleSpan != undefined) {
                var img2 = document.createElement('IMG');
                img2.setAttribute('src', requestContextPath + '/images/tick.gif');
                img2.setAttribute('class', 'imageAction');
                img2.setAttribute('className', 'imageAction');
                img2.setAttribute('id', 'markAllAsReadImg2');
                northPanelTitleSpan.appendChild(img2);
                el = Ext.get(img2);
                el.on('click', this.setAllMessagesAsSeen, this);
                addTooltip('markAllAsReadImg2', '', getI18nResource("caqs.messages.setAllAsSeen"));
            }
        }
    },

    removeEnveloppe: function() {
        this.setPanelTitle(this.getPanelTitle());
    },

    getMessagesTypeTitle: function() {
        var titre = '&nbsp;';
        if(this.nbMessages>0) {
            titre += '( ';
            var previous = false;
            if(this.oldNbRunning>0) {
                titre += getI18nResource("caqs.messages.running") + ' : '+this.oldNbRunning;
                previous = true;
            }
            if(this.oldNbSuccess>0) {
                if(previous) {
                    titre += ', ';
                }
                titre += getI18nResource("caqs.messages.success") + ' : '+this.oldNbSuccess;
                previous = true;
            }
            if(this.oldNbFailed>0) {
                if(previous) {
                    titre += ', ';
                }
                titre += getI18nResource("caqs.messages.failed") + ' : '+this.oldNbFailed;
            }

            titre += ' )';
            titre += '&nbsp;&nbsp;';
        }
        return titre;
    },
    
    checkMessages: function(check) {
        Ext.Ajax.request({
            url:		requestContextPath + '/RetrieveMessages.do',
            success:	this.messagesRenderer,
            scope:      this
        });
        if(check!=='false') {
            var myscope = this;
            window.setTimeout(function(){
                myscope.checkMessages();
            }, 15000);
        }
    },

    initComponent : function(){
        this.on('collapse', this.onMessagesPanelCollapse, this);
        this.on('expand', this.onMessagesPanelExpand, this);
        var config = {
            region : 		this.messagesZone,
            autoScroll:		true,
            split:			true,
            title:			getI18nResource("caqs.messages.nbMessTitle") + ' 0',
            collapsedTitle:	getI18nResource("caqs.messages.nbMessTitle") + ' 0',
            collapsible: 	true,
            collapsed: 		true,
            floatable:		false,
            height:			100,
            minHeight:		100,
            maxHeight:		200
        };
        Ext.apply(this, Ext.apply(this.initialConfig, config));
        Ext.ux.MessagesPanel.superclass.initComponent.apply(this, arguments);

        this.messagesAlreadyRendered = new Array();
        this.messagesToRender = new Array();
        this.pageLoading = true;
        this.northPanelVisible = false;
        //gestion specifique a la generation de rapport
        this.progressBars = new Array();
    },

    onRender: function(ct, position){
        Ext.ux.MessagesPanel.superclass.onRender.call(this, ct, position);
        this.checkMessages();
    },

    onMessagesPanelCollapse: function(p) {
        this.northPanelVisible = false;
    },

    onMessagesPanelExpand: function(p) {
        this.northPanelVisible = true;
        for(var i=0; i<this.messagesToRender.length; i++) {
            var mess = this.messagesToRender[i];
            this.renderMessageFunction(mess);
            this.messagesToRender.splice(i,1);
            i--;
            this.messagesAlreadyRendered[this.messagesAlreadyRendered.length] = mess;
        }
        this.removeEnveloppe();
        this.doLayout();
        putTooltips();
    }
});