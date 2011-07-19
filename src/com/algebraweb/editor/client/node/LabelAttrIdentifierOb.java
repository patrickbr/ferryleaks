package com.algebraweb.editor.client.node;

public class LabelAttrIdentifierOb extends LabelOb{


	private String identifier;

	public LabelAttrIdentifierOb(String identifier) {

		this.identifier= identifier;

	}

	public LabelAttrIdentifierOb() {

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
