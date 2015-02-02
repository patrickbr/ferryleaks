package com.algebraweb.editor.client.services;

import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.RemoteManipulationMessage;
import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.client.logicalcanvas.PlanNodeCopyMessage;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.shared.exceptions.GraphIsEmptyException;
import com.algebraweb.editor.shared.exceptions.GraphNotConnectedException;
import com.algebraweb.editor.shared.exceptions.LogicalCanvasSQLException;
import com.algebraweb.editor.shared.exceptions.PathFinderCompilationErrorException;
import com.algebraweb.editor.shared.exceptions.PlanHasCycleException;
import com.algebraweb.editor.shared.exceptions.PlanManipulationException;
import com.algebraweb.editor.shared.exceptions.RemoteIOException;
import com.algebraweb.editor.shared.exceptions.SessionExpiredException;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.algebraweb.editor.shared.node.ContentNode;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.Property;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The Remote Manipulation Service. The main class for editing communications.
 * Provides methods for changed to plan model on the server.
 * 
 * @author Patrick Brosi
 * 
 */
@RemoteServiceRelativePath("manipulate")
public interface RemoteManipulationService extends RemoteService {

	/**
	 * Adds an edge to the plan model.
	 * 
	 * @param planid
	 *            The plan to use
	 * @param fromTo
	 *            the tuple (from,to)
	 * @param pos
	 *            the child position
	 * @return a RemoteManipulationMessage
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public RemoteManipulationMessage addEdge(int planid, Tuple fromTo, int pos)
			throws PlanManipulationException, PlanHasCycleException;

	/**
	 * Adds a new node to the plan.
	 * 
	 * @param planid
	 *            The plan to use
	 * @param nodeType
	 *            the type of the node to add
	 * @param x
	 *            the x position of the new node
	 * @param y
	 *            the y position of the new node
	 * @return a RemoteManipulationMessage
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public RemoteManipulationMessage addNode(int planid, String nodeType,
			int x, int y) throws PlanManipulationException,
			PlanHasCycleException;

	/**
	 * Copy nodes to the clipboard
	 * 
	 * @param msg
	 *            the PlanNodeCopyMessage
	 * @param pid
	 *            the plan to use
	 * @throws SessionExpiredException
	 * @throws PlanManipulationException
	 */
	public void copyNodes(List<PlanNodeCopyMessage> msg, int pid)
			throws SessionExpiredException, PlanManipulationException;

	/**
	 * Create a new plan on the server.
	 * 
	 * @param clearFirst
	 *            set to true of old plans should be cleared first.
	 * @return the id of the added plan
	 * @throws SessionExpiredException
	 */
	public Integer createNewPlan(boolean clearFirst)
			throws SessionExpiredException;

	/**
	 * Delete edges.
	 * 
	 * @param map
	 *            a map of all edges that should be deleted. Key is tuple
	 *            (from,to), value is the child position.
	 * @param planid
	 *            the plan to use
	 * @return A RemoteManipulationMessage
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public RemoteManipulationMessage deleteEdges(Map<Tuple, Integer> map,
			int planid) throws PlanManipulationException, PlanHasCycleException;

	/**
	 * Delete nodes.
	 * 
	 * @param nids
	 *            an integer array of all node ids to be deleted
	 * @param planid
	 *            the plan to sue
	 * @return A RemoteManipulationMessage
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public RemoteManipulationMessage deleteNodes(Integer[] nids, int planid)
			throws PlanManipulationException, PlanHasCycleException;

	/**
	 * Evaluate a node.
	 * 
	 * @param pid
	 *            the plan to use
	 * @param nid
	 *            the node to evaluate
	 * @param context
	 *            the evaluation context to use
	 * @param saveContext
	 *            true of the eval context should be saved
	 * @return a SQL result
	 * @throws PlanManipulationException
	 * @throws PathFinderCompilationErrorException
	 * @throws LogicalCanvasSQLException
	 * @throws PlanHasCycleException
	 */
	public List<Map<String, String>> eval(int pid, int nid,
			EvaluationContext context, boolean saveContext)
			throws PlanManipulationException,
			PathFinderCompilationErrorException, LogicalCanvasSQLException,
			PlanHasCycleException;

