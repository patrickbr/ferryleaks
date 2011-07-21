package com.algebraweb.editor.client;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
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
	
	public ArrayList<Property> getReferencableColumns(int nid, int pid);
	
	public ArrayList<Property> getReferencableColumnsWithoutAdded(int nid, int pid);
	
	public ArrayList<Property> getReferencableColumnsWithoutAddedFromPos(int nid, int pid, int pos) throws PlanManipulationException;
	
	public RemoteManipulationMessage updatePlanNode(int nid, int pid,PlanNode p) throws PlanManipulationException;
	
	public RemoteManipulationMessage updatePlanNode(int nid, int pid,String xml) throws PlanManipulationException;
	
	public String getXMLFromContentNode(ContentNode c);
	
	public String getXMLFromPlanNode(int pid, int nid);
	
	public String getXMLLogicalPlanFromRootNode(int pid, int nid,EvaluationContext c, boolean saveContext) throws PlanManipulationException;
	
	public String getSQLFromPlanNode(int pid, int nid,EvaluationContext c, boolean saveContext) throws PlanManipulationException, PathFinderCompilationError;
	
	public String[] getNodeTypes();
	
	public ArrayList<HashMap<String,String>> eval(int pid, int nid, EvaluationContext context, boolean saveContext) throws PlanManipulationException, PathFinderCompilationError, SqlError;

	public Integer createNewPlan() throws SessionExpiredException;
	
	public Integer removePlan(int pid) throws SessionExpiredException;
	
	public void markAsRoot(int pid, int nid);
	
	public EvaluationContext getEvaluationContext(int pid, int nid) throws PlanManipulationException;

	public RemoteManipulationMessage deleteEdge(HashMap<Coordinate,Integer>  edges, int planid) throws PlanManipulationException;

	public void copyNodes(ArrayList<PlanNodeCopyMessage> msg, int pid) throws SessionExpiredException, PlanManipulationException;
	
	public RemoteManipulationMessage insert(int pid,int x, int y) throws PlanManipulationException;

}
