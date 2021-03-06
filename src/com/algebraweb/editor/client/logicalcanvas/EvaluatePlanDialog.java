package com.algebraweb.editor.client.logicalcanvas;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.dialogs.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.EditorCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The dialog for plan evaluation
 * 
 * @author Patrick Brosi
 * 
 */
public class EvaluatePlanDialog extends EvaluationDialog {
	private AsyncCallback<List<Map<String, String>>> evalCb = new EditorCommunicationCallback<List<Map<String, String>>>(
			"evaluating") {

		@Override
		public void onSuccess(List<Map<String, String>> result) {
			GraphCanvas.hideLoading();
			new SqlResDialog(result);
		}
	};

	public EvaluatePlanDialog(int pid, RemoteManipulationServiceAsync manServ) {
		super(pid, -1, manServ);
		super.setText("Evaluate Plan");
	}

	@Override
	protected void submit() {
		EvaluationContext c = saveContext();
		GraphCanvas.showLoading("Evaluating...");
		getManServ().evalPlan(getPid(), c, getSaveCurrenNodeValue(), evalCb);
		hide();
	}
}