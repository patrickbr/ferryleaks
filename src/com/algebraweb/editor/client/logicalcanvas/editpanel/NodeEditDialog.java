package com.algebraweb.editor.client.logicalcanvas.editpanel;

import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.PlanModelManipulator;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.scheme.GoAble;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.Widget;

public class NodeEditDialog extends DialogBox {

	private RemoteManipulationServiceAsync manServ;
	private PlanModelManipulator pmm;
	private int nid;
	private int pid;

	private NodeEditTree tree = new NodeEditTree();
	private PlanNode node = null;
	private Widget editPanelWidget = null;

	private HashMap<TreeItem, Composite> panels = new HashMap<TreeItem, Composite>();

	TabLayoutPanel t = new TabLayoutPanel(1.5, Unit.EM);

	NodeEditSourcePanel xmlEditPanel;
	LayoutPanel nodeEditPanel = new LayoutPanel();

	SplitLayoutPanel p = new SplitLayoutPanel();
	LayoutPanel v = new LayoutPanel();
	LayoutPanel h = new LayoutPanel();

	private AsyncCallback<PlanNode> nodeLoadCallBack = new AsyncCallback<PlanNode>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(PlanNode result) {

			node = result;
			Iterator<GoAble> schemas = result.getScheme().getSchema()
					.iterator();

			while (schemas.hasNext()) {
				GoAble currentSchema = schemas.next();
				if (currentSchema.isEditable()) {
					tree.addItem(new LogicalSchemeTreeItem(manServ,
							currentSchema, result));
				}
			}
		}
	};

	public NodeEditDialog(PlanModelManipulator pmm,
			RemoteManipulationServiceAsync manServ, int nid, final int pid) {

		super();
		super.setAnimationEnabled(true);
		super.setGlassEnabled(true);
		this.manServ = manServ;
		this.pmm = pmm;
		this.nid = nid;
		this.pid = pid;
		xmlEditPanel = new NodeEditSourcePanel(nid, pid, manServ, pmm);
		super.setText("Edit node #" + nid);
		t.setAnimationVertical(false);
		t.setAnimationDuration(500);

		p.setSize("550px", "350px");

		ScrollPanel treeScroll = new ScrollPanel(tree);

		p.addWest(treeScroll, 220);

		tree.addSelectionHandler(new SelectionHandler<TreeItem>() {

			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {

				if (event.getSelectedItem() instanceof ContentNodeTreeItem) {

					if (panels.containsKey(event.getSelectedItem())) {

						NodeEditDialog.this.setEditWidget(panels.get(event
								.getSelectedItem()));

					} else {

						PropertyEditPanel p = new PropertyEditPanel(pid,
								(ContentNodeTreeItem) event.getSelectedItem(),
								NodeEditDialog.this.manServ,
								((ContentNodeTreeItem) event.getSelectedItem())
										.getContentNode(),
								((ContentNodeTreeItem) event.getSelectedItem())
										.getScheme(), node);
						panels.put(event.getSelectedItem(), p);

						NodeEditDialog.this.setEditWidget(p);

					}

				} else if (event.getSelectedItem() instanceof LogicalSchemeTreeItem) {

					if (panels.containsKey(event.getSelectedItem())) {

						NodeEditDialog.this.setEditWidget(panels.get(event
								.getSelectedItem()));

					} else {

						SchemeEditPanel p = new SchemeEditPanel(
								(LogicalSchemeTreeItem) event.getSelectedItem(),
								NodeEditDialog.this.manServ,
								((LogicalSchemeTreeItem) event
										.getSelectedItem()).getScheme(), node);
						panels.put(event.getSelectedItem(), p);

						NodeEditDialog.this.setEditWidget(p);

					}

				}
			}
		});

		nodeEditPanel.add(p);

		t.add(nodeEditPanel, "Properties");

		Button ok = new Button("Save");
		Button cancel = new Button("cancel");
		HorizontalPanel hh = new HorizontalPanel();

		cancel.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				NodeEditDialog.this.hide();

			}
		});

		ok.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				NodeEditDialog.this.save();

			}
		});

		t.add(xmlEditPanel, "Source");
		t.setSize("550px", "350px");

		ok.getElement().getStyle().setMargin(10, Unit.PX);
		cancel.getElement().getStyle().setMargin(10, Unit.PX);
		cancel.getElement().getStyle().setMarginLeft(0, Unit.PX);

		hh.add(ok);
		hh.add(cancel);

		v.setSize("550px", "400px");
		v.add(t);
		v.add(hh);
		v.setWidgetBottomHeight(hh, 0, Unit.PX, 50, Unit.PX);

		super.add(v);
		super.show();
		super.center();
		pmm.blurr(true);

		load();

	}

	public void clearEditWidget() {

		if (editPanelWidget != null) {
			p.remove(editPanelWidget);
		}
		editPanelWidget = null;

	}

	@Override
	public void hide() {

		pmm.blurr(false);
		super.hide();

	}

	private void load() {

		manServ.getPlanNode(nid, pid, nodeLoadCallBack);

	}

	// TODO: this should be in the PlanModelManipulator

	public void save() {

		if (t.getSelectedIndex() == 0) {

			Iterator<Composite> it = panels.values().iterator();

			while (it.hasNext()) {

				Composite current = it.next();

				if (current instanceof PropertyEditPanel) {
					((PropertyEditPanel) current).save();
				}

			}

			pmm.updateNodeContent(pid, node);
		} else {
			xmlEditPanel.save();
		}

		this.hide();
	}

	public void setEditWidget(Widget w) {

		clearEditWidget();
		p.add(w);

		editPanelWidget = w;

	}

}
