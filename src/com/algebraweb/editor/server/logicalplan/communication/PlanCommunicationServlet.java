package com.algebraweb.editor.server.logicalplan.communication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.configuration.Configuration;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.RemoteManipulationMessage;
import com.algebraweb.editor.client.RemoteManipulationService;
import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.GraphIsEmptyException;
import com.algebraweb.editor.client.logicalcanvas.GraphNotConnectedException;
import com.algebraweb.editor.client.logicalcanvas.LogicalCanvasSQLException;
import com.algebraweb.editor.client.logicalcanvas.PathFinderCompilationError;
import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
import com.algebraweb.editor.client.logicalcanvas.PlanManipulationException;
import com.algebraweb.editor.client.logicalcanvas.PlanNodeCopyMessage;
import com.algebraweb.editor.client.logicalcanvas.RemoteIOException;
import com.algebraweb.editor.client.logicalcanvas.SessionExpiredException;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.evaluator.SqlEvaluator;
import com.algebraweb.editor.server.logicalplan.sqlbuilder.PlanNodeSQLBuilder;
import com.algebraweb.editor.server.logicalplan.validation.ValidationMachine;
import com.algebraweb.editor.server.logicalplan.validation.validators.AbandondedNodeValidator;
import com.algebraweb.editor.server.logicalplan.validation.validators.GrammarValidator;
import com.algebraweb.editor.server.logicalplan.validation.validators.ReferencedColumnsValidator;
import com.algebraweb.editor.server.logicalplan.validation.validators.ReferencedNodesValidator;
import com.algebraweb.editor.server.logicalplan.xmlbuilder.XMLNodePlanBuilder;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.XMLPlanFiller;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.EvaluationContextProvider;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser.PlanParser;
import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeSchemeLoader;
import com.algebraweb.editor.shared.logicalplan.ClipBoardPlanNode;
import com.algebraweb.editor.shared.logicalplan.QueryPlanBundle;
import com.algebraweb.editor.shared.node.ContentNode;
import com.algebraweb.editor.shared.node.ContentVal;
import com.algebraweb.editor.shared.node.NodeContent;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.Property;
import com.algebraweb.editor.shared.node.QueryPlan;
import com.algebraweb.editor.shared.node.ValGroup;
import com.algebraweb.editor.shared.scheme.NodeScheme;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Provides all basic plan manipulation methods like adding and deleting nodes, creating edges etc.
 * @author Patrick Brosi
 *
 */

