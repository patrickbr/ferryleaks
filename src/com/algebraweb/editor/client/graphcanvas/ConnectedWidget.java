package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * A widget connected to a plan node.
 * 
 * @author Patrick brosi
 * 
 */
public class ConnectedWidget extends FlowPanel {
	private Widget w;
	private int x;
	private int y;

	public ConnectedWidget(Widget w, int x, int y) {
		this.x = x;
		this.y = y;
		this.w = w;

		this.getElement().getStyle().setPosition(Position.ABSOLUTE);
		this.getElement().getStyle().setHeight(1, Unit.PX);
		this.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		this.add(w);
	}

	public Widget getWidget() {
		return w;
	}

	/**
	 * Returns the relative x position of this widget
	 * 
	 * @return the x position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the relative y position of this widget
	 * 
	 * @return the y position
	 */
	public int getY() {
		return y;
	}
}