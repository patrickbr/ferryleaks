package com.algebraweb.editor.client;




import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorter;
import com.algebraweb.editor.client.logicalcanvas.AddSQListenerDIalog;
import com.algebraweb.editor.client.logicalcanvas.CreateSQLDialog;
import com.algebraweb.editor.client.logicalcanvas.CreateXMLDialog;
import com.algebraweb.editor.client.logicalcanvas.EvaluationDialog;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.logicalcanvas.NodeEditDialog;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;

/**
 * A panel with some testing buttons. Highly experimental.
 * 
 * TODO: This will be changed into to tool panel some time...
 * @author Patrick Brosi
 *
 */

public class ControllPanel extends AbsolutePanel{


	final UploadDialog d;


	private PlanModelManipulator m;
	private AlgebraEditor e;
	private RemoteManipulationServiceAsync rmsa;

	public ControllPanel(AlgebraEditor e,PlanModelManipulator man,int width, int height,final RemoteManipulationServiceAsync rmsa) {

		super();

		this.m=man;

		this.rmsa = rmsa;
		this.e=e;


		LayoutPanel l = new LayoutPanel();
		l.getElement().getStyle().setOverflow(Overflow.HIDDEN);

		l.setHeight("550px");
		NumberedStackLayoutPanel p = new NumberedStackLayoutPanel(Unit.PX);



		FlowPanel editPanel = new FlowPanel();

		ControllPanelButton addNodeButton = new ControllPanelButton("Add new node","add");

		addNodeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				m.getNodeTypes();


			}});

		editPanel.add(addNodeButton);


		ControllPanelButton addEdgeButton = new ControllPanelButton("Add new edge","add-edge");

		addEdgeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				ControllPanel.this.e.getActiveCanvas().enterEdgeAddingMode();


			}});

		editPanel.add(addEdgeButton);

		ControllPanelButton delete = new ControllPanelButton("Delete selected nod","delete");

		delete.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				m.deleteNode(ControllPanel.this.e.getActiveCanvas().getSelectedNode().keySet().toArray(new Integer[0]), ControllPanel.this.e.getActiveCanvas().getId());
				m.deleteEdges(ControllPanel.this.e.getActiveCanvas().getSelectedEdgesWithPos(), ControllPanel.this.e.getActiveCanvas().getId());

			}});

		editPanel.add(delete);

		ControllPanelButton edit = new ControllPanelButton("Edit selected node","edit");

		edit.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				new NodeEditDialog(m,ControllPanel.this.rmsa,ControllPanel.this.e.getActiveCanvas().getSelectedNode().values().iterator().next().getId(),ControllPanel.this.e.getActiveCanvas().getId());

			}});

		editPanel.add(edit);

		ControllPanelButton xml = new ControllPanelButton("Get XML for selected node","xml");

		xml.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				ControllPanel.this.rmsa.getXMLFromPlanNode(ControllPanel.this.e.getActiveCanvas().getId(),ControllPanel.this.e.getActiveCanvas().getSelectedNode().values().iterator().next().getId(), xmlCb);
			}});

		editPanel.add(xml);

		ControllPanelButton xmlPlan = new ControllPanelButton("Get XML plan beginning with selected node","xml-down");

		xmlPlan.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new CreateXMLDialog(ControllPanel.this.e.getActiveCanvas().getId(),ControllPanel.this.e.getActiveCanvas().getSelectedNode().keySet().toArray(new Integer[0])[0],rmsa);
			}});

		editPanel.add(xmlPlan);



		ControllPanelButton sqlB = new ControllPanelButton("Get SQL of subgraph","sql-down");

		sqlB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				new CreateSQLDialog(ControllPanel.this.e.getActiveCanvas().getId(),ControllPanel.this.e.getActiveCanvas().getSelectedNode().keySet().toArray(new Integer[0])[0],rmsa);
				//ControllPanel.this.rmsa.getSQLFromPlanNode(ControllPanel.this.e.getActiveCanvas().getId(), ControllPanel.this.e.getActiveCanvas().getSelectedNode().values().iterator().next().getId(), sqlCb);
			}});

		editPanel.add(sqlB);

		ControllPanelButton evalB = new ControllPanelButton("Evaluate node","eva");

		evalB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				new EvaluationDialog(ControllPanel.this.e.getActiveCanvas().getId(),ControllPanel.this.e.getActiveCanvas().getSelectedNode().keySet().toArray(new Integer[0])[0],rmsa);
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
				int pid = ControllPanel.this.e.getActiveCanvas().getId();
				Window.open(GWT.getModuleBaseURL() + "fileserver?pid="+ pid , "_self", "");
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


	private GraphCanvasCommunicationCallback<String> xmlCb = new GraphCanvasCommunicationCallback<String>() {

		@Override
		public void onSuccess(String result) {


			Window.alert(result);


		}

	};



}
