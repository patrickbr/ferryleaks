package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.GraphIsEmptyException;
import com.algebraweb.editor.client.logicalcanvas.GraphNotConnectedException;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvasSQLException;
import com.algebraweb.editor.client.logicalcanvas.PathFinderCompilationError;
import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
import com.algebraweb.editor.client.logicalcanvas.PlanManipulationException;
import com.algebraweb.editor.client.logicalcanvas.PlanNodeCopyMessage;
import com.algebraweb.editor.client.logicalcanvas.SessionExpiredException;
import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("manipulate")

public interface RemoteManipulationService extends RemoteService {
	
	
	public RemoteManipulationMessage addEdge(int planid,Coordinate fromTo, int pos) throws PlanManipulationException, PlanHasCycleException;
	
	public RemoteManipulationMessage addNode(int planid,String nodeType, int x, int y) throws PlanManipulationException, PlanHasCycleException;
	
	public void copyNodes(ArrayList<PlanNodeCopyMessage> msg, int pid) throws SessionExpiredException, PlanManipulationException;
		
	public Integer createNewPlan(boolean clearFirst) throws SessionExpiredException;
	
	public RemoteManipulationMessage deleteEdge(HashMap<Coordinate,Integer>  edges, int planid) throws PlanManipulationException, PlanHasCycleException;
		
	public RemoteManipulationMessage deleteNodes(Integer[] nids, int planid) throws PlanManipulationException, PlanHasCycleException;
	
	public ArrayList<HashMap<String,String>> eval(int pid, int nid, EvaluationContext context, boolean saveContext) throws PlanManipulationException, PathFinderCompilationError, LogicalCanvasSQLException, PlanHasCycleException;
	
	ArrayList<HashMap<String, String>> evalPlan(int pid, EvaluationContext c,
			boolean saveCurrenNodeValue) throws GraphNotConnectedException, GraphIsEmptyException, PlanManipulationException, PathFinderCompilationError, LogicalCanvasSQLException, PlanHasCycleException;
	
	public EvaluationContext getEvaluationContext(int pid, int nid) throws PlanManipulationException, GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException;
	
	public String getNodeInformationHTML(int nid, int planid) throws PlanManipulationException, PlanHasCycleException;
	
	public String[] getNodeTypes();
	
	public PlanNode getPlanNode(int nid, int pid) throws PlanManipulationException;
	
	public ArrayList<Property> getReferencableColumns(int nid, int pid) throws GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException;
	
	public ArrayList<Property> getReferencableColumnsWithoutAdded(int nid, int pid) throws PlanHasCycleException;
	
	public ArrayList<Property> getReferencableColumnsWithoutAddedFromPos(int nid, int pid, int pos) throws PlanManipulationException, PlanHasCycleException;
	
	public PlanNode getRootNode(int pid) throws PlanManipulationException, PathFinderCompilationError, LogicalCanvasSQLException, GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException;
		
	public String getSQLFromPlan(int pid) throws PlanManipulationException, PathFinderCompilationError, GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException;
	
	public String getSQLFromPlanNode(int pid, int nid,EvaluationContext c, boolean saveContext) throws PlanManipulationException, PathFinderCompilationError, PlanHasCycleException;

	public ValidationResult getValidation(int planid) throws PlanManipulationException, PlanHasCycleException;
	
	public String getXMLFromContentNode(ContentNode c);
	
	public String getXMLFromPlanNode(int pid, int nid);
	
	public String getXMLLogicalPlanFromRootNode(int pid, int nid,EvaluationContext c, boolean saveContext) throws PlanManipulationException, PlanHasCycleException;

	public RemoteManipulationMessage insert(int pid,int x, int y) throws PlanManipulationException, PlanHasCycleException;

	public Integer removePlan(int pid) throws SessionExpiredException;
	
	public void updatePlanEvaluationContext(EvaluationContext c, int pid) throws PlanManipulationException;

	public RemoteManipulationMessage updatePlanNode(int nid, int pid,PlanNode p) throws PlanManipulationException, PlanHasCycleException;

	public RemoteManipulationMessage updatePlanNode(int nid, int pid,String xml) throws PlanManipulationException, PlanHasCycleException;
	
}
