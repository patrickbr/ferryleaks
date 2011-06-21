package com.algebraweb.editor.client.scheme;
import java.util.ArrayList;
import java.util.Iterator;


public class GoInto implements GoAble {

	protected String xmlObject;
	protected String howOften;
	protected String humanName;

	protected ArrayList<GoAble> childs = new ArrayList<GoAble>();


	public GoInto() {

	}


	public GoInto(String xmlObject, String howOften) {

		this.xmlObject=xmlObject;
		this.howOften = howOften;
		this.humanName = xmlObject;

	}

	public GoInto(String xmlObject, String howOften, String humanName) {

		this.xmlObject=xmlObject;
		this.howOften = howOften;
		this.humanName = humanName;

	}

	public void addChild(GoAble child) {
		this.childs.add(child);
	}

	public String getXmlObject() {
		return xmlObject;
	}
	
	public String getHumanName() {
		return humanName;
	}

	public void setXmlObject(String xmlObject) {
		this.xmlObject = xmlObject;
	}

	public String getHowOften() {
		return howOften;
	}

	public void setHowOften(String howOften) {
		this.howOften = howOften;
	}

	@Override
	public ArrayList<GoAble> getSchema() {
		return childs;
	}

	@Override
	public boolean hasChilds() {
		return childs.size()>0;
	}

	public String toString() {

		String ret =  "{GOINTO: howOften=" + howOften + " XMLob:" + xmlObject + " childs:(";

		Iterator<GoAble> i = childs.iterator();

		while(i.hasNext()) {

			ret +=i.next().toString();

		}

		ret +=")}";

		return ret;

	}

	@Override
	public String getInternalName() {
		return xmlObject;
	}


	@Override
	public ArrayList<Field> getFields() {

		return null;
	}


	@Override
	public boolean hasFields() {

		return false;
	}


}
