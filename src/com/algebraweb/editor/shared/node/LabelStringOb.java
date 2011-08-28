package com.algebraweb.editor.shared.node;

public class LabelStringOb extends LabelOb{

	private String stringOb;

	public LabelStringOb() {
	}

	public LabelStringOb(String stringOb) {
		this.stringOb= stringOb;
	}

	@Override
	public void addChar(String e) {
		stringOb = stringOb + e;
	}

	/**
	 * @return the identifier
	 */
	public String getStringOb() {
		return stringOb;
	}

	@Override
	public String getVal() {
		return stringOb;
	}
}
