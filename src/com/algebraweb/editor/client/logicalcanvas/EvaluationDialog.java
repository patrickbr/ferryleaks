package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.SqlResDialog;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
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

public class EvaluationDialog extends CreateXMLDialog {



	public EvaluationDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {

		super(pid,nid,manServ);
		super.setText("Evaluation");
		
	
	}
	
	@Override
	protected void submit() {
		
		
		EvaluationContext c = new EvaluationContext();
		
		getSerializationPanel().fillEvaluationContext(c);
				
		getManServ().eval(getPid(), getNid(),c, evalCb);
		
	}
	
	private  GraphCanvasCommunicationCallback<ArrayList<HashMap<String,String>>> evalCb = new  GraphCanvasCommunicationCallback<ArrayList<HashMap<String,String>>>() {

		@Override
		public void onSuccess(ArrayList<HashMap<String,String>> result) {


			new SqlResDialog(result);


		}

	};


}
