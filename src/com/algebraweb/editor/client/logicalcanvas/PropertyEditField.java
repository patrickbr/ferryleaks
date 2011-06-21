package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class PropertyEditField extends Composite{
	
	
	private FlowPanel p = new FlowPanel();
	private PropertyValue pv;
	private TextBox fieldContent;
	
	
	public PropertyEditField(Field f,PropertyValue pv) {
		
		
		HTML fieldTitle = new HTML(f.getVal());
		fieldTitle.addStyleName("content-edit-field-title");
		p.add(fieldTitle);
		
		fieldContent = new TextBox();
		fieldContent.addStyleName("content-edit-field-textbox");
		
		
		p.add(fieldContent);
		bindToPropertyVal(pv);
	
		initWidget(p);
		
	}
	
	private void bindToPropertyVal(PropertyValue pv) {
		
		this.pv=pv;
		
		fieldContent.setText(pv.getVal());
		
		
	}
	

	public void save() {
		
		GWT.log("Saving value " + fieldContent.getText());
		pv.setVal(fieldContent.getText());
		
		
	}
	

}
