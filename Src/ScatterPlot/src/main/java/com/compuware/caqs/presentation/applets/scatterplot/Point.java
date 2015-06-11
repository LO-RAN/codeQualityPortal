package com.compuware.caqs.presentation.applets.scatterplot;

/**
 * Titre : <p>
 * Description : <p>
 * Copyright : Copyright (c) <p>
 * Société : <p>
 * @author
 * @version 1.0
 */


public class Point {
  String m_name;
  double m_x;
  double m_y;

  public Point(String name, double x, double y) {
    this.m_name = name;
    this.m_x = x;
    this.m_y = y;
  }
}