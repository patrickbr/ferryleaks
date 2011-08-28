package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.node.Property;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class FixedPossibilitiesField extends Composite{

	private ListBox b;
	private int erroneousIndex;
	private boolean markError = false;
	private AbsolutePanel p;
	private String[] projSel;
	private String[] projDel;
	
	public FixedPossibilitiesField(boolean allowMultipleSelection) {

		b = new ListBox(allowMultipleSelection);
		
		p = new AbsolutePanel();
		p.addStyleName("field-loading");

		this.initWidget(p);
		this.addStyleName("available-columns-selector");
		
		b.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {

				if (erroneousIndex > -1 && b.getSelectedIndex() != erroneousIndex) {
					FixedPossibilitiesField.this.removeStyleName("erroneous");
					b.removeItem(erroneousIndex);
					erroneousIndex = -1;
				}
			}
		});
		
	}
	
	
	protected ListBox getListBox() {
		return b;
	}

	public String[] getSelectedItems() {

		ArrayList<String> ret = new ArrayList<String>();
		for (int i=0;i<b.getItemCount();i++) {
			if (b.isItemSelected(i)) ret.add(b.getItemText(i));
		}
		return ret.toArray(new String[0]);
	}
	
	protected int selectStringItem(String item) {

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

	public void setMarkError(boolean markError) {
		this.markError=markError;
	}
	
	protected void showResults(List<Property> result) {

		this.removeStyleName("field-loading");
		p.add(b);

		Iterator<Property> it = result.iterator();

		while (it.hasNext()) {
			b.addItem(it.next().getPropertyVal().getVal());
		}
	}
	
	protected void showResults(String[] result) {

		this.removeStyleName("field-loading");
		p.add(b);

		for (String s:result) {
			b.addItem(s);
		}
	}
	
	public void setSelection(String[] item) {

		projSel = item;
		if (item != null) {
			for (String s : item)
				selectStringItem(s);
		}
	}
	
	public void setSelection(String item) {

		String[] tmp = new String[1];
		tmp[0] = item;

		setSelection(tmp);
	}

}
