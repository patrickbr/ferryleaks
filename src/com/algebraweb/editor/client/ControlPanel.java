package com.algebraweb.editor.client;

import com.algebraweb.editor.client.dialogs.LogicialPlanUploadDialog;
import com.algebraweb.editor.client.dialogs.NumberedStackLayoutPanel;
import com.algebraweb.editor.client.dialogs.TextPresentationDialog;
import com.algebraweb.editor.client.graphcanvas.EditorCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorter;
import com.algebraweb.editor.client.logicalcanvas.CreateXMLDialog;
import com.algebraweb.editor.client.logicalcanvas.EvaluatePlanDialog;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.dialogs.OkPanel;
import com.algebraweb.editor.client.logicalcanvas.editpanel.NodeEditDialog;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.LayoutPanel;

/**
 * The editor's control panel. ControllPanelButtons can be added to this.
 * 
 * @author Patrick Brosi
 * 
 */
public class ControlPanel extends AbsolutePanel {
	final LogicialPlanUploadDialog d;

	private PlanModelManipulator m;
	private RemoteManipulationServiceAsync rmsa;

	private EditorCommunicationCallback<String> xmlCb = new EditorCommunicationCallback<String>(
			"getting XML") {

		@Override
		public void onSuccess(String result) {
			GraphCanvas.hideLoading();
			new TextPresentationDialog("XML source", result);
		}
	};

	private EditorCommunicationCallback<String> sqlCb = new EditorCommunicationCallback<String>(
			"compiling SQL") {
		@Override
		public void onSuccess(String result) {
			GraphCanvas.hideLoading();
			new TextPresentationDialog("Compiled SQL", result);
		}
	};

