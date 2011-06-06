package com.algebraweb.editor.server.logicalplan;

import java.util.ArrayList;
import java.util.Iterator;

public interface NodeContent {

	public String getName();

	public void setName(String name);
	
	public ArrayList<NodeContent> getChilds();
	
	public NodeContent[] getValuesByName(String name);
	
}
