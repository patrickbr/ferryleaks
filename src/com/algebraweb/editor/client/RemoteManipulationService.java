package com.algebraweb.editor.client;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.GraphIsEmptyException;
import com.algebraweb.editor.client.logicalcanvas.GraphNotConnectedException;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvasSQLException;
import com.algebraweb.editor.client.logicalcanvas.PathFinderCompilationError;
import com.algebraweb.editor.client.logicalcanvas.PlanManipulationException;
import com.algebraweb.editor.client.logicalcanvas.PlanNodeCopyMessage;
import com.algebraweb.editor.client.logicalcanvas.SessionExpiredException;
import com.algebraweb.editor.client.logicalcanvas.SqlError;
import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("manipulate")

public interface RemoteManipulationService extends RemoteService {
	
	
	public RemoteManipulationMessage deleteNodes(Integer[] nids, int planid) throws PlanManipulationException;
	
	public RemoteManipulationMessage addNode(int planid,String nodeType, int x, int y) throws PlanManipulationException;
	
	public RemoteManipulationMessage addEdge(int planid,Coordinate fromTo, int pos) throws PlanManipulationException;
		
	public ValidationResult getValidation(int planid) throws PlanManipulationException;
	
	public String getNodeInformationHTML(int nid, int planid) throws PlanManipulationException;
		
	public PlanNode getPlanNode(int nid, int pid) throws PlanManipulationException;
	
	public ArrayList<Property> getReferencableColumns(int nid, int pid) throws GraphNotConnectedException, GraphIsEmptyException;
	
	public ArrayList<Property> getReferencableColumnsWithoutAdded(int nid, int pid);
	
	public ArrayList<Property> getReferencableColumnsWithoutAddedFromPos(int nid, int pid, int pos) throws PlanManipulationException;
	
	public RemoteManipulationMessage updatePlanNode(int nid, int pid,PlanNode p) throws PlanManipulationException;
	
	public RemoteManipulationMessage updatePlanNode(int nid, int pid,String xml) throws PlanManipulationException;
	
	public String getXMLFromContentNode(ContentNode c);
	
	public String getXMLFromPlanNode(int pid, int nid);
	
	public String getXMLLogicalPlanFromRootNode(int pid, int nid,EvaluationContext c, boolean saveContext) throws PlanManipulationException;
	
	public String getSQLFromPlanNode(int pid, int nid,EvaluationContext c, boolean saveContext) throws PlanManipulationException, PathFinderCompilationError;
	
	public String getSQLFromPlan(int pid) throws PlanManipulationException, PathFinderCompilationError, GraphNotConnectedException, GraphIsEmptyException;
		
	public String[] getNodeTypes();
	
	public ArrayList<HashMap<String,String>> eval(int pid, int nid, EvaluationContext context, boolean saveContext) throws PlanManipulationException, PathFinderCompilationError, LogicalCanvasSQLException;

	public PlanNode getRootNode(int pid) throws PlanManipulationException, PathFinderCompilationError, LogicalCanvasSQLException, GraphNotConnectedException, GraphIsEmptyException;
	
	public Integer createNewPlan(boolean clearFirst) throws SessionExpiredException;
	
	public Integer removePlan(int pid) throws SessionExpiredException;
	
	public EvaluationContext getEvaluationContext(int pid, int nid) throws PlanManipulationException, GraphNotConnectedException, GraphIsEmptyException;

	public RemoteManipulationMessage deleteEdge(HashMap<Coordinate,Integer>  edges, int planid) throws PlanManipulationException;

	public void copyNodes(ArrayList<PlanNodeCopyMessage> msg, int pid) throws SessionExpiredException, PlanManipulationException;
	
	public RemoteManipulationMessage insert(int pid,int x, int y) throws PlanManipulationException;

	public void updatePlanEvaluationContext(EvaluationContext c, int pid) throws PlanManipulationException;

	ArrayList<HashMap<String, String>> evalPlan(int pid, EvaluationContext c,
			boolean saveCurrenNodeValue) throws GraphNotConnectedException, GraphIsEmptyException, PlanManipulationException, PathFinderCompilationError, LogicalCanvasSQLException;
	
}
