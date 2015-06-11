package com.compuware.caqs.presentation.applets.architecture.panels.control;

import java.util.EventListener;

public interface ControlModeListener extends EventListener {
        public void modeChanged(int mode);
}
