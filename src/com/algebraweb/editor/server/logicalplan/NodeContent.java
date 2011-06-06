package com.algebraweb.editor.server.logicalplan;

import java.io.Serializable;
import java.util.ArrayList;


public interface NodeContent extends Serializable {

	public String getName();

	public void setName(String name);
	
	public ArrayList<NodeContent> getChilds();
	
	public NodeContent[] getValuesByName(String name);
	
}
