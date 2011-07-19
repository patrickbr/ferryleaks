package com.algebraweb.editor.client.node;

public class LabelStringOb extends LabelOb{
	
	
private String stringOb;
	
	public LabelStringOb(String stringOb) {
		
		this.stringOb= stringOb;
		
	}
	
	public LabelStringOb() {
		
	}

	/**
	 * @return the identifier
	 */
	public String getStringOb() {
		return stringOb;
	}
	
	@Override
	public void addChar(String e) {
		
		stringOb = stringOb + e;
		
	}
	
	@Override
	public String getVal() {

		return stringOb;
		
	}

}
