package com.algebraweb.editor.client.node;


import java.util.ArrayList;
import java.util.Iterator;


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
	private String kind;

	protected NodeScheme scheme;
	private QueryPlan mother;


	/**
	 * Returns the nodes general scheme as specified in the
	 * scheme XML
	 * @return
	 */

	public NodeScheme getScheme() {
		return scheme;
	}


	public void setScheme(NodeScheme scheme) {
		this.scheme = scheme;
	}



	public PlanNode(int id, String kind, NodeScheme scheme, QueryPlan mother) {

		this.id=id;
		this.kind=kind;
		this.mother=mother;
		this.scheme=scheme;
				
	}	

	public PlanNode() {

	}	



	public ArrayList<PlanNode> getChilds() {

		return nodeChilds;

	}

	public ArrayList<NodeContent> getContent() {
		return childs;
	}

	public PropertyMap getProperties() {
		return properties;
	}

	public void setContent(ArrayList<NodeContent> content) {
		this.childs = content;
	}

	public void setChilds(ArrayList<PlanNode> childs) {
		this.nodeChilds = childs;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public ArrayList<Property> getReferencableColumnsWithoutAdded() {

		ArrayList<Property> ret = new ArrayList<Property>();


		Iterator<PlanNode> i = this.getChilds().iterator();

		while (i.hasNext()) {

			PlanNode cur = i.next();

			if (cur != null) {

				ArrayList<Property> gurr = cur.getReferencableColumnsFromValues();
				ret.addAll(gurr);

			}

		}


		return ret;

	}

	public boolean resetsColumns() {


		return (((NodeScheme)this.getScheme()).getProperties().containsKey("reset_columns") &&
				((NodeScheme)this.getScheme()).getProperties().get("reset_columns").equals("true"));

	}

	public ArrayList<Property> getReferencableColumnsFromValues() {

		ArrayList<Property> ret = new ArrayList<Property>();

		if (!resetsColumns()) {

			ret.addAll(getReferencableColumnsWithoutAdded());

			ret.removeAll(getRemovedColumns());


		}

		ret.addAll(getAddedColumns());

		return ret;


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

		nodeChilds.set(pos-1, c);


		//first, clear all old edges
		Iterator<NodeContent> it = getDirectContentWithInternalName("edge").iterator();

		while(it.hasNext()) {
			childs.remove(it.next());
		}

		Iterator<PlanNode> childsIt = nodeChilds.iterator();

		while (childsIt.hasNext()) {



			PlanNode cur = childsIt.next();

			ContentVal edge = new ContentVal("edge", "edge", "");

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

		Iterator<NodeContent> it = childs.iterator();
		boolean success=false;

		while (it.hasNext()) {

			NodeContent cur = it.next();

			if (cur.getInternalName().equals("edge") && cur.getAttributes().get("to").getVal().equals(Integer.toString(nid))) {

				it.remove();		
				success=true;

			}

		}

		if (success) {
			nodeChilds.set(nodeChilds.indexOf(mother.getPlanNodeById(nid)),null);
		}

		return success;


	}



	public String toString() {

		String ret= "{NODE: id=" + id + " kind:" + kind + " CONTENT:[";
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



			if (cur.getXmlObject().equals("edge")) {

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

}
