package org.loran.smartgwt.connectors.client;

import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.drawing.DrawCurve;
import com.smartgwt.client.widgets.drawing.DrawPane;
import com.smartgwt.client.widgets.layout.VStack;
import com.smartgwt.client.widgets.menu.Menu;
import com.smartgwt.client.widgets.menu.MenuBar;
import com.smartgwt.client.widgets.menu.MenuItem;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripMenuButton;

/**
 * @author cwfr-lizac
 * @version $Revision: 1.0 $
 */
public class MyDialog extends Window {

	/**
	 * Field dragLabel1.
	 */
	private DragLabel dragLabel1;
	/**
	 * Field dragLabel2.
	 */
	private DragLabel dragLabel2;
	/**
	 * Field dragLabel3.
	 */
	private DragLabel dragLabel3;
	private VStack vStack;
	private ToolStrip toolStrip;
	private ToolStripButton toolStripButton;
	private ToolStripMenuButton toolStripMenuButton;
	private MenuBar menuBar;
	private MenuItem menuItem;
	private MenuItem menuItem_1;
	private DrawPane drawpane;
	
	/**
	 * Constructor for MyDialog.
	 */
	public MyDialog() {
		setShowHeaderIcon(true);
		setShowFooter(true);
		setShowTitle(true);
		setShowStatusBar(true);
		setShowResizer(true);
		setShowModalMask(true);
		setTitle("New dialog");
		setIsModal(true);
		setModalMaskOpacity(30);
		setRedrawOnResize(true);
		setCanDragResize(true);
		setCanDragReposition(true);

		resizeTo(640, 480);
		addItem(getVStack());
	}


	/**
	 * Method getDragLabel1.
	
	 * @return DragLabel */
	private DragLabel getDragLabel1() {
		if (dragLabel1 == null) {
			dragLabel1 = new DragLabel("Shadow 1", 100,40);
			dragLabel1.setSize("100px", "40px");
		}
		return dragLabel1;
	}
	
	/**
	 * Method getDragLabel2.
	
	 * @return DragLabel */
	private DragLabel getDragLabel2() {
		if (dragLabel2 == null) {
			dragLabel2 = new DragLabel("Shadow 2", 300, 60);
			dragLabel2.setSize("100px", "40px");
		}
		return dragLabel2;
	}
	/**
	 * Method getDragLabel3.
	
	 * @return DragLabel */
	private DragLabel getDragLabel3() {
		if (dragLabel3 == null) {
			dragLabel3 = new DragLabel("Shadow 3", 200, 120);
			dragLabel3.setSize("100px", "40px");
		}
		return dragLabel3;
	}
	private VStack getVStack() {
		if (vStack == null) {
			vStack = new VStack();
			vStack.addMember(getToolStrip());

			/*
			getDrawPane().addChild(getDragLabel1());
			getDrawPane().addChild(getDragLabel2());
			getDrawPane().addChild(getDragLabel3());
			
			getDrawPane().addDrawItem(new Connector(getDragLabel1(), getDragLabel2()),true);

			getDrawPane().addDrawItem(new Connector(getDragLabel1(), getDragLabel3()),true);
*/
			vStack.addMember(getDrawPane());
		}
		return vStack;
	}
	
	
	private DrawPane getDrawPane() {
		if (drawpane == null) {
			drawpane = new DrawPane();
			drawpane.setWidth("400");
			drawpane.setHeight("300");
		}
		return drawpane;
	}
	private ToolStrip getToolStrip() {
		if (toolStrip == null) {
			toolStrip = new ToolStrip();
			toolStrip.setWidth("100%");
			toolStrip.addButton(getToolStripButton());
			toolStrip.addMenuButton(getToolStripMenuButton());
			toolStrip.addMember(getMenuBar());
		}
		return toolStrip;
	}
	private ToolStripButton getToolStripButton() {
		if (toolStripButton == null) {
			toolStripButton = new ToolStripButton("New Button");
		}
		return toolStripButton;
	}
	private ToolStripMenuButton getToolStripMenuButton() {
		if (toolStripMenuButton == null) {
			toolStripMenuButton = new ToolStripMenuButton("New MenuButton");
		}
		return toolStripMenuButton;
	}
	private MenuBar getMenuBar() {
		if (menuBar == null) {
			menuBar = new MenuBar();
			Menu menu = new Menu();
			menu.setTitle("Options");
			menu.addItem(getMenuItem());
			menu.addItem(getMenuItem_1());
			menuBar.setMenus(new Menu[] { menu});
		}
		return menuBar;
	}
	private MenuItem getMenuItem() {
		if (menuItem == null) {
			menuItem = new MenuItem("New MenuItem");
		}
		return menuItem;
	}
	private MenuItem getMenuItem_1() {
		if (menuItem_1 == null) {
			menuItem_1 = new MenuItem("New MenuItem");
		}
		return menuItem_1;
	}
}
