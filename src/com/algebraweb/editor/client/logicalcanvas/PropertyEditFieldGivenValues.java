package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.ListBox;

public class PropertyEditFieldGivenValues extends PropertyEditField {


	private ArrayList<Property> possibleValues;
	private ListBox lb;
	private boolean lastIsError=false;

	public PropertyEditFieldGivenValues(Field f) {

		super(f);

		lb = new ListBox();
		lb.addClickHandler(c);
		
	}

	public void setPossibleValues(ArrayList<Property> possibleValues) {

		this.possibleValues = possibleValues;
		drawField();

	}

	@Override
	public void drawField() {


		super.p.add(lb);

		Iterator<Property> it = possibleValues.iterator();

		while(it.hasNext()) {

			String cur = it.next().getPropertyVal().getVal();

			if (!contains(cur,lb)) {
				lb.addItem(cur);
			}

		}




	}
	
	
	private ClickHandler c = new ClickHandler() {
		
		@Override
		public void onClick(ClickEvent event) {
			if (lastIsError) {
				lb.removeItem(lb.getItemCount()-1);
				lastIsError=false;
				setErroneous(false);
			}
			
		}
	};

	private boolean contains(Object e, ListBox b) {


		for (int i=0;i<b.getItemCount();i++) {


			if (b.getValue(i).equals(e)) return true;

		}

		return false;

	}

	@Override
	public void setLocked(boolean locked) {

		if (locked) {

			lb.setEnabled(false);


		}else{

			lb.setEnabled(true);


		}


	}

	@Override
	public void bindToPropertyVal(PropertyValue pv) {

		this.pv=pv;


		if (contains(pv.getVal(),lb)) {

			for (int i=0;i<lb.getItemCount();i++) {

				if (lb.getValue(i).equals(pv.getVal())) {

					lb.setItemSelected(i, true);
				}

			}

		}else{
			
			lastIsError=true;
			setErroneous(true);
			lb.addItem(pv.getVal());
			lb.setSelectedIndex(lb.getItemCount()-1);
			
			
		}




	}

	@Override
	public void save() {

		GWT.log("Saving value " + lb.getValue(lb.getSelectedIndex()));
		pv.setVal(lb.getValue(lb.getSelectedIndex()));


	}





}
