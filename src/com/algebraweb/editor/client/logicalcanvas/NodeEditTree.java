package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.GoInto;
import com.algebraweb.editor.client.scheme.Value;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;

public class NodeEditTree extends Tree{


	NodeTreeItem root;
	private PlanNode p;

	public NodeEditTree() {

		super();



	}
	
	public void setNode(PlanNode p) {
		
		this.p=p;
		
	}

	
	public NodeTreeItem addItem(String s) {
		
		NodeTreeItem t = new NodeTreeItem(s);
		
		this.addItem(t);
		return t;
		
		
	}
	

	




}
