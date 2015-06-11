package com.compuware.caqs.presentation.applets.scatterplot.draw;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.EntityCollection;
import org.jfree.chart.plot.CrosshairState;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.PlotRenderingInfo;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBubbleRenderer;
import org.jfree.chart.renderer.xy.XYItemRendererState;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYZDataset;
import org.jfree.ui.RectangleEdge;

import com.compuware.caqs.presentation.applets.scatterplot.data.ScatterPlotDataSet;
import com.compuware.caqs.presentation.applets.scatterplot.data.ScatterplotBubble;

public class ScatterplotBubbleRenderer extends XYBubbleRenderer {
	private static final double MIN_WIDTH = 7.0;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2586544514486217831L;
	private ScatterPlotDataSet datas = null;
	
	public ScatterplotBubbleRenderer(ScatterPlotDataSet d) {
		super(XYBubbleRenderer.SCALE_ON_BOTH_AXES);
		this.datas = d;
	}
	
	public Paint getItemPaint(int row, int column) {
		Paint 				p = null;
		ScatterplotBubble 	elts = this.datas.getElements(row, column);
		if(elts==null) {
			p = Color.PINK;
		} else {
			if(elts.isSelected()) {
				p = Color.RED;
			} else {
				p = this.getSeriesPaint(row);
			}
		}
		return p;
    }
	
	public Paint getSeriesPaint(int row) {
		Paint retour = null;
		switch(row) {
		case 0 :
			retour = Color.BLUE;
			break;
		case 1 :
			retour = Color.YELLOW;
			break;
		case 2 :
			retour = Color.LIGHT_GRAY;
			break;
		case 3 :
			retour = Color.BLACK;
			break;
		default :
			retour = Color.WHITE;
			break;
		}
		return retour;
	}

	public void setDatas(ScatterPlotDataSet datas) {
		this.datas = datas;
	}
	
	 /**
     * Draws the visual representation of a single data item.
     *
     * @param g2  the graphics device.
     * @param state  the renderer state.
     * @param dataArea  the area within which the data is being drawn.
     * @param info  collects information about the drawing.
     * @param plot  the plot (can be used to obtain standard color 
     *              information etc).
     * @param domainAxis  the domain (horizontal) axis.
     * @param rangeAxis  the range (vertical) axis.
     * @param dataset  the dataset (an {@link XYZDataset} is expected).
     * @param series  the series index (zero-based).
     * @param item  the item index (zero-based).
     * @param crosshairState  crosshair information for the plot 
     *                        (<code>null</code> permitted).
     * @param pass  the pass index.
     */
    public void drawItem(Graphics2D g2, XYItemRendererState state,
            Rectangle2D dataArea, PlotRenderingInfo info, XYPlot plot,
            ValueAxis domainAxis, ValueAxis rangeAxis, XYDataset dataset, 
            int series, int item, CrosshairState crosshairState, int pass) {

        // return straight away if the item is not visible
        if (!getItemVisible(series, item)) {
            return;   
        }
        
        PlotOrientation orientation = plot.getOrientation();
        
        // get the data point...
        double x = dataset.getXValue(series, item);
        double y = dataset.getYValue(series, item);
        double z = Double.NaN;
        if (dataset instanceof XYZDataset) {
            XYZDataset xyzData = (XYZDataset) dataset;
            z = xyzData.getZValue(series, item);
        }
        if (!Double.isNaN(z)) {
            RectangleEdge domainAxisLocation = plot.getDomainAxisEdge();
            RectangleEdge rangeAxisLocation = plot.getRangeAxisEdge();
            double transX = domainAxis.valueToJava2D(x, dataArea, 
                    domainAxisLocation);
            double transY = rangeAxis.valueToJava2D(y, dataArea, 
                    rangeAxisLocation);

            double transDomain = 0.0;
            double transRange = 0.0;
            double zero;

            switch(getScaleType()) {
                case SCALE_ON_DOMAIN_AXIS:
                    zero = domainAxis.valueToJava2D(0.0, dataArea, 
                            domainAxisLocation);
                    transDomain = domainAxis.valueToJava2D(z, dataArea, 
                            domainAxisLocation) - zero;
                    transRange = transDomain;
                    break;
                case SCALE_ON_RANGE_AXIS:
                    zero = rangeAxis.valueToJava2D(0.0, dataArea, 
                            rangeAxisLocation);
                    transRange = zero - rangeAxis.valueToJava2D(z, dataArea, 
                            rangeAxisLocation);
                    transDomain = transRange;
                    break;
                default:
                	//Point2D zeroP = ChartPanelUtils.getInstance().translatePointFromChartToScreen(plot, 0.0, 0.0);
                    double zero1 = domainAxis.valueToJava2D(0.0, dataArea, 
                            domainAxisLocation);
                    double zero2 = rangeAxis.valueToJava2D(0.0, dataArea, 
                            rangeAxisLocation);
                    transDomain = domainAxis.valueToJava2D(z, dataArea, 
                            domainAxisLocation) - zero1;
                    transRange = zero2 - rangeAxis.valueToJava2D(z, dataArea, 
                            rangeAxisLocation);
            }
            transDomain = Math.abs(transDomain);
            transRange = Math.abs(transRange);
            double width = Math.min(transDomain, transRange);
            width = Math.max(width, ScatterplotBubbleRenderer.MIN_WIDTH);
            width = (int)width;
            Ellipse2D circle = null;
            if (orientation == PlotOrientation.VERTICAL) {
                circle = new Ellipse2D.Double(transX - width / 2.0, 
                        transY - width / 2.0, width, width);
            }
            else if (orientation == PlotOrientation.HORIZONTAL) {
                circle = new Ellipse2D.Double(transY - width / 2.0, 
                        transX - width / 2.0, width, width);
            }
            g2.setPaint(getItemPaint(series, item));
            g2.fill(circle);
            g2.setStroke(getItemOutlineStroke(series, item));
            g2.setPaint(getItemOutlinePaint(series, item));
            g2.draw(circle);

            if (isItemLabelVisible(series, item)) {
                if (orientation == PlotOrientation.VERTICAL) {
                    drawItemLabel(g2, orientation, dataset, series, item, 
                            transX, transY, false);
                }
                else if (orientation == PlotOrientation.HORIZONTAL) {
                    drawItemLabel(g2, orientation, dataset, series, item, 
                            transY, transX, false);                
                }
            }
            
            // add an entity if this info is being collected
            EntityCollection entities = null;
            if (info != null) {
                entities = info.getOwner().getEntityCollection();
                if (entities != null && circle!=null && circle.intersects(dataArea)) {
                    addEntity(entities, circle, dataset, series, item, 
                            circle.getCenterX(), circle.getCenterY());
                }
            }

            int domainAxisIndex = plot.getDomainAxisIndex(domainAxis);
            int rangeAxisIndex = plot.getRangeAxisIndex(rangeAxis);
            updateCrosshairValues(crosshairState, x, y, domainAxisIndex, 
                    rangeAxisIndex, transX, transY, orientation);
        }

    }


}
