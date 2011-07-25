package com.algebraweb.editor.client.scheme;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.rpc.IsSerializable;


public class NodeScheme implements IsSerializable,GoAble{

	private String kind;
	private HashMap<String,String> properties = new HashMap<String,String>();
	private ArrayList<GoAble> schema = new ArrayList<GoAble>();


	public NodeScheme() {

	}

	public NodeScheme(String kind) {
		this.kind=kind;
	}

	public void addToSchema(GoAble goAble) {
		this.schema.add(goAble);
	}

	@Override
	public ArrayList<Field> getFields() {

		return null;
	}

	@Override
	public String getHowOften() {
		return "1";
	}


	public String getHumanName() {
		return "Scheme of type '"+ kind + "'";
	}


	@Override
	public String getInternalName() {
		return "_scheme_" + kind;
	}

	public String getKind() {
		return kind;
	}

	public HashMap<String,String> getProperties() {
		return properties;
	}

	public ArrayList<GoAble> getSchema() {
		return schema;
	}

	@Override
	public String getXmlObject() {
		return null;
	}

	@Override
	public boolean hasChilds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean hasFields() {

		return false;
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public void setEditable(boolean editable) {

	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public void setSchema(ArrayList<GoAble> schema) {
		this.schema = schema;
	}

	@Override
	public String toString() {

		Iterator<GoAble> i = schema.iterator();

		String ret ="";

		while(i.hasNext()) {

			ret+="\n" + i.next().toString();

		}

		return ret;


	}



}