	public ControlPanel(AlgebraEditor e, PlanModelManipulator man, int width,
			int height, final RemoteManipulationServiceAsync rmsa) {
		super();
		this.m = man;
		this.rmsa = rmsa;
		LayoutPanel l = new LayoutPanel();
		l.getElement().getStyle().setOverflow(Overflow.HIDDEN);
		l.setHeight("550px");
		NumberedStackLayoutPanel p = new NumberedStackLayoutPanel(Unit.PX);
		FlowPanel editPanel = new FlowPanel();
		ControlPanelButton addNodeButton = new ControlPanelButton(
				"Add new node", "add");
		addNodeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				m.showNodeTypes();
			}
		});

		editPanel.add(addNodeButton);
		ControlPanelButton addEdgeButton = new ControlPanelButton(
				"Add new edge", "add-edge");

		addEdgeButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getC().enterEdgeAddingMode();
			}
		});

		editPanel.add(addEdgeButton);
		ControlPanelButton deleteButton = new ControlPanelButton(
				"Delete selected nod", "delete");

		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				m.deleteNode(getC().getSelectedNodes().keySet().toArray(
						new Integer[0]), AlgebraEditor.getActiveView().getId());
				m.deleteEdges(getC().getSelectedEdgesWithPos(), AlgebraEditor
						.getActiveView().getId());

			}
		});

		editPanel.add(deleteButton);
		ControlPanelButton editButton = new ControlPanelButton(
				"Edit selected node", "edit");

		editButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getC().getSelectedNodes().size() > 0) {
					new NodeEditDialog(m, ControlPanel.this.rmsa, getC()
							.getSelectedNodes().values().iterator().next()
							.getId(), AlgebraEditor.getActiveView().getId());
				}else{
					OkPanel p = new OkPanel("No node(s) selected!", "Warning"); 
					p.center();
					p.show();
					
				}
			}
		});

		editPanel.add(editButton);
		ControlPanelButton xmlButton = new ControlPanelButton(
				"Get XML for selected node", "xml");

		xmlButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getC().getSelectedNodes().size() > 0) {
					GraphCanvas.showLoading("Getting XML...");
					ControlPanel.this.rmsa.getXMLFromPlanNode(AlgebraEditor
							.getActiveView().getId(), AlgebraEditor
							.getActiveView().getSelectedNodes().values()
							.iterator().next().getId(), xmlCb);
				}else{
					OkPanel p = new OkPanel("No node(s) selected!","Warning"); 
					p.show();
					p.center();
				}
			}
		});

		editPanel.add(xmlButton);
		ControlPanelButton xmlPlanButton = new ControlPanelButton(
				"Get XML plan beginning with selected node", "xml-down");

		xmlPlanButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (getC().getSelectedNodes().size() > 0) {
					new CreateXMLDialog(getC().getId(), AlgebraEditor
							.getActiveView().getSelectedNodes().keySet()
							.toArray(new Integer[0])[0], rmsa);
				}else{
					OkPanel p = new OkPanel("No node(s) selected!", "Warning"); 
					p.center();
					p.show();
				}
			}
		});

		editPanel.add(xmlPlanButton);
		ControlPanelButton sqlButton = new ControlPanelButton("Get SQL",
				"sql-down");

		sqlButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GraphCanvas.showLoading("Getting SQL...");
				rmsa.getSQLFromPlan(getC().getId(), sqlCb);
			}
		});

		editPanel.add(sqlButton);
		ControlPanelButton evaluationButton = new ControlPanelButton(
				"Evaluate node", "eva");

		evaluationButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new EvaluatePlanDialog(getC().getId(), rmsa);
			}
		});

		editPanel.add(evaluationButton);
		p.add(editPanel, "Edit", 30);
		FlowPanel sortPanel = new FlowPanel();
		ControlPanelButton dotSortButton = new ControlPanelButton(
				"Sort with dot", "sort-dot");
		dotSortButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getC().sort(new RemoteSorter("dot"));
			}
		});

		sortPanel.add(dotSortButton);
		ControlPanelButton sortCircleButton = new ControlPanelButton("circle",
				"sort-circle");
		sortCircleButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getC().sort(new RemoteSorter("circle"));
			}
		});

		sortPanel.add(sortCircleButton);
		ControlPanelButton sortLineButton = new ControlPanelButton("line",
				"sort-line");
		sortLineButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				getC().sort(new RemoteSorter("inline"));
			}
		});

		sortPanel.add(sortLineButton);
		p.add(sortPanel, "Sort", 30);
		ControlPanelButton downloadPlanButton = new ControlPanelButton(
				"Download plan", "download-plan");
		downloadPlanButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				GraphCanvas.showLoading("Preparing file...");
				Window.open(GWT.getModuleBaseURL() + "fileserver?pid=-1",
						"_self", "");
				GraphCanvas.hideLoading();
			}
		});

		ControlPanelButton getSvgButton = new ControlPanelButton(
				"Get SVG of current plan", "svg-plan");
		getSvgButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				new TextPresentationDialog(
						"SVG of plan #" + getC().getId(),
						"<?xml version=\"1.0\" encoding=\"UTF-8\"?><!DOCTYPE svg PUBLIC \"-//W3C//DTD SVG 1.1//EN\" \"http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd\">"
								+ ((LogicalCanvas) getC()).getRaphaelElement()
										.getInnerHTML());
			}
		});

		d = new LogicialPlanUploadDialog(this, e);
		ControlPanelButton uploadButton = new ControlPanelButton(
				"Upload XML plan", "upload");
		uploadButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				d.center();
				d.show();
			}
		});

		FlowPanel ioPanel = new FlowPanel();
		ioPanel.add(uploadButton);
		ioPanel.add(downloadPlanButton);
		ioPanel.add(getSvgButton);
		p.add(ioPanel, "I/O", 30);
		this.setStylePrimaryName("controllpanel");
		this.getElement().getStyle().setPosition(Position.FIXED);
		l.add(p);
		this.add(l);
	}

	public AlgebraEditorCanvasView getC() {
		return AlgebraEditor.getActiveView();
	}

	public PlanModelManipulator getM() {
		return m;
	}
}
