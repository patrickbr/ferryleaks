package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.ArrayList;

import com.algebraweb.editor.client.AlgebraEditor;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasErrorDialogBox;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class RemoteFiller {

	private ArrayList<RawNode> nodes;
	private GraphCanvasRemoteFillingMachine m;

	GraphManipulationCallback cb;

	private RemoteFillingServiceAsync commServ = (RemoteFillingServiceAsync) GWT.create(RemoteFillingService.class);
	private String filler;
	private String args = "";

	private AsyncCallback<ArrayList<RawNode>> nodeCallback = new AsyncCallback<ArrayList<RawNode>>() {

		@Override
		public void onFailure(Throwable caught) {
			new GraphCanvasErrorDialogBox("Error while getting graph from server (" + caught.getClass().getName() + "): " + caught.getMessage());
		}

		@Override
		public void onSuccess(ArrayList<RawNode> result) {
			RemoteFiller.this.nodes = result;
			RemoteFiller.this.m.fillWith(RemoteFiller.this.nodes);
			if (RemoteFiller.this.cb!= null) RemoteFiller.this.cb.onComplete();
		}
	};

	public RemoteFiller(String filler, String args) {
		this.filler=filler;
		this.args=args;
	}

	public void init(GraphCanvasRemoteFillingMachine m, GraphManipulationCallback cb) {
		AlgebraEditor.log("Calling commServ for raw nodes with args: " + args);
		commServ.getRawNodes(filler,args,nodeCallback);
		this.m=m;
		this.cb=cb;
	}

}
