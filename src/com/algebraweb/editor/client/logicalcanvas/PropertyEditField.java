package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public abstract class PropertyEditField extends Composite {


	protected PropertyValue pv;
	protected FlowPanel p = new FlowPanel();
	protected boolean locked=false;

	public PropertyEditField(Field f) {

		HTML fieldTitle = new HTML(f.getVal());
		fieldTitle.addStyleName("content-edit-field-title");
		p.add(fieldTitle);

	}

	public PropertyEditField(String title) {

		HTML fieldTitle = new HTML(title);
		fieldTitle.addStyleName("content-edit-field-title");
		p.add(fieldTitle);

	}

	public void bindToPropertyVal(PropertyValue pv) {
		this.pv=pv;
	}


	public boolean isLocked() {
		return locked;
	}

	public abstract void save();

	public void setLocked(boolean locked) {
		this.locked=locked;
	}


}
