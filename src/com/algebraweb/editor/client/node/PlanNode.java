package com.algebraweb.editor.client.node;


import java.util.ArrayList;
import java.util.Iterator;


import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.NodeScheme;



public class PlanNode extends ContentNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1765471707304843471L;


	private PropertyMap properties = new PropertyMap();
	private ArrayList<PlanNode> nodeChilds = new ArrayList<PlanNode>();
	private int id;
	protected NodeScheme scheme;
	private QueryPlan mother;

	private EvaluationContext c;


	public PlanNode(int id, NodeScheme scheme, QueryPlan mother) {
		this.id=id;
		this.mother=mother;
		this.scheme=scheme;

		System.out.println("Creating instance of node with kind=" + scheme.getKind() + " and max child count = " + getMaxChildCount());


	}	

	public PlanNode() {

	}	

	/**
	 * Returns the nodes general scheme as specified in the
	 * constructor
	 * @return
	 */

	public NodeScheme getScheme() {
		return scheme;
	}

	/**
	 * Returns the node's childs, that is the
	 * nodes this node has edges to
	 * @return the childs of this node
	 */
	public ArrayList<PlanNode> getChilds() {
		return nodeChilds;
	}

	/**
	 * Returns the content of this node
	 * @return the content of this node as an ArrayList
	 */
	public ArrayList<NodeContent> getContent() {
		return childs;
	}

	/**
	 * Returns the properties of this node as a PropertyMap
	 * @return the properties of the node 
	 */

	public PropertyMap getProperties() {
		return properties;
	}

	/**
	 * Sets the content of this node. This should not be
	 * called after node creation
	 * @param content
	 */	

	public void setContent(ArrayList<NodeContent> content) {
		this.childs = content;
	}

	/**
	 * Sets the childs of this node. This should not be
	 * called after node creation since synchronization with
	 * the childs specified in the content is NOT guaranteed.
	 * @param childs
	 */

	public void setChilds(ArrayList<PlanNode> childs) {
		this.nodeChilds = childs;
	}

	/**
	 * Returns the node id
	 * @return the id of this node
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the kind of this node as specified in
	 * its scheme
	 * @return the kind as a string
	 */
	public String getKind() {
		return scheme.getKind();
	}

	/**
	 * Returns the columns referancable from this node <b>without</b>
	 * the columns introduced
	 * @return
	 */

	public ArrayList<Property> getReferencableColumnsWithoutAdded() {

		ArrayList<Property> ret = new ArrayList<Property>();
		Iterator<PlanNode> i = this.getChilds().iterator();

		while (i.hasNext()) {
			PlanNode cur = i.next();
			if (cur != null) {
				addPropertiesDistinct(ret,cur.getReferencableColumnsFromValues());
			}
		}
		return ret;
	}

	/**
	 * Returns the columns referancable from this node <b>without</b>
	 * the columns introduced in the specifed child (left to right)
	 * @return
	 */

	public ArrayList<Property> getReferencableColumnsWithoutAdded(int pos) {

		ArrayList<Property> ret = new ArrayList<Property>();

		if (this.getChilds().size() < pos) return ret;
		
		PlanNode cur = this.getChilds().get(pos-1);
		if (cur != null) {
			addPropertiesDistinct(ret,cur.getReferencableColumnsFromValues());
		}

		return ret;
	}


	/**
	 * Returns whether this node is marked as resetting all columns
	 * in its scheme
	 * @return true if node resets columns
	 */

	public boolean resetsColumns() {
		return (((NodeScheme)this.getScheme()).getProperties().containsKey("reset_columns") &&
				((NodeScheme)this.getScheme()).getProperties().get("reset_columns").equals("true"));
	}

	/**
	 * Returns all columns referencable <b>in</b> this node.
	 * @return all columns that can be referenced in this node from a parent node
	 */

	public ArrayList<Property> getReferencableColumnsFromValues() {

		ArrayList<Property> ret = new ArrayList<Property>();

		if (!resetsColumns()) {
			addPropertiesDistinct(ret, getReferencableColumnsWithoutAdded());
			ret.removeAll(getRemovedColumns());
		}

		ret.addAll(getAddedColumns());
		return ret;
	}


	private void addPropertiesDistinct(ArrayList<Property> ret,	ArrayList<Property> toAdd) {
		Iterator<Property> it = toAdd.iterator();

		while (it.hasNext()) {
			Property cur = it.next();
			if (!containsColumnWithName(ret,cur)) {
				ret.add(cur);
			}
		}
	}


	private boolean containsColumnWithName(ArrayList<Property> ret, Property cur) {
		Iterator<Property> it = ret.iterator();
		while (it.hasNext()) if (it.next().getPropertyVal().getVal().equals(cur.getPropertyVal().getVal())) return true;
		return false;
	}


	private ArrayList<Property> getAttributePropertiesByTypesFromValues(NodeContent nc, String[] types) {

		ArrayList<Property> ret = new ArrayList<Property>();
		PropertyMap attributes = nc.getAttributes();
		Iterator<Property> it = attributes.properties().iterator();

		while (it.hasNext()) {

			Property current = it.next();

			for (String type : types) {
				if (current.getPropertyVal().getType().matches(type)) {
					ret.add(current);
				}
			}
		}

		Iterator<NodeContent> childsIt = nc.getContent().iterator();

		while (childsIt.hasNext()) {
			ret.addAll(getAttributePropertiesByTypesFromValues(childsIt.next(), types));
		}
		return ret;
	}

	public ArrayList<Property> getReferencedColumns() {

		String[] types = {"__COLUMN[\\{]?[0-9]*[\\}]?","__COLUMN_REMOVE"};

		ArrayList<Property> ret = new ArrayList<Property>();

		Iterator<NodeContent> it = getContent().iterator();

		while (it.hasNext()) {

			ret.addAll(getAttributePropertiesByTypesFromValues(it.next(), types));
		}

		return ret;

	}

	public ArrayList<Property> getAddedColumns() {

		String[] types = {"__COLUMN_NEW"};


		ArrayList<Property> ret = new ArrayList<Property>();

		Iterator<NodeContent> it = getContent().iterator();

		while (it.hasNext()) {

			ret.addAll(getAttributePropertiesByTypesFromValues(it.next(), types));
		}

		return ret;

	}

	public ArrayList<Property> getRemovedColumns() {

		String[] types = {"__COLUMN_REMOVE"};


		ArrayList<Property> ret = new ArrayList<Property>();

		Iterator<NodeContent> it = getContent().iterator();

		while (it.hasNext()) {

			ret.addAll(getAttributePropertiesByTypesFromValues(it.next(), types));
		}

		return ret;

	}


	public void addChild(PlanNode c, int pos) {

		while (nodeChilds.size() < pos) {
			nodeChilds.add(null);
		}
		
		nodeChilds.set(pos-1, c);


		//first, clear all old edges
		Iterator<NodeContent> it = getDirectContentWithInternalName("edge").iterator();

		while(it.hasNext()) {
			childs.remove(it.next());
		}

		Iterator<PlanNode> childsIt = nodeChilds.iterator();

		while (childsIt.hasNext()) {



			PlanNode cur = childsIt.next();


			ContentVal edge = new ContentVal("edge", "edge", null);

			PropertyMap pm = new PropertyMap();
			Property to;

			if (cur != null) to = new Property("to", Integer.toString(cur.getId()), "int");
			else to = new Property("to", Integer.toString(-1), "int");

			pm.put(to);
			edge.setAttributes(pm);

			childs.add(edge);


		}




	}

	public boolean removeChild(int nid) {

		int count = nodeChilds.size();
		boolean success=false;

		for (int i=0;i<count;i++) {

			success=removeChild(nid,i+1);

		}

		return success;

	}

	public boolean removeChild(int nid, int pos) {

		System.out.println("Removing child #" + nid + "from pos " + pos);

		Iterator<NodeContent> it = childs.iterator();
		boolean success=false;
		int count = 1;

		while (it.hasNext() && !success) {

			NodeContent cur = it.next();

			if (cur.getInternalName().equals("edge")) {

				if (count != pos) {
					count++;
				}else if (cur.getAttributes().get("to").getVal().equals(Integer.toString(nid))){
					cur.getAttributes().get("to").setVal("-1");		
					success=true;
				}

			}

		}

		if (success) {
			nodeChilds.set(pos-1,null);
		}

		return success;


	}



	public String toString() {

		String ret= "{NODE: id=" + id + " kind:" + scheme.getKind() + " CONTENT:[";
		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {
			ret+=i.next().toString();
		}

		return ret+"]}";
	}


	public ArrayList<NodeContent> getDirectContentWithInternalName(String name) {
		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();

		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c.getInternalName().equals(name)) temp.add(c);

		}

		return temp;
	}


	@Override
	public String getInternalName() {
		return "node_" + id;
	}


	/**
	 * @return the mother
	 */
	public QueryPlan getMother() {
		return mother;
	}


	/**
	 * @return the maxChildCount
	 */
	public int getMaxChildCount() {

		Iterator<GoAble> it = scheme.getSchema().iterator();

		while (it.hasNext()) {

			GoAble cur = it.next();



			if (cur.getInternalName().equals("edge")) {

				if (cur.getHowOften().equals("2") || cur.getHowOften().equals("{,2}")) {

					return 2;

				}

				if (cur.getHowOften().equals("1") || cur.getHowOften().equals("{,1}")) {

					return 1;

				}

			}

		}

		return -1;

	}


	public String getLabel() {

		String ret="";

		Iterator<LabelOb> it = labelScheme.iterator();

		while (it.hasNext()) {

			LabelOb cur = it.next();

			if (cur instanceof LabelStringOb) ret += cur.getVal();

			if (cur instanceof LabelAttrIdentifierOb) {

				if (cur.getVal().equals("_kind")) {

					ret += getKind();

				}
			}


			if (cur instanceof LabelContentIdentifierOb) {

				Iterator<NodeContent> iter = getAllContentWithValName(cur.getVal()).iterator();

				String temp ="";

				while (iter.hasNext()) {

					temp += iter.next().getLabel() + " ";

				}

				if (temp.endsWith(" ")) ret += temp.substring(0,temp.length()-1).trim();

			}

		}

		if (ret == "") return getKind();
		else return ret;
	}

	/**
	 * @return the c
	 */
	public EvaluationContext getEvaluationContext() {
		return c;
	}

	/**
	 * @param c the c to set
	 */
	public void setEvaluationContext(EvaluationContext c) {
		this.c = c;
	}





}
