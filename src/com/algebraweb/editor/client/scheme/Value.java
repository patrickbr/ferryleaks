package com.algebraweb.editor.client.scheme;
import java.util.ArrayList;
import java.util.Iterator;




public class Value extends GoInto{
	
	
	String valName;
	String nameField = "";
	
	ArrayList<Field> fields = new ArrayList<Field>();
	
	
	public Value() {
		
	}
	

	public Value(String xmlObject, String howOften, String valName, String humanName,String nameField) {
	
		this(xmlObject, howOften, valName, humanName);
		this.nameField=nameField;
		
	}
	
	public Value(String xmlObject, String howOften, String valName, String humanName) {
		super(xmlObject, howOften,humanName);
		this.valName=valName;
	}
	
	
	public String getNameField() {
		
		return nameField;
		
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
