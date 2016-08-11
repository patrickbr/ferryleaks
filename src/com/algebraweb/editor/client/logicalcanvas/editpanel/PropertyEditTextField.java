package com.algebraweb.editor.client.logicalcanvas.editpanel;

import com.algebraweb.editor.shared.node.PropertyValue;
import com.algebraweb.editor.shared.scheme.Field;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PropertyEditTextField extends PropertyEditField {

	private TextBox fieldContent;

	public PropertyEditTextField(Field f) {
		super(f);

		fieldContent = new TextBox();
		p.add(fieldContent);
		initWidget(p);
	}

	public PropertyEditTextField(String f) {
		super(f);

		fieldContent = new TextBox();
		p.add(fieldContent);
		initWidget(p);
	}

	@Override
	public void bindToPropertyVal(PropertyValue pv) {
		super.bindToPropertyVal(pv);
		fieldContent.setText(pv.getVal());
	}

	protected Widget getInputElement() {
		return fieldContent;
	}

	@Override
	public void save() {
		pv.setVal(fieldContent.getText());
	}

	public void setErroneous(boolean erroneous) {
		if (erroneous) {
			getInputElement().getElement().getStyle().setBorderStyle(
					BorderStyle.SOLID);
			getInputElement().getElement().getStyle()
					.setBorderWidth(1, Unit.PX);
			getInputElement().getElement().getStyle().setBorderColor("red");
		} else {
			getInputElement().getElement().getStyle().setBorderStyle(
					BorderStyle.NONE);
		}
	}

	@Override
	public void setLocked(boolean locked) {
		super.setLocked(locked);

		if (locked) {
			fieldContent.setEnabled(false);
			fieldContent.setReadOnly(true);
		} else {
			fieldContent.setEnabled(true);
			fieldContent.setReadOnly(false);
		}
	}
}