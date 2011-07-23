package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;

public class EvaluatePlanDialog extends EvaluationDialog {

	public EvaluatePlanDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {
		
		super(pid, nid, manServ);
		super.setText("Evaluate Plan");
		

	}
	
	@Override
	protected void submit() {


		EvaluationContext c = saveContext();
		getManServ().eval(getPid(), getNid(), c,getSaveCurrenNodeValue(), evalCb);

	}
	
	private  GraphCanvasCommunicationCallback<ArrayList<HashMap<String,String>>> evalCb = new  GraphCanvasCommunicationCallback<ArrayList<HashMap<String,String>>>("evaluating") {

		@Override
		public void onSuccess(ArrayList<HashMap<String,String>> result) {


			new SqlResDialog(result);


		}

	};
	
	

}
