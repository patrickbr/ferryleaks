package com.algebraweb.editor.client;

import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.Fillable;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.graphcanvas.GraphSorter;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.google.gwt.user.client.ui.Widget;

public interface AlgebraEditorCanvasView extends Fillable {
		
	public int getId();
	public GraphNode getGraphNodeById(int nid);
	public void setSelectedNode(GraphNode n);
	public void selectNodeWithSubs(GraphNode n);
	public void addSQLListener(int nid, RemoteManipulationServiceAsync manServ, EvaluationContext c);
	public double getScale();
	public Map<Integer, GraphNode> getSelectedNodes();
	public void sort(GraphSorter remoteSorter);
	public void enterEdgeAddingMode();
	public Map<Coordinate, Integer> getSelectedEdgesWithPos();
	public void enterNodeAddingMode(String selectedScheme);
	public void setBlurred(boolean blur);
	public void unbugMe();
	public void deleteNode(GraphNode graphNodeById);
	public void removeEdge(int from, int id, int cur);
	public boolean hasEdge(int id, int id2, int fixedParentPos);
	public void showEdges();
	public void updateSQLListener();
	public void clearErroneous();
	public void setErroneous(int nodeId);
	public Widget getWidget();
	public void setNotActive(boolean b);
	public void zoomIn();
	public void zoomOut();
	
}
