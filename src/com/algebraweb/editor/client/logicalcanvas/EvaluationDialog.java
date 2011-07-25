package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;

public class EvaluationDialog extends CreateSQLDialog {

	private DatabaseConfigPanel db;

	private  GraphCanvasCommunicationCallback<ArrayList<HashMap<String,String>>> evalCb = new  GraphCanvasCommunicationCallback<ArrayList<HashMap<String,String>>>("evaluating") {

		@Override
		public void onSuccess(ArrayList<HashMap<String,String>> result) {


			new SqlResDialog(result);


		}

	};

	public EvaluationDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {


		super(pid,nid,manServ);
		super.setText("Evaluation");
		db = new DatabaseConfigPanel(pid, nid, manServ);
		super.addTab(db, "Database");


	}

	protected DatabaseConfigPanel getDatabaseConfigPanel() {

		return db;

	}



	@Override
	protected void processContextResult(EvaluationContext result) {

		super.processContextResult(result);
		getDatabaseConfigPanel().loadEvaluationContext(result);


	}

	@Override 
	protected EvaluationContext saveContext() {

		EvaluationContext c = super.saveContext();
		db.fillEvaluationContext(c);
		return c;		

	}

	@Override
	protected void submit() {


		EvaluationContext c = saveContext();
		getManServ().eval(getPid(), getNid(), c,getSaveCurrenNodeValue(), evalCb);

	}


}
