package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.hydro4ge.raphaelgwt.client.Raphael.Shape;

public class ConnectedWidget extends FlowPanel{


	private Widget w;
	private int x;
	private int y;

	public ConnectedWidget(Widget w, int x, int y) {

		this.x=x;
		this.y=y;
		this.w=w;

		this.getElement().getStyle().setPosition(Position.ABSOLUTE);
		this.getElement().getStyle().setHeight(1, Unit.PX);
		this.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		this.add(w);
		
	}

	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public Widget getWidget() {
		return w;
	}



}
