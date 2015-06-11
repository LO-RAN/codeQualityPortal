package com.compuware.caqs.presentation.applets.scatterplot.draw;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;

import javax.swing.JOptionPane;

import org.jfree.chart.ChartMouseEvent;
import org.jfree.chart.ChartMouseListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;

import com.compuware.caqs.business.chart.resources.Messages;
import com.compuware.caqs.presentation.applets.scatterplot.data.Element;
import com.compuware.caqs.presentation.applets.scatterplot.data.ScatterPlotDataSet;
import com.compuware.caqs.presentation.applets.scatterplot.data.ScatterplotBubble;
import com.compuware.caqs.presentation.applets.scatterplot.util.ChartPanelUtils;

public class ScatterplotChartPanel extends ChartPanel implements MouseWheelListener {
	private static final long serialVersionUID = -8576047252200925831L;

	private JFreeChart 					jfreechart;
	private ScatterplotAppletContainer 	parent = null;
	/** The zoom rectangle (selected by the user with the mouse). */
	private transient Rectangle2D 		multiSelectRectangle = null;
	private Point 						startMultiSelectPoint = null;

	public ScatterplotChartPanel(JFreeChart c, ScatterplotAppletContainer p, Locale loc) {
		super(c, true);
		this.parent = p;
		this.jfreechart = c;
		this.setMouseZoomable(false);
		this.addChartMouseListener(new ScatterplotMouseListener(this.parent, this, loc));
		this.setPreferredSize(new Dimension(450,529));
		//this.setMinimumSize(new Dimension(500,490));
		this.addComponentListener(new ScatterplotComponentListener(this));
		this.addMouseWheelListener(this);
	}
	
	public JFreeChart getChart() {
		return this.jfreechart;
	}
	
	public void updateDrawingSize() {
		this.setMaximumDrawHeight(this.getHeight());
		this.setMaximumDrawWidth(this.getWidth());
		this.setMinimumDrawHeight(this.getHeight());
		this.setMinimumDrawWidth(this.getWidth());
	}

	private Graphics 	buffer = null;
	private Image 		image = null;
	public void paintComponent(Graphics g) {
		this.updateDrawingSize();
		if(buffer==null) {
			image = this.createImage(this.getWidth(), this.getHeight());
			buffer = image.getGraphics();
		}
		super.paintComponent(buffer);
		g.drawImage(image, 0, 0, this);
		g.dispose();
	}

	public void deleteImageBuffer() {
		buffer = null;
	}

	/**
	 * Draws zoom rectangle (if present).
	 * The drawing is performed in XOR mode, therefore
	 * when this method is called twice in a row,
	 * the second call will completely restore the state
	 * of the canvas.
	 * 
	 * @param g2 the graphics device. 
	 */
	private void drawZoomRectangle(Graphics2D g2) {
		// Set XOR mode to draw the zoom rectangle
		g2.setXORMode(Color.ORANGE);
		if(this.multiSelectRectangle!=null) {
			g2.fill(this.multiSelectRectangle);
		}
		// Reset to the default 'overwrite' mode
		g2.setPaintMode();
	}

	/**
	 * Handles a 'mouse dragged' event.
	 *
	 * @param e  the mouse event.
	 */
	private Point ctrlDownPoint = null;
	public void mouseDragged(MouseEvent e) {
		if(startMultiSelectPoint!=null && ((e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK)!=MouseEvent.CTRL_DOWN_MASK )) {
			Graphics2D g2 = (Graphics2D) getGraphics();

			// Erase the previous zoom rectangle (if any)...
			drawZoomRectangle((Graphics2D)buffer);

			Rectangle2D scaledDataArea = getScreenDataArea(
					(int) this.startMultiSelectPoint.getX(), (int) this.startMultiSelectPoint.getY());

			// selected rectangle shouldn't extend outside the data area...
			double startX = Math.min(this.startMultiSelectPoint.getX(), e.getX());
			double startY = Math.min(this.startMultiSelectPoint.getY(), e.getY());
			startX = Math.max(startX, scaledDataArea.getMinX());
			double endX = Math.max(e.getX(), this.startMultiSelectPoint.getX());
			double endY = Math.max(e.getY(), this.startMultiSelectPoint.getY());
			double xmax = Math.min(endX, scaledDataArea.getMaxX());
			double ymax = Math.min(endY, scaledDataArea.getMaxY());
			this.multiSelectRectangle = new Rectangle2D.Double(
					startX, startY,
					xmax - startX, ymax - startY);

			// Draw the new zoom rectangle...
			drawZoomRectangle((Graphics2D)buffer);
			g2.drawImage(image, 0, 0, this);
			g2.dispose();
		} else if(this.ctrlDownPoint!=null) {
			int translateX = e.getX() - (int)this.ctrlDownPoint.getX();
			int translateY = e.getY() - (int)this.ctrlDownPoint.getY();
			final Rectangle2D scaledDataArea = this.getScreenDataArea();
			ValueAxis axisY = ((XYPlot)this.getChart().getPlot()).getRangeAxis();
			ValueAxis axisX = ((XYPlot)this.getChart().getPlot()).getDomainAxis();
            double deltaY = axisY.getRange().getLength() / (scaledDataArea.getWidth())*translateY;
            double deltaX = axisX.getRange().getLength() / (scaledDataArea.getHeight())*translateX;
            axisX.setRange(axisX.getLowerBound()-deltaX,axisX.getUpperBound()-deltaX);
            axisY.setRange(axisY.getLowerBound()+deltaY,axisY.getUpperBound()+deltaY);
            this.ctrlDownPoint = new Point(e.getX(), e.getY());
		}
		e.consume();
	}

