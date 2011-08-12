package com.algebraweb.editor.client.logicalcanvas;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;

public class EvaluationDialog extends CreateSQLDialog {

	private DatabaseConfigPanel db;

	private  GraphCanvasCommunicationCallback<List<Map<String,String>>> evalCb = new  GraphCanvasCommunicationCallback<List<Map<String,String>>>("evaluating") {

		@Override
		public void onSuccess(List<Map<String,String>> result) {


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
