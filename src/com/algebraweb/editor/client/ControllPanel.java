package com.algebraweb.editor.client;


import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.InlineSorter;
import com.algebraweb.editor.client.graphcanvas.SimpleSorter;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorter;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;


/**
 * A panel with some testing buttons. Highly experimental.
 * 
 * TODO: This will be changed into to tool panel some time...
 * @author Patrick Brosi
 *
 */

public class ControllPanel extends AbsolutePanel{


	private int dragOffsetX;
	private int dragOffsetY;
	private boolean dragging=false;

	private GraphCanvas c;

	public ControllPanel(int width, int height,GraphCanvas g) {

		super();

		this.c=g;
				
		Button sortB = new Button("Sort circle");
		
		sortB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new RemoteSorter());
				
			}});
		
		
		this.add(sortB,140,60);
		
	Button sortBB = new Button("Sort simple");
		
		sortBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new SimpleSorter());
				
			}});
		
		
		this.add(sortBB,60,60);
		
	Button sortBBB = new Button("Sort simple");
		
		sortBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new InlineSorter());
				
			}});
		
		
		this.add(sortBBB,260,60);
		
		
	Button sortBBBB = new Button("Delete selected");
		
		sortBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.deleteNode(c.getSelectedNode());
				
			}});
		
		
		this.add(sortBBBB,260,260);
		
	Button sortBBBBB = new Button("Load testnodes");
		
		sortBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				makeTest();
				
			}});
		
		
		this.add(sortBBBBB,160,260);
		
		
	Button sortBBBBBB = new Button("Zoom in");
		
		sortBBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.zoom(((1 / c.getScale()) * 100) + 10);
				
			}});
		
		
		this.add(sortBBBBBB,20,220);
		
Button sortBBBBBBB = new Button("Zoom out");
		
		sortBBBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.zoom(((1 / c.getScale()) * 100) - 10);
				
			}});
		
		
		this.add(sortBBBBBBB,20,260);
		

		this.setStylePrimaryName("controllpanel");

		this.getElement().getStyle().setPosition(Position.FIXED);



		addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {

				ControllPanel.this.dragging = false;
				c.removeStyleName("movy");
				removeStyleName("movy");
			}


		}, MouseUpEvent.getType());


		MouseMoveHandler mm=new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				
				if (ControllPanel.this.dragging) {
					ControllPanel.this.doDrag(event.getClientX(), event.getClientY());
				}

			}


		};	
		
		addDomHandler(mm,MouseMoveEvent.getType());

		c.addDomHandler(mm,MouseMoveEvent.getType());
		
		



		addDomHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {
				
				ControllPanel.this.c.addStyleName("movy");
				ControllPanel.this.addStyleName("movy");
				dragStart(event.getX(), event.getY());

			}

		}, MouseDownEvent.getType());

	}

	public void doDrag(int x, int y) {
		
		x=x - dragOffsetX;
		y=y - dragOffsetY;
		
		if (x<0) x=0;
		if (y<0) y=0;
		
		if (x+this.getOffsetWidth() > Window.getClientWidth()) x= Window.getClientWidth() -this.getOffsetWidth(); 
		if (y+this.getOffsetHeight() > Window.getClientHeight()) y= Window.getClientHeight() -this.getOffsetHeight(); 


		this.getElement().getStyle().setLeft(x, Style.Unit.PX);
		this.getElement().getStyle().setTop(y, Style.Unit.PX);

	}


	private void dragStart(int x,int y) {

		dragOffsetX = x;
		dragOffsetY =y;
		dragging=true;

	}
	

	
	private void makeTest() {
		
		
		GraphCanvasRemoteFillingMachine f = new GraphCanvasRemoteFillingMachine(c);
		
			
		f.fill(new RemoteFiller());
		
		
			
		
	}




}
