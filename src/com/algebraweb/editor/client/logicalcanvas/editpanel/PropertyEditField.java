package com.algebraweb.editor.client.logicalcanvas.editpanel;

import com.algebraweb.editor.shared.node.PropertyValue;
import com.algebraweb.editor.shared.scheme.Field;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public abstract class PropertyEditField extends Composite {
	protected PropertyValue pv;
	protected FlowPanel p = new FlowPanel();
	protected boolean locked = false;

	public PropertyEditField(Field f) {

		HTML fieldTitle = new HTML(f.getName());
		fieldTitle.addStyleName("content-edit-field-title");
		p.add(fieldTitle);

	}

	public PropertyEditField(String title) {

		HTML fieldTitle = new HTML(title);
		fieldTitle.addStyleName("content-edit-field-title");
		p.add(fieldTitle);

	}

	public void bindToPropertyVal(PropertyValue pv) {
		this.pv = pv;
	}

	public boolean isLocked() {
		return locked;
	}

	public abstract void save();

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}