/*
 * ProcessListener.java
 *
 * Created on August 11, 2004, 5:05 PM
 */

package com.compuware.toolbox.util.process.listener;

import com.compuware.toolbox.util.process.ProcessDescriptor;
/**
 *
 * @author  cwfr-fxalbouy
 */

public abstract class AbstractProcessListener {
    abstract public void updateInfo(ProcessDescriptor aProcess);
}
