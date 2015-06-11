/*
 * Layouter.java
 *
 * Created on 27 juillet 2005, 10:04
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package com.compuware.caqs.presentation.applets.architecture.modellayout;

/**
 *
 * @author cwfr-fxalbouy
 */
public class Layouter implements Runnable {

    private Thread me;
    private int refreshSpeed = 200;

    /** Creates a new instance of Layouter */
    public Layouter(int refreshSpeed) {
        this.refreshSpeed = refreshSpeed;
    }

    public void run() {
        Thread me = Thread.currentThread();

        while (this.me == me) {
            Relaxer.springRelax();
            try {
                Thread.sleep(this.refreshSpeed);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void start() {
        this.me = new Thread(this);
        this.me.start();
    }

    public void stop() {
        this.me = null;
    }
}
