package com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader;
import java.util.ArrayList;
import java.util.Iterator;




public class Value extends GoInto{
	
	
	String valName;
	
	ArrayList<Field> fields = new ArrayList<Field>();
	

	public Value(String xmlObject, String howOften, String valName) {
		super(xmlObject, howOften);
		this.valName=valName;
	}
	
	
	public void addField(Field f) {
		this.fields.add(f);
	}
	
	public String getValName() {
		return valName;
	}


	public void setValName(String valName) {
		this.valName = valName;
	}


	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}


	public ArrayList<Field> getFields() {
		return fields;
	}
	
	public boolean hasFields() {
		return fields.size()>0;
	}
		
	public String toString() {
		
		String ret =  "{VAL: name=" + valName + " howOften=" + super.howOften + " XMLob:" + super.xmlObject + " childs:(";
		
		Iterator<GoAble> i = super.childs.iterator();
		
		while(i.hasNext()) {
			
			ret +=i.next().toString();
			
		}
		
		ret +=") fields:(";
		
		Iterator<Field> a = fields.iterator();
		
		while(a.hasNext()) {
			
			ret +=a.next().toString();
			
		}
		
		
		return ret+")";
		
	}

}
