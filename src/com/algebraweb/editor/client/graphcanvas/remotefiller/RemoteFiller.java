package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.ArrayList;


import com.algebraweb.editor.client.RawNode;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;




public class RemoteFiller {
	
	
	private ArrayList<RawNode> nodes;

	private int state=0;
	
	private GraphCanvasRemoteFillingMachine m;
	
	private RemoteFillingServiceAsync commServ = (RemoteFillingServiceAsync) GWT.create(RemoteFillingService.class);
	
	
	public RemoteFiller() {
		
				
	}
	
	public void init(GraphCanvasRemoteFillingMachine m) {
	
		
		
		commServ.getRawNodes(nodeCallback);
		
		this.m=m;
		
	}
	

	
	private AsyncCallback<ArrayList<RawNode>> nodeCallback = new AsyncCallback<ArrayList<RawNode>>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(ArrayList<RawNode> result) {

			RemoteFiller.this.nodes = result;
		    RemoteFiller.this.m.fillWith(RemoteFiller.this.nodes);
			
				
		}

	};

}
