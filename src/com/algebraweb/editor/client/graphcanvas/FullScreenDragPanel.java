package com.algebraweb.editor.client.graphcanvas;



import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.Style.Visibility;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
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

public class FullScreenDragPanel extends FlowPanel {


	private static int mainDragOffsetX = -1;
	private static int mainDragOffsetY = -1;

	public static void clearDrag() {
		mainDragOffsetX = -1;
		mainDragOffsetY = -1;
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
	private int mainDragOffsetLeft = -1;

	private int mainDragOffsetTop = -1;


	private static boolean dragPreventer = false;

	private boolean lockMainDrag = false;

	private Coordinate scrollPosition = new Coordinate();

	private MouseUpHandler mouseUpHandler = new MouseUpHandler() {
		@Override
		public void onMouseUp(MouseUpEvent event) {
			FullScreenDragPanel.clearDrag();
			dragPreventer = false;
		}
	};

	private MouseOutHandler mouseOutHandler = new MouseOutHandler() {
		@Override
		public void onMouseOut(MouseOutEvent event) {
			FullScreenDragPanel.clearDrag();
			dragPreventer = false;
		}
	};

	private MouseDownHandler mouseDownHandler = new MouseDownHandler() {
		@Override
		public void onMouseDown(MouseDownEvent event) {
			if (mainDragOffsetX < 0 && ! dragPreventer) 
				startMainDrag(event.getScreenX(), event.getScreenY());
			else if (dragPreventer) FullScreenDragPanel.clearDrag();
		}
	};

	private MouseMoveHandler mouseMoveHandler = new MouseMoveHandler() {
		@Override
		public void onMouseMove(MouseMoveEvent event) {
			if (mainDragOffsetX > -1 && ! dragPreventer) {

				//we don't want the browser to start its own drag&drop
				DOM.eventGetCurrentEvent().preventDefault();
				FullScreenDragPanel.this.scrollTo(mainDragOffsetLeft -(event.getScreenX() -mainDragOffsetX ) , mainDragOffsetTop -(event.getScreenY() -mainDragOffsetY ));
			}else if (dragPreventer) FullScreenDragPanel.clearDrag();
		}
	};

	public FullScreenDragPanel() {
		sinkEvents(Event.KEYEVENTS);
		scrollPosition.setX(0);
		scrollPosition.setY(0);
		this.addStyleName("dragpanel");
	}

	@Override
	public void add(Widget child) {
		child.addDomHandler(mouseUpHandler, MouseUpEvent.getType());
		child.addDomHandler(mouseDownHandler, MouseDownEvent.getType());
		child.addDomHandler(mouseMoveHandler, MouseMoveEvent.getType());
		super.add(child);
	}

	public void center(){
		this.scrollTo((this.getOffsetWidth()/2 - Window.getClientWidth()/2), (this.getOffsetHeight()/2) -  Window.getClientHeight()/2);
	}

	public void center(int width, int height){
		this.scrollTo((width/2 - Window.getClientWidth()/2), (height/2) -  Window.getClientHeight()/2);
	}

	public void changeSavedScrollPos(int x, int y) {
		scrollPosition  = new Coordinate(x,y);
	}


	public void hide() {
		scrollPosition = new Coordinate(Window.getScrollLeft(),Window.getScrollTop());
		super.getElement().getStyle().setVisibility(Visibility.HIDDEN);
		super.getElement().getStyle().setWidth(1, Unit.PX);
		super.getElement().getStyle().setHeight(1, Unit.PX);
		super.getElement().getStyle().setOverflow(Overflow.HIDDEN);
	}

	@Override
	public void onLoad() {					
		this.addDomHandler(mouseUpHandler, MouseUpEvent.getType());
		this.addDomHandler(mouseDownHandler, MouseDownEvent.getType());
		this.addDomHandler(mouseMoveHandler, MouseMoveEvent.getType());
		this.addDomHandler(mouseOutHandler, MouseOutEvent.getType());
	}


	@Override
	public boolean remove(Widget child) {
		return super.remove(child);
	}

	private native void scrollTo(int x,int y) /*-{ 
	$wnd.scrollTo(x, y); }-*/;

	public void scrollToUpperLeft() {
		scrollTo(0,0);
	}

	public void show() {
		super.getElement().getStyle().setVisibility(Visibility.VISIBLE);
		super.getElement().getStyle().clearWidth();
		super.getElement().getStyle().clearHeight();
		super.getElement().getStyle().setOverflow(Overflow.VISIBLE);
		Window.scrollTo((int)scrollPosition.getX(), (int)scrollPosition.getY());
	}

	private void startMainDrag(int x, int y) {
		if (lockMainDrag) return;

		mainDragOffsetLeft = Window.getScrollLeft();
		mainDragOffsetX = x;

		mainDragOffsetTop = Window.getScrollTop();
		mainDragOffsetY = y;
	}

}
