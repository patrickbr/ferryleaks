package com.algebraweb.editor.client.node;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;


public class Property implements IsSerializable {

	String propertyName;
	PropertyValue propertyVal;

	ArrayList<Property> properties = new ArrayList<Property>();

	public Property(String name, PropertyValue value) {

		this.propertyName = name;
		this.propertyVal = value;

	}
	
	public Property() {
		
	}

	public Property(String name, String value, String type) {

		this.propertyName = name;
		this.propertyVal = new PropertyValue(value,type);

	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public PropertyValue getPropertyVal() {
		return propertyVal;
	}

	public void setPropertyVal(PropertyValue propertyVal) {
		this.propertyVal = propertyVal;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}



}
