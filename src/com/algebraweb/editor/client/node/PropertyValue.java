package com.algebraweb.editor.client.node;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PropertyValue implements IsSerializable{
	
	
	private String val;
	private String type;
	
	public PropertyValue() {
		
	}
	
	public PropertyValue(String val, String type) {
		this.val = val;
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public String getVal() {
		return val;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public void setVal(String val) {
		this.val = val;
	}
	
	
	

}
