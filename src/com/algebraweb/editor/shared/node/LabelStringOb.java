package com.algebraweb.editor.shared.node;

public class LabelStringOb extends LabelOb {

	/**
	 *
	 */
	private static final long serialVersionUID = 6131445738532838707L;
	private String stringOb;

	public LabelStringOb() {
	}

	public LabelStringOb(String stringOb) {
		this.stringOb = stringOb;
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