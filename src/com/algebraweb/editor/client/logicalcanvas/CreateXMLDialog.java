package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.dialogs.TextPresentationDialog;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;

/**
 * The dialog for XMl creation
 * 
 * @author Patrick Brosi
 * 
 */
public class CreateXMLDialog extends CreateEvaluationContextDialog {
	private CheckBox cb;
	private AsyncCallback<String> xmlCb = new AsyncCallback<String>() {
		@Override
		public void onFailure(Throwable caught) {
		}

		@Override
		public void onSuccess(String result) {
			GraphCanvas.hideLoading();
			new TextPresentationDialog("XML source", result);
		}
	};

	public CreateXMLDialog(int pid, int nid,
			RemoteManipulationServiceAsync manServ) {
		super(pid, nid, manServ);
		super.setText("XML generation");
		cb = new CheckBox();
		cb.setText("Save for current node");
		addButton(cb);
	}

	@Override
	protected void submit() {
		EvaluationContext c = saveContext();
		GraphCanvas.showLoading("Getting XML...");
		getManServ().getXMLLogicalPlanFromRootNode(super.getPid(),
				super.getNid(), c, cb.getValue(), xmlCb);
		hide();
	}
}