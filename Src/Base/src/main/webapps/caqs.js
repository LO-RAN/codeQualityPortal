Caqs = {};

Caqs.Labellisation = function() {
   return {
        setLabellisationInfos: function(tooltip, label, icon, scope) {
            if(scope.labelButton != undefined) {
                scope.labelButton.setTooltip(tooltip);
                scope.labelButton.setText(label);
                scope.labelButton.setIcon(requestContextPath+icon);
            }
        },

        launchLabellisation: function(scope) {
            if(scope.labelisationWnd == undefined) {
                scope.labelisationWnd = new Ext.ux.CaqsLabellisationWindow({
                    caqsSynthesisFrame:        scope
                });
            }
            scope.labelisationWnd.setLabelId(scope.idLabel);
            scope.labelisationWnd.show();
        }
   };
}();

Caqs.Computation = function() {
    redirectScope:      undefined;
    
    return {
        recalculRedirect: function() {
            //Caqs.Computation.redirectScope.location.href = requestContextPath + '/gestionqualite/nowComputing.jsp';
            Caqs.Messages.checkMessages();
            Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().hideMask();
            Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().setNowComputingMask();
        },

        askLaunchRecalcul: function(redirectScope) {
            this.redirectScope = redirectScope;
            Ext.MessageBox.confirm('', getI18nResource("caqs.synthese.confirmRecalcul"),
                Caqs.Computation.launchRecalcul);
        },

        launchRecalcul: function(result) {
            if(result=='yes') {
                Caqs.Portal.getCaqsPortal().getGestionQualiteActivity().showMask();
                Ext.Ajax.request({
                    url: requestContextPath + '/GestionQualiteRecalcul.do',
                    params: {
                        'metrics': 'false'
                    }
                });
                window.setTimeout(function(){
                    Caqs.Computation.recalculRedirect();
                }, 2000);
            }
        },

        isComputing: function(idElt) {
            var retour = false;
            var messagesArray = Caqs.Portal.getCaqsPortal().getMessagesPanel().getAllMessages('COMPUTING');
            for(var i=0; i<messagesArray.length && !retour; i++) {
                var m = messagesArray[i];
                if(m.status=='IN_PROGRESS' && m.eltId==idElt) {
                    retour = true;
                }
            }
            return retour;
        }
    };
}();

Caqs.Portal = function() {
    return {
        maskShown: undefined,
        
        getCaqsPortal: function() {
            var safeGuard = 0;
            var retour = window.caqsPortal;
            var currentParent = window.parent;
            while(retour == undefined && currentParent!=null && safeGuard < 10) {
                if(currentParent!=null) {
                    retour = currentParent.caqsPortal;
                    currentParent = currentParent.parent;
                    safeGuard++;
                }
            }
            return retour;
        },

        hasMaskShown: function() {
            return (this.maskShown != undefined);
        },

        setShownMask: function(m) {
            this.maskShown = m;
        },

        hideShownMask: function() {
            this.maskShown.hide();
            this.maskShown = undefined;
        },

        showGlobalLoadingMask: function() {
            this.maskShown = globalMaskOnBody;
            globalMaskOnBody.show();
        },

        hideGlobalLoadingMask: function() {
            this.maskShown = undefined;
            globalMaskOnBody.hide();
        },

        setCurrentScreen: function(id) {
            Caqs.Portal.getCaqsPortal().setCurrentScreenId(id);
        }
    };
}();

Caqs.Messages = function() {
    return {
        checkMessages: function() {
            Caqs.Portal.getCaqsPortal().messagesPanel.checkMessages('false');
        }
    };
}();