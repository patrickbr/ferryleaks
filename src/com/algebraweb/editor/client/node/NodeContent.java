package com.algebraweb.editor.client.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.core.client.GWT;


public abstract class NodeContent extends ContentNode {

	protected String internalName;
	protected PropertyMap attributes = new PropertyMap();
	
	protected ArrayList<ValidationError> evalRes = new  ArrayList<ValidationError>();


	protected String name;

	public String getName() {
		return name;
	}

	public PropertyMap getAttributes() {
		return attributes;
	}


	public void setAttributes(PropertyMap attributes) {
		this.attributes = attributes;
	}


	public String getLabel() {

		String ret="";

		Iterator<LabelOb> it = labelScheme.iterator();

		while (it.hasNext()) {

			LabelOb cur = it.next();

			if (cur instanceof LabelStringOb) ret += cur.getVal();

			if (cur instanceof LabelAttrIdentifierOb) {

				if (cur.getVal().equals("_val") && this instanceof ContentVal) {
					
					ret += ((ContentVal) this).getValue().getVal();
					
				}else if  (getAttributes().get(cur.getVal()) != null) {
					ret += getAttributes().get(cur.getVal()).getVal();
				}else{
					ret += "{" + cur.getVal() + "}";
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

		if (ret == "") return getInternalName();
		else return ret;
	}

	/**
	 * @return the evalRes
	 */
	public ArrayList<ValidationError> getEvalRes() {
		return evalRes;
	}

	/**
	 * @param evalRes the evalRes to set
	 */
	public void setEvalRes(ArrayList<ValidationError> evalRes) {
		this.evalRes = evalRes;
	}

	
	




}
