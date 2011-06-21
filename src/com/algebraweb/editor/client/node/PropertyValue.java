package com.algebraweb.editor.client.node;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PropertyValue implements IsSerializable{
	
	
	private String val;
	private String type;
	
	public PropertyValue() {
		
	}
	
	public String getVal() {
		return val;
	}
	public void setVal(String val) {
		this.val = val;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public PropertyValue(String val, String type) {
		this.val = val;
		this.type = type;
	}
	
	
	

}
