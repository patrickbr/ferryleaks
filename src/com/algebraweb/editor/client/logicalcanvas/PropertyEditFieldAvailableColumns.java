package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.logging.shared.RemoteLoggingServiceAsync;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class PropertyEditFieldAvailableColumns extends PropertyEditField {


	private AvailableColumnsField lb;

	public PropertyEditFieldAvailableColumns(int pid, int nid, RemoteManipulationServiceAsync manServ, Field f) {

		super(f);
		GWT.log(f.getType());
		
		if (f.getType().matches("__COLUMN\\{[0-9]*\\}")) {

			int num = Integer.parseInt(f.getType().split("\\{")[1].replaceAll("\\}", ""));
			lb =  new AvailableColumnsField(pid, nid,num, false,manServ);
		}else{
			lb = new AvailableColumnsField(pid, nid, false,manServ);
		}
			
		
		lb.setMarkError(true);
		p.add(lb);
		initWidget(p);
	}

	@Override
	public void bindToPropertyVal(PropertyValue pv) {
		
		super.bindToPropertyVal(pv);
		GWT.log(pv.getVal());
		lb.setProjectedSelection(pv.getVal());
		
	}

	@Override
	public void save() {

		GWT.log("Saving value " + lb.getListBox().getValue(lb.getListBox().getSelectedIndex()));
		pv.setVal(lb.getListBox().getValue(lb.getListBox().getSelectedIndex()));


	}





}
