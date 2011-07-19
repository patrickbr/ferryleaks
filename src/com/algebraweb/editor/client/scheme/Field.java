package com.algebraweb.editor.client.scheme;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Field implements IsSerializable{
	
	private String type;
	private String val;
	private String must_be;
	private String[] canBe;
	private boolean hasMustBe = false;
	private boolean hasCanBe = false;
	
	public Field() {
		
	}
	
	
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
	
	public boolean hasCanBe() {
		return hasCanBe;
	}

	public void setVal(String val) {
		this.val = val;
	}
	
	public String toString() {
		return "[" + val + "=" + type +"]";
	}


	/**
	 * @return the canBe
	 */
	public String[] getCanBe() {
		return canBe;
	}


	/**
	 * @param canBe the canBe to set
	 */
	public void setCanBe(String[] canBe) {
		this.canBe = canBe;
		this.hasCanBe=true;
	}
	
	

}
