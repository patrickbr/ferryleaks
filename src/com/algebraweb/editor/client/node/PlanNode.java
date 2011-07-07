package com.algebraweb.editor.client.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.scheme.Value;


public class PlanNode extends ContentNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1765471707304843471L;


	private PropertyMap properties = new PropertyMap();
	private QueryPlan myPlan;


	private int id;
	private String kind;
	
	protected NodeScheme scheme;
	
	
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
		


	public PlanNode(int id, String kind, NodeScheme scheme, QueryPlan myPlan) {

		this.id=id;
		this.kind=kind;
		this.myPlan = myPlan;
		this.scheme=scheme;

	}	

	public PlanNode() {

	}	





	public boolean deleteChild(int nid) {


		Iterator<NodeContent> it =  getAllContentWithInternalName("edge").iterator();

		while (it.hasNext()) {

			NodeContent current = it.next();

			if (Integer.parseInt(current.getAttributes().get("to").getVal()) == nid) {

				if (removeContent(current)) System.out.println("gurri");;
				return true;	

			}
		}

		return false;
	}


	public ArrayList<PlanNode> getChilds() {

		ArrayList<PlanNode> ret = new ArrayList<PlanNode>();

		NodeContent[] edges = getAllContentWithInternalName("edge").toArray(new NodeContent[0]);

		for (NodeContent edge : edges) {
			ret.add(myPlan.getPlanNodeById(Integer.parseInt(edge.getAttributes().get("to").getVal())));
		}

		return ret;
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
	/**

	public ArrayList<NodeContent> getAllContentWithInternalName(String name) {

		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();

		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c.getInternalName().equals(name)) temp.add(c);

			//go into child
			temp.addAll(c.getAllContentWithInternalName(name));

		}

		return temp;

	}


	public boolean removeContent(NodeContent con) {


		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c == con) {
				i.remove();
				return true;
			}

			if (c.removeContent(con)) return true;

		}


		return false;

	}

	 **/

	public ArrayList<Property> getReferencableColumnsWithoutAdded() {

		ArrayList<Property> ret = new ArrayList<Property>();


		Iterator<PlanNode> i = this.getChilds().iterator();

		while (i.hasNext()) {


			ArrayList<Property> gurr = i.next().getReferencableColumnsFromValues();

			ret.addAll(gurr);

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

}
