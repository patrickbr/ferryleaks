package com.algebraweb.editor.shared.scheme;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A GoAble that can't hold any fields of its own. Will be parsed to an internal
 * wrapper class accumulating elements which hold fields.
 * 
 * @author Patrick Brosi
 * 
 */
public class GoInto implements GoAble {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5191485854733606095L;
	protected String xmlObject;
	protected String howOften;
	protected String humanName;
	protected boolean editable = true;
	protected String nameToPrint = "";
	protected List<GoAble> childs = new ArrayList<GoAble>();

	public GoInto() {
	}

	public GoInto(String xmlObject, String howOften) {
		this.xmlObject = xmlObject;
		this.howOften = howOften;
		this.humanName = xmlObject;
	}

	public GoInto(String xmlObject, String howOften, String humanName) {
		this.xmlObject = xmlObject;
		this.howOften = howOften;
		this.humanName = humanName;
	}

	public GoInto(String xmlObject, String howOften, String humanName,
			String nameToPrint) {
		this(xmlObject, howOften, humanName);
		this.nameToPrint = nameToPrint;
	}

	/**
	 * Adds a child element to this object
	 * 
	 * @param child
	 *            the child to ad
	 */
	public void addChild(GoAble child) {
		this.childs.add(child);
	}

	@Override
	public List<Field> getFields() {
		return null;
	}

	@Override
	public String getHowOften() {
		return howOften;
	}

	@Override
	public String getHumanName() {
		return humanName;
	}

	@Override
	public String getInternalName() {
		return xmlObject;
	}

	@Override
	public String getNameToPrint() {
		return nameToPrint;
	}

	@Override
	public List<GoAble> getSchema() {
		return childs;
	}

	@Override
	public String getXmlObject() {
		return xmlObject;
	}

	@Override
	public boolean hasChilds() {
		return childs.size() > 0;
	}

	@Override
	public boolean hasFields() {
		return false;
	}

	@Override
	public boolean isEditable() {
		return editable;
	}

	@Override
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	@Override
	public void setHowOften(String howOften) {
		this.howOften = howOften;
	}

	@Override
	public void setSchema(List<GoAble> schema) {
		this.childs = schema;
	}

	public void setXmlObject(String xmlObject) {
		this.xmlObject = xmlObject;
	}

	@Override
	public String toString() {
		String ret = "{GOINTO: howOften=" + howOften + " XMLob:" + xmlObject
				+ " childs:(";
		Iterator<GoAble> i = childs.iterator();
		while (i.hasNext()) {
			ret += i.next().toString();
		}
		ret += ")}";
		return ret;
	}
}
