/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.compuware.caqs.domain.dataschemas;

/**
 *
 * @author cwfr-dzysman
 */
public class FavoriteBean {

    private String elementId;
    private int x;
    private int y;

    public String getElementId() {
        return elementId;
    }

    public void setElementId(String elementId) {
        this.elementId = elementId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(this.getElementId()).append(",").append(this.getX()).append(",").append(this.getY());
        return sb.toString();
    }

    public static FavoriteBean fromString(String s) {
        FavoriteBean fb = null;
        if (s.length() > 0) {
            fb = new FavoriteBean();
            if (s.indexOf(",") == -1) {
                fb.setElementId(s);
                fb.setX(0);
                fb.setY(0);
            } else {
                String tab[] = s.split(",");
                if (tab != null && tab.length == 3) {
                    fb.setElementId(tab[0]);
                    fb.setX(Integer.parseInt(tab[1]));
                    fb.setY(Integer.parseInt(tab[2]));
                }
            }
        }
        return fb;
    }
}
