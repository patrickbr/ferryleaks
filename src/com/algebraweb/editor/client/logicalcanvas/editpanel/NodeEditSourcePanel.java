package com.algebraweb.editor.client.logicalcanvas.editpanel;

import com.algebraweb.editor.client.PlanModelManipulator;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextArea;

public class NodeEditSourcePanel extends LayoutPanel {

	private TextArea editArea = new TextArea();
	private RemoteManipulationServiceAsync manServ;
	private PlanModelManipulator pmm;
	private int nid;
	private int pid;

	private AsyncCallback<String> xmlCb = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(String result) {
			editArea.setText(result);

		}

	};

	public NodeEditSourcePanel(int nid, int pid,
			RemoteManipulationServiceAsync manServ, PlanModelManipulator pmm) {

		super();

		this.manServ = manServ;
		this.pmm = pmm;
		this.nid = nid;
		this.pid = pid;

		this.manServ.getXMLFromPlanNode(pid, nid, xmlCb);
		editArea.addStyleName("sourceedit");
		super.add(editArea);

	}

	public void save() {

		pmm.updateNodeContent(pid, nid, editArea.getText());

	}

}