	/**
	 * Handles a 'mouse pressed' event.
	 * <P>
	 * This event is the popup trigger on Unix/Linux.  For Windows, the popup
	 * trigger is the 'mouse released' event.
	 *
	 * @param e  The mouse event.
	 */
	public void mousePressed(MouseEvent e) {
		if( (e.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK)==MouseEvent.CTRL_DOWN_MASK ) {
			this.ctrlDownPoint = new Point(e.getX(), e.getY());
		} else {
			this.ctrlDownPoint = null;
			if (this.multiSelectRectangle == null) {
				Rectangle2D screenDataArea = getScreenDataArea(e.getX(), e.getY());
				if (screenDataArea != null) {
					this.startMultiSelectPoint = getPointInRectangle(e.getX(), e.getY(), 
							screenDataArea);
				}
				else {
					this.startMultiSelectPoint = null;
				}
			}
		}
	}

	/**
	 * Returns a point based on (x, y) but constrained to be within the bounds
	 * of the given rectangle.  This method could be moved to JCommon.
	 * 
	 * @param x  the x-coordinate.
	 * @param y  the y-coordinate.
	 * @param area  the rectangle (<code>null</code> not permitted).
	 * 
	 * @return A point within the rectangle.
	 */
	private Point getPointInRectangle(int x, int y, Rectangle2D area) {
		x = (int) Math.max(Math.ceil(area.getMinX()), Math.min(x, 
				Math.floor(area.getMaxX())));
		y = (int) Math.max(Math.ceil(area.getMinY()), Math.min(y, 
				Math.floor(area.getMaxY())));
		return new Point(x, y);
	}

	/**
	 * Handles a 'mouse released' event.  On Windows, we need to check if this 
	 * is a popup trigger, but only if we haven't already been tracking a zoom
	 * rectangle.
	 *
	 * @param e  information about the event.
	 */
	public void mouseReleased(MouseEvent e) {
		this.ctrlDownPoint = null;
		if (this.startMultiSelectPoint != null && multiSelectRectangle!=null) {
			if( (e.getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != MouseEvent.SHIFT_DOWN_MASK) { 
				this.parent.reset();
			}
			Hashtable<com.compuware.caqs.presentation.applets.scatterplot.data.Point, ScatterplotBubble> bubbles = this.parent.getDatasRetriever().getPlotDatas().getAllBubbles();
			Rectangle2D rect = new Rectangle2D.Double();
			//les coordonnées y sont inversées sur l'écran min <==> max
			Point2D min = ChartPanelUtils.getInstance().translatePointFromScreenToChart(this, this.jfreechart.getXYPlot(), 
					this.multiSelectRectangle.getMinX(), this.multiSelectRectangle.getMaxY());
			Point2D max = ChartPanelUtils.getInstance().translatePointFromScreenToChart(this, this.jfreechart.getXYPlot(), 
					this.multiSelectRectangle.getMaxX(), this.multiSelectRectangle.getMinY());
			rect.setRect(min.getX(), min.getY(), max.getX() - min.getX(), max.getY() - min.getY());

			Enumeration<com.compuware.caqs.presentation.applets.scatterplot.data.Point> points = bubbles.keys();
			for(; points.hasMoreElements(); ) {
				com.compuware.caqs.presentation.applets.scatterplot.data.Point point = (com.compuware.caqs.presentation.applets.scatterplot.data.Point)points.nextElement();
				ScatterplotBubble bubble = bubbles.get(point);
				if(this.pointInsideRectangle(point, rect)) {
					if(!bubble.isSelected()) {
						bubble.setSelected(true);
					}
				}
			}
			this.parent.getEastPanel().majSelectedElementsList();

			this.startMultiSelectPoint = null;
			this.multiSelectRectangle = null;
			parent.getScatterplotPanel().redraw();
		}
		e.consume();
	}

	private boolean pointInsideRectangle(com.compuware.caqs.presentation.applets.scatterplot.data.Point point, Rectangle2D rect) {
		boolean retour = true;

		double x = (double)point.getX().intValue();
		double y = (double)point.getY().intValue();

		double rectMinX = rect.getMinX();
		double rectMaxX = rect.getMaxX();
		double rectMinY = rect.getMinY();
		double rectMaxY = rect.getMaxY();

		if(point!=null && rect!=null) {
			if(x<rectMinX) {
				retour = false;
			} else if(x>rectMaxX) {
				retour = false;
			} else if(y>rectMaxY) {
				retour = false;
			} else if(y<rectMinY) {
				retour = false;
			}
		} else {
			retour = false;
		}
		return retour;
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getWheelRotation()<0) {
			//zoom in
			this.zoomInBoth(e.getX(), e.getY());
		} else {
			//zoom out
			this.zoomOutBoth(e.getX(), e.getY());
		}
	}    
}

