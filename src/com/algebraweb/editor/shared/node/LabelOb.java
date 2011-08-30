package com.algebraweb.editor.shared.node;

import com.google.gwt.user.client.rpc.IsSerializable;

public abstract class LabelOb implements IsSerializable {

	public LabelOb() {
	}

	/**
	 * Adds a character to this label
	 * 
	 * @param e
	 */
	public abstract void addChar(String e);

	/**
	 * Returns the value of this label ob
	 * 
	 * @return
	 */
	public abstract String getVal();
}
