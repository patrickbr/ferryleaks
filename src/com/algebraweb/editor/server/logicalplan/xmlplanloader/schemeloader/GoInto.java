package com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader;
import java.util.ArrayList;
import java.util.Iterator;


public class GoInto implements GoAble {
	
	protected String xmlObject;
	protected String howOften;
	
	protected ArrayList<GoAble> childs = new ArrayList<GoAble>();
	
	
	public GoInto(String xmlObject, String howOften) {
		
		this.xmlObject=xmlObject;
		this.howOften = howOften;
		
	}
	
	public void addChild(GoAble child) {
		this.childs.add(child);
	}

	public String getXmlObject() {
		return xmlObject;
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


}
