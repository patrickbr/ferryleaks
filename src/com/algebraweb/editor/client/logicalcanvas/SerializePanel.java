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

	private ListBox columnsSelected = new ListBox();
	private AvailableColumnsField columnsAvailable;
	private final AvailableColumnsField iterColumn;
	private final TextBox valueTextBox;
	private RadioButton userUseColumn;
	private final AvailableColumnsField sortColumn;
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
				}

			}
		});

		useSortColumnButton.addValueChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {

				if (event.getValue()) {
					sortColumn.getWidget().setEnabled(true);
					preDefinedSortingListBox.setEnabled(false);
				}

			}
		});




		columnsAvailable = new AvailableColumnsField(pid, nid, manServ);

		columnsSelected.setVisibleItemCount(10);

		columnsAvailable.getWidget().setVisibleItemCount(10);

		columnAvPanel.add(new Label("Columns available"));
		columnAvPanel.add(columnsAvailable);

		itemsPanel.add(columnSelPanel);



		columnsSelected.addStyleName("eval-item-columns-selected");
		columnsAvailable.addStyleName("eval-item-columns-available");

		columnSelPanel.add(new Label("Item columns selected:"));
		columnSelPanel.add(columnsSelected);

		Button addButton = new Button("<-");
		addButton.addStyleName("eval-item-add-button");

		addButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				addColumnToSelected(columnsAvailable.getWidget().getSelectedIndex());

			}
		});



		Button removeButton = new Button("->");
		removeButton.addStyleName("eval-item-remove-button");


		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				removeColumnFromSelected(columnsSelected.getSelectedIndex());

			}
		});


		itemsPanel.add(addButton);
		itemsPanel.add(removeButton);
		itemsPanel.add(columnAvPanel);



		mainPanel.add(iterPanel);
		mainPanel.add(new Label("Sorting:"));
		mainPanel.add(serPanel);
		mainPanel.add(itemsPanel);
		this.add(mainPanel);

	}

	private void addColumnToSelected(int i) {

		columnsSelected.addItem(columnsAvailable.getWidget().getItemText(i));
		columnsAvailable.getWidget().removeItem(i);

	}

	private void removeColumnFromSelected(int i) {

		columnsAvailable.getWidget().addItem(columnsSelected.getItemText(i));
		columnsSelected.removeItem(i);

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
	}



}
