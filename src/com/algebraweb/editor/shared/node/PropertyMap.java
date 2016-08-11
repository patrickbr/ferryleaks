package com.algebraweb.editor.shared.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A map for properties
 *
 * @author Patrick Brosi
 *
 */
public class PropertyMap extends HashMap<String, PropertyValue> implements
		IsSerializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public PropertyValue get(String key) {
		return super.get(key);
	}

	/**
	 * Return the property specified by its string identifiert
	 *
	 * @param key
	 *            the property name to return
	 * @return the Property with the given name
	 */
	public Property getProperty(String key) {
		return new Property(key, super.get(key));
	}

	/**
	 * Returns a collection of all properties hold by this PropertyMap
	 *
	 * @return all properties as a collection
	 */
	public Collection<Property> properties() {
		List<Property> ret = new ArrayList<Property>();
		Iterator<String> it = super.keySet().iterator();

		while (it.hasNext()) {
			String currentKey = it.next();
			ret.add(new Property(currentKey, super.get(currentKey)));
		}
		return ret;
	}

	/**
	 * Adds a property to this map
	 *
	 * @param p
	 *            the property to add
	 * @return the propertyvalue added
	 */
	public PropertyValue put(Property p) {
		return super.put(p.getPropertyName(), p.getPropertyVal());
	}
}