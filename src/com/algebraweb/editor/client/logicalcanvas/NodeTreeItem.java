package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.scheme.GoAble;
import com.google.gwt.user.client.ui.TreeItem;

public  class NodeTreeItem extends TreeItem {
	
	
	public void addMultipleItems(ArrayList<TreeItem> items) {

		Iterator<TreeItem> it = items.iterator();

		while (it.hasNext()) {

			this.addItem(it.next());

		}


	}
	
	public NodeTreeItem(String s) {
		super(s);
		setState(true,true);
	}
	
	public NodeTreeItem() {
		super();
		setState(true,true);
	}
	
	

}
