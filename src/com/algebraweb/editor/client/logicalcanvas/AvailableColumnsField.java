package com.algebraweb.editor.client.logicalcanvas;

import java.util.List;

import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.node.Property;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ListBox;

/**
 * A dropdown menu providing available columns as options
 * 
 * @author Patrick Brosi
 * 
 */
public class AvailableColumnsField extends FixedPossibilitiesField {

	private String[] projSel;
	private String[] projDel;
	private boolean received = false;
	private AsyncCallback<List<Property>> cb = new GraphCanvasCommunicationCallback<List<Property>>(
			"getting available columns") {

		@Override
		public void onSuccess(List<Property> result) {
			AvailableColumnsField.this.loadPropertyList(result);

			if (projSel != null && projSel != null) {
				for (String s : projSel) {
					selectStringItem(s);
				}
			}

			if (projDel != null && projSel != null) {
				for (String s : projDel) {
					getListBox().removeItem(selectStringItem(s));
				}
			}
			received = true;
		}
	};

	public AvailableColumnsField(int pid, int nid, boolean includeThisNode,
			RemoteManipulationServiceAsync manServ) {
		this(pid, nid, -1, includeThisNode, manServ, false);
	}

	public AvailableColumnsField(int pid, int nid, boolean includeThisNode,
			RemoteManipulationServiceAsync manServ,
			boolean allowMultipleSelection) {
		this(pid, nid, -1, includeThisNode, manServ, allowMultipleSelection);
	}

	public AvailableColumnsField(int pid, int nid, int position,
			boolean includeThisNode, RemoteManipulationServiceAsync manServ) {
		this(pid, nid, position, includeThisNode, manServ, false);
	}

	public AvailableColumnsField(int pid, int nid, int position,
			boolean includeThisNode, RemoteManipulationServiceAsync manServ,
			boolean allowMultipleSelection) {
		super(allowMultipleSelection);
		if (includeThisNode) {
			manServ.getReferencableColumns(nid, pid, cb);
		} else {
			if (position > -1) {
				manServ.getReferencableColumnsWithoutAddedFromPos(nid, pid,
						position, cb);
			} else {
				manServ.getReferencableColumnsWithoutAdded(nid, pid, cb);
			}
		}
	}

	
	@Override
	public ListBox getListBox() {
		return super.getListBox();
	}

	@Override
	public ListBox getWidget() {
		return getListBox();
	}

	/**
	 * Set an array of item strings that will be deleted as soon as this field
	 * is loaded. If the field is already loaded, they will be removed
	 * immediately
	 * 
	 * @param items
	 *            the items to remove
	 */
	public void setProjectedDelete(String[] items) {
		projDel = items;
		if (received && items != null) {
			for (String s : items) {
				if (selectStringItem(s) > -1) {
					getListBox().removeItem(selectStringItem(s));
				}
			}
		}
	}

	/**
	 * Set a single item that will be selected as soon as this field is loaded.
	 * If the field is already loaded, it will be selected immediately
	 * 
	 * @param item
	 *            the item to select
	 */
	public void setProjectedSelection(String item) {

		String[] tmp = new String[1];
		tmp[0] = item;
		setProjectedSelection(tmp);
	}

	/**
	 * Set an array of items that will be selected as soon as this field is
	 * loaded. If the field is already loaded, they will be selected immediately
	 * 
	 * @param item
	 *            the item to select
	 */
	public void setProjectedSelection(String[] items) {
		projSel = items;
		if (received) {
			super.setSelection(items);
		}
	}
}
