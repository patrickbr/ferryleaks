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
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RemoteManipulationServiceAsync {

	void deleteNode(int nid, int planid, AsyncCallback<RemoteManipulationMessage> callback);

	void getValidation(int planid, AsyncCallback<ValidationResult> callback);

	void getNodeInformationHTML(int nid, int planid,
			AsyncCallback<String> callback);


	void getPlanNode(int nid, int pid, AsyncCallback<PlanNode> callback);

	void updatePlanNode(int nid, int pid, String xml,
			AsyncCallback<RemoteManipulationMessage> callback);

	void valideContentNodeGrammer(ContentNode c, ArrayList<GoAble> schema,
			boolean stayFlat, AsyncCallback<ArrayList<ValidationError>> callback);

	void getReferencableColumnsWithoutAdded(int nid, int pid,
			AsyncCallback<ArrayList<Property>> callback);

	void getXMLFromContentNode(ContentNode c, AsyncCallback<String> callback);

	void getXMLFromPlanNode(int pid, int nid, AsyncCallback<String> callback);

	void updatePlanNode(int nid, int pid, PlanNode p,
			AsyncCallback<RemoteManipulationMessage> callback);

	void getXMLLogicalPlanFromRootNode(int pid, int nid,
			AsyncCallback<String> callback);

	void addNode(int planid, String nodeType, int x, int y,
			AsyncCallback<RemoteManipulationMessage> callback);
	
	

}
