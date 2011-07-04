package com.algebraweb.editor.client.logicalcanvas;

import java.util.Iterator;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.graphcanvas.NodePopup;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;

public class LogicalNodePopup extends NodePopup{

	private LogicalCanvas c;
	private RemoteManipulationServiceAsync rmsa;
	
	
	public LogicalNodePopup(LogicalCanvas c, RemoteManipulationServiceAsync rmsa) {
		
		super();
		this.c=c;
		this.rmsa=rmsa;
		super.setWidth("400px");
		
		
	}
	
	@Override
	protected void render() {
		
		super.add(new HTML("<img src='loading.gif'>"));
		rmsa.getNodeInformationHTML(super.getNodeId(), c.getId(), nodeInfoCallback);
		
	}
	
	private AsyncCallback<String> nodeInfoCallback = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			LogicalNodePopup.this.clear();
			LogicalNodePopup.this.add(new HTML("Error while obtaining node data..."));
		}

		@Override
		public void onSuccess(String result) {

			LogicalNodePopup.this.clear();
			LogicalNodePopup.this.add(new HTML(result));
			correctPosition();

		}

	};
	
	
	
}
