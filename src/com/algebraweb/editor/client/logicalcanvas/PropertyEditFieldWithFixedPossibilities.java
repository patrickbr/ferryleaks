package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.shared.node.PropertyValue;
import com.algebraweb.editor.shared.scheme.Field;

public class PropertyEditFieldWithFixedPossibilities extends PropertyEditField {
	
	private FixedPossibilitiesField lb;

	public PropertyEditFieldWithFixedPossibilities(Field f) {
		super(f);
	
		lb = new FixedPossibilitiesField(false);
		lb.setMarkError(true);
	
		lb.showResults(f.getCanBe());
		p.add(lb);
		initWidget(p);
	
	}
	
	@Override
	public void bindToPropertyVal(PropertyValue pv) {
		super.bindToPropertyVal(pv);
		lb.setSelection(pv.getVal());
	}

	@Override
	public void save() {
		pv.setVal(lb.getListBox().getValue(lb.getListBox().getSelectedIndex()));		
	}

}
