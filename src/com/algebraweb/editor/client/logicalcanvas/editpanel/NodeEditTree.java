package com.algebraweb.editor.client.logicalcanvas.editpanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Tree;

public class NodeEditTree extends Tree {
	protected interface CustomImages extends Tree.Resources {
		ImageResource treeClosed();
		ImageResource treeOpen();
	}

	NodeTreeItem root;

	public NodeEditTree() {
		super((Resources) GWT.create(CustomImages.class));
		super.setAnimationEnabled(true);
	}

	@Override
	public NodeTreeItem addItem(String s) {

		NodeTreeItem t = new NodeTreeItem(s);

		this.addItem(t);
		return t;

	}

}
