package com.algebraweb.editor.client.logicalcanvas;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.dialogs.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.EditorCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;

/**
 * A basic evaluation dialog for plan evaluation.
 * 
 * @author Patrick Brosi
 * 
 */
public class EvaluationDialog extends CreateSQLDialog {
	private EditorCommunicationCallback<List<Map<String, String>>> evalCb = new EditorCommunicationCallback<List<Map<String, String>>>(
			"evaluating") {

		@Override
		public void onSuccess(List<Map<String, String>> result) {
			GraphCanvas.hideLoading();
			new SqlResDialog(result);
		}
	};

	public EvaluationDialog(int pid, int nid,
			RemoteManipulationServiceAsync manServ) {
		super(pid, nid, manServ);
		super.setText("Evaluation");
	}

	@Override
	protected void processContextResult(EvaluationContext result) {
		super.processContextResult(result);
	}

	@Override
	protected EvaluationContext saveContext() {
		EvaluationContext c = super.saveContext();
		return c;
	}

	@Override
	protected void submit() {
		hide();
		EvaluationContext c = saveContext();
		GraphCanvas.showLoading("Evaluating...");
		getManServ().eval(getPid(), getNid(), c, getSaveCurrenNodeValue(),
				evalCb);
	}
}
