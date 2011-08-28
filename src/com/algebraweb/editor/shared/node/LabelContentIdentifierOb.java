package com.algebraweb.editor.shared.node;

/**
 * A Label object identifying child content nodes
 * @author Patrick Brosi
 *
 */
public class LabelContentIdentifierOb extends LabelOb{

	private String identifier;

	public LabelContentIdentifierOb() {

	}

	public LabelContentIdentifierOb(String identifier) {
		this.identifier= identifier;
	}

	@Override
	public void addChar(String e) {
		identifier = identifier + e;
	}

	/**
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getVal() {
		return identifier;
	}

}