	/**
	 * Evaluate a whole plan
	 * 
	 * @param pid
	 *            the plan's id
	 * @param c
	 *            the evaluation context to use
	 * @param saveCurrenNodeValue
	 *            true of the evaluation context should be saved for this plan
	 * @return an SQL result
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanManipulationException
	 * @throws PathFinderCompilationErrorException
	 * @throws LogicalCanvasSQLException
	 * @throws PlanHasCycleException
	 */
	public List<Map<String, String>> evalPlan(int pid, EvaluationContext c,
			boolean saveCurrenNodeValue) throws GraphNotConnectedException,
			GraphIsEmptyException, PlanManipulationException,
			PathFinderCompilationErrorException, LogicalCanvasSQLException,
			PlanHasCycleException;

	/**
	 * Get the (saved) evaluation context for a node
	 * 
	 * @param pid
	 *            the node's id
	 * @param nid
	 *            the plan to use
	 * @return the evaluation context
	 * @throws PlanManipulationException
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanHasCycleException
	 */
	public EvaluationContext getEvaluationContext(int pid, int nid)
			throws PlanManipulationException, GraphNotConnectedException,
			GraphIsEmptyException, PlanHasCycleException;

	/**
	 * Get a HTML representation of the node information
	 * 
	 * @param nid
	 *            the node's id
	 * @param planid
	 *            the plan to use
	 * @return a string containing raw HTML.
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public String getNodeInformationHTML(int nid, int planid)
			throws PlanManipulationException, PlanHasCycleException;

	/**
	 * Returns all node types loaded on the server.
	 * 
	 * @return a string array of node types
	 * @throws RemoteIOException
	 */
	public String[] getNodeTypes() throws RemoteIOException;

	/**
	 * Returns a plan node object for editing
	 * 
	 * @param nid
	 *            the node's id
	 * @param pid
	 *            the plan to use
	 * @return a loaded plan node
	 * @throws PlanManipulationException
	 */
	public PlanNode getPlanNode(int nid, int pid)
			throws PlanManipulationException;

	/**
	 * Returns a list of all referencable columns within a node
	 * 
	 * @param nid
	 *            the node's id
	 * @param pid
	 *            the plan to use
	 * @return a list of columns (as properties)
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanHasCycleException
	 */
	public List<Property> getReferencableColumns(int nid, int pid)
			throws GraphNotConnectedException, GraphIsEmptyException,
			PlanHasCycleException;

	/**
	 * Returns a list of referencable columns without the columns added within
	 * this node
	 * 
	 * @param nid
	 *            the node's id
	 * @param pid
	 *            the plan to use
	 * @return a list of columns (as properties)
	 * @throws PlanHasCycleException
	 */
	public List<Property> getReferencableColumnsWithoutAdded(int nid, int pid)
			throws PlanHasCycleException;

	public List<Property> getReferencableColumnsWithoutAddedFromPos(int nid,
			int pid, int pos) throws PlanManipulationException,
			PlanHasCycleException;

	/**
	 * Returns a plan's root node
	 * 
	 * @param pid
	 *            the plan to use
	 * @return the root node
	 * @throws PlanManipulationException
	 * @throws PathFinderCompilationErrorException
	 * @throws LogicalCanvasSQLException
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanHasCycleException
	 */
	public PlanNode getRootNode(int pid) throws PlanManipulationException,
			PathFinderCompilationErrorException, LogicalCanvasSQLException,
			GraphNotConnectedException, GraphIsEmptyException,
			PlanHasCycleException;

	/**
	 * Returns the compiled SQL for plan
	 * 
	 * @param pid
	 *            the plan's id
	 * @return the SQL query as a string
	 * @throws PlanManipulationException
	 * @throws PathFinderCompilationErrorException
	 * @throws GraphNotConnectedException
	 * @throws GraphIsEmptyException
	 * @throws PlanHasCycleException
	 */
	public String getSQLFromPlan(int pid) throws PlanManipulationException,
			PathFinderCompilationErrorException, GraphNotConnectedException,
			GraphIsEmptyException, PlanHasCycleException;

