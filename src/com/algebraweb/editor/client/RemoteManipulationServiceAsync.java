package com.algebraweb.editor.client;


import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.PlanNodeCopyMessage;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.shared.node.ContentNode;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.Property;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The CommunicationServiceAsync for the RemoteManipulationService.
 * Calls to these methods will go directly to the servers implementation
 * of the corresponding CommnicationService (RemoteManipulationService)
 * 
 * Try not to access these methods directly. Instead, use the AlgebraEditor
 * or PlanModelManipulater instances where possible.
 * 
 * @author Patrick Brosi
 *
 */
public interface RemoteManipulationServiceAsync {

	void addEdge(int planid, Coordinate fromTo, int pos,
			AsyncCallback<RemoteManipulationMessage> callback);

	void addNode(int planid, String nodeType, int x, int y,
			AsyncCallback<RemoteManipulationMessage> callback);

	void copyNodes(List<PlanNodeCopyMessage> msg, int pid,
			AsyncCallback<Void> callback);

	void createNewPlan(boolean clearFirst, AsyncCallback<Integer> callback);

	void deleteEdge(Map<Coordinate, Integer> map, int planid,
			AsyncCallback<RemoteManipulationMessage> manipulationCallback);

	void deleteNodes(Integer[] nids, int planid,
			AsyncCallback<RemoteManipulationMessage> callback);

	void eval(int pid, int nid, EvaluationContext context, boolean saveContext,
			AsyncCallback<List<Map<String, String>>> callback);

	void evalPlan(int pid, EvaluationContext c, boolean saveCurrenNodeValue,
			AsyncCallback<List<Map<String, String>>> evalCb);

	void getEvaluationContext(int pid, int nid,
			AsyncCallback<EvaluationContext> callback);

	void getNodeInformationHTML(int nid, int planid,
			AsyncCallback<String> callback);

	void getNodeTypes(AsyncCallback<String[]> callback);

	void getPlanNode(int nid, int pid, AsyncCallback<PlanNode> callback);

	void getReferencableColumns(int nid, int pid,
			AsyncCallback<List<Property>> callback);

	void getReferencableColumnsWithoutAdded(int nid, int pid,
			AsyncCallback<List<Property>> callback);

	void getReferencableColumnsWithoutAddedFromPos(int nid, int pid, int pos,
			AsyncCallback<List<Property>> callback);

	void getRootNode(int pid, AsyncCallback<PlanNode> callback);

	void getSQLFromPlan(int pid, AsyncCallback<String> callback);

	void getSQLFromPlanNode(int pid, int nid, EvaluationContext c,
			boolean saveContext, AsyncCallback<String> callback);

	void getValidation(int planid, AsyncCallback<ValidationResult> callback);

	void getXMLFromContentNode(ContentNode c, AsyncCallback<String> callback);

	void getXMLFromPlanNode(int pid, int nid, AsyncCallback<String> callback);

	void getXMLLogicalPlanFromRootNode(int pid, int nid, EvaluationContext c,
			boolean saveContext, AsyncCallback<String> callback);

	void insert(int pid, int x, int y,
			AsyncCallback<RemoteManipulationMessage> callback);

	void removePlan(int pid, AsyncCallback<Integer> callback);

	void updatePlanEvaluationContext(EvaluationContext c, int pid,
			AsyncCallback<Void> callback);

	void updatePlanNode(int nid, int pid, PlanNode p,
			AsyncCallback<RemoteManipulationMessage> callback);

	void updatePlanNode(int nid, int pid, String xml,
			AsyncCallback<RemoteManipulationMessage> callback);

}
