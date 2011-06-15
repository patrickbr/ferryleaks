package com.algebraweb.editor.server.logicalplan.communication;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;

import com.algebraweb.editor.client.RemoteManipulationMessage;
import com.algebraweb.editor.client.RemoteManipulationService;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.ContentNode;
import com.algebraweb.editor.server.logicalplan.ContentVal;
import com.algebraweb.editor.server.logicalplan.NodeContent;
import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.Property;
import com.algebraweb.editor.server.logicalplan.QueryPlan;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.server.logicalplan.ValGroup;
import com.algebraweb.editor.server.logicalplan.validation.ValidationMachine;
import com.algebraweb.editor.server.logicalplan.validation.validators.AbandondedNodeValidator;
import com.algebraweb.editor.server.logicalplan.validation.validators.GrammarValidator;
import com.algebraweb.editor.server.logicalplan.validation.validators.ReferencedColumnsValidator;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.PlanParser;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Provides all basic plan manipulation methods like adding and deleting nodes, creating edges etc.
 * @author Patrick Brosi
 *
 */

public class PlanModelCommunicationServlet extends RemoteServiceServlet implements RemoteManipulationService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3178031477829024689L;


	private ValidationMachine vm;


	public PlanModelCommunicationServlet() {
		
		vm = new ValidationMachine();
		
		vm.addValidator(new ReferencedColumnsValidator());
		vm.addValidator(new AbandondedNodeValidator());
		vm.addValidator(new GrammarValidator());

	}


	@Override
	public RemoteManipulationMessage deleteNode(int nid, int planid) {


		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(planid);		
		PlanNode nodeToWork = planToWork.getPlanNodeById(nid);

		if (nodeToWork == null) {

			//node doesnt exist...

			return new RemoteManipulationMessage("delete", 3, "Node doesn't exists in plan", null);


		}

		Iterator<PlanNode> it = planToWork.getPlan().iterator();

		while (it.hasNext()) {

			PlanNode current = it.next();

			current.deleteChild(nid);

		}

		if (planToWork.getPlan().remove(nodeToWork)) {	

			System.out.println("Deleted node #" + nid);
			//deleted, revalidating...

			ValidationResult res = getValidation(planid);

			RemoteManipulationMessage rmm= new RemoteManipulationMessage("delete", 1, "", res);
			
			rmm.getNodesAffected().add(nid);
			
			return rmm;
			
		}else{
			
			return new RemoteManipulationMessage("delete", 3, "Node doesn't exists in plan", null);

			
		}

	}

	@Override
	public ValidationResult getValidation(int planid) {

		System.out.println("Validation requested for " + planid);

		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToValidate = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(planid);		


		//TODO: NODE ID IS FIX

		return vm.validate(planToValidate, planToValidate.getPlan());


	}

	@Override
	public String getNodeInformationHTML(int nid, int planid) {

		String ret = "";

		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(planid);		

		PlanNode nodeToWork = planToWork.getPlanNodeById(nid);

		ArrayList<ValidationError> vErrors;

		if ((vErrors = vm.validate(planToWork,nodeToWork)).size() > 0) {

			ret += "<div class='nodeinfo-error'>";

			Iterator<ValidationError> it = vErrors.iterator();

			while (it.hasNext()) {

				ret +="<div class='nodeinfo-error-item'>" + it.next().getErrorMsg() + "</div>";

			}


			ret +="</div>";


		}


		ret += "<div class='nodeinfo-block'><h4>Id:</h4>" + nid + "</div>";
		ret += "<div class='nodeinfo-block'><h4>Type:</h4>"+ nodeToWork.getKind() +"</div>";

		ret += "<div class='nodeinfo-block'><h4>Referencable columns:</h4>";

		Iterator<Property> it = nodeToWork.getReferencableColumnsWithoutAdded().iterator();

		while(it.hasNext()) {

			ret += it.next().getPropertyVal().getVal();

			if (it.hasNext()) ret+= ", ";

		}

		ret+="</div>";

		ret += "<div class='nodeinfo-block'><h4>Referenced columns:</h4>";

		it = nodeToWork.getReferencedColumns().iterator();

		while(it.hasNext()) {

			ret += it.next().getPropertyVal().getVal();

			if (it.hasNext()) ret+= ", ";

		}

		ret+="</div>";

		ret += "<div class='nodeinfo-block'><h4>Columns introduced:</h4>";

		it = nodeToWork.getAddedColumns().iterator();

		while(it.hasNext()) {

			ret += it.next().getPropertyVal().getVal();

			if (it.hasNext()) ret+= ", ";

		}

		ret+="</div>";

		ret += "<div class='nodeinfo-block'><h4>Columns discarded:</h4>";

		if (nodeToWork.resetsColumns()) {
			ret += "(all)";
		}else{

			it = nodeToWork.getRemovedColumns().iterator();

			while(it.hasNext()) {

				ret += it.next().getPropertyVal().getVal();

				if (it.hasNext()) ret+= ", ";

			}
		}

		ret+="</div>";
		
		
		ret +=getNodeContentList(nodeToWork);


		return ret;

	}
	
	
	private String getNodeContentList(ContentNode n) {
		
		
		String ret="";
		
			
		if (n instanceof ValGroup) {
			
			ret +="<li class='nodeinfo-valgroup'><span class='valgroupname'>" + ((ValGroup)n).getName() + "</span>";
		}
		
		if (n instanceof ContentVal) {
			
			ret +="<li class='nodeinfo-nodecontent'><span class='contentname'>" + ((NodeContent)n).getName() + "</span><div class='content-attrs'><ul>";
			
			Iterator<Property> it = ((ContentVal)n).getAttributes().properties().iterator();
			
			while (it.hasNext()) {
				
				Property current = it.next();
				
				ret+="<li>" + current.getPropertyName() + "=" + current.getPropertyVal().getVal() + "</li>";
				
			}
			ret +="</ul>";
			ret +="</div>";
			
		}
		
		
		ret +="<ul class='nodecontentinfo-ul'>";
		
		
		Iterator<NodeContent> i = n.getContent().iterator();
		
		while (i.hasNext()) {
			
			ret += getNodeContentList(i.next());
			
		}
			
			
		ret +="</ul>";
		
		
		ret +="</li>";
		
		
		
		return ret;
		
		
	}












}
