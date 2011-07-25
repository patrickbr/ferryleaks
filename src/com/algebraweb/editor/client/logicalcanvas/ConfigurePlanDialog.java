package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.AlgebraEditor;
import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;

public class ConfigurePlanDialog extends EvaluationDialog {

	
	public ConfigurePlanDialog(int pid, RemoteManipulationServiceAsync manServ) {
		super(pid, -1, manServ);
	
		super.getButtonsPanel().remove(super.getSaveCheckBox());
		
		super.setText("Configure plan settings");

		
	}
	
	@Override
	protected void submit() {

		EvaluationContext c = saveContext();
		getManServ().updatePlanEvaluationContext(c, getPid(),updateCb);
		GraphCanvas.showLoading("Saving...");
		
	}
	
	private  GraphCanvasCommunicationCallback<Void> updateCb = new  GraphCanvasCommunicationCallback<Void>("evaluating") {

		@Override
		public void onSuccess(Void result) {

			GraphCanvas.hideLoading();
			hide();

		}

	};


}