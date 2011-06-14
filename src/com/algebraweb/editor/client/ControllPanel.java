package com.algebraweb.editor.client;


import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.IUploadStatus.Status;

import com.algebraweb.editor.client.graphcanvas.ConnectedShape;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.client.graphcanvas.remotefiller.GraphCanvasRemoteFillingMachine;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFiller;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFillingService;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFillingServiceAsync;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorter;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
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
import com.google.gwt.user.client.ui.HTML;
import com.hydro4ge.raphaelgwt.client.Raphael.Text;

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
	private String awaitingFileUpload;
	private boolean dragging=false;

	private LogicalCanvas c;
	PlanModelManipulator m;
	RemoteManipulationServiceAsync rmsa;
	
	public ControllPanel(int width, int height,LogicalCanvas g,RemoteManipulationServiceAsync rmsa) {

		super();

		this.c=g;
		this.rmsa = rmsa;
		this.m= new PlanModelManipulator(c,rmsa);

		Button sortB = new Button("Sort using dot");

		sortB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new RemoteSorter("dot"));

			}});


		this.add(sortB,40,100);

		Button sortC = new Button("Sort as a circle");

		sortC.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new RemoteSorter("circle"));

			}});


		this.add(sortC,40,130);


		Button sortBBB = new Button("Sort inline");

		sortBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new RemoteSorter("inline"));

			}});


		this.add(sortBBB,40,70);


		Button sortBBBB = new Button("Delete selected");

		sortBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				m.deleteNode(c.getSelectedNode().getId(), 0);

			}});


		this.add(sortBBBB,260,260);

		Button sortBBBBB = new Button("Load testnodes");

		sortBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				makeTest();

			}});


		this.add(sortBBBBB,260,230);


		Button sortBBBBBB = new Button("+");

		sortBBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.zoom(((1 / c.getScale()) * 100) + 10);

			}});


		this.add(sortBBBBBB,60,220);

		Button sortBBBBBBB = new Button("-");

		sortBBBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.zoom(((1 / c.getScale()) * 100) - 10);

			}});


		this.add(sortBBBBBBB,40,220);
		
		
		
		Button validate = new Button("Revalidate");

		validate.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				
				m.validate(0);

			}});


		this.add(validate,40,260);

		SingleUploader defaultUploader = new SingleUploader();
		defaultUploader.setAutoSubmit(true);
		defaultUploader.setWidth("160px");
		defaultUploader.getFileInput().setLength(20);
		this.add(new HTML("Upload XML File:"),40,170);


		this.add(defaultUploader,40,190);


		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.addOnStartUploadHandler(onStartUploaderHandler);


		this.setStylePrimaryName("controllpanel");

		this.getElement().getStyle().setPosition(Position.FIXED);



		addDomHandler(new MouseUpHandler() {

			@Override
			public void onMouseUp(MouseUpEvent event) {

				ControllPanel.this.dragging = false;

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


		f.fill(new RemoteFiller("random"),null);




	}



	private IUploader.OnFinishUploaderHandler onFinishUploaderHandler = new IUploader.OnFinishUploaderHandler() {
		public void onFinish(IUploader uploader) {
			if (uploader.getStatus() == Status.SUCCESS) {

				if (ControllPanel.this.awaitingFileUpload.equals(uploader.getServerInfo().message)) {

					GraphCanvasRemoteFillingMachine f = new GraphCanvasRemoteFillingMachine(c);

					ControllPanel.this.awaitingFileUpload = "";
					f.fill(new RemoteFiller("xml"), new GraphManipulationCallback() {
						
						@Override
						public void onComplete() {
							m.validate(0);
							
						}
					});
					
				}

			}
		}
	};

	private IUploader.OnStartUploaderHandler onStartUploaderHandler = new IUploader.OnStartUploaderHandler() {

		@Override
		public void onStart(IUploader uploader) {

			GWT.log("gurr:" + uploader.getFileName());
			ControllPanel.this.awaitingFileUpload = uploader.getFileName();



		}


	};



}
