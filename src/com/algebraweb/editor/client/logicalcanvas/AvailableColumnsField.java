package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasCommunicationCallback;
import com.algebraweb.editor.client.node.Property;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;

public class AvailableColumnsField extends FixedPossibilitiesField {

	private String[] projSel;
	private String[] projDel;
	private boolean received = false;

	AsyncCallback<List<Property>> cb = new GraphCanvasCommunicationCallback<List<Property>>("getting available columns") {

		@Override
		public void onSuccess(List<Property> result) {
			AvailableColumnsField.this.showResults(result);

			if (projSel != null && projSel != null)
				for (String s : projSel)
					selectStringItem(s);

			if (projDel != null  && projSel != null)
				for (String s : projDel)
					getListBox().removeItem(selectStringItem(s));
			received=true;
		}		
	};

	public AvailableColumnsField(int pid, int nid, boolean includeThisNode,RemoteManipulationServiceAsync manServ) {
		this(pid, nid, -1, includeThisNode, manServ, false);
	}

	public AvailableColumnsField(int pid, int nid, boolean includeThisNode,RemoteManipulationServiceAsync manServ, boolean allowMultipleSelection) {
		this(pid, nid, -1, includeThisNode, manServ, allowMultipleSelection);
	}

	public AvailableColumnsField(int pid, int nid, int position, boolean includeThisNode,RemoteManipulationServiceAsync manServ) {
		this(pid, nid, position, includeThisNode, manServ, false);
	}

	public AvailableColumnsField(int pid, int nid, int position, boolean includeThisNode,RemoteManipulationServiceAsync manServ, boolean allowMultipleSelection) {

		super(allowMultipleSelection);


		if(includeThisNode){
			manServ.getReferencableColumns(nid, pid, cb);
		}else{
			if (position > -1) {
				manServ.getReferencableColumnsWithoutAddedFromPos(nid, pid, position, cb);
			}else	manServ.getReferencableColumnsWithoutAdded(nid, pid, cb);
		}
	}

	public ListBox getListBox() {
		return super.getListBox();
	}

	@Override
	public ListBox getWidget() {
		return getListBox();
	}
	
	public void setProjectedDelete(String[] item) {
		projDel = item;
		if (received && item != null) {
			for (String s : item)
				if (selectStringItem(s)>-1) getListBox().removeItem(selectStringItem(s));
		}
	}

	public void setProjectedSelection(String item) {

		String[] tmp = new String[1];
		tmp[0] = item;
		setProjectedSelection(tmp);
	}

	public void setProjectedSelection(String[] item) {

		projSel = item;
		if (received) {
			super.setSelection(item);
		}
	}
}
