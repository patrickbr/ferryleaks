package com.algebraweb.editor.client.scheme;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;


public interface GoAble extends IsSerializable  {
	
	
	public ArrayList<GoAble> getSchema();
	public boolean hasChilds();
	public String getXmlObject();
	public String getInternalName();
	public String getHumanName();
	public String getHowOften();
	public ArrayList<Field> getFields();
	
	public boolean hasFields();
	public boolean isEditable();
	public void setEditable(boolean editable);

}
