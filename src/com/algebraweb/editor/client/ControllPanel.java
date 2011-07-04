package com.algebraweb.editor.client;


import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	private boolean dragging=false;
	final UploadDialog d;


	private PlanModelManipulator m;
	private AlgebraEditor e;
	private RemoteManipulationServiceAsync rmsa;

	public ControllPanel(AlgebraEditor e,PlanModelManipulator man,int width, int height,RemoteManipulationServiceAsync rmsa) {

		super();

		this.m=man;
	
		this.rmsa = rmsa;
		this.e=e;


		LayoutPanel l = new LayoutPanel();
		l.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		l.setHeight("500px");
		NumberedStackLayoutPanel p = new NumberedStackLayoutPanel(Unit.PX);



		FlowPanel editPanel = new FlowPanel();

		ControllPanelButton addNodeButton = new ControllPanelButton("Add new node","add");

		addNodeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ControllPanel.this.rmsa.getNodeTypes(nodeTypesCb);


			}});

		editPanel.add(addNodeButton);

		ControllPanelButton delete = new ControllPanelButton("Delete selected nod","delete");

		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				m.deleteNode(ControllPanel.this.e.getActiveCanvas().getSelectedNode().getId(), ControllPanel.this.e.getActiveCanvas().getId());

			}});

		editPanel.add(delete);

		ControllPanelButton edit = new ControllPanelButton("Edit selected node","edit");

		edit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				//TODO: planid is fix
				new NodeEditDialog(m,ControllPanel.this.rmsa,ControllPanel.this.e.getActiveCanvas().getSelectedNode().getId(),ControllPanel.this.e.getActiveCanvas().getId());

			}});

		editPanel.add(edit);

		ControllPanelButton xml = new ControllPanelButton("Get XML for selected node","xml");

		xml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ControllPanel.this.rmsa.getXMLFromPlanNode(ControllPanel.this.e.getActiveCanvas().getId(),ControllPanel.this.e.getActiveCanvas().getSelectedNode().getId(), xmlCb);
			}});

		editPanel.add(xml);

		ControllPanelButton xmlPlan = new ControllPanelButton("Get XML plan beginning with selected node","xml-down");

		xmlPlan.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ControllPanel.this.rmsa.getXMLLogicalPlanFromRootNode(ControllPanel.this.e.getActiveCanvas().getId(),ControllPanel.this.e.getActiveCanvas().getSelectedNode().getId(), xmlCb);
			}});

		editPanel.add(xmlPlan);

		ControllPanelButton sqlB = new ControllPanelButton("Get SQL of subgraph","sql-down");

		sqlB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ControllPanel.this.rmsa.getSQLFromPlanNode(ControllPanel.this.e.getActiveCanvas().getId(), ControllPanel.this.e.getActiveCanvas().getSelectedNode().getId(), sqlCb);
			}});

		editPanel.add(sqlB);

		ControllPanelButton evalB = new ControllPanelButton("Evaluate node","eva");

		evalB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ControllPanel.this.rmsa.eval(ControllPanel.this.e.getActiveCanvas().getId(), ControllPanel.this.e.getActiveCanvas().getSelectedNode().getId(), evalCb);
			}});

		editPanel.add(evalB);





		p.add(editPanel,"Edit",30);


		FlowPanel sortPanel = new FlowPanel();

		ControllPanelButton dotSort = new ControllPanelButton("Sort with dot","sort-dot");

		dotSort.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ControllPanel.this.e.getActiveCanvas().sort(new RemoteSorter("dot"));

			}});


		sortPanel.add(dotSort);

		ControllPanelButton sortC = new ControllPanelButton("circle","sort-circle");

		sortC.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ControllPanel.this.e.getActiveCanvas().sort(new RemoteSorter("circle"));

			}});

		sortPanel.add(sortC);


		ControllPanelButton sortBBB = new ControllPanelButton("line","sort-line");

		sortBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ControllPanel.this.e.getActiveCanvas().sort(new RemoteSorter("inline"));

			}});

		sortPanel.add(sortBBB);


		p.add(sortPanel,"Sort",30);



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
				ControllPanel.this.e.getActiveCanvas().zoom(((1 / ControllPanel.this.e.getActiveCanvas().getScale()) * 100) + 10);

			}});



		Button sortBBBBBBB = new Button("-");

		sortBBBBBBB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ControllPanel.this.e.getActiveCanvas().zoom(((1 / ControllPanel.this.e.getActiveCanvas().getScale()) * 100) - 10);

			}});


		d= new UploadDialog(this,e);




		ControllPanelButton uploadButton = new ControllPanelButton("Upload XML plan", "upload");

		uploadButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				d.center();
				d.show();

			}});







		FlowPanel ioPanel = new FlowPanel();

		ioPanel.add(uploadButton);
		ioPanel.add(downloadPlan);

		p.add(ioPanel,"I/O",30);


		this.setStylePrimaryName("controllpanel");

		this.getElement().getStyle().setPosition(Position.FIXED);


		l.add(p);
		this.add(l);





	}






	public LogicalCanvas getC() {
		return ControllPanel.this.e.getActiveCanvas();
	}

	public PlanModelManipulator getM() {
		return m;
	}


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


	private AsyncCallback<String> sqlCb = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(String result) {


			Window.alert(result);


		}

	};



	private AsyncCallback<String[]> nodeTypesCb = new AsyncCallback<String[]>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(String[] result) {


			new NodeTypeSelector(result, ControllPanel.this.e.getActiveCanvas());


		}

	};

	private  AsyncCallback<ArrayList<HashMap<String,String>>> evalCb = new  AsyncCallback<ArrayList<HashMap<String,String>>>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(ArrayList<HashMap<String,String>> result) {


			new SqlResDialog(result);


		}

	};


}