public class PlanCommunicationServlet extends RemoteServiceServlet implements RemoteManipulationService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3178031477829024689L;

	private ValidationMachine vm;
	private GrammarValidator gv = new GrammarValidator();
	private XMLNodePlanBuilder npb = new XMLNodePlanBuilder();

	public PlanCommunicationServlet() {
		vm = new ValidationMachine();
		vm.addValidator(new ReferencedColumnsValidator());
		vm.addValidator(new AbandondedNodeValidator());
		vm.addValidator(gv);
		vm.addValidator(new ReferencedNodesValidator());
	}

	@Override
	public RemoteManipulationMessage addEdge(int planid, Coordinate e, int pos) throws PlanManipulationException {
		XMLPlanFiller xmlpl = new XMLPlanFiller(getSession(),getServletContext(),planid);
		RemoteManipulationMessage ret = new RemoteManipulationMessage(planid, "update", 1, "", null);

		int from = (int) e.getX();
		int to = (int) e.getY();

		PlanNode fromNode = getPlanToWork(planid).getPlanNodeById(from);
		PlanNode toNode = getPlanToWork(planid).getPlanNodeById(to);
		fromNode.addChild(toNode, pos);
		ret.getNodesAffected().add(xmlpl.getRawNode(fromNode));
		try {
			ret.setValidationResult(getValidation(planid));
		}catch(PlanHasCycleException ee) {
			ValidationResult r = new ValidationResult(planid);
			r.setPlanid(planid);
			r.addError(new ValidationError(fromNode.getId(), "Plan contains a cycle!"));
			ret.setValidationResult(r);
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteManipulationMessage addNode(int pid,String nodeType, int x, int y) throws PlanManipulationException, PlanHasCycleException {
		HashMap<String,NodeScheme> schemes = (HashMap<String,NodeScheme>) getServletContext().getAttribute("nodeSchemes");

		if (schemes.containsKey(nodeType)) {
			if (getPlanToWork(pid) != null) {	
				PlanNode newNode = getPlanToWork(pid).addNode(schemes.get(nodeType));
				PlanParser p = new PlanParser(getSession());
				p.parseNodeLabelSchema(newNode, newNode.getScheme());

				ValidationResult res = getValidation(pid);
				RemoteManipulationMessage rmm= new RemoteManipulationMessage(pid,"add", 1, "", res);
				HashMap<Integer,Coordinate> coords = new HashMap<Integer,Coordinate>();
				coords.put(newNode.getId(), new Coordinate(x,y));

				rmm.setCoordinates(coords);

				XMLPlanFiller xmlpl = new XMLPlanFiller(getSession(),getServletContext(),pid);
				rmm.getNodesAffected().add(xmlpl.getRawNode(newNode));
				return rmm;

			}else{
				throw new PlanManipulationException("Plan doesn't exist in session");
			}}else{
				throw new PlanManipulationException("Scheme for type " + nodeType + " not found.");
			}
	}

	@Override
	public void copyNodes(List<PlanNodeCopyMessage> msg, int pid) throws PlanManipulationException {
		List<ClipBoardPlanNode> clipboard = new ArrayList<ClipBoardPlanNode>();
		QueryPlan p = getPlanToWork(pid);
		Iterator<PlanNodeCopyMessage> it = msg.iterator();
		double offX=Double.MAX_VALUE;
		double offY=Double.MAX_VALUE;

		while (it.hasNext()) {
			PlanNodeCopyMessage cur = it.next();
			if (cur.getPos().getY() < offY) {
				offX = cur.getPos().getX();
				offY = cur.getPos().getY();
			}
		}

		it = msg.iterator();

		while (it.hasNext()) {
			PlanNodeCopyMessage cur = it.next();
			PlanNode curNode = p.getPlanNodeById(cur.getId());
			PlanNode copy = new PlanNode(curNode.getId(), curNode.getScheme(), null);

			Iterator<PlanNode> childs = curNode.getChilds().iterator();
			int pos=1;
			while (childs.hasNext()) {
				PlanNode curr = childs.next();
				if (curr != null && listContainsNode(curr.getId(),msg)) {
					copy.addChild(curr, pos);
					pos++;
				}
			}

			Iterator<NodeContent> content = curNode.getContent().iterator();
			while (content.hasNext()) {
				NodeContent curC = content.next();
				if (!curC.getInternalName().equals("edge")) {
					copy.getContent().add(curC);
				}
			}
			clipboard.add(new ClipBoardPlanNode(copy, new Coordinate(cur.getPos().getX()-offX,cur.getPos().getY()-offY)));
		}

		String s= "";

		Iterator<ClipBoardPlanNode> iter = clipboard.iterator();
		while(iter.hasNext()) {
			s+=iter.next().getPlanNode().getId() + ", ";
		}
		if (s.length()>1) s = s.substring(0, s.length()-2);
		getSession().setAttribute("clipboard",clipboard);
	}


	@Override
	public Integer createNewPlan(boolean clearFirst) throws SessionExpiredException {
		QueryPlanBundle b= getQueryPlanBundleFromSession();
		if (clearFirst) b.getPlans().clear();
		int id= b.getFreePlanId();
		b.addPlan(new QueryPlan(id));
		return id;
	}

	@Override
	public RemoteManipulationMessage deleteEdge(Map<Coordinate,Integer> edges, int planid) throws PlanManipulationException, PlanHasCycleException {
		XMLPlanFiller xmlpl = new XMLPlanFiller(getSession(),getServletContext(),planid);
		RemoteManipulationMessage ret = new RemoteManipulationMessage(planid, "update", 1, "", null);
		Iterator<Coordinate> it = edges.keySet().iterator();

		while(it.hasNext()) {
			Coordinate e = it.next();
			int from = (int) e.getX();
			int to = (int) e.getY();

			PlanNode fromNode = getPlanToWork(planid).getPlanNodeById(from);
			fromNode.removeChild(to,edges.get(e));
			ret.getNodesAffected().add(xmlpl.getRawNode(fromNode));
		}
		ret.setValidationResult(getValidation(planid));
		return ret;
	}

	@Override
	public RemoteManipulationMessage deleteNodes(Integer[] nids, int planid) throws PlanManipulationException, PlanHasCycleException {
		RemoteManipulationMessage ret = new RemoteManipulationMessage(planid,"delete", 1, "", null);

		for (int nid : nids) {
			PlanNode nodeToWork = getPlanToWork(planid).getPlanNodeById(nid);
			if (nodeToWork == null) return new RemoteManipulationMessage(planid,"delete", 3, "Node doesn't exists in plan", null);
			Iterator<PlanNode> it = getPlanToWork(planid).getPlan().iterator();

			while (it.hasNext()) {
				PlanNode current = it.next();
				current.removeChild(nid);
			}
			if (getPlanToWork(planid).getPlan().remove(nodeToWork)) {	
				ret.getNodesAffected().add(new RawNode(nid));
			}else{
				throw new PlanManipulationException( "Node doesn't exists in plan");
			}
		}
		ret.setValidationResult(getValidation(planid));
		return ret;
	}

	@Override
	public List<Map<String,String>> eval(int pid, int nid, EvaluationContext context,boolean saveContext) throws PlanManipulationException, PathFinderCompilationError, LogicalCanvasSQLException, PlanHasCycleException {
		if (saveContext) saveEvaluationContextForNode(nid, pid, context);
		if (context.isDatabaseSetGlobal()) {
			System.out.println("tester");
			saveDefaultDatabaseConfiguration(context);
		}
		SqlEvaluator eval = new SqlEvaluator(context);
		return eval.eval(getSQLFromPlanNode(pid,nid,context,saveContext));
	}

	@Override
	public List<Map<String, String>> evalPlan(int pid,	EvaluationContext c, boolean saveContext) throws GraphNotConnectedException, GraphIsEmptyException, PlanManipulationException, PathFinderCompilationError, LogicalCanvasSQLException, PlanHasCycleException {
		PlanNode root = getPlanToWork(pid).getRootNode();
		if (c.isDatabaseSetGlobal()) saveDefaultDatabaseConfiguration(c);
		if (saveContext) getPlanToWork(pid).setEvContext(c);
		return eval(pid, root.getId(), c, false);
	}

	private void fillDatabaseConfigurationFromGlobalDefault(EvaluationContext c)
	throws SessionExpiredException {
		c.setDatabase((String)getSession().getAttribute("databaseName"));
		c.setDatabasePassword((String)getSession().getAttribute("databasePw"));
		c.setDatabasePort((getSession().getAttribute("databasePort") != null?(Integer) getSession().getAttribute("databasePort"):5432));
		c.setDatabaseServer((getSession().getAttribute("databaseHost")!= null?(String)getSession().getAttribute("databaseHost"):"localhost"));
		c.setDatabaseUser((String)getSession().getAttribute("databaseUser"));
	}

	private Element getDomXMLFromPlanNode(int pid, int nid) throws PlanManipulationException {
		return npb.getXMLElementFromContentNode(getNodeToWork(pid, nid));
	}

	private Element getDomXMLLogicalPlanFromRootNode(int pid, int nid, EvaluationContext c) throws PlanManipulationException, PlanHasCycleException {
		return npb.getNodePlan(pid, getNodeToWork(pid, nid),c,getServletContext());
	}

	@Override
	public EvaluationContext getEvaluationContext(int pid, int nid) throws PlanManipulationException, GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException {
		EvaluationContext c = null;
		if (nid == -1) {
			c = getPlanEvaluationContext(pid);
		}else{
			if (getNodeToWork(pid, nid).getEvaluationContext() == null) {
				c = getPlanEvaluationContext(pid);
			}else{
				c = getNodeToWork(pid, nid).getEvaluationContext();
			}
		}
		if ((c.getDatabase() == null || c.getDatabase() == "")) {
			fillDatabaseConfigurationFromGlobalDefault(c);
		}
		return c;
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
	public String getNodeInformationHTML(int nid, int planid) throws PlanManipulationException, PlanHasCycleException {
		String ret = "";
		List<ValidationError> vErrors;

		if ((vErrors = vm.validate(getPlanToWork(planid), getNodeToWork(planid,nid))).size() > 0) {
			ret += "<div class='nodeinfo-error'>";
			Iterator<ValidationError> it = vErrors.iterator();

			while (it.hasNext()) {
				ret +="<div class='nodeinfo-error-item'>" + it.next().getErrorMsg() + "</div>";
			}
			ret +="</div>";
		}
		ret += "<div class='nodeinfo-block'><h4>Id:</h4>" + nid + "</div>";
		ret += "<div class='nodeinfo-block'><h4>Type:</h4>"+  getNodeToWork(planid,nid).getKind() +"</div>";
		ret += "<div class='nodeinfo-block'><h4>Referencable columns:</h4>";

		Iterator<Property> it =  getNodeToWork(planid,nid).getReferencableColumnsWithoutAdded().iterator();

		while(it.hasNext()) {
			ret += it.next().getPropertyVal().getVal();
			if (it.hasNext()) ret+= ", ";
		}
		ret+="</div>";
		ret += "<div class='nodeinfo-block'><h4>Referenced columns:</h4>";

		it = getNodeToWork(planid,nid).getReferencedColumns().iterator();

		while(it.hasNext()) {
			ret += it.next().getPropertyVal().getVal();
			if (it.hasNext()) ret+= ", ";
		}
		ret+="</div>";
		ret += "<div class='nodeinfo-block'><h4>Columns introduced:</h4>";

		it = getNodeToWork(planid,nid).getAddedColumns().iterator();

		while(it.hasNext()) {
			ret += it.next().getPropertyVal().getVal();
			if (it.hasNext()) ret+= ", ";
		}

		ret+="</div>";
		ret += "<div class='nodeinfo-block'><h4>Columns discarded:</h4>";

		if (getNodeToWork(planid,nid).resetsColumns()) {
			ret += "(all)";
		}else{
			it = getNodeToWork(planid,nid).getRemovedColumns().iterator();
			while(it.hasNext()) {
				ret += it.next().getPropertyVal().getVal();
				if (it.hasNext()) ret+= ", ";
			}
		}
		ret+="</div>";
		ret +=getNodeContentList(getNodeToWork(planid,nid));

		return ret;
	}

	private PlanNode getNodeToWork(int pid, int nid) throws PlanManipulationException {
		return getPlanToWork(pid).getPlanNodeById(nid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public String[] getNodeTypes() throws RemoteIOException {
		Map<String,NodeScheme> nodeSchemes;

		if (getServletContext().getAttribute("nodeSchemes") == null) {

			NodeSchemeLoader l = new NodeSchemeLoader(getServletContext().getRealPath(getConfiguration().getString("server.schemes.hid","/schemes")));
			nodeSchemes = new HashMap<String,NodeScheme>();
			Iterator<NodeScheme> i = l.parse().iterator();

			while (i.hasNext()) {
				NodeScheme n = i.next();
				nodeSchemes.put(n.getKind(), n);
			}
			getServletContext().setAttribute("nodeSchemes", nodeSchemes);

		}else nodeSchemes = (Map<String,NodeScheme>) getServletContext().getAttribute("nodeSchemes");

		List<String> retList = new ArrayList<String>(nodeSchemes.keySet());

		Iterator<String> retIt = retList.iterator();
		while (retIt.hasNext()) {
			String cur = retIt.next();
			List<String> blackList = getConfiguration().getList("server.schemes.hide", new ArrayList());
			if (blackList.contains(cur)) retIt.remove();
		}
		String[] schemes = retList.toArray(new String[0]);
		Arrays.sort(schemes);
		return schemes;
	}

	private EvaluationContext getPlanEvaluationContext(int pid) throws GraphNotConnectedException, GraphIsEmptyException, PlanManipulationException, PlanHasCycleException {
		if (getPlanToWork(pid).getEvContext() == null) {
			EvaluationContextProvider p = new EvaluationContextProvider(getSession());
			p.fillEvaluationContext(getPlanToWork(pid));
		}
		EvaluationContext c = getPlanToWork(pid).getEvContext();
		if (c.getDatabase() == null && c.getDatabaseServer() == null) {
			fillDatabaseConfigurationFromGlobalDefault(c);
		}
		return getPlanToWork(pid).getEvContext();
	}

	@Override
	public PlanNode getPlanNode(int nid, int pid) throws PlanManipulationException {
		PlanNode n = getNodeToWork(pid,nid);
		gv.fillContentNodeWithContentValidationResults(n,n.getScheme().getSchema());
		return n;
	}

	private QueryPlan getPlanToWork(int pid) throws PlanManipulationException {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession(false);
		if (session == null) throw new SessionExpiredException();
		return ((QueryPlanBundle)session.getAttribute("queryPlans")).getPlan(pid);		
	}

	private QueryPlanBundle getQueryPlanBundleFromSession() throws SessionExpiredException {
		QueryPlanBundle b = ((QueryPlanBundle)getSession().getAttribute("queryPlans"));		
		if (b==null) {
			b = new QueryPlanBundle();
			getSession().setAttribute("queryPlans",b);
		}
		return b;
	}

	@Override
	public List<Property> getReferencableColumns(int nid, int pid) throws GraphNotConnectedException, PlanHasCycleException {
		HttpServletRequest request = this.getThreadLocalRequest();
		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		
		PlanNode nodeToWork;

		try {
			nodeToWork = (nid > -1 ? planToWork.getPlanNodeById(nid): planToWork.getRootNode());
		}catch (GraphIsEmptyException e) {
			return new ArrayList<Property>();
		}

		if (nodeToWork == null) return new ArrayList<Property>();
		return nodeToWork.getReferencableColumnsFromValues();
	}

	@Override
	public List<Property> getReferencableColumnsWithoutAdded(int nid,
			int pid) throws PlanHasCycleException {

		HttpServletRequest request = this.getThreadLocalRequest();
		QueryPlan planToWork = ((QueryPlanBundle)request.getSession(true).getAttribute("queryPlans")).getPlan(pid);		
		PlanNode nodeToWork = planToWork.getPlanNodeById(nid);
		return nodeToWork.getReferencableColumnsWithoutAdded();
	}

	@Override
	public List<Property> getReferencableColumnsWithoutAddedFromPos(
			int nid, int pid, int pos) throws PlanManipulationException, PlanHasCycleException {
		return getNodeToWork(pid,nid).getReferencableColumnsWithoutAdded(pos);
	}

	@Override
	public PlanNode getRootNode(int pid)
	throws PlanManipulationException, PathFinderCompilationError,
	LogicalCanvasSQLException, GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException {
		return getPlanToWork(pid).getRootNode();
	}

	private HttpSession getSession() throws SessionExpiredException {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession(false);
		if (session == null) throw new SessionExpiredException();
		return session;
	}

	@Override
	public String getSQLFromPlan(int pid) throws PlanManipulationException, PathFinderCompilationError, GraphNotConnectedException, GraphIsEmptyException, PlanHasCycleException {
		return getSQLFromPlanNode(pid, getPlanToWork(pid).getRootNode().getId(), getPlanEvaluationContext(pid), false); 
	}

	@Override
	public String getSQLFromPlanNode(int pid, int nid,EvaluationContext c, boolean saveContext) throws PlanManipulationException, PathFinderCompilationError, PlanHasCycleException {
		if (saveContext) saveEvaluationContextForNode(nid, pid, c);
		Element d = getDomXMLLogicalPlanFromRootNode(pid,nid,c);
				PlanNodeSQLBuilder sqlB = new PlanNodeSQLBuilder(getConfiguration().getString("server.pf.path","pf"), getConfiguration().getString("server.pf.args","-IS"));
		return sqlB.getCompiledSQL(d).get(pid);
	}

	@Override
	public ValidationResult getValidation(int planid) throws PlanManipulationException, PlanHasCycleException {
		return vm.validate(getPlanToWork(planid),  getPlanToWork(planid).getPlan());
	}

	@Override
	public String getXMLFromContentNode(ContentNode c) {
		Element e =  npb.getXMLElementFromContentNode(c);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(e);
	}

	@Override
	public String getXMLFromPlanNode(int pid, int nid) throws PlanManipulationException {
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		return outputter.outputString(getDomXMLFromPlanNode(pid,nid));
	}

	@Override
	public String getXMLLogicalPlanFromRootNode(int pid, int nid,EvaluationContext c,boolean saveContext) throws PlanManipulationException, PlanHasCycleException {
		Element e = getDomXMLLogicalPlanFromRootNode(pid, nid,c);
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		if (saveContext) saveEvaluationContextForNode(nid, pid, c);
		return outputter.outputString(e);
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteManipulationMessage insert(int pid,int x, int y) throws PlanManipulationException, PlanHasCycleException {
		List<ClipBoardPlanNode> nodes = (List<ClipBoardPlanNode>)getSession().getAttribute("clipboard");

		if (nodes != null) {

			QueryPlan p = getPlanToWork(pid);
			Map<Integer,PlanNode> newNodes = new HashMap<Integer,PlanNode>();
			Map<Integer,Integer> idReplacements = new HashMap<Integer,Integer>();
			Iterator<ClipBoardPlanNode> it = nodes.iterator();
			List<Integer> blackList = new ArrayList<Integer>();

			while (it.hasNext()) {
				PlanNode cur = it.next().getPlanNode();
				int newId = p.getFreeId(blackList);
				blackList.add(newId);
				idReplacements.put(cur.getId(),newId);
			}

			it = nodes.iterator();

			while (it.hasNext()) {
				PlanNode cur = it.next().getPlanNode();
				PlanNode newNode = new PlanNode(idReplacements.get(cur.getId()), cur.getScheme(), p);

				PlanParser pa = new PlanParser(getSession());
				pa.parseNodeLabelSchema(newNode, newNode.getScheme());

				Iterator<NodeContent> content = cur.getContent().iterator();
				while (content.hasNext()) {
					NodeContent curC = content.next();
					if (!curC.getInternalName().equals("edge")) {
						newNode.getContent().add(curC);
					}
				}				
				p.getPlan().add(newNode);
				newNodes.put(newNode.getId(),newNode);
			}

			it = nodes.iterator();
			while (it.hasNext()) {
				PlanNode cur = it.next().getPlanNode();
				Iterator<PlanNode> childs = cur.getChilds().iterator();
				int pos=1;

				while (childs.hasNext()) {
					PlanNode child = childs.next();
					newNodes.get(idReplacements.get(cur.getId())).addChild(p.getPlanNodeById(idReplacements.get(child.getId())), pos);
					pos++;
				}
			}

			RemoteManipulationMessage res =  new RemoteManipulationMessage(pid, "add", 1, "", null);
			Map<Integer,Coordinate> coords = new HashMap<Integer,Coordinate>();
			XMLPlanFiller xmlpl = new XMLPlanFiller(getSession(),getServletContext(),pid);
			Iterator<ClipBoardPlanNode> newNodesIt = nodes.iterator();

			while (newNodesIt.hasNext()) {
				ClipBoardPlanNode cur = newNodesIt.next();
				PlanNode curNode = p.getPlanNodeById(idReplacements.get(cur.getPlanNode().getId()));
				res.getNodesAffected().add(xmlpl.getRawNode(curNode));
				coords.put(curNode.getId(),new Coordinate(cur.getPos().getX()+x,cur.getPos().getY()+y));
			}

			res.setCoordinates(coords);
			res.setValidationResult(getValidation(pid));

			return res;
		}else{
			return new RemoteManipulationMessage(pid, "update", 0, "", null );
		}
	}

	private boolean listContainsNode(int id, List<PlanNodeCopyMessage> msg) {
		Iterator<PlanNodeCopyMessage> it = msg.iterator();
		while(it.hasNext()) if (it.next().getId() == id) return true;
		return false;
	}

	@Override
	public Integer removePlan(int pid) throws SessionExpiredException {
		getQueryPlanBundleFromSession().getPlans().remove(pid);
		return pid;
	}

	private Configuration getConfiguration() {
		return (Configuration) getServletContext().getAttribute("configuration");
	}

	private void saveDefaultDatabaseConfiguration(EvaluationContext c)
	throws SessionExpiredException {
		getSession().setAttribute("databaseHost", c.getDatabaseServer());
		getSession().setAttribute("databasePort",  c.getDatabasePort());
		getSession().setAttribute("databaseName", c.getDatabase());
		getSession().setAttribute("databaseUser", c.getDatabaseUser());
		getSession().setAttribute("databasePw", c.getDatabasePassword());
	}

	private void saveEvaluationContextForNode(int nid, int pid, EvaluationContext c) throws PlanManipulationException {
		if (c.isDatabaseSetGlobal()) saveDefaultDatabaseConfiguration(c);
		getNodeToWork(pid, nid).setEvaluationContext(c);
	}

	@Override
	public void updatePlanEvaluationContext(EvaluationContext c, int pid) throws PlanManipulationException {
		if (c.isDatabaseSetGlobal()) saveDefaultDatabaseConfiguration(c);
		getPlanToWork(pid).setEvContext(c);
	}

	@Override
	public RemoteManipulationMessage updatePlanNode(int nid, int pid, PlanNode p) throws PlanManipulationException, PlanHasCycleException {
		HttpServletRequest request = this.getThreadLocalRequest();
		QueryPlan planToWork = getPlanToWork(pid);
		PlanNode nodeToWork = getNodeToWork(pid, nid);

		if (nodeToWork != null) {	
			nodeToWork.setContent(p.getContent());

			Iterator<PlanNode> nodeIt = p.getChilds().iterator();
			nodeToWork.getChilds().clear();
			while (nodeIt.hasNext()) {
				PlanNode cur = nodeIt.next();
				if (cur != null) {
					nodeToWork.getChilds().add(planToWork.getPlanNodeById(cur.getId()));
				}else{
					nodeToWork.getChilds().add(null);
				}
			}

			ValidationResult res = getValidation(pid);
			RemoteManipulationMessage rmm= new RemoteManipulationMessage(pid,"update", 1, "", res);
			XMLPlanFiller xmlpl = new XMLPlanFiller(request.getSession(),getServletContext(),pid);
			rmm.getNodesAffected().add(xmlpl.getRawNode(nodeToWork));
			return rmm;
		}else{
			throw new PlanManipulationException("Node doesn't exists in plan");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public RemoteManipulationMessage updatePlanNode(int nid, int pid, String xml) throws PlanManipulationException, PlanHasCycleException {
		PlanParser p = new PlanParser((HashMap<String,NodeScheme>)getServletContext().getAttribute("nodeSchemes"),getSession());
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();

			InputStream s = new ByteArrayInputStream(xml.getBytes());
			Document doc;

			doc = db.parse(s);
			PlanNode newNode = p.parseNode(getPlanToWork(pid), (org.w3c.dom.Element)doc.getElementsByTagName("node").item(0));
			return updatePlanNode(nid,pid,newNode);

		} catch (SAXException e) {
			throw new PlanManipulationException( "Error while parsing XML: " + e.getMessage());
		} catch (IOException e) {
			throw new PlanManipulationException( "Error while parsing XML: " + e.getMessage());
		} catch (ParserConfigurationException e) {
			throw new PlanManipulationException( "Error while parsing XML: " + e.getMessage());
		}
	}
}
