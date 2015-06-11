package org.loran.smartgwt.connectors.client;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DragAppearance;
import com.smartgwt.client.widgets.Label;

/**
 * @author cwfr-lizac
 * @version $Revision: 1.0 $
 */
public class DragLabel extends Label {
	/**
	 * Constructor for DragLabel.
	 * @param contents String
	 * @param left int
	 * @param top int
	 */
	public DragLabel(String contents, int left, int top) {
		setTop(top);
		setLeft(left);
		setContents(contents);
		setAlign(Alignment.CENTER);
		setPadding(4);
		setShowEdges(true);
		setBackgroundColor("#EEEEEE");
		setKeepInParentRect(true);
		setCanDragReposition(true);
		setCanDragResize(true);
		setDragAppearance(DragAppearance.TARGET);
		setShowDragShadow(true);
		setIcon("person.png");
	}
}