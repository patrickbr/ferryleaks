package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;

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
	
	
	public RemoteManipulationMessage deleteNode(int nid, int planid);
	
	public ValidationResult getValidation(int planid);
	
	public String getNodeInformationHTML(int nid, int planid);
		
	public PlanNode getPlanNode(int nid, int pid);
	
	public ArrayList<Property> getReferencableColumnsWithoutAdded(int nid, int pid);
	
	public RemoteManipulationMessage updatePlanNode(int nid, int pid,PlanNode p);
	
	public ArrayList<ValidationError> valideContentNodeGrammer(ContentNode c,ArrayList<GoAble> schema, boolean stayFlat); 

}
