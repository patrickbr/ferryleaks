package com.algebraweb.editor.server.logicalplan.communication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.css.sac.InputSource;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.RemoteManipulationMessage;
import com.algebraweb.editor.client.RemoteManipulationService;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.ContentVal;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.node.QueryPlan;
import com.algebraweb.editor.client.node.ValGroup;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.server.logicalplan.evaluator.SqlEvaluator;
import com.algebraweb.editor.server.logicalplan.sqlbuilder.PlanNodeSQLBuilder;
import com.algebraweb.editor.server.logicalplan.validation.ValidationMachine;
import com.algebraweb.editor.server.logicalplan.validation.validators.AbandondedNodeValidator;
import com.algebraweb.editor.server.logicalplan.validation.validators.GrammarValidator;
import com.algebraweb.editor.server.logicalplan.validation.validators.ReferencedColumnsValidator;
import com.algebraweb.editor.server.logicalplan.xmlbuilder.XMLNodePlanBuilder;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.XMLPlanFiller;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.PlanParser;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeSchemeLoader;
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
	private GrammarValidator gv = new GrammarValidator();
	private XMLNodePlanBuilder npb = new XMLNodePlanBuilder();


	public PlanModelCommunicationServlet() {

		vm = new ValidationMachine();

		vm.addValidator(new ReferencedColumnsValidator());
		vm.addValidator(new AbandondedNodeValidator());
		vm.addValidator(gv);

	}


	@Override
	public RemoteManipulationMessage deleteNodes(Integer[] nids, int planid) {


		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(planid);		

		RemoteManipulationMessage ret = new RemoteManipulationMessage(planid,"delete", 1, "", null);
		
		
		for (int nid : nids) {

			PlanNode nodeToWork = planToWork.getPlanNodeById(nid);

			if (nodeToWork == null) return new RemoteManipulationMessage(planid,"delete", 3, "Node doesn't exists in plan", null);


			Iterator<PlanNode> it = planToWork.getPlan().iterator();

			while (it.hasNext()) {

				PlanNode current = it.next();

				current.deleteChild(nid);

			}

			if (planToWork.getPlan().remove(nodeToWork)) {	

			
				ret.getNodesAffected().add(new RawNode(nid));


			}else{

				return new RemoteManipulationMessage(planid,"delete", 3, "Node doesn't exists in plan", null);


			}

		}
		
		ret.setValidationResult(getValidation(planid));
		
		return ret;
		
		

	}

	@Override
	public ValidationResult getValidation(int planid) {

		System.out.println("Validation requested for " + planid);

		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToValidate = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(planid);		

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


	@Override
	public PlanNode getPlanNode(int nid, int pid) {
		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		
		PlanNode nodeToWork = planToWork.getPlanNodeById(nid);

		return nodeToWork;
	}


	@Override
	public RemoteManipulationMessage updatePlanNode(int nid, int pid, String xml) {

		System.out.println("Trying to save " + xml);

		HttpServletRequest request = this.getThreadLocalRequest();
		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		

		PlanParser p = new PlanParser((HashMap<String,NodeScheme>)getServletContext().getAttribute("nodeSchemes"));


		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputStream s = new ByteArrayInputStream(xml.getBytes());

			Document doc;

			doc = db.parse(s);
			PlanNode newNode = p.parseNode(planToWork, (org.w3c.dom.Element)doc.getElementsByTagName("node").item(0));
			return updatePlanNode(nid,pid,newNode);

		} catch (SAXException e) {
			return new RemoteManipulationMessage(pid, "", 3, "Error while parsing XML: " + e.getMessage(), null);
		} catch (IOException e) {
			return new RemoteManipulationMessage(pid, "", 3, "Error while parsing XML: " +e.getMessage(), null);
		} catch (ParserConfigurationException e) {
			return new RemoteManipulationMessage(pid, "", 3, "Error while parsing XML: " +e.getMessage(), null);
		}




	}

	@Override
	public RemoteManipulationMessage updatePlanNode(int nid, int pid, PlanNode p) {
		// TODO update other things 

		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		
		PlanNode nodeToWork = planToWork.getPlanNodeById(nid);




		if (nodeToWork != null) {	

			nodeToWork.setContent(p.getContent());

			System.out.println("Updated node #" + nid);
			//deleted, revalidating...

			ValidationResult res = getValidation(pid);


			RemoteManipulationMessage rmm= new RemoteManipulationMessage(pid,"update", 1, "", res);

			XMLPlanFiller xmlpl = new XMLPlanFiller(request.getSession(),getServletContext(),pid);

			rmm.getNodesAffected().add(xmlpl.getRawNode(nodeToWork));

			return rmm;

		}else{

			return new RemoteManipulationMessage(pid,"update", 3, "Node doesn't exists in plan", null);


		}

	}


	@Override
	public ArrayList<ValidationError> valideContentNodeGrammer(ContentNode c,ArrayList<GoAble> schema,boolean stayFlat) {

		return gv.validateContentNode(c, schema,stayFlat);


	}


	@Override
	public ArrayList<Property> getReferencableColumnsWithoutAdded(int nid,
			int pid) {

		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		
		PlanNode nodeToWork = planToWork.getPlanNodeById(nid);

		return nodeToWork.getReferencableColumnsWithoutAdded();
	}


	@Override
	public String getXMLFromContentNode(ContentNode c) {
		Element e =  npb.getXMLElementFromContentNode(c);

		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(e);

	}

	@Override
	public String getXMLFromPlanNode(int pid, int nid) {


		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(getDomXMLFromPlanNode(pid,nid));

	}

	private Element getDomXMLFromPlanNode(int pid, int nid) {

		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		
		PlanNode nodeToWork = planToWork.getPlanNodeById(nid);

		return npb.getXMLElementFromContentNode(nodeToWork);

	}


	@Override
	public String getXMLLogicalPlanFromRootNode(int pid, int nid) {

		Element e = getDomXMLLogicalPlanFromRootNode(pid, nid);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(e);
	}


	private Element getDomXMLLogicalPlanFromRootNode(int pid, int nid) {
		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		
		PlanNode nodeToWork = planToWork.getPlanNodeById(nid);

		Element e = npb.getNodePlan(pid, nodeToWork);
		return e;
	}


	@Override
	public RemoteManipulationMessage addNode(int pid,String nodeType, int x, int y) {

		HttpServletRequest request = this.getThreadLocalRequest();
		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		



		HashMap<String,NodeScheme> schemes = (HashMap<String,NodeScheme>) getServletContext().getAttribute("nodeSchemes");

		if (schemes.containsKey(nodeType)) {

			if (planToWork != null) {	

				PlanNode newNode = planToWork.addNode(schemes.get(nodeType));



				ValidationResult res = getValidation(0);

				RemoteManipulationMessage rmm= new RemoteManipulationMessage(pid,"add", 1, "", res);

				HashMap<Integer,Coordinate> coords = new HashMap<Integer,Coordinate>();
				coords.put(newNode.getId(), new Coordinate(x,y));

				rmm.setCoordinates(coords);

				XMLPlanFiller xmlpl = new XMLPlanFiller(request.getSession(),getServletContext(),pid);

				rmm.getNodesAffected().add(xmlpl.getRawNode(newNode));

				return rmm;

			}else{

				return new RemoteManipulationMessage(pid, "update", 3, "Plan doesn't exist in session", null);


			}}else{

				return new RemoteManipulationMessage(pid, "update", 3, "Scheme for type " + nodeType + " not found.", null);



			}
	}


	@Override
	public String[] getNodeTypes() {

		HashMap<String,NodeScheme> nodeSchemes;


		if (getServletContext().getAttribute("nodeSchemes") == null) {

			//TODO: make this configurable
			NodeSchemeLoader l = new NodeSchemeLoader(getServletContext().getRealPath("/schemes"));
			nodeSchemes = new HashMap<String,NodeScheme>();

			Iterator<NodeScheme> i = l.parse().iterator();

			while (i.hasNext()) {

				NodeScheme n = i.next();
				nodeSchemes.put(n.getKind(), n);

			}

			getServletContext().setAttribute("nodeSchemes", nodeSchemes);

		}else	nodeSchemes = (HashMap<String,NodeScheme>) getServletContext().getAttribute("nodeSchemes");

		return nodeSchemes.keySet().toArray(new String[0]);


	}


	@Override
	public String getSQLFromPlanNode(int pid, int nid) {

		System.out.println("getting sql for plan #" + pid);

		Element d = getDomXMLLogicalPlanFromRootNode(pid,nid);

		PlanNodeSQLBuilder sqlB = new PlanNodeSQLBuilder();

		return sqlB.getCompiledSQL(d).get(pid);



	}


	@Override
	public ArrayList<HashMap<String,String>> eval(int pid, int nid, EvaluationContext context) {

		SqlEvaluator eval = new SqlEvaluator(context);

		return eval.eval(getSQLFromPlanNode(pid,nid));

	}


	@Override
	public Integer createNewPlan() {

		//TODO: everthing fixed

		QueryPlanBundle b = new QueryPlanBundle();

		b.addPlan(new QueryPlan(0));

		HttpServletRequest request = this.getThreadLocalRequest();

		request.getSession(true).setAttribute("queryPlans",b);

		return 0;

	}


	@Override
	public void markAsRoot(int pid, int nid) {

		HttpServletRequest request = this.getThreadLocalRequest();
		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		

		planToWork.setRoot(planToWork.getPlanNodeById(nid));

	}


	@Override
	public EvaluationContext getEvaluationContext(int pid, int nid) {
		
		HttpServletRequest request = this.getThreadLocalRequest();

		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		
		
		
		return planToWork.getEvContext() ;
	}

}
