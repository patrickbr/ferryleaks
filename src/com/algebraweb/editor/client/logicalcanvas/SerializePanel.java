package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SerializePanel extends LayoutPanel {

	private RemoteManipulationServiceAsync manServ;


	private AvailableColumnsField columnsAvailable;
	private final AvailableColumnsField iterColumn;
	private final TextBox valueTextBox;
	private RadioButton userUseColumn;
	private final AvailableColumnsField sortColumn;
	private final AvailableColumnsField sortOnColumn;
	private final ListBox preDefinedSortingListBox;
	private RadioButton useSortColumnButton;

	public SerializePanel(int pid, int nid, RemoteManipulationServiceAsync manServ ) {


		super();
		this.manServ=manServ;
		FlowPanel mainPanel = new FlowPanel();
		FlowPanel iterPanel = new FlowPanel();
		FlowPanel serPanel = new FlowPanel();

		FlowPanel columnSelPanel = new FlowPanel();
		FlowPanel columnAvPanel = new FlowPanel();

		HorizontalPanel itemsPanel = new HorizontalPanel();

		mainPanel.add(new Label("Iteration:"));
		RadioButton iterUseValue  = new RadioButton("iteration","Use value: ");
		iterPanel.add(iterUseValue);
		valueTextBox = new TextBox();
		iterPanel.add(valueTextBox);
		userUseColumn = new RadioButton("iteration","Use column: ");
		iterPanel.add(userUseColumn);
		iterColumn = new AvailableColumnsField(pid, nid,manServ);
		iterPanel.add(iterColumn);

		this.addStyleName("loading");
		this.addStyleName("serialize-panel");


		iterUseValue.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					iterColumn.getWidget().setEnabled(false);
					valueTextBox.setEnabled(true);
					valueTextBox.setReadOnly(false);
				}						

			}
		});

		userUseColumn.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					iterColumn.getWidget().setEnabled(true);
					valueTextBox.setEnabled(false);
					valueTextBox.setReadOnly(true);
				}


			}
		});



		RadioButton useSortPreDefButton = new RadioButton("sorting","Use predefined: ");

		serPanel.add(useSortPreDefButton);

		preDefinedSortingListBox = new ListBox();

		preDefinedSortingListBox.addItem("ASCENDING");
		preDefinedSortingListBox.addItem("DESCENDING");

		serPanel.add(preDefinedSortingListBox);

		sortOnColumn = new AvailableColumnsField(pid, nid, manServ);

		serPanel.add(new Label("sort on:"));
		serPanel.add(sortOnColumn);

		useSortColumnButton = new RadioButton("sorting","Use column: ");

		serPanel.add(useSortColumnButton);

		sortColumn = new AvailableColumnsField(pid, nid,manServ);

		serPanel.add(sortColumn);

		useSortPreDefButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					sortColumn.getWidget().setEnabled(false);
					preDefinedSortingListBox.setEnabled(true);
					sortOnColumn.getWidget().setEnabled(true);
				}

			}
		});

		useSortColumnButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					sortColumn.getWidget().setEnabled(true);
					preDefinedSortingListBox.setEnabled(false);
					sortOnColumn.getWidget().setEnabled(false);
				}

			}
		});


		columnsAvailable = new AvailableColumnsField(pid, nid, manServ,true);

		columnsAvailable.getWidget().setVisibleItemCount(10);

		columnAvPanel.add(new Label("Columns available"));
		columnAvPanel.add(columnsAvailable);

		itemsPanel.add(columnSelPanel);


		columnsAvailable.addStyleName("eval-item-columns-available");

		columnSelPanel.add(new Label("Item columns selected:"));
	
		itemsPanel.add(columnAvPanel);



		mainPanel.add(iterPanel);
		mainPanel.add(new Label("Sorting:"));
		mainPanel.add(serPanel);
		mainPanel.add(itemsPanel);
		this.add(mainPanel);

	}

	


	public boolean fillEvaluationContext(EvaluationContext c) {

		c.setIterColumnName(iterColumn.getWidget().getItemText(iterColumn.getWidget().getSelectedIndex()));

		if (!valueTextBox.getValue().matches("[0-9]*")) {

			GWT.log("error: must be number!");
			return false;
		}
		c.setIterColumnNat(Integer.parseInt(valueTextBox.getValue()));


		c.setIterUseColumn(userUseColumn.getValue());

		c.setSortColumnName(sortColumn.getWidget().getItemText(sortColumn.getWidget().getSelectedIndex()));
		c.setSortOrder(preDefinedSortingListBox.getValue(preDefinedSortingListBox.getSelectedIndex()));
		c.setSortUseColumn(useSortColumnButton.getValue());

		String[] itemCols = columnsAvailable.getSelectedItems();

		c.setItemColumns(itemCols);

		return true;

	}


	public void loadEvaluationContext(EvaluationContext c) {

		this.removeStyleName("loading");

		iterColumn.setProjectedSelection(c.getIterColumnName());
		valueTextBox.setValue(Integer.toString(c.getIterColumnNat()),true);

		userUseColumn.setValue(!c.isIterUseColumn(),true);
		userUseColumn.setValue(c.isIterUseColumn(),true);


		sortColumn.setProjectedSelection(c.getSortColumnName());

		for (int i=0;i<preDefinedSortingListBox.getItemCount();i++) {

			if (preDefinedSortingListBox.getValue(i).equals(c.getSortOrder())) preDefinedSortingListBox.setSelectedIndex(i);

		}

		useSortColumnButton.setValue(!c.isSortUseColumn(),true);
		useSortColumnButton.setValue(c.isSortUseColumn(),true);

		columnsAvailable.setProjectedSelection(c.getItemColumns());
		
	}



}
