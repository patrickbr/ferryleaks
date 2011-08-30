package com.algebraweb.editor.shared.node;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A property which can hold an identifying name as well as a PropertyValue
 * 
 * @author Patrick Brosi
 * 
 */
public class Property implements IsSerializable {

	String propertyName;
	PropertyValue propertyVal;
	List<Property> properties = new ArrayList<Property>();

	public Property() {

	}

	public Property(String name, PropertyValue value) {
		this.propertyName = name;
		this.propertyVal = value;
	}

	public Property(String name, String value, String type) {
		this.propertyName = name;
		this.propertyVal = new PropertyValue(value, type);
	}

	/**
	 * Returns the childs of this property
	 * 
	 * @return the childs of this property
	 */
	public List<Property> getProperties() {
		return properties;
	}

	/**
	 * Returns the identifier of this property
	 * 
	 * @return the identifier
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Returns the value hold by this property as a PropertyVal
	 * 
	 * @return the PropertyVal hold by this value
	 */
	public PropertyValue getPropertyVal() {
		return propertyVal;
	}

	/**
	 * Sets the name of this property
	 * 
	 * @param propertyName
	 *            the name to set
	 */
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	/**
	 * Sets the value of this property
	 * 
	 * @param propertyVal
	 *            the value to set.
	 */
	public void setPropertyVal(PropertyValue propertyVal) {
		this.propertyVal = propertyVal;
	}
}
