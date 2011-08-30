package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.shared.node.Property;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

/**
 * A drop field item displaying only given values as options
 * 
 * @author Patrick Brosi
 * 
 */
public class FixedPossibilitiesField extends Composite {

	private ListBox b;
	private int erroneousIndex = -1;
	private boolean markError = false;
	private AbsolutePanel p;

	public FixedPossibilitiesField(boolean allowMultipleSelection) {
		b = new ListBox(allowMultipleSelection);
		p = new AbsolutePanel();
		p.addStyleName("field-loading");
		this.initWidget(p);
		this.addStyleName("available-columns-selector");

		b.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (erroneousIndex > -1
						&& b.getSelectedIndex() != erroneousIndex) {
					FixedPossibilitiesField.this.removeStyleName("erroneous");
					b.removeItem(erroneousIndex);
					erroneousIndex = -1;
				}
			}
		});
	}

	/**
	 * Returns the underlying list box object
	 * 
	 * @return the list box
	 */
	public ListBox getListBox() {
		return b;
	}

	/**
	 * Returns all selected items as a string array
	 * 
	 * @return the string array containing all selected items
	 */
	public String[] getSelectedItems() {
		List<String> ret = new ArrayList<String>();
		for (int i = 0; i < b.getItemCount(); i++) {
			if (b.isItemSelected(i)) {
				ret.add(b.getItemText(i));
			}
		}
		return ret.toArray(new String[0]);
	}

	/**
	 * Load a property list
	 * 
	 * @param propertyList
	 *            the property list to load
	 */
	public void loadPropertyList(List<Property> propertyList) {
		this.removeStyleName("field-loading");
		p.add(b);
		Iterator<Property> it = propertyList.iterator();

		while (it.hasNext()) {
			b.addItem(it.next().getPropertyVal().getVal());
		}
	}

	/**
	 * Loads a string array
	 * 
	 * @param toLoad
	 *            the string array to load
	 */
	public void loadStringArray(String[] toLoad) {
		this.removeStyleName("field-loading");
		p.add(b);

		for (String s : toLoad) {
			b.addItem(s);
		}
	}

	protected int selectStringItem(String item) {
		for (int i = 0; i < b.getItemCount(); i++) {
			if (b.getValue(i).equals(item)) {
				b.setItemSelected(i, true);
				return i;
			}
		}
		if (markError && !b.isMultipleSelect()) {
			b.addItem(item);
			b.setSelectedIndex(b.getItemCount() - 1);
			;
			this.addStyleName("erroneous");
			erroneousIndex = b.getItemCount() - 1;
		}
		return -1;
	}

	/**
	 * If true, erroneous selection will be marked red
	 * 
	 * @param markError
	 *            true if errors should be marked
	 */
	public void setMarkError(boolean markError) {
		this.markError = markError;
	}

	/**
	 * Set the selected item
	 * 
	 * @param item
	 *            the item to select
	 */
	public void setSelection(String item) {
		String[] tmp = new String[1];
		tmp[0] = item;
		setSelection(tmp);
	}

	/**
	 * Set the selected items
	 * 
	 * @param items
	 *            the items to select as a string array
	 */
	public void setSelection(String[] items) {
		if (items != null) {
			for (String s : items) {
				selectStringItem(s);
			}
		}
	}

}
