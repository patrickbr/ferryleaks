package com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader;

public class Field {
	
	String type;
	String val;
	
	public Field(String type,String val) {
		this.type=type;
		this.val=val;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getVal() {
		return val;
	}

	public void setVal(String val) {
		this.val = val;
	}
	
	public String toString() {
		return "[" + val + "=" + type +"]";
	}

}