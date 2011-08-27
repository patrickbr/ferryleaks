package com.algebraweb.editor.client.scheme;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;


public interface GoAble extends IsSerializable  {
	
	
	public ArrayList<Field> getFields();
	public String getHowOften();
	public String getHumanName();
	public String getInternalName();
	public ArrayList<GoAble> getSchema();
	public String getXmlObject();
	public boolean hasChilds();
	
	public boolean hasFields();
	public boolean isEditable();
	public void setEditable(boolean editable);
	public String getNameToPrint();

}
