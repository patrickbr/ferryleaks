package com.algebraweb.editor.server.logicalplan.xmlplanloader.schemeloader;

public class Field {
	
	private String type;
	private String val;
	private String must_be;
	private boolean hasMustBe = false;
	
	
	
	public String getMust_be() {
		return must_be;
	}

	public void setMust_be(String mustBe) {
		must_be = mustBe;
		hasMustBe=true;
	}


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
	
	public boolean hasMustBe() {
		return hasMustBe;
	}

	public void setVal(String val) {
		this.val = val;
	}
	
	public String toString() {
		return "[" + val + "=" + type +"]";
	}

}
