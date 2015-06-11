package com.compuware.caqs.flow;
/*
 * © 2002 Compuware Corporation. All rights reserved.
 * Unpublished - rights reserved under the Copyright Laws of the United States.
 */

/* Disclaimer
 * You have a royalty-free right to use, modify, reproduce and distribute this 
 * sample code (and/or any modified version) in any way you find useful,
 * provided that you agree that Compuware has no warranty obligations or 
 * liability for any sample code which has been modified.
 */
/**
 * Demo of a Java batch task component
 */

public class BTCOMP {

  public BTCOMP() {}

  public String DOIT(String batin) {
    return batin + " OK.";
  }

  public double DOITDOUBLE(double batin1, double batin2) {
    return batin1+batin2;
  }

}
