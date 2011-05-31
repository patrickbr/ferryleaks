package com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader;
import java.util.ArrayList;


public interface GoAble {
	
	public ArrayList<GoAble> getSchema();
	public boolean hasChilds();
	public String getXmlObject();

}
