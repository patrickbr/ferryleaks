package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.Property;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class AvailableColumnsField extends Composite {

	private RemoteManipulationServiceAsync manServ;
	private AbsolutePanel p;
	ListBox b = new ListBox();
	private String projSel;
	private String[] projDel;
	private boolean received = false;

	public AvailableColumnsField(int pid, int nid, RemoteManipulationServiceAsync manServ) {

		super();
		this.manServ=manServ;

		p = new AbsolutePanel();

		p.addStyleName("field-loading");

		this.initWidget(p);

		manServ.getReferencableColumns(nid, pid, cb);


	}

	public void setProjectedSelection(String item) {

		projSel = item;
		if (received) {

			selectStringItem(item);

		}

	}
	
	public void setProjectedDelete(String[] item) {

		projDel = item;
		if (received) {
			
			for (String s : item)
			b.removeItem(selectStringItem(s));

		}

	}

	/**
	 * Selects the first occurrence of the item "item" in the listbox.
	 * @param item
	 */

	private int selectStringItem(String item) {


		for (int i=0;i<b.getItemCount();i++) {
			
			
			if (b.getValue(i).equals(item)) {
				b.setSelectedIndex(i);
				return i;
			}
			
			
		}
		
		return -1;
		

	}

	private void showResults(ArrayList<Property> result) {

		this.removeStyleName("field-loading");

		p.add(b);

		Iterator<Property> it = result.iterator();

		while (it.hasNext()) {


			b.addItem(it.next().getPropertyVal().getVal());


		}


	}

	public ListBox getListBox() {

		return b;
	}

	AsyncCallback<ArrayList<Property>> cb = new AsyncCallback<ArrayList<Property>>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(ArrayList<Property> result) {


			AvailableColumnsField.this.showResults(result);
			selectStringItem(projSel);
			
			if (projDel != null)
			for (String s : projDel)
				b.removeItem(selectStringItem(s));
			received=true;

		}		


	};

	public ListBox getWidget() {

		return b;

	}


}
