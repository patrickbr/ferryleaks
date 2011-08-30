package com.algebraweb.editor.shared.node;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Holds a value as well as the value's type
 * 
 * @author Patrick Brosi
 * 
 */
public class PropertyValue implements IsSerializable {

	private String val;
	private String type;

	public PropertyValue() {

	}

	public PropertyValue(String val, String type) {
		this.val = val;
		this.type = type;
	}

	/**
	 * Returns the type of this value
	 * 
	 * @return the type of this value
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns the actual value of this PropertyValue
	 * 
	 * @return the value as a string
	 */
	public String getVal() {
		return val;
	}

	/**
	 * Set the type of this PropertyValue
	 * 
	 * @param type
	 *            the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Sets the value of this PropertyValue
	 * 
	 * @param val
	 *            the value as a string
	 */
	public void setVal(String val) {
		this.val = val;
	}
}
