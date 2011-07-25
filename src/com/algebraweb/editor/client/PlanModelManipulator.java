package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.graphcanvas.GraphEdge;
import com.algebraweb.editor.client.graphcanvas.GraphNode;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvas;
import com.algebraweb.editor.client.logicalcanvas.PlanNodeCopyMessage;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;

/**
 * Handles PlanManipulation and synchronization with the server plan 
 * model.
 * This class works as an interface for the server model. Thus, all 
 * methods affecting the structure of one of the query plans or
 * providing information about any of them should go in here. If 
 * possible, don't call RemoteManipulationService(-Async) (rmsa) directly.
 * 
 * 
 * @author Patrick Brosi
 *
 */

public class PlanModelManipulator {

	private RemoteManipulationServiceAsync manServ;
	private AlgebraEditor e;

	private GraphCanvasCommunicationCallback<ValidationResult> validationCallback = new GraphCanvasCommunicationCallback<ValidationResult>("validating plan") {
		@Override
		public void onSuccess(ValidationResult result) {
			showValidation(result);
		}
	};

	private GraphCanvasCommunicationCallback<RemoteManipulationMessage> manipulationCallback = new GraphCanvasCommunicationCallback<RemoteManipulationMessage>("manipulating logical plan") {

		@Override
		public void onFailure(Throwable caught) {
			super.onFailure(caught);
			GraphCanvas.hideLoading();
		}

		@Override
		public void onSuccess(RemoteManipulationMessage result) {

			GraphCanvas.hideLoading();
			if (result.getReturnCode() == 1 ) {

				AlgebraEditor.log(("   received a '" + result.getAction() + "' manipulation for plan #" + result.getPlanid()));
				LogicalCanvas c = e.getCanvas(result.getPlanid());

				if (result.getAction().equals("delete")) {
					Iterator<RawNode> it = result.getNodesAffected().iterator();
					while(it.hasNext()) {
						c.deleteNode(c.getGraphNodeById(it.next().getNid()));
					}
				}

				if (result.getAction().equals("update")) {
					Iterator<RawNode> it = result.getNodesAffected().iterator();
					while(it.hasNext()) {

						RawNode current = it.next();
						GraphNode from = c.getGraphNodeById(current.getNid());

						c.getGraphNodeById(current.getNid()).setText(current.getText());
						HashMap<Integer,GraphNode> markedForDeletion = new HashMap<Integer,GraphNode>();

						Iterator<GraphEdge> a = from.getEdgesFrom().iterator();

						while (a.hasNext()) {

							GraphEdge e = a.next();
							GraphNode child = e.getTo();

							if (!containsEdge(current.getEdgesToList(),child.getId(),e.getFixedParentPos())) {
								markedForDeletion.put(e.getFixedParentPos(),child);
							}
						}

						Iterator<Integer> u = markedForDeletion.keySet().iterator();

						while (u.hasNext()) {	
							Integer cur = u.next();
							c.removeEdge(from, markedForDeletion.get(cur).getId(),cur);							
						}

						Iterator<RawEdge> i = current.getEdgesToList().iterator();

						while(i.hasNext()) {

							RawEdge ed = i.next();
							GraphNode to = e.getCanvas(result.getPlanid()).getGraphNodeById(ed.getTo());
						
							if (to != null && !c.hasEdge(from.getId(), to.getId(),ed.getFixedParentPos())) {
								c.createEdge(from, to, ed.getFixedParentPos(),false);
							}
						}
						c.showEdges();
					}
				}

				if (result.getAction().equals("add")) {

					Iterator<RawNode> it = result.getNodesAffected().iterator();

					while(it.hasNext()) {

						RawNode current = it.next();

						e.getCanvas(result.getPlanid()).addNode(current.getNid(), current.getColor(), 
								(int)result.getCoordinates().get(current.getNid()).getX(), 
								(int)result.getCoordinates().get(current.getNid()).getY(),
								current.getWidth(),
								current.getHeight(),
								current.getText(),								
								current.getFixedChildCount());
					}

					it = result.getNodesAffected().iterator();
					while (it.hasNext()) {

						RawNode current = it.next();

						GraphNode from = c.getGraphNodeById(current.getNid());
						Iterator<RawEdge> i = current.getEdgesToList().iterator();

						while(i.hasNext()) {

							RawEdge ed = i.next();

							GraphNode to = c.getGraphNodeById(ed.getTo());
							c.createEdge(from, to, ed.getFixedParentPos(),true);

						}
						c.showEdges();
					}
				}
				showValidation(result.getValidationResult());
				c.updateSQLListener();
			}
		}
	};
	
	private GraphCanvasCommunicationCallback<String[]> nodeTypesCb = new GraphCanvasCommunicationCallback<String[]>("getting node types") {
		@Override
		public void onSuccess(String[] result) {
			new NodeTypeSelector(result, AlgebraEditor.getActiveCanvas());
		}
	};

	public PlanModelManipulator(RemoteManipulationServiceAsync manServ) {
		this.manServ = manServ;
	}

	/**
	 * Adds an edge to a plan
	 * @param e the edge to be added as a tuple (from,to)
	 * @param planid the id of the plan the edge should be added to
	 * @param pos the edge's position at the parent node beginning with 1
	 */
	public void addEdge(Coordinate e, int planid, int pos) {
		AlgebraEditor.log("Adding edge from #" + (int)e.getX() + " to #" +(int) e.getY() + " to plan #" + planid);
		GraphCanvas.showLoading("Adding edge...");
		manServ.addEdge(planid, e, pos, manipulationCallback);
	}

