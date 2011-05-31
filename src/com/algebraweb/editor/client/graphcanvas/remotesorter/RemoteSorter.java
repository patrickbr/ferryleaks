package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.GraphSorter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class RemoteSorter implements GraphSorter {


	private RemoteSorterServiceAsync commServ;
	private ArrayList<GraphNode> nodes;
	private ArrayList<GraphEdge> edges;
	
	private String sorter;

	public RemoteSorter(String sorter) {


		commServ = (RemoteSorterServiceAsync) GWT.create(RemoteSorterService.class);

		this.sorter=sorter;

	}


	@Override
	public void doSort(ArrayList<GraphNode> nodes,ArrayList<GraphEdge> edges,GraphManipulationCallback cb) {

		this.nodes=nodes;
		this.edges=edges;

		//TODO: this should be in an external class, maybe static

		ArrayList<RawNode> rawNodeList = new ArrayList<RawNode>();

		Iterator<GraphNode> i = nodes.iterator();

		while (i.hasNext()) {

			GraphNode c= i.next();
			
			RawNode nNode = new RawNode(c.getId(),c.getTextString(),c.getColor(),c.getWidth(),c.getHeight());

			Iterator<GraphEdge> u = c.getEdgesFrom().iterator();
			
			while (u.hasNext()) {
				
				GraphEdge current = u.next();
				
				nNode.getEdgesToList().add(current.getTo().getId());
				
			}
			

			rawNodeList.add(nNode);
		}


		commServ.doSort(sorter,rawNodeList, sortedCallback(cb));


	}


	private void processPositionTuples(HashMap<Integer,Coordinate> tuples, GraphManipulationCallback cb) {


		Iterator<GraphNode> i = nodes.iterator();

		while (i.hasNext()) {

			GraphNode current = i.next();

			current.setX(tuples.get(current.getId()).getX());
			current.setY(tuples.get(current.getId()).getY());

			
		}

		cb.onComplete();


	}


	private AsyncCallback<HashMap<Integer,Coordinate>> sortedCallback(final GraphManipulationCallback cb) {
		return new AsyncCallback<HashMap<Integer,Coordinate>>() {


			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(HashMap<Integer,Coordinate> result) {

				processPositionTuples(result,cb);

			}

		};
	}




}
