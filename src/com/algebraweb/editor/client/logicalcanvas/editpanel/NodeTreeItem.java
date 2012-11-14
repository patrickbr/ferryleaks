package com.algebraweb.editor.client.logicalcanvas.editpanel;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.user.client.ui.TreeItem;

public class NodeTreeItem extends TreeItem {

	public NodeTreeItem() {
		super();
		setState(true, true);
	}

	public NodeTreeItem(String s) {
		super(s);
		setState(true, true);
	}

	public void addMultipleItems(ArrayList<TreeItem> items) {

		Iterator<TreeItem> it = items.iterator();

		while (it.hasNext()) {

			this.addItem(it.next());

		}

	}

}
