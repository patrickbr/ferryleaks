package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.Property;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class AvailableColumnsField extends Composite {

	private RemoteManipulationServiceAsync manServ;
	private AbsolutePanel p;
	final ListBox b;
	private String[] projSel;
	private String[] projDel;
	private boolean received = false;
	private boolean markError = false;
	private int erroneousIndex = -1;

	public AvailableColumnsField(int pid, int nid, boolean includeThisNode,RemoteManipulationServiceAsync manServ) {
		this(pid, nid, -1, includeThisNode, manServ, false);
	}

	public AvailableColumnsField(int pid, int nid, int position, boolean includeThisNode,RemoteManipulationServiceAsync manServ) {
		this(pid, nid, position, includeThisNode, manServ, false);
	}

	public AvailableColumnsField(int pid, int nid, boolean includeThisNode,RemoteManipulationServiceAsync manServ, boolean allowMultipleSelection) {

		this(pid, nid, -1, includeThisNode, manServ, allowMultipleSelection);
	}


	public AvailableColumnsField(int pid, int nid, int position, boolean includeThisNode,RemoteManipulationServiceAsync manServ, boolean allowMultipleSelection) {

		super();
		this.manServ=manServ;
		b = new ListBox(allowMultipleSelection);

		p = new AbsolutePanel();

		p.addStyleName("field-loading");

		b.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				if (erroneousIndex > -1 && b.getSelectedIndex() != erroneousIndex) {

					AvailableColumnsField.this.removeStyleName("erroneous");
					b.removeItem(erroneousIndex);
					erroneousIndex = -1;

				}

			}
		});

		this.initWidget(p);

		this.addStyleName("available-columns-selector");

		if(includeThisNode){
			manServ.getReferencableColumns(nid, pid, cb);
		}else{
			if (position > -1) {
				manServ.getReferencableColumnsWithoutAddedFromPos(nid, pid, position, cb);
			}else	manServ.getReferencableColumnsWithoutAdded(nid, pid, cb);
		}

	}

	public void setMarkError(boolean markError) {
		this.markError=markError;
	}

	public void setProjectedSelection(String[] item) {

		projSel = item;
		if (received) {
			for (String s : item)
				selectStringItem(s);

		}

	}

	public void setProjectedSelection(String item) {

		String[] tmp = new String[1];
		tmp[0] = item;

		setProjectedSelection(tmp);
	}

	public void setProjectedDelete(String[] item) {

		projDel = item;
		if (received) {

			for (String s : item)
				if (selectStringItem(s)>-1) b.removeItem(selectStringItem(s));

		}

	}

	/**
	 * Selects the first occurrence of the item "item" in the listbox.
	 * @param item
	 */

	private int selectStringItem(String item) {


		for (int i=0;i<b.getItemCount();i++) {


			if (b.getValue(i).equals(item)) {
				b.setItemSelected(i, true);
				return i;
			}

		}

		if (markError && !b.isMultipleSelect()) {
			b.addItem(item);
			b.setSelectedIndex(b.getItemCount()-1);;
			this.addStyleName("erroneous");
			erroneousIndex = b.getItemCount()-1;

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

			if (projSel != null)
				for (String s : projSel)
					selectStringItem(s);


			if (projDel != null)
				for (String s : projDel)
					b.removeItem(selectStringItem(s));
			received=true;

		}		


	};

	public ListBox getWidget() {

		return b;

	}

	public String[] getSelectedItems() {

		ArrayList<String> ret = new ArrayList<String>();

		for (int i=0;i<b.getItemCount();i++) {

			if (b.isItemSelected(i)) ret.add(b.getItemText(i));

		}


		return ret.toArray(new String[0]);

	}


}