	/**
	 * Returns the compiled SQL for a plan node
	 * 
	 * @param pid
	 *            the plan to use
	 * @param nid
	 *            the node's id
	 * @param c
	 *            the evaluation context to use
	 * @param saveContext
	 *            true if the eval context should be saved
	 * @return the SQL query as a string
	 * @throws PlanManipulationException
	 * @throws PathFinderCompilationErrorException
	 * @throws PlanHasCycleException
	 */
	public String getSQLFromPlanNode(int pid, int nid, EvaluationContext c,
			boolean saveContext) throws PlanManipulationException,
			PathFinderCompilationErrorException, PlanHasCycleException;

	/**
	 * Returns the validation of a plan. The plan is revalidated.
	 * 
	 * @param planid
	 *            the plan's id
	 * @return the ValidationResult
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public ValidationResult getValidation(int planid)
			throws PlanManipulationException, PlanHasCycleException;

	/**
	 * Returns the XML of a ContentNode (for editing purposes)
	 * 
	 * @param c
	 *            the ContentNoe
	 * @return the XML string
	 */
	public String getXMLFromContentNode(ContentNode c);

	/**
	 * Returns the XML of a PlanNode
	 * 
	 * @param pid
	 *            the plan to use
	 * @param nid
	 *            the node's id
	 * @return the XML as a string
	 * @throws PlanManipulationException
	 */
	public String getXMLFromPlanNode(int pid, int nid)
			throws PlanManipulationException;

	/**
	 * Returns the XML for a whole plan, beginning with the root node
	 * 
	 * @param pid
	 *            the plan's id
	 * @param nid
	 *            the root node's id
	 * @param c
	 *            the evaluation context to use
	 * @param saveContext
	 *            true if the eval context should be saved
	 * @return the XML as a string
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public String getXMLLogicalPlanFromRootNode(int pid, int nid,
			EvaluationContext c, boolean saveContext)
			throws PlanManipulationException, PlanHasCycleException;

	/**
	 * Pastes the clipboard into a plan at a given position
	 * 
	 * @param pid
	 *            the plan's id
	 * @param x
	 *            the x position
	 * @param y
	 *            the y position
	 * @return a RemoteManipulationMessage
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public RemoteManipulationMessage insert(int pid, int x, int y)
			throws PlanManipulationException, PlanHasCycleException;

	/**
	 * Removes a plan from the serve
	 * 
	 * @param pid
	 *            the plan's id
	 * @return the removed plan's id
	 * @throws SessionExpiredException
	 */
	public Integer removePlan(int pid) throws SessionExpiredException;

	/**
	 * Updates the evaluation context of a plan
	 * 
	 * @param c
	 *            the evaluation context
	 * @param pid
	 *            the plan's id
	 * @throws PlanManipulationException
	 */
	public void updatePlanEvaluationContext(EvaluationContext c, int pid)
			throws PlanManipulationException;

	/**
	 * Updates a plan node. Changes to ID and type will be ignored.
	 * 
	 * @param nid
	 *            the node's id
	 * @param pid
	 *            the plan to use
	 * @param p
	 *            the new plan node
	 * @return a RemoteManipulationMessage
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public RemoteManipulationMessage updatePlanNode(int nid, int pid, PlanNode p)
			throws PlanManipulationException, PlanHasCycleException;

	/**
	 * Updates a plan node from XML source. Changes to ID and type will be
	 * ignored.
	 * 
	 * @param nid
	 *            the node's id
	 * @param pid
	 *            the plan to use
	 * @param xml
	 *            the XML source
	 * @return a RemoteManipulationMessage
	 * @throws PlanManipulationException
	 * @throws PlanHasCycleException
	 */
	public RemoteManipulationMessage updatePlanNode(int nid, int pid, String xml)
			throws PlanManipulationException, PlanHasCycleException;
}