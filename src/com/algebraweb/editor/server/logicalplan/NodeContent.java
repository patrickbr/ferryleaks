package com.algebraweb.editor.server.logicalplan;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;


public interface NodeContent extends ContentNode {

	public String getName();

	public PropertyMap getAttributes();

	public void setAttributes(PropertyMap attributes);
	
	public boolean removeContent(NodeContent c);
	
	public String getInternalName();
}
