package com.algebraweb.editor.client.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.AlgebraEditor;
import com.algebraweb.editor.client.graphcanvas.EditorCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.GraphSorter;
import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.node.RawEdge;
import com.algebraweb.editor.shared.node.RawNode;
import com.google.gwt.core.client.GWT;

public class RemoteSorter implements GraphSorter {

	private RemoteSorterServiceAsync commServ;
	private List<GraphNode> nodes;
	private String sorter;

	public RemoteSorter(String sorter) {
		commServ = (RemoteSorterServiceAsync) GWT
				.create(RemoteSorterService.class);
		this.sorter = sorter;
	}

	@Override
	public void doSort(List<GraphNode> nodes, List<GraphEdge> edges,
			GraphManipulationCallback cb) {
		this.nodes = nodes;
		List<RawNode> rawNodeList = new ArrayList<RawNode>();
		Iterator<GraphNode> i = nodes.iterator();
		while (i.hasNext()) {

			GraphNode c = i.next();
			RawNode nNode = new RawNode(c.getId(), c.getTextString(), c
					.getColor(), c.getWidth(), c.getHeight());
			Iterator<GraphEdge> u = c.getEdgesFrom().iterator();

			while (u.hasNext()) {
				GraphEdge current = u.next();
				nNode.getEdgesToList().add(
						new RawEdge(current.getTo().getId(), current.getFrom()
								.getId()));
			}
			rawNodeList.add(nNode);
		}

		AlgebraEditor.log("Sorting with sorter '" + sorter + "'");
		commServ.doSort(sorter, rawNodeList, sortedCallback(cb));
	}

	private void processPositionTuples(Map<Integer, Tuple> tuples,
			GraphManipulationCallback cb) {
		writePositionTuplesToGraphNodes(tuples, nodes);
		cb.onComplete();
	}

	private EditorCommunicationCallback<Map<Integer, Tuple>> sortedCallback(
			final GraphManipulationCallback cb) {
		return new EditorCommunicationCallback<Map<Integer, Tuple>>(
				"sorting graph") {

			@Override
			public void onSuccess(Map<Integer, Tuple> result) {
				processPositionTuples(result, cb);
			}
		};
	}

	public void writePositionTuplesToGraphNodes(Map<Integer, Tuple> tuples,
			List<GraphNode> nodes) {
		Iterator<GraphNode> i = nodes.iterator();

		while (i.hasNext()) {
			GraphNode current = i.next();
			current.setX(tuples.get(current.getId()).getX());
			current.setY(tuples.get(current.getId()).getY());
		}
	}
}
