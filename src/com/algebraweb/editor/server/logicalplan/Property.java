package com.algebraweb.editor.server.logicalplan;

import java.util.ArrayList;

public class Property {
	
	String propertyName;
	String propertyVal;
	
	ArrayList<Property> properties = new ArrayList<Property>();
	
	public Property(String name, String value) {
		
		this.propertyName = name;
		this.propertyVal = value;
		
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyVal() {
		return propertyVal;
	}

	public void setPropertyVal(String propertyVal) {
		this.propertyVal = propertyVal;
	}
	
	public ArrayList<Property> getProperties() {
		return properties;
	}
	
	

}
