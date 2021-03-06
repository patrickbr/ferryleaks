package com.algebraweb.editor.shared.scheme;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A field object representing a "field" tag within the schema file
 * 
 * @author Patrick Brosi
 * 
 */
public class Field implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8064773679553711457L;
	private String type;
	private String name;
	private String must_be;
	private String[] canBe;
	private boolean hasMustBe = false;
	private boolean hasCanBe = false;

	public Field() {
	}

	public Field(String type, String val) {
		this.type = type;
		this.name = val;
	}

	/**
	 * Returns a string array of possible values of this field
	 * 
	 * @return the allowed values of this fiel
	 */
	public String[] getCanBe() {
		return canBe;
	}

	/**
	 * Returns the value this field must have
	 * 
	 * @return the value this field must have
	 */
	public String getMust_be() {
		return must_be;
	}

	/**
	 * Returns the field's identifier
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns this field's type
	 * 
	 * @return the field's type
	 */
	public String getType() {
		return type;
	}

	/**
	 * Returns true of the field holds a list of possible values
	 * 
	 * @return true of the field holds a list of possible values
	 */
	public boolean hasCanBe() {
		return hasCanBe;
	}

	/**
	 * Returns true of the field must have a specific value
	 * 
	 * @return true of the field must have a specific value
	 */
	public boolean hasMustBe() {
		return hasMustBe;
	}

	/**
	 * Sets the list of possible values
	 * 
	 * @param canBe
	 *            the list of possible values as a string array to set
	 */
	public void setCanBe(String[] canBe) {
		this.canBe = canBe;
		this.hasCanBe = true;
	}

	/**
	 * Sets the value this field must hold
	 * 
	 * @param mustBe
	 *            the value this field must hold
	 */
	public void setMust_be(String mustBe) {
		must_be = mustBe;
		hasMustBe = true;
	}

	/**
	 * Sets this field's name
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "[" + name + "=" + type + "]";
	}
}
