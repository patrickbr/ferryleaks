package com.algebraweb.editor.client.node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

public class PropertyMap extends HashMap<String,PropertyValue> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	public PropertyValue put(Property p) {
				
		return super.put(p.getPropertyName(), p.getPropertyVal());
				
	}
	
	public PropertyValue get(String key) {
		
		return super.get(key);
		
	}
	
	public Property getProperty(String key) {
		
		return new Property(key,super.get(key));
		
	}
	


	public Collection<Property> properties() {
		
		
		ArrayList<Property> ret = new ArrayList<Property>();
		
		
		Iterator<String> it = super.keySet().iterator();
		
		while(it.hasNext()) {
			
			String currentKey = it.next();
			ret.add(new Property(currentKey,super.get(currentKey)));
						
		}
		
		return ret;
		
	}
	
	

}
