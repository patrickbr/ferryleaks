package com.algebraweb.editor.client;


import gwtupload.client.IUploader;
import gwtupload.client.MultiUploader;
import gwtupload.client.SingleUploader;
import gwtupload.client.Uploader;
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
import com.algebraweb.editor.client.logicalcanvas.NodeEditDialog;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Cursor;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.StackPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
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
	private PlanModelManipulator m;
	private RemoteManipulationServiceAsync rmsa;

	public ControllPanel(PlanModelManipulator man,int width, int height,LogicalCanvas g,RemoteManipulationServiceAsync rmsa) {

		super();

		this.m=man;
		this.c=g;
		this.rmsa = rmsa;
		

		LayoutPanel l = new LayoutPanel();
		l.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		l.setHeight("500px");
		NumberedStackLayoutPanel p = new NumberedStackLayoutPanel(Unit.PX);
	
	

		FlowPanel editPanel = new FlowPanel();
		
		ControllPanelButton addNodeButton = new ControllPanelButton("Add new node","add");

		addNodeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.enterNodeAddingMode("attach");

			}});

		editPanel.add(addNodeButton);

		ControllPanelButton delete = new ControllPanelButton("Delete selected nod","delete");

		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				m.deleteNode(c.getSelectedNode().getId(), 0);

			}});

		editPanel.add(delete);

		ControllPanelButton edit = new ControllPanelButton("Edit selected node","edit");

		edit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				//TODO: planid is fix
				new NodeEditDialog(m,ControllPanel.this.rmsa,c.getSelectedNode().getId(),0);

			}});

		editPanel.add(edit);
		
		ControllPanelButton xml = new ControllPanelButton("Get XML for selected node","xml");

		xml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				ControllPanel.this.rmsa.getXMLFromPlanNode(0,c.getSelectedNode().getId(), xmlCb);
			}});

		editPanel.add(xml);
		
		ControllPanelButton xmlPlan = new ControllPanelButton("Get XML plan beginning with selected node","xml-down");

		xmlPlan.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				ControllPanel.this.rmsa.getXMLLogicalPlanFromRootNode(0,c.getSelectedNode().getId(), xmlCb);
			}});

		editPanel.add(xmlPlan);



		p.add(editPanel,"Edit",30);


		FlowPanel sortPanel = new FlowPanel();

		ControllPanelButton dotSort = new ControllPanelButton("Sort with dot","sort-dot");

		dotSort.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new RemoteSorter("dot"));

			}});


		sortPanel.add(dotSort);

		ControllPanelButton sortC = new ControllPanelButton("circle","sort-circle");

		sortC.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new RemoteSorter("circle"));

			}});

		sortPanel.add(sortC);


		ControllPanelButton sortBBB = new ControllPanelButton("line","sort-line");

		sortBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.sort(new RemoteSorter("inline"));

			}});

		sortPanel.add(sortBBB);


		p.add(sortPanel,"Sort",30);


		ControllPanelButton sortBBBBB = new ControllPanelButton("Load testnodes","testnodes");

		sortBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				makeTest();

			}});
		
		ControllPanelButton downloadPlan = new ControllPanelButton("Download plan","download-plan");

		downloadPlan.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				GraphCanvas.showLoading("Preparing file...");
				Window.open(GWT.getModuleBaseURL() + "fileserver?pid=0" , "_self", "");
				GraphCanvas.hideLoading();

			}});


		

		Button sortBBBBBB = new Button("+");

		sortBBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.zoom(((1 / c.getScale()) * 100) + 10);

			}});



		Button sortBBBBBBB = new Button("-");

		sortBBBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				c.zoom(((1 / c.getScale()) * 100) - 10);

			}});







		SingleUploader defaultUploader = new SingleUploader();
		defaultUploader.setAutoSubmit(true);
		defaultUploader.setWidth("160px");
		defaultUploader.getFileInput().setLength(20);
		
		FlowPanel ioPanel = new FlowPanel();
		
		ioPanel.add(defaultUploader);
		ioPanel.add(sortBBBBB );
		ioPanel.add(downloadPlan);
		
		p.add(ioPanel,"I/O",30);


		defaultUploader.addOnFinishUploadHandler(onFinishUploaderHandler);
		defaultUploader.addOnStartUploadHandler(onStartUploaderHandler);

		Uploader.setStatusInterval(100);

		this.setStylePrimaryName("controllpanel");

		this.getElement().getStyle().setPosition(Position.FIXED);


		l.add(p);
		this.add(l);





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
	
			ControllPanel.this.awaitingFileUpload = Long.toString(System.currentTimeMillis());
			
			String servPath = uploader.getServletPath().split("\\?")[0];
			
			GWT.log(servPath);
			uploader.setServletPath(servPath+ "?myinfo=" + awaitingFileUpload);

		}


	};

	
	
	private AsyncCallback<String> xmlCb = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(String result) {


			Window.alert(result);


		}

	};


}
