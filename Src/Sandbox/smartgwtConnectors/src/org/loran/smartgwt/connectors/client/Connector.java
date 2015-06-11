package org.loran.smartgwt.connectors.client;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.drawing.DrawCurve;
import com.smartgwt.client.widgets.drawing.Point;
import com.smartgwt.client.widgets.drawing.events.MouseOutEvent;
import com.smartgwt.client.widgets.drawing.events.MouseOutHandler;
import com.smartgwt.client.widgets.drawing.events.MouseOverEvent;
import com.smartgwt.client.widgets.drawing.events.MouseOverHandler;
import com.smartgwt.client.widgets.events.DragRepositionMoveEvent;
import com.smartgwt.client.widgets.events.DragRepositionMoveHandler;
import com.smartgwt.client.widgets.events.DragRepositionStartEvent;
import com.smartgwt.client.widgets.events.DragRepositionStartHandler;
import com.smartgwt.client.widgets.events.DragRepositionStopEvent;
import com.smartgwt.client.widgets.events.DragRepositionStopHandler;
import com.smartgwt.client.widgets.events.DragResizeMoveEvent;
import com.smartgwt.client.widgets.events.DragResizeMoveHandler;

/**
 * @author cwfr-lizac
 * @version $Revision: 1.0 $
 */
public class Connector extends DrawCurve {
	/**
	 * Field lineWidth.
	 */
	int lineWidth = 3;
	/**
	 * Field c2.
	 */
	/**
	 * Field c1.
	 */
	Canvas c1, c2;
	/**
	 * Field strokeColor.
	 */
	String strokeColor;

	/**
	 * Constructor for Connector.
	 * @param c1 Canvas
	 * @param c2 Canvas
	 * @param dc DrawingCanvas
	 */
	public Connector(Canvas c1, Canvas c2) {
		this.c1 = c1;
		this.c2 = c2;

		this.setFillColor("#7F92FF");
		this.setLineWidth(lineWidth);

		this.addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				DrawCurve curve = (DrawCurve) event.getSource();

				strokeColor = curve.getFillColor();
				curve.setFillColor("#FF7B00");
			}
		});
		
		this.addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				DrawCurve curve = (DrawCurve) event.getSource();
				curve.setFillColor(strokeColor);
			}
		});

		refresh();

		addHandlers(c1);
		addHandlers(c2);

//		c1.bringToFront();
//		c2.bringToFront();
	}

	/**
	 * Method addHandlers.
	 * @param c Canvas
	 */
	private void addHandlers(Canvas c) {
		c.addDragRepositionMoveHandler(new DragRepositionMoveHandler() {

			public void onDragRepositionMove(DragRepositionMoveEvent event) {
				refresh();
			}
		});

		c.addDragResizeMoveHandler(new DragResizeMoveHandler() {

			@Override
			public void onDragResizeMove(DragResizeMoveEvent event) {
				refresh();
			}
		});

		c.addDragRepositionStartHandler(new DragRepositionStartHandler() {

			@Override
			public void onDragRepositionStart(DragRepositionStartEvent event) {
				DrawCurve curve = (DrawCurve) event.getSource();
				strokeColor = curve.getFillColor();
				curve.setFillColor("#DAFF7F");
			}
		});

		c.addDragRepositionStopHandler(new DragRepositionStopHandler() {

			@Override
			public void onDragRepositionStop(DragRepositionStopEvent event) {
				DrawCurve curve = (DrawCurve) event.getSource();
				curve.setFillColor(strokeColor);
			}
		});
	}

	/**
	 * Method refresh.
	 */
	private void refresh() {
		int c1x, c1y, c2x, c2y;

		c1x = c1.getLeft() + (c1.getWidth() / 2);
		c2x = c2.getLeft() + (c2.getWidth() / 2);
		c1y = c1.getTop() + (c1.getHeight() / 2);
		c2y = c2.getTop() + (c2.getHeight() / 2);

		this.setStartPoint(new Point(c1x,c1y));
		this.setEndPoint(new Point(c2x,c2y));
	}

}
