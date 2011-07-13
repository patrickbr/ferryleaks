package com.algebraweb.editor.client;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.PlanManipulationException;
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
		
	public ValidationResult getValidation(int planid);
	
	public String getNodeInformationHTML(int nid, int planid);
		
	public PlanNode getPlanNode(int nid, int pid);
	
	public ArrayList<Property> getReferencableColumns(int nid, int pid);
	
	public ArrayList<Property> getReferencableColumnsWithoutAdded(int nid, int pid);
	
	public RemoteManipulationMessage updatePlanNode(int nid, int pid,PlanNode p) throws PlanManipulationException;
	
	public RemoteManipulationMessage updatePlanNode(int nid, int pid,String xml) throws PlanManipulationException;
	
	public ArrayList<ValidationError> valideContentNodeGrammer(ContentNode c,ArrayList<GoAble> schema, boolean stayFlat); 

	public String getXMLFromContentNode(ContentNode c);
	
	public String getXMLFromPlanNode(int pid, int nid);
	
	public String getXMLLogicalPlanFromRootNode(int pid, int nid,EvaluationContext c) throws PlanManipulationException;
	
	public String getSQLFromPlanNode(int pid, int nid,EvaluationContext c) throws PlanManipulationException;
	
	public String[] getNodeTypes();
	
	public ArrayList<HashMap<String,String>> eval(int pid, int nid, EvaluationContext context) throws PlanManipulationException;

	public Integer createNewPlan();
	
	public void markAsRoot(int pid, int nid);
	
	public EvaluationContext getEvaluationContext(int pid, int nid);

	public RemoteManipulationMessage deleteEdge(Coordinate[] edges, int planid);
	

}
