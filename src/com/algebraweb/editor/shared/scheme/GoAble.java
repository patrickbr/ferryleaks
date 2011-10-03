package com.algebraweb.editor.shared.scheme;

import java.io.Serializable;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * An interface declaring various methods for accessing objects parsed from a
 * schema file
 * 
 * @author Patrick Brosi
 * 
 */
public interface GoAble extends IsSerializable, Serializable{

	/**
	 * Returns all fields of the underlying XML element
	 * 
	 * @return the fields of the element
	 */
	public List<Field> getFields();

	/**
	 * Returns the how_often value specified within the XML file
	 * 
	 * @return the how_often value specified within the XML file
	 */
	public String getHowOften();

	/**
	 * Returns the human name of this field
	 * 
	 * @return the human name of this field
	 */
	public String getHumanName();

	/**
	 * Returns the <i>internal</i> name of this field. This is usually the name
	 * of the XML object to be parse
	 * 
	 * @return the field's internal name
	 */
	public String getInternalName();

	/**
	 * Returns the name_to_print attribute parsed from the schema file
	 * 
	 * @return the name_to_print attribute
	 */
	public String getNameToPrint();

	/**
	 * Returns the schema of this GoAble, that is the list of this GoAbles'
	 * children
	 * 
	 * @return the schema of this goable
	 */
	public List<GoAble> getSchema();

	/**
	 * Returns the XML object name this GoAble will parse. This is usually the
	 * same as getInternalName().
	 * 
	 * @return the XML object identifiert as a string
	 */
	public String getXmlObject();

	/**
	 * True if this GoAble has nested GoAbles
	 * 
	 * @return true if this GoAble has nested GoAbles
	 */
	public boolean hasChilds();

	/**
	 * True if this GoAble has fields.
	 * 
	 * @return true if this GoAble has fields.
	 */
	public boolean hasFields();

	/**
	 * True if this field can be edited by the user
	 * 
	 * @return true if this field can be edited by the user
	 */
	public boolean isEditable();

	/**
	 * Specifies whether this field can be edited by the user
	 * 
	 * @param editable
	 *            true if this field should be editable
	 */
	public void setEditable(boolean editable);

	/**
	 * Sets how often the element described by this GoAble is expected to occur
	 * 
	 * @param howOften
	 *            the howoften string (a RegExpression quantifier)
	 */
	public void setHowOften(String howOften);

	/**
	 * Sets the schema of this GoAble.
	 * 
	 * @param schema
	 *            the schema to set.
	 */
	public void setSchema(List<GoAble> schema);

}
