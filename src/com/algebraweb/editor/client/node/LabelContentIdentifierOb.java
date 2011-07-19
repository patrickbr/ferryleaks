package com.algebraweb.editor.client.node;

public class LabelContentIdentifierOb extends LabelOb{


	private String identifier;

	public LabelContentIdentifierOb(String identifier) {

		this.identifier= identifier;

	}

	public LabelContentIdentifierOb() {

	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public void addChar(String e) {

		identifier = identifier + e;

	}

	@Override
	public String getVal() {

		return identifier;

	}


}
