package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.BorderStyle;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;

public class PropertyEditField extends Composite{
	
	
	protected FlowPanel p = new FlowPanel();
	protected PropertyValue pv;
	private TextBox fieldContent;
	
	
	
	public PropertyEditField(Field f) {
		
		
		HTML fieldTitle = new HTML(f.getVal());
		fieldTitle.addStyleName("content-edit-field-title");
		p.add(fieldTitle);
		
		fieldContent = new TextBox();
	
		initWidget(p);
		
	}
	
	public void drawField() {
		
		
		fieldContent.addStyleName("content-edit-field-textbox");
		p.add(fieldContent);
		
	}
	
	public void setErroneous(boolean erroneous) {
		
		if (erroneous) {
			
			p.getElement().getStyle().setBorderStyle(BorderStyle.SOLID);
			p.getElement().getStyle().setBorderWidth(1, Unit.PX);
			p.getElement().getStyle().setBorderColor("red");
			
		}else{
			
			p.getElement().getStyle().setBorderStyle(BorderStyle.NONE);
				
		}
		
	}
		
	public void setLocked(boolean locked) {
		
		if (locked) {
			
			fieldContent.setEnabled(false);
			fieldContent.setReadOnly(true);
			
		}else{
			
			fieldContent.setEnabled(true);
			fieldContent.setReadOnly(false);
			
		}
		
		
	}
	
	public void bindToPropertyVal(PropertyValue pv) {
		
		this.pv=pv;
		fieldContent.setText(pv.getVal());
		
		
	}
	

	public void save() {
		
		GWT.log("Saving value " + fieldContent.getText());
		pv.setVal(fieldContent.getText());
		
		
	}
	

}
