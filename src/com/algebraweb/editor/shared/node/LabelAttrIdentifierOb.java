package com.algebraweb.editor.shared.node;

/**
 * A Label object identfying XML attributes
 * 
 * @author Patrick Brosi
 * 
 */
public class LabelAttrIdentifierOb extends LabelOb {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4676624034679551103L;
	private String identifier;

	public LabelAttrIdentifierOb() {

	}

	public LabelAttrIdentifierOb(String identifier) {
		this.identifier = identifier;
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
