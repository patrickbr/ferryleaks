package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PropertyEditPanel extends Composite{


	private NodeContent c;
	private GoAble scheme;
	private VerticalPanel p = new VerticalPanel();
	private ArrayList<PropertyEditField> fields = new ArrayList<PropertyEditField>();

	public PropertyEditPanel(NodeContent c, GoAble scheme) {

		this.c=c;
		this.scheme=scheme;

		HTML title= new HTML("Edit " + this.c.getInternalName());
		

		title.addStyleName("content-edit-title");

		p.add(title);

		if (this.scheme.hasFields()) {
			Iterator<Field> it = scheme.getFields().iterator();

			while (it.hasNext()) {

				Field current = it.next();

				PropertyValue pv;

				if (c.getAttributes().containsKey(current.getVal())) {
					
					pv=c.getAttributes().get(current.getVal());
					
				}else{
					
					pv=c.getAttributes().put(current.getVal(), new PropertyValue("", current.getType()));
					
				}
				
				PropertyEditField f = new PropertyEditField(current,pv);
				
				fields.add(f);
				p.add(f);


			}



		}




		initWidget(p);



	}

	public void save() {
		
		GWT.log("Saving panel");
		
		Iterator<PropertyEditField> it = fields.iterator();
		
		while (it.hasNext()) {
			it.next().save();
		}
		
		
		
	}




}
