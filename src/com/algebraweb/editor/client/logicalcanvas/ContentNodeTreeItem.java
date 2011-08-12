package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.List;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.validation.ValidationError;
import com.google.gwt.user.client.ui.HTML;

public class ContentNodeTreeItem extends NodeTreeItem{


	private NodeContent c;
	private GoAble scheme;
	protected RemoteManipulationServiceAsync manServ;
	private String name = "";
	private List<ValidationError> errors;


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

	public NodeContent getContentNode() {

		return c;

	}

	/**
	 * @return the errors
	 */
	public List<ValidationError> getErrors() {
		return errors;
	}

	public GoAble getScheme() {

		return scheme;

	}




	@Override
	public void setSelected(boolean selected) {

		if (selected) {
			this.getWidget().getElement().getStyle().setBackgroundColor("#CCC");
		}else{
			this.getWidget().getElement().getStyle().setBackgroundColor("");
		}

	}






}
