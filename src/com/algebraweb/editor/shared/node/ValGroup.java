package com.algebraweb.editor.shared.node;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A group of node values with no own parameters. On editing 
 * a node, the corresponding GoInto will be presented by an
 * own tab in the editor window and this group will be filled
 * with the values provided there.
 * @author Patrick Brosi
 *
 */

public class ValGroup extends NodeContent {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4630462466854800864L;

	public ValGroup() {		

	}

	public ValGroup(String name) {
		this.name=name;
		this.internalName = name;
	}

	@Override
	public ArrayList<NodeContent> getContent() {
		return childs;
	}

	@Override
	public String getInternalName() {
		return internalName;
	}

	public void setChilds(ArrayList<NodeContent> childs) {
		this.childs = childs;
	}

	@Override
	public String toString() {
		String ret = "{VALGROUP '" + name + "' childs:";
		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {
			ret+=i.next().toString();
		}
		return ret + "}";
}
}
