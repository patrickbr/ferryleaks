package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;

public class PropertyEditFieldAvailableColumns extends PropertyEditField {


	private AvailableColumnsField lb;

	public PropertyEditFieldAvailableColumns(int pid, int nid, RemoteManipulationServiceAsync manServ, Field f) {

		super(f);
		
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
		lb.setProjectedSelection(pv.getVal());

	}

	@Override
	public void save() {

		pv.setVal(lb.getListBox().getValue(lb.getListBox().getSelectedIndex()));


	}





}
