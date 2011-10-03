package com.algebraweb.editor.shared.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * A node content object that holds a semantic value
 * 
 * @author Patrick Brosi
 * 
 */

public class ContentVal extends NodeContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 940954145318097812L;
	private PropertyValue value;

	public ContentVal() {
	}

	public ContentVal(String name, String internalName, PropertyValue value) {
		this.name = name;
		this.value = value;
		this.internalName = internalName;
	}

	@Override
	public ArrayList<NodeContent> getContent() {
		return childs;
	}

	@Override
	public String getInternalName() {
		return internalName;
	}

	/**
	 * Returns the value of this ContentValue
	 * 
	 * @return the value of this ContentValue
	 */
	public PropertyValue getValue() {
		return value;
	}

	/**
	 * Sets the content childs of this content node
	 * 
	 * @param childs
	 */
	public void setChilds(ArrayList<NodeContent> childs) {
		this.childs = childs;
	}

	/**
	 * Sets the value of this ContentVal
	 * 
	 * @param value
	 *            the PropertyValue to be set
	 */
	public void setValue(PropertyValue value) {
		this.value = value;
	}

	@Override
	public String toString() {
		String ret = "[CONTENTVAL '" + name + "' value:" + value + " childs:(";
		Iterator<NodeContent> i = childs.iterator();
		while (i.hasNext()) {
			ret += i.next().toString();
		}

		ret += ") attributes:";
		Set<String> s = attributes.keySet();
		Iterator<String> a = s.iterator();
		while (a.hasNext()) {
			String n = a.next();
			ret += "{" + n + "=" + attributes.get(n) + "}";
		}
		return ret + "]";
	}
}
