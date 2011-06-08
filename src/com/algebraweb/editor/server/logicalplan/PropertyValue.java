package com.algebraweb.editor.server.logicalplan;

public class PropertyValue {
	
	
	private String val;
	private String type;
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
