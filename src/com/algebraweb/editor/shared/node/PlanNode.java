package com.algebraweb.editor.shared.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.shared.exceptions.PlanHasCycleException;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.algebraweb.editor.shared.scheme.GoAble;
import com.algebraweb.editor.shared.scheme.NodeScheme;

/**
 * Represents a plan node.
 * 
 * @author Patrick Brosi
 * 
 */
public class PlanNode extends ContentNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1564622892393796345L;
	private PropertyMap properties = new PropertyMap();
	private List<PlanNode> nodeChilds = new ArrayList<PlanNode>();
	private int id;
	protected NodeScheme scheme;
	private QueryPlan mother;
	private EvaluationContext c;

	public PlanNode() {

	}

	public PlanNode(int id, NodeScheme scheme, QueryPlan mother) {
		this.id = id;
		this.mother = mother;
		this.scheme = scheme;
	}

		
	/**
	 * Adds a child to this plan node
	 * 
	 * @param c
	 *            the child to add
	 * @param pos
	 *            the position of this node from left to right, beginning with 1
	 */
	public void addChild(PlanNode c, int pos) {
		while (nodeChilds.size() < pos) {
			nodeChilds.add(null);
		}
		nodeChilds.set(pos - 1, c);
		Iterator<NodeContent> it = getDirectContentWithInternalName("edge")
				.iterator();
		while (it.hasNext()) {
			childs.remove(it.next());
		}

		Iterator<PlanNode> childsIt = nodeChilds.iterator();
		while (childsIt.hasNext()) {

			PlanNode cur = childsIt.next();
			ContentVal edge = new ContentVal("edge", "edge", null);
			PropertyMap pm = new PropertyMap();
			Property to;

			if (cur != null) {
				to = new Property("to", Integer.toString(cur.getId()), "int");
			} else {
				to = new Property("to", Integer.toString(-1), "int");
			}

			pm.put(to);
			edge.setAttributes(pm);
			childs.add(edge);
		}
	}

	/**
	 * Adds properties to this PlanNode. Single properties will only be added if
	 * they don't exist already
	 * 
	 * @param ret
	 *            the property list the new properties should be added
	 * @param toAdd
	 *            the new properties
	 */
	private void addPropertiesDistinct(List<Property> ret, List<Property> toAdd) {
		Iterator<Property> it = toAdd.iterator();
		while (it.hasNext()) {
			Property cur = it.next();
			if (!containsColumnWithName(ret, cur)) {
				ret.add(cur);
			}
		}
	}

	/**
	 * Returns true if a column with a given name is found
	 * 
	 * @param ret
	 *            the list of columns to look in
	 * @param cur
	 *            the column (given as a property) to look for
	 * @return true if a column with a given name is found
	 */
	private boolean containsColumnWithName(List<Property> ret, Property cur) {
		Iterator<Property> it = ret.iterator();
		while (it.hasNext()) {
			if (it.next().getPropertyVal().getVal().equals(
					cur.getPropertyVal().getVal())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns all columns that are added to the overall schema by this plan
	 * node
	 * 
	 * @return all columns that are added by this node
	 */
	public List<Property> getAddedColumns() {
		String[] types = { "__COLUMN_NEW" };
		List<Property> ret = new ArrayList<Property>();
		Iterator<NodeContent> it = getContent().iterator();
		while (it.hasNext()) {
			ret
					.addAll(getAttributePropertiesByTypesFromValues(it.next(),
							types));
		}
		return ret;
	}

	/**
	 * Get all attributes of given types
	 * 
	 * @param nc
	 *            the NodeContent to search in
	 * @param types
	 *            the types to look for
	 * @return the attributes with the given types
	 */
	private List<Property> getAttributePropertiesByTypesFromValues(
			NodeContent nc, String[] types) {
		List<Property> ret = new ArrayList<Property>();
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
			ret.addAll(getAttributePropertiesByTypesFromValues(childsIt.next(),
					types));
		}
		return ret;
	}

	/**
	 * Returns the node's childs, that is the nodes this node has edges to
	 * 
	 * @return the childs of this node
	 */
	public List<PlanNode> getChilds() {
		return nodeChilds;
	}

	/**
	 * Returns the content of this node
	 * 
	 * @return the content of this node as an ArrayList
	 */
	@Override
	public ArrayList<NodeContent> getContent() {
		return childs;
	}

	@Override
	public List<NodeContent> getDirectContentWithInternalName(String name) {
		List<NodeContent> temp = new ArrayList<NodeContent>();
		Iterator<NodeContent> i = childs.iterator();
		while (i.hasNext()) {
			NodeContent c = i.next();
			if (c.getInternalName().equals(name)) {
				temp.add(c);
			}
		}
		return temp;
	}

	/**
	 * Returns the evaluation context hold by this plannode
	 * 
	 * @return the evaluation context
	 */
	public EvaluationContext getEvaluationContext() {
		return c;
	}

	/**
	 * Returns the node id
	 * 
	 * @return the id of this node
	 */
	public int getId() {
		return id;
	}

	@Override
	public String getInternalName() {
		return "node_" + id;
	}

	/**
	 * Returns the kind of this node as specified in its scheme
	 * 
	 * @return the kind as a string
	 */
	public String getKind() {
		return scheme.getKind();
	}

	@Override
	public String getLabel() {
		String ret = "";
		Iterator<LabelOb> it = labelScheme.iterator();

		while (it.hasNext()) {
			LabelOb cur = it.next();
			if (cur instanceof LabelStringOb) {
				ret += cur.getVal();
			}
			if (cur instanceof LabelAttrIdentifierOb) {
				if (cur.getVal().equals("_kind")) {
					ret += getKind();
				}
			}
			if (cur instanceof LabelContentIdentifierOb) {
				Iterator<NodeContent> iter = getAllContentWithValName(
						cur.getVal()).iterator();
				String temp = "";
				while (iter.hasNext()) {
					temp += iter.next().getLabel() + " ";
				}
				if (temp.endsWith(" ")) {
					ret += temp.substring(0, temp.length() - 1).trim();
				}
			}
		}
		if (ret == "") {
			return getKind();
		} else {
			return ret;
		}
	}

	/**
	 * Returns the number of childs this plan node can hold.
	 * 
	 * @return the maxChildCount of this PlanNode
	 */
	public int getMaxChildCount() {
		Iterator<GoAble> it = scheme.getSchema().iterator();

		while (it.hasNext()) {
			GoAble cur = it.next();
			if (cur.getInternalName().equals("edge")) {
				if (cur.getHowOften().equals("2")
						|| cur.getHowOften().equals("{,2}")) {
					return 2;
				}
				if (cur.getHowOften().equals("1")
						|| cur.getHowOften().equals("{,1}")) {
					return 1;
				}
			}
		}
		return -1;
	}

	/**
	 * Returns the mother plan of this node
	 * 
	 * @return the mother plan of this node
	 */
	public QueryPlan getMother() {
		return mother;
	}

	/**
	 * Returns the properties of this node as a PropertyMap
	 * 
	 * @return the properties of the node
	 */

	public PropertyMap getProperties() {
		return properties;
	}

	public List<Property> getReferencableColumnsFromValues()
			throws PlanHasCycleException {
		return getReferencableColumnsFromValues(new ArrayList<PlanNode>());
	}

	/**
	 * Returns all columns referencable <b>in</b> this node.
	 * 
	 * @return all columns that can be referenced in this node from a parent
	 *         node
	 * @throws PlanHasCycleException
	 */

	public ArrayList<Property> getReferencableColumnsFromValues(
			ArrayList<PlanNode> way) throws PlanHasCycleException {
		ArrayList<Property> ret = new ArrayList<Property>();
		if (!resetsColumns()) {
			addPropertiesDistinct(ret, getReferencableColumnsWithoutAdded(way));
			ret.removeAll(getRemovedColumns());
		}
		ret.addAll(getAddedColumns());
		return ret;
	}

	/**
	 * Returns all columns referencable from this plannode <b>without</b> the
	 * columns added within this node
	 * 
	 * @return the referencable columns
	 * @throws PlanHasCycleException
	 */
	public ArrayList<Property> getReferencableColumnsWithoutAdded()
			throws PlanHasCycleException {
		return getReferencableColumnsWithoutAdded(new ArrayList<PlanNode>());
	}

	public ArrayList<Property> getReferencableColumnsWithoutAdded(
			ArrayList<PlanNode> way) throws PlanHasCycleException {
		ArrayList<Property> ret = new ArrayList<Property>();
		Iterator<PlanNode> i = this.getChilds().iterator();

		while (i.hasNext()) {
			PlanNode cur = i.next();
			if (cur != null) {
				ArrayList<PlanNode> wayCopy = new ArrayList<PlanNode>();
				wayCopy.addAll(way);
				if (wayCopy.contains(cur)) {
					throw new PlanHasCycleException(cur.getId());
				}
				wayCopy.add(cur);
				addPropertiesDistinct(ret, cur
						.getReferencableColumnsFromValues(wayCopy));
			}
		}
		return ret;
	}

	public List<Property> getReferencableColumnsWithoutAdded(int pos)
			throws PlanHasCycleException {
		return getReferencableColumnsWithoutAdded(pos,
				new ArrayList<PlanNode>());
	}

	/**
	 * Returns the columns referencable from this node <b>without</b> the
	 * columns introduced in the specifed child (left to right)
	 * 
	 * @return the referencable columns
	 * @throws PlanHasCycleException
	 */
	public List<Property> getReferencableColumnsWithoutAdded(int pos,
			ArrayList<PlanNode> way) throws PlanHasCycleException {
		List<Property> ret = new ArrayList<Property>();
		if (this.getChilds().size() < pos) {
			return ret;
		}
		PlanNode cur = this.getChilds().get(pos - 1);
		if (cur != null) {
			ArrayList<PlanNode> wayCopy = new ArrayList<PlanNode>();
			wayCopy.addAll(way);
			if (wayCopy.contains(cur)) {
				throw new PlanHasCycleException(cur.getId());
			}
			wayCopy.add(cur);
			addPropertiesDistinct(ret, cur
					.getReferencableColumnsFromValues(wayCopy));
		}
		return ret;
	}

	/**
	 * Returns all columns referenced by this node
	 * 
	 * @return the columns this node refers to.
	 */
	public List<Property> getReferencedColumns() {
		String[] types = { "__COLUMN[\\{]?[0-9]*[\\}]?", "__COLUMN_REMOVE" };
		List<Property> ret = new ArrayList<Property>();
		Iterator<NodeContent> it = getContent().iterator();
		while (it.hasNext()) {
			ret
					.addAll(getAttributePropertiesByTypesFromValues(it.next(),
							types));
		}
		return ret;
	}

	/**
	 * Returns all columns removed by this node from the overall schema.
	 * 
	 * @return The columns removed by this node.
	 */
	public List<Property> getRemovedColumns() {
		String[] types = { "__COLUMN_REMOVE" };
		List<Property> ret = new ArrayList<Property>();
		Iterator<NodeContent> it = getContent().iterator();
		while (it.hasNext()) {
			ret
					.addAll(getAttributePropertiesByTypesFromValues(it.next(),
							types));
		}
		return ret;
	}

	/**
	 * Returns the nodes general scheme as specified in the constructor
	 * 
	 * @return this PlanNodes scheme
	 */
	public NodeScheme getScheme() {
		return scheme;
	}

	/**
	 * Removes a childs with a given node id
	 * 
	 * @param nid
	 *            the id of the node to delete
	 * @return true if deletion was successfull
	 */
	public boolean removeChild(int nid) {
		int count = nodeChilds.size();
		boolean success = false;
		for (int i = 0; i < count; i++) {
			success = removeChild(nid, i + 1);
		}
		return success;
	}

	/**
	 * Removes a childs with a given node id from a specific position
	 * 
	 * @param nid
	 *            the id of the node to delete
	 * @param pos
	 *            the position from left to right beginning with 1
	 * @return true if deletion was successfull
	 */
	public boolean removeChild(int nid, int pos) {
		Iterator<NodeContent> it = childs.iterator();
		boolean success = false;
		int count = 1;

		while (it.hasNext() && !success) {
			NodeContent cur = it.next();
			if (cur.getInternalName().equals("edge")) {
				if (count != pos) {
					count++;
				} else if (cur.getAttributes().get("to").getVal().equals(
						Integer.toString(nid))) {
					cur.getAttributes().get("to").setVal("-1");
					success = true;
				}
			}
		}

		if (success) {
			nodeChilds.set(pos - 1, null);
		}
		return success;
	}

	/**
	 * Returns whether this node is marked as resetting all columns in its
	 * scheme
	 * 
	 * @return true if node resets columns
	 */

	public boolean resetsColumns() {
		return this.getScheme().getProperties().containsKey("reset_columns")
				&& this.getScheme().getProperties().get("reset_columns")
						.equals("true");
	}

	/**
	 * Sets the childs of this node. This should not be called after node
	 * creation since synchronization with the childs specified in the content
	 * is NOT guaranteed.
	 * 
	 * @param childs
	 *            the childs to set
	 */

	public void setChilds(List<PlanNode> childs) {
		this.nodeChilds = childs;
	}

	/**
	 * Sets the content of this node. This should not be called after node
	 * creation
	 * 
	 * @param content
	 */

	public void setContent(ArrayList<NodeContent> content) {
		this.childs = content;
	}

	/**
	 * Sets the evaluation context of this plan node
	 * 
	 * @param c
	 *            the context to set
	 */
	public void setEvaluationContext(EvaluationContext c) {
		this.c = c;
	}

	@Override
	public String toString() {
		String ret = "{NODE: id=" + id + " kind:" + scheme.getKind()
				+ " CONTENT:[";
		Iterator<NodeContent> i = childs.iterator();
		while (i.hasNext()) {
			ret += i.next().toString();
		}
		return ret + "]}";
	}
}
