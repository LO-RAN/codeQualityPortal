package com.compuware.caqs.presentation.applets.architecture.panels.graphpanels;

import java.util.EventListener;
import java.util.EventObject;

public interface ViewListener extends EventListener {
        public void viewChanged(EventObject e);
}
