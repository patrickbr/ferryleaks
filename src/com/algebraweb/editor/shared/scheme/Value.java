package com.algebraweb.editor.shared.scheme;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;



/**
 * A GoAble which describes content allowed to hold its own 
 * semantic value. Will be parsed to an internal class.
 * 
 * @author Patrick Brosi
 *
 */
public class Value extends GoInto{

	private String valName;
	private boolean hasVal=false;	
	private List<Field> fields = new ArrayList<Field>();

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

	/**
	 * Adds a field to this Value.
	 * @param f the field to add.
	 */
	public void addField(Field f) {
		this.fields.add(f);
	}

	@Override
	public List<Field> getFields() {
		return fields;
	}

	/**
	 * Sets the fields of this Value
	 * @param fields the fields to be set as a List
	 */
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

	/**
	 * Returns the name of this value object
	 * @return the name of this value object
	 */
	public String getValName() {
		return valName;
	}

	@Override
	public boolean hasFields() {
		return fields.size()>0;
	}

	/**
	 * True if the has_val attribute was set to "true"
	 * @return true if the has_val attribute was set to "true"
	 */
	public boolean hasVal() {
		return hasVal;
	}

	/**
	 * Sets the value name of this Value. This is the internal name
	 * that will be given to the actual XML value the XML element described
	 * by this Value-object holds.
	 * @param valName the value to set.
	 */
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
