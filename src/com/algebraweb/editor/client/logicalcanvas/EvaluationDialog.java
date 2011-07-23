package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasErrorDialogBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EvaluationDialog extends CreateSQLDialog {

	private DatabaseConfigPanel db;

	public EvaluationDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {


		super(pid,nid,manServ);
		super.setText("Evaluation");
		db = new DatabaseConfigPanel(pid, nid, manServ);
		super.addTab(db, "Database");


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



	protected DatabaseConfigPanel getDatabaseConfigPanel() {

		return db;

	}

	private  GraphCanvasCommunicationCallback<ArrayList<HashMap<String,String>>> evalCb = new  GraphCanvasCommunicationCallback<ArrayList<HashMap<String,String>>>("evaluating") {

		@Override
		public void onSuccess(ArrayList<HashMap<String,String>> result) {


			new SqlResDialog(result);


		}

	};

	@Override
	protected void processContextResult(EvaluationContext result) {

		super.processContextResult(result);
		getDatabaseConfigPanel().loadEvaluationContext(result);


	}


}
