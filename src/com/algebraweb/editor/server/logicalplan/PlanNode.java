package com.algebraweb.editor.server.logicalplan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader.NodeScheme;


public class PlanNode implements Serializable,ContentNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1765471707304843471L;


	private ArrayList<NodeContent> content = new ArrayList<NodeContent>();
	private PropertyMap properties = new PropertyMap();
	private QueryPlan myPlan;
	private NodeScheme scheme;

	private int id;
	private String kind;


	public PlanNode(int id, String kind, NodeScheme scheme, QueryPlan myPlan) {

		this.id=id;
		this.kind=kind;
		this.myPlan = myPlan;
		this.scheme=scheme;

	}	

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


	public ArrayList<PlanNode> getChilds() {

		ArrayList<PlanNode> ret = new ArrayList<PlanNode>();

		NodeContent[] edges = getValuesByInternalName("edgeto").toArray(new NodeContent[0]);

		for (NodeContent edge : edges) {
			ret.add(myPlan.getPlanNodeById(Integer.parseInt(edge.getAttributes().get("to").getVal())));
		}

		return ret;
	}

	public ArrayList<NodeContent> getContent() {
		return content;
	}

	public PropertyMap getProperties() {
		return properties;
	}

	public void setContent(ArrayList<NodeContent> content) {
		this.content = content;
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
	

	public ArrayList<NodeContent> getValuesByInternalName(String name) {

		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();

		Iterator<NodeContent> i = content.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c.getName().equals(name)) temp.add(c);
			
			//go into child
			temp.addAll(c.getValuesByInternalName(name));
			
		}

		return temp;

	}


	public ArrayList<Property> getReferencableColumnsFromValues() {

		ArrayList<Property> ret = new ArrayList<Property>();


		if (!(this.getScheme().getProperties().containsKey("reset_columns") &&
				this.getScheme().getProperties().get("reset_columns").equals("true"))) {


			Iterator<PlanNode> i = this.getChilds().iterator();

			while (i.hasNext()) {

				ret.addAll(i.next().getReferencableColumnsFromValues());

			}
		}


		ret.removeAll(getRemovedColumns());
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

				if (current.getPropertyVal().getType().equals(type)) ret.add(current);

			}
		}

		Iterator<NodeContent> childsIt = nc.getContent().iterator();

		while (childsIt.hasNext()) {

			ret.addAll(getAttributePropertiesByTypesFromValues(childsIt.next(), types));

		}


		return ret;
	}

	public ArrayList<Property> getReferencedColumns() {

		String[] types = {"__COLUMN","__COLUMN_REMOVE"};

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
		Iterator<NodeContent> i = content.iterator();

		while (i.hasNext()) {
			ret+=i.next().toString();
		}

		return ret+"]}";
	}

}
