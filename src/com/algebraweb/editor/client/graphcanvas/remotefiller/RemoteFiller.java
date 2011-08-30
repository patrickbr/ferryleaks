package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.List;

import com.algebraweb.editor.client.AlgebraEditor;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.core.client.GWT;

public class RemoteFiller {

	private List<RawNode> nodes;
	private GraphCanvasRemoteFillingMachine m;
	private GraphManipulationCallback cb;
	private RemoteFillingServiceAsync commServ = (RemoteFillingServiceAsync) GWT
			.create(RemoteFillingService.class);
	private String filler;
	private String args = "";

	private GraphCanvasCommunicationCallback<List<RawNode>> nodeCallback = new GraphCanvasCommunicationCallback<List<RawNode>>(
			"getting graph from server") {
		@Override
		public void onSuccess(List<RawNode> result) {
			RemoteFiller.this.nodes = result;
			RemoteFiller.this.m.fillWith(RemoteFiller.this.nodes);
			if (RemoteFiller.this.cb != null) {
				RemoteFiller.this.cb.onComplete();
			}
		}
	};

	public RemoteFiller(String filler, String args) {
		this.filler = filler;
		this.args = args;
	}

	public void init(GraphCanvasRemoteFillingMachine m,
			GraphManipulationCallback cb) {
		AlgebraEditor.log("Calling commServ for raw nodes with args: " + args);
		commServ.getRawNodes(filler, args, nodeCallback);
		this.m = m;
		this.cb = cb;
	}

}
