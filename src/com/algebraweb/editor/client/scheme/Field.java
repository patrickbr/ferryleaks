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
	
	
	public Field(String type,String val) {
		this.type=type;
		this.val=val;
	}

	/**
	 * @return the canBe
	 */
	public String[] getCanBe() {
		return canBe;
	}


	public String getMust_be() {
		return must_be;
	}

	public String getType() {
		return type;
	}

	public String getVal() {
		return val;
	}

	public boolean hasCanBe() {
		return hasCanBe;
	}
	
	public boolean hasMustBe() {
		return hasMustBe;
	}
	
	/**
	 * @param canBe the canBe to set
	 */
	public void setCanBe(String[] canBe) {
		this.canBe = canBe;
		this.hasCanBe=true;
	}

	public void setMust_be(String mustBe) {
		must_be = mustBe;
		hasMustBe=true;
	}
	
	public void setType(String type) {
		this.type = type;
	}


	public void setVal(String val) {
		this.val = val;
	}


	@Override
	public String toString() {
		return "[" + val + "=" + type +"]";
	}
	
	

}
