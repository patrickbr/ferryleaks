package com.algebraweb.editor.client.logicalcanvas.editpanel;

import java.util.List;

import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.shared.node.NodeContent;
import com.algebraweb.editor.shared.scheme.GoAble;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.HTML;

/**
 * An item of the content tree
 * 
 * @author Patrick Brosi
 * 
 */
public class ContentNodeTreeItem extends NodeTreeItem {
	private NodeContent c;
	private GoAble scheme;
	protected RemoteManipulationServiceAsync manServ;
	private String name = "";
	private List<ValidationError> errors;

	public ContentNodeTreeItem(RemoteManipulationServiceAsync manServ,
			NodeContent c, GoAble scheme) {
		this.c = c;
		this.scheme = scheme;
		this.manServ = manServ;
		name = c.getLabel().replaceAll("\\\\n", "");
		name = SafeHtmlUtils.htmlEscape(name);
		super.setWidget(new HTML("<span style='font-style:italic'>" + name
				+ "</span>"));
		if (c.getEvalRes() != null && !c.getEvalRes().isEmpty()) {
			ContentNodeTreeItem.this.setWidget(new HTML(
					"<span style='color:red'>"
							+ "<span style='font-style:italic'>" + name
							+ "</span>" + "</span>"));
			errors = c.getEvalRes();
		}
	}

	/**
	 * Returns the content node associated with this tree node
	 * 
	 * @return the associated content node
	 */
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
		} else {
			this.getWidget().getElement().getStyle().setBackgroundColor("");
		}
	}
}
