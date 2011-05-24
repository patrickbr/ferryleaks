package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.ArrayList;


import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;




public class RemoteFiller {
	
	
	private ArrayList<RawNode> nodes;
	private ArrayList<RawEdge> edges;

	private int state=0;
	
	private GraphCanvasRemoteFillingMachine m;
	
	private RemoteFillingServiceAsync commServ = (RemoteFillingServiceAsync) GWT.create(RemoteFillingService.class);
	
	
	public RemoteFiller() {
		
				
	}
	
	public void init(GraphCanvasRemoteFillingMachine m) {
	
		
		
		commServ.getRawNodes(1, nodeCallback);
		
		this.m=m;
		
	}
	
	
	private AsyncCallback<ArrayList<RawEdge>> edgeCallback = new AsyncCallback<ArrayList<RawEdge>>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(ArrayList<RawEdge> result) {

			RemoteFiller.this.edges = result;
			state++;
			
			if (state == 2) RemoteFiller.this.m.fillWith(RemoteFiller.this.nodes, RemoteFiller.this.edges);
		
		}

	};
	
	private AsyncCallback<ArrayList<RawNode>> nodeCallback = new AsyncCallback<ArrayList<RawNode>>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(ArrayList<RawNode> result) {

			RemoteFiller.this.nodes = result;
			state++;
			commServ.getRawEdges(1, edgeCallback);
			
			if (state == 2) RemoteFiller.this.m.fillWith(RemoteFiller.this.nodes, RemoteFiller.this.edges);
			
				
		}

	};

}
