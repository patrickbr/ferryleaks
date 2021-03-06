package com.algebraweb.editor.shared.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.validation.ValidationError;

/**
 * A NodeContent object is an element representing a node's content.
 * 
 * @author Patrick Brosi
 * 
 */
public abstract class NodeContent extends ContentNode {

	/**
	 * 
	 */
	private static final long serialVersionUID = 121588202610463857L;
	protected String internalName;
	protected PropertyMap attributes = new PropertyMap();
	protected List<ValidationError> evalRes = new ArrayList<ValidationError>();
	protected String name;

	public PropertyMap getAttributes() {
		return attributes;
	}

	/**
	 * @return the evalRes
	 */
	public List<ValidationError> getEvalRes() {
		return evalRes;
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
				if (cur.getVal().equals("_val") && this instanceof ContentVal) {
					ret += ((ContentVal) this).getValue().getVal();
				} else if (getAttributes().get(cur.getVal()) != null) {
					ret += getAttributes().get(cur.getVal()).getVal();
				} else {
					ret += "{" + cur.getVal() + "}";
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
			return getInternalName();
		} else {
			return ret;
		}
	}

	/**
	 * Returns the name of this NodeContent
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the attributes of this NodeContent
	 * 
	 * @param attributes
	 */
	public void setAttributes(PropertyMap attributes) {
		this.attributes = attributes;
	}

	/**
	 * @param list
	 *            the evalRes to set
	 */
	public void setEvalRes(List<ValidationError> list) {
		this.evalRes = list;
	}

}
