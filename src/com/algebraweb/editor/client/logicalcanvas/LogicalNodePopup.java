package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.graphcanvas.EditorCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.NodePopup;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.google.gwt.user.client.ui.HTML;

public class LogicalNodePopup extends NodePopup {

	private LogicalCanvas c;
	private RemoteManipulationServiceAsync rmsa;

	private EditorCommunicationCallback<String> nodeInfoCallback = new EditorCommunicationCallback<String>(
			"getting node info") {

		@Override
		public void onFailure(Throwable caught) {
			LogicalNodePopup.this.clear();
			LogicalNodePopup.this.add(new HTML(
					"<span style='color:red'>Error:</span> "
							+ caught.getMessage()));
		}

		@Override
		public void onSuccess(String result) {

			LogicalNodePopup.this.clear();
			LogicalNodePopup.this.add(new HTML(result));
			correctPosition();

		}

	};

	public LogicalNodePopup(LogicalCanvas c, RemoteManipulationServiceAsync rmsa) {

		super();
		this.c = c;
		this.rmsa = rmsa;
		super.setWidth("400px");

	}

	@Override
	protected void render() {

		super.add(new HTML("<img src='loading.gif'>"));
		rmsa.getNodeInformationHTML(super.getNodeId(), c.getId(),
				nodeInfoCallback);

	}

}
