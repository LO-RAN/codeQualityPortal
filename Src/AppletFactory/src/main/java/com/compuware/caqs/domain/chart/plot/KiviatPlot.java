package com.compuware.caqs.domain.chart.plot;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.SpiderWebPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.util.TableOrder;

public class KiviatPlot extends SpiderWebPlot {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5510347964065274633L;
	
	private int REAL_SERIES_NUM;

	public KiviatPlot(CategoryDataset catds, int realSeriesNum) {
		super(catds);
                REAL_SERIES_NUM = realSeriesNum;
	}
    
    /**
     * Draws a radar plot polygon.
     * 
     * @param g2 the graphics device.
     * @param plotArea the area we are plotting in (already adjusted).
     * @param centre the centre point of the radar axes
     * @param info chart rendering info.
     * @param series the series within the dataset we are plotting
     * @param catCount the number of categories per radar plot
     * @param headH the data point height
     * @param headW the data point width
     */
    protected void drawRadarPoly(Graphics2D g2, 
                                 Rectangle2D plotArea,
                                 Point2D centre,
                                 PlotRenderingInfo info,
                                 int series, int catCount,
                                 double headH, double headW) {

        Polygon polygon = new Polygon();

        EntityCollection entities = null;
        if (info != null) {
            entities = info.getOwner().getEntityCollection();
        }

        // plot the data...
        for (int cat = 0; cat < catCount; cat++) {
            Number dataValue = getPlotValue(series, cat);

            if (dataValue != null) {
                double value = dataValue.doubleValue();
  
                if (value >= 0) { // draw the polygon series...
              
                    // Finds our starting angle from the centre for this axis

                    double angle = getStartAngle()
                        + (getDirection().getFactor() * cat * 360 / catCount);

                    // The following angle calc will ensure there isn't a top 
                    // vertical axis - this may be useful if you don't want any 
                    // given criteria to 'appear' move important than the 
                    // others..
                    //  + (getDirection().getFactor() 
                    //        * (cat + 0.5) * 360 / catCount);

                    // find the point at the appropriate distance end point 
                    // along the axis/angle identified above and add it to the
                    // polygon

                    Point2D point = getWebPoint(plotArea, angle, 
                            ((value - 1) * 4/3) / getMaxValue());
                    polygon.addPoint((int) point.getX(), (int) point.getY());
                    
                    // put an elipse at the point being plotted..

                    Paint paint = getSeriesPaint(series);
                    Paint outlinePaint = getSeriesOutlinePaint(series);
                    Stroke outlineStroke = getSeriesOutlineStroke(series);

                    Ellipse2D head = new Ellipse2D.Double(point.getX() 
                            - headW / 2, point.getY() - headH / 2, headW, 
                            headH);
                    g2.setPaint(paint);
                    g2.fill(head);
                    g2.setStroke(outlineStroke);
                    g2.setPaint(outlinePaint);
                    g2.draw(head);

                    if (entities != null) {
                        String tip = null;
                        if (getToolTipGenerator() != null) {
                            tip = getToolTipGenerator().generateToolTip(
                                    getDataset(), series, cat);
                        }

                        String url = null;
                        if (getURLGenerator() != null) {
                            url = getURLGenerator().generateURL(getDataset(), 
                                   series, cat);
                        } 
                   
                        Shape area = new Rectangle((int) (point.getX() - headW), 
                                (int) (point.getY() - headH), 
                                (int) (headW * 2), (int) (headH * 2));
                        CategoryItemEntity entity = new CategoryItemEntity(
                                area, tip, url, getDataset(), series,
                                getDataset().getColumnKey(cat), cat); 
                        entities.add(entity);                                
                    }

                    // then draw the axis and category label, but only on the 
                    // first time through.....
                    if (series == REAL_SERIES_NUM) {
                    	g2.drawString(""+value, (float)point.getX() + 2, (float)point.getY() - 2);
                    }
                    
                    if (series == 0) {
                        Point2D endPoint = getWebPoint(plotArea, angle, 1); 
                                                             // 1 = end of axis
                        Line2D  line = new Line2D.Double(centre, endPoint);
                        g2.draw(line);
                        drawLabel(g2, plotArea, value, cat, angle, 
                                360.0 / catCount);
                    }
                }
            }
        }
        // Plot the polygon
    
        Paint paint = getSeriesPaint(series);
        g2.setPaint(paint);
        g2.draw(polygon);

        // Lastly, fill the web polygon if this is required
    
        if (isWebFilled()) {
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 
                    0.1f));
            g2.fill(polygon);
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 
                    getForegroundAlpha()));
        }
    }    
    
    /**
     * Returns the value to be plotted at the interseries of the 
     * series and the category.  This allows us to plot
     * BY_ROW or BY_COLUMN which basically is just reversing the
     * definition of the categories and data series being plotted
     * 
     * @param series the series to be plotted 
     * @param cat the category within the series to be plotted
     * 
     * @return The value to be plotted
     */
    public Number getPlotValue(int series, int cat) {
        Number value = null;
        if (getDataExtractOrder() == TableOrder.BY_ROW) {
            value = getDataset().getValue(series, cat);
        }
        else if (getDataExtractOrder() == TableOrder.BY_COLUMN) {
            value = getDataset().getValue(cat, series);
        }
        return value;
    }
    
}
