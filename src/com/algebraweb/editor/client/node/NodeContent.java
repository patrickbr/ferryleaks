package com.algebraweb.editor.client.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public abstract class NodeContent extends ContentNode {
	
	protected String internalName;
	protected PropertyMap attributes = new PropertyMap();

	protected String name;
	
	public String getName() {
		return name;
	}

	public PropertyMap getAttributes() {
		return attributes;
	}


	public void setAttributes(PropertyMap attributes) {
		this.attributes = attributes;
	}

	

}
