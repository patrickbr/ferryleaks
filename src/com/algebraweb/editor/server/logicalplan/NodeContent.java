package com.algebraweb.editor.server.logicalplan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public interface NodeContent extends ContentNode {

	public String getName();

	public void setName(String name);
	
	public PropertyMap getAttributes();

	public void setAttributes(PropertyMap attributes);
}
