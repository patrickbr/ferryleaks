package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
	
	


}
