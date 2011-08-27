package com.algebraweb.editor.client.scheme;
import java.util.ArrayList;
import java.util.Iterator;




public class Value extends GoInto{
	
	
	String valName;
	private boolean hasVal=false;
	
	ArrayList<Field> fields = new ArrayList<Field>();
	
	
	public Value() {
		
	}
	

	public Value(String xmlObject, String howOften, String valName, String humanName) {
		super(xmlObject, howOften,humanName);
		this.valName=valName;
	}
	
	public Value(String xmlObject, String howOften, String valName, String humanName,String nameToPrint,boolean hasVal) {
	
		super(xmlObject, howOften,  humanName,nameToPrint);
		this.valName=valName;
		this.hasVal=hasVal;
		
	}
	
	
	public void addField(Field f) {
		this.fields.add(f);
	}


	@Override
	public ArrayList<Field> getFields() {
		return fields;
	}
	
	
	public String getValName() {
		return valName;
	}


	@Override
	public boolean hasFields() {
		return fields.size()>0;
	}


	public boolean hasVal() {
		return hasVal;
	}

	public void setFields(ArrayList<Field> fields) {
		this.fields = fields;
	}
	
	public void setValName(String valName) {
		this.valName = valName;
	}
	
	
			



	@Override
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
