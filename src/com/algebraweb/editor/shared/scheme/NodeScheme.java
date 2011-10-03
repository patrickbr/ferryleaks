package com.algebraweb.editor.shared.scheme;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A GoAble class holding the actual NodeScheme specified withing the nodeScheme
 * tags.
 * 
 * @author Patrick Brosi
 * 
 */
public class NodeScheme implements IsSerializable, GoAble {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1144608405518784236L;
	private String kind;
	private Map<String, String> properties = new HashMap<String, String>();
	private List<GoAble> schema = new ArrayList<GoAble>();

	public NodeScheme() {
	}

	public NodeScheme(String kind) {
		this.kind = kind;
	}

	/**
	 * Add a GoAble to the overall schema
	 * 
	 * @param goAble
	 *            the GoAble to add
	 */
	public void addToSchema(GoAble goAble) {
		this.schema.add(goAble);
	}

	@Override
	public List<Field> getFields() {
		return null;
	}

	@Override
	public String getHowOften() {
		return "1";
	}

	public String getHumanName() {
		return "Scheme of type '" + kind + "'";
	}

	@Override
	public String getInternalName() {
		return "_scheme_" + kind;
	}

	/**
	 * Return the node kind this schema describes
	 * 
	 * @return the node kind as a string
	 */
	public String getKind() {
		return kind;
	}

	@Override
	public String getNameToPrint() {
		return kind;
	}

	/**
	 * Return the properties of the NodeScheme as specified within the
	 * properties-tag
	 * 
	 * @return the properties as a Map holding identifiert and value
	 */
	public Map<String, String> getProperties() {
		return properties;
	}

	@Override
	public List<GoAble> getSchema() {
		return schema;
	}

	@Override
	public String getXmlObject() {
		return null;
	}

	@Override
	public boolean hasChilds() {
		return false;
	}

	@Override
	public boolean hasFields() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public void setEditable(boolean editable) {

	}

	@Override
	public void setHowOften(String howOften) {
	}

	/**
	 * Set the identifier of the node kind this NodeScheme describes
	 * 
	 * @param kind
	 *            the identifiert as a string
	 */
	public void setKind(String kind) {
		this.kind = kind;
	}

	@Override
	public void setSchema(List<GoAble> schema) {
		this.schema = schema;
	}

	@Override
	public String toString() {
		Iterator<GoAble> i = schema.iterator();
		String ret = "";
		while (i.hasNext()) {
			ret += "\n" + i.next().toString();
		}
		return ret;
	}
}