class ScatterplotMouseListener implements ChartMouseListener {
	private ScatterplotAppletContainer 	parent = null;
	private ScatterplotChartPanel		container = null;
	private final Locale				locale;

	public ScatterplotMouseListener(ScatterplotAppletContainer p, ScatterplotChartPanel s, Locale loc) {
		this.parent = p;
		this.locale = loc;
		this.container = s;
	}

	public void chartMouseClicked(ChartMouseEvent cme) {
		ChartEntity ce = cme.getEntity();
		if(cme.getTrigger().getButton() == MouseEvent.BUTTON2) {
			((XYPlot)this.container.getChart().getPlot()).getRangeAxis().setAutoRange(true);
			((XYPlot)this.container.getChart().getPlot()).getDomainAxis().setAutoRange(true);
		} else {
			if(ce!=null && (ce instanceof XYItemEntity)) {
				XYItemEntity xyEntity = (XYItemEntity) ce;
				XYDataset datas = xyEntity.getDataset();
				int serindex = xyEntity.getSeriesIndex();
				int itemindex = xyEntity.getItem(); 
				if(datas instanceof ScatterPlotDataSet) {
					ScatterPlotDataSet scatterplotDatas = (ScatterPlotDataSet) datas;
					if(cme.getTrigger().getButton() == MouseEvent.BUTTON1) {
						if( (cme.getTrigger().getModifiersEx() & MouseEvent.SHIFT_DOWN_MASK) != MouseEvent.SHIFT_DOWN_MASK) { 
							this.parent.reset();
						}
						scatterplotDatas.changeElementsSelection(serindex, itemindex);
						this.parent.getEastPanel().majSelectedElementsList();					
					} else if(cme.getTrigger().getButton() == MouseEvent.BUTTON3) {
						ScatterplotBubble bubble = scatterplotDatas.getElements(serindex, itemindex);
						StringBuffer buff = new StringBuffer();
						int cpt = 0;
						for(Element elt : bubble) {
							buff.append(elt.getLabel()).append('\n');
							cpt++;
							if(cpt==20) {
								buff.append("...");
								break;
							}
						}
						String val = buff.toString();
						JOptionPane.showMessageDialog(this.parent, val, Messages.getString("scatterplot.elementList", 
								this.locale), JOptionPane.INFORMATION_MESSAGE);
					}
				}
			}
		}
	}

	public void chartMouseMoved(ChartMouseEvent cme) {
		ChartEntity ce = cme.getEntity();
		if(ce!=null && (ce instanceof XYItemEntity)) {
			XYItemEntity xyEntity = (XYItemEntity) ce;
			XYDataset datas = xyEntity.getDataset();
			int serindex = xyEntity.getSeriesIndex();
			int itemindex = xyEntity.getItem(); 
			if(datas instanceof ScatterPlotDataSet) {
				ScatterPlotDataSet scatterplotDatas = (ScatterPlotDataSet) datas;
				ScatterplotBubble elements = scatterplotDatas.getElements(serindex, itemindex);
				StringBuffer elementsTooltipBuffer = new StringBuffer();
				elementsTooltipBuffer.append(Messages.getString("scatterplot.nbElements", this.locale)).append(" : ").append(elements.size());
				elementsTooltipBuffer.append(" (").append(this.parent.getDatasRetriever().getMetricX().getLib()).append(" : ").append(elements.getX());
				elementsTooltipBuffer.append(", ").append(this.parent.getDatasRetriever().getMetricY().getLib()).append(" : ").append(elements.getY()).append(")");
				String tooltip = elementsTooltipBuffer.toString();
				xyEntity.setToolTipText(tooltip);
			}
		}
	}

}

class ScatterplotComponentListener implements ComponentListener {
	private ScatterplotChartPanel parent = null;


	public ScatterplotComponentListener(ScatterplotChartPanel p) {
		this.parent = p;
	}	

	public void componentHidden(ComponentEvent arg0) {
	}

	public void componentMoved(ComponentEvent arg0) {
	}

	public void componentResized(ComponentEvent e) {
		parent.deleteImageBuffer();
		parent.repaint();
	}

	public void componentShown(ComponentEvent arg0) {
	}	
}