	/**
	 * Adds a node to a plan at a specific position
	 * @param pid the plan the node should be added to
	 * @param type the type of the node
	 * @param x node's x position
	 * @param y node's y position
	 */
	public void addNode(int pid, String type, int x, int y) {
		AlgebraEditor.log("Adding node of type " + type + " to plan #" + pid);
		GraphCanvas.showLoading("Adding node...");
		manServ.addNode(pid, type,x,y, manipulationCallback);
	}

	//TODO: shoudlnt be in here
	public void blurr(boolean blur) {
		AlgebraEditor.getActiveCanvas().setBlurred(blur);
	}

	private boolean containsEdge(ArrayList<RawEdge> edges, int to, int pos) {
		Iterator<RawEdge> it = edges.iterator();
		while (it.hasNext()) {
			RawEdge cur = it.next();
			if (cur.getTo() == to && cur.getFixedParentPos() == pos) return true;
		}
		return false;
	}
	
	/**
	 * Invoke a copy on a certain plan. All selected nodes will
	 * be written into the servers 'clipboard', meaning that they will
	 * be saved to the session for a later pasting.
	 * @param planid the plan the copy functoin should be invoked on
	 */
	public void copy(int planid) {
		AlgebraEditor.log("Copying selected nodes for #" + planid);
		ArrayList<PlanNodeCopyMessage> msg = new ArrayList<PlanNodeCopyMessage>();

		Iterator<GraphNode> it = e.getCanvas(planid).getSelectedNodes().values().iterator();

		while (it.hasNext()) {
			GraphNode cur = it.next();
			msg.add(new PlanNodeCopyMessage(cur.getId(), new Coordinate(cur.getX(), cur.getY())));
		}

		manServ.copyNodes(msg, planid, new GraphCanvasCommunicationCallback<Void>("copying node(s)"){
			@Override
			public void onSuccess(Void result) {
			}});
	}

	
	/**
	 * Removes edges from a plan
	 * @param edges the edges to be removed as a HashMap providing a tuple (from,to) as a key and
	 * an position integer as a value
	 * @param planid the plan the edges should be removed from
	 */
	public void deleteEdges(HashMap<Coordinate,Integer> edges, int planid) {
		if (edges.size() == 0) return;
		AlgebraEditor.log("Deleting edge(s) from plan #" + planid);
		GraphCanvas.showLoading("Deleting edge...");
		manServ.deleteEdge(edges, planid, manipulationCallback);
	}

	/**
	 * Removes nodes from a plan
	 * @param nids the ids of the nodes to be removed
	 * @param planid the plan the nodes should be removed from
	 */
	public void deleteNode(Integer[] nids, int planid) {
		if (nids.length == 0) return;
		AlgebraEditor.log("Deleting node(s) " + Arrays.toString(nids) + " from plan #" + planid);
		GraphCanvas.showLoading("Deleting node...");
		manServ.deleteNodes(nids, planid, manipulationCallback);
	}
	
	/**
	 * Invoke a paste on a plan at a specific position. All
	 * nodes contained in the sessions 'clipboard' will be pasted
	 * into the plan. The node with the lowest y-value will be
	 * inserted at (x,y), all other nodes will be positioned in respect to 
	 * their former relative position to the top node.
	 * @param planid the plan the paste should be invoked on
	 * @param x the x position the top node should be inserted at
	 * @param y the y position the top node should be inserted at
	 */
	public void paste(int planid,int x, int y ) {
		manServ.insert(planid, x,y, manipulationCallback);
	}

	public void setEditor(AlgebraEditor e) {
		this.e=e;
	}

	/**
	 * Display a node adding dialog with all node types
	 * contained on the server
	 */
	public void showNodeTypes() {
		manServ.getNodeTypes(nodeTypesCb);
	}

	private void showValidation(ValidationResult r) {
		e.getCanvas(r.getPlanid()).clearErroneous();

		Iterator<ValidationError> it = r.getErrors().iterator();

		while(it.hasNext()) {
			e.getCanvas(r.getPlanid()).setErroneous(it.next().getNodeId());
		}
	}

	/**
	 * Update a PlanNode's content using XML source
	 * @param planid the plan the node is on
	 * @param nid the node's id
	 * @param XML node's new XML source. Note that changes to the node's id or the node's type in the source will be ignored.
	 */
	public void updateNodeContent(int planid, int nid, String XML) {
		AlgebraEditor.log("Committing source update of node #" + nid + " for plan #" + planid);
		manServ.updatePlanNode(nid, planid, XML, manipulationCallback);
	}

	/**
	 * Update a PlanNode's content
	 * @param planid the plan the node is on
	 * @param p a copy of the server plannode containing the new content
	 */
	public void updateNodeContent(int planid, PlanNode p) {
		AlgebraEditor.log("Committing update of node #" + p.getId() + " for plan #" + planid);
		manServ.updatePlanNode(p.getId(), planid, p, manipulationCallback);
	}

	/**
	 * Validate a plan
	 * @param planid plan to validate
	 */
	public void validate(int planid) {
		AlgebraEditor.log("Requesting validation for #" + planid);
		manServ.getValidation(planid,validationCallback);
	}

}
