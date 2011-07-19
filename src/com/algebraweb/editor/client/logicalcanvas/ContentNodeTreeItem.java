package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.scheme.Value;
import com.algebraweb.editor.client.validation.ValidationError;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TreeItem;

public class ContentNodeTreeItem extends NodeTreeItem{


	private NodeContent c;
	private GoAble scheme;
	protected RemoteManipulationServiceAsync manServ;
	private String name = "";
	private ArrayList<ValidationError> errors;


	public ContentNodeTreeItem(RemoteManipulationServiceAsync manServ,NodeContent c, GoAble scheme) {

		this.c=c;
		this.scheme=scheme;
		this.manServ=manServ;

		name = c.getLabel();

		super.setWidget(new HTML("<span style='font-style:italic'>"+ name + "</span>"));
		
		if (c.getEvalRes() != null && !c.getEvalRes().isEmpty()) {
			ContentNodeTreeItem.this.setWidget(new HTML("<span style='color:red'>" + "<span style='font-style:italic'>"+ name + "</span>" + "</span>"));
			errors = c.getEvalRes();
		}

	}

	@Override
	public void setSelected(boolean selected) {

		if (selected) {
			this.getWidget().getElement().getStyle().setBackgroundColor("#CCC");
		}else{
			this.getWidget().getElement().getStyle().setBackgroundColor("");
		}

	}

	public NodeContent getContentNode() {

		return c;

	}

	public GoAble getScheme() {

		return scheme;

	}




	/**
	 * @return the errors
	 */
	public ArrayList<ValidationError> getErrors() {
		return errors;
	}






}
