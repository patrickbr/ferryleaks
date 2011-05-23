package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * A panel-widget that provides mouse dragging. Will fill the whole client-area.
 * Uses the browser standard scrolling-mechanisms, so scrolling will still remain
 * possible if mouse dragging isn't supported. (In that case, the widget will be 
 * fully transparent.)
 * 
 * Childs are added using a standard FlowLayout (position:inline / float:left)
 * To prevent dragging for a specific period of time,
 * use static preventDrag() and unPreventDrag() 
 * 
 * @author Patrick Brosi
 *
 */

public class DragPanel extends FlowPanel {


	private int mainDragOffsetX = -1;
	private int mainDragOffsetY = -1;

	private int mainDragOffsetLeft = -1;
	private int mainDragOffsetTop = -1;

	private static boolean dragPreventer = false;
	private boolean lockMainDrag = false;


	private MouseUpHandler mouseUpHandler = new MouseUpHandler() {

		@Override
		public void onMouseUp(MouseUpEvent event) {
			DragPanel.this.clearDrag();
			dragPreventer = false;
		}
	};

	private MouseOutHandler mouseOutHandler = new MouseOutHandler() {

		@Override
		public void onMouseOut(MouseOutEvent event) {
			DragPanel.this.clearDrag();
			dragPreventer = false;
		}
	};

	private MouseDownHandler mouseDownHandler = new MouseDownHandler() {

		@Override
		public void onMouseDown(MouseDownEvent event) {
			if (mainDragOffsetX < 0 && ! dragPreventer) 
				startMainDrag(event.getScreenX(), event.getScreenY());
		}

	};

	private MouseMoveHandler mouseMoveHandler = new MouseMoveHandler() {

		@Override
		public void onMouseMove(MouseMoveEvent event) {

			if (mainDragOffsetX > -1 && ! dragPreventer) {

				//we don't want the browser to start its own drag&drop
				DOM.eventGetCurrentEvent().preventDefault();
				DragPanel.this.scrollTo(mainDragOffsetLeft -(event.getScreenX() -mainDragOffsetX ) , mainDragOffsetTop -(event.getScreenY() -mainDragOffsetY ));
			}
		}
	};

	public void onLoad() {					

		this.addDomHandler(mouseUpHandler, MouseUpEvent.getType());
		this.addDomHandler(mouseDownHandler, MouseDownEvent.getType());
		this.addDomHandler(mouseMoveHandler, MouseMoveEvent.getType());
		this.addDomHandler(mouseOutHandler, MouseOutEvent.getType());

	}

	public void center(){

		this.scrollTo((int)(this.getOffsetWidth()/2 - Window.getClientWidth()/2), (int)(this.getOffsetHeight()/2) -  Window.getClientHeight()/2);

	}

	public void center(int width, int height){


		this.scrollTo((int)(width/2 - Window.getClientWidth()/2), (int)(height/2) -  Window.getClientHeight()/2);


	}



	private void startMainDrag(int x, int y) {

		if (lockMainDrag) return;

		mainDragOffsetLeft = Window.getScrollLeft();
		mainDragOffsetX = x;

		mainDragOffsetTop = Window.getScrollTop();
		mainDragOffsetY = y;

		this.addStyleName("movy");

	}


	private void clearDrag() {

		mainDragOffsetX = -1;
		mainDragOffsetY = -1;

		this.removeStyleName("movy");

	}


	private native void scrollTo(int x,int y) /*-{ 
	$wnd.scrollTo(x, y); }-*/;



	@Override
	public boolean remove(Widget child) {

		return super.remove(child);

	}

	@Override
	public void add(Widget child) {

		//we have to handle the child's mouse event in order to start
		//dragging anywhere on the panel

		child.addDomHandler(mouseUpHandler, MouseUpEvent.getType());
		child.addDomHandler(mouseDownHandler, MouseDownEvent.getType());
		child.addDomHandler(mouseMoveHandler, MouseMoveEvent.getType());
		super.add(child);
	}

	/**
	 * Prevent panel-dragging until unPreventDrag() is called. Since 
	 * the drag-panel is designed to fill the whole browser-area, this is
	 * a static method.
	 */
	public static void preventDrag() {

		dragPreventer = true;

	}

	/**
	 * Allow dragging.
	 */

	public static void unPreventDrag() {

		dragPreventer = false;

	}


}
