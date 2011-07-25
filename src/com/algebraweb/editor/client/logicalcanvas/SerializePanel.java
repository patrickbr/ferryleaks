package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;

public class SerializePanel extends LayoutPanel {

	private RemoteManipulationServiceAsync manServ;


	private AvailableColumnsField columnsAvailable;
	private final AvailableColumnsField iterColumn;
	private final TextBox valueTextBox;
	private RadioButton userUseColumn;
	private RadioButton iterUseValue;
	private final AvailableColumnsField sortColumn;
	private final AvailableColumnsField sortOnColumn;
	private final ListBox preDefinedSortingListBox;
	private RadioButton useSortColumnButton;
	private RadioButton useSortPreDefButton;

	public SerializePanel(int pid, int nid, RemoteManipulationServiceAsync manServ ) {


		super();
		
		this.manServ=manServ;
		FlowPanel mainPanel = new FlowPanel();
		FlowPanel iterPanel = new FlowPanel();
		FlowPanel serPanel = new FlowPanel();

		mainPanel.addStyleName("serialize-panel-inner");
		
		FlowPanel columnSelPanel = new FlowPanel();
		FlowPanel columnAvPanel = new FlowPanel();

		HorizontalPanel itemsPanel = new HorizontalPanel();

		
		
		mainPanel.add(new Label("Iteration:"));
		iterUseValue  = new RadioButton("iteration","Use value: ");
		iterUseValue.addStyleName("iter-use-value");
		iterPanel.add(iterUseValue);
		valueTextBox = new TextBox();
		iterPanel.add(valueTextBox);
		valueTextBox.addStyleName("value-text-box");
		userUseColumn = new RadioButton("iteration","Use column: ");
		iterPanel.add(userUseColumn);
		userUseColumn.addStyleName("iter-use-column");
		iterColumn = new AvailableColumnsField(pid, nid,true,manServ);
		iterPanel.add(iterColumn);
		iterColumn.addStyleName("iter-column");

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



		useSortPreDefButton = new RadioButton("sorting","Use predefined: ");
		useSortPreDefButton.addStyleName("use-sort-predef-button");
		serPanel.add(useSortPreDefButton);

		preDefinedSortingListBox = new ListBox();

		preDefinedSortingListBox.addItem("ascending");
		preDefinedSortingListBox.addItem("descending");
		
		preDefinedSortingListBox.addStyleName("predef-sorting-lb");

		serPanel.add(preDefinedSortingListBox);

		sortOnColumn = new AvailableColumnsField(pid, nid, true,manServ);
		sortOnColumn.addStyleName("sort-on-col");

		serPanel.add(new InlineHTML(" on: "));
		serPanel.add(sortOnColumn);
		serPanel.add(new InlineHTML("<br>"));

		useSortColumnButton = new RadioButton("sorting","Use column: ");
		useSortColumnButton.addStyleName("sort-column-button");
		
		serPanel.add(useSortColumnButton);

		sortColumn = new AvailableColumnsField(pid, nid,true,manServ);
		sortColumn.addStyleName("sort-column");
		
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


		columnsAvailable = new AvailableColumnsField(pid, nid, true,manServ,true);
		columnsAvailable.addStyleName("cols-av");
		columnsAvailable.getWidget().setVisibleItemCount(7);

		columnAvPanel.add(new Label("Item columns"));
		columnAvPanel.add(columnsAvailable);

		itemsPanel.add(columnSelPanel);


		columnsAvailable.addStyleName("eval-item-columns-available");

		itemsPanel.add(columnAvPanel);



		mainPanel.add(iterPanel);
		mainPanel.add(new Label("Sorting:"));
		mainPanel.add(serPanel);
		mainPanel.add(itemsPanel);
		this.add(mainPanel);
		

	}

	


	public boolean fillEvaluationContext(EvaluationContext c) {

	
		if (iterColumn.getWidget().getSelectedIndex() > -1) c.setIterColumnName(iterColumn.getWidget().getItemText(iterColumn.getWidget().getSelectedIndex()));

		if (!valueTextBox.getValue().matches("[0-9]*")) {

			return false;
		}
		c.setIterColumnNat(Integer.parseInt(valueTextBox.getValue()));


		c.setIterUseColumn(userUseColumn.getValue());

		if (sortColumn.getWidget().getSelectedIndex()> -1) c.setSortColumnName(sortColumn.getWidget().getItemText(sortColumn.getWidget().getSelectedIndex()));
		c.setSortOrder(preDefinedSortingListBox.getValue(preDefinedSortingListBox.getSelectedIndex()));
		c.setSortUseColumn(useSortColumnButton.getValue());

		if (sortOnColumn.getListBox().getSelectedIndex() > -1) c.setSortOrderColumnOn(sortOnColumn.getListBox().getValue(sortOnColumn.getListBox().getSelectedIndex()));
		
		String[] itemCols = columnsAvailable.getSelectedItems();

		c.setItemColumns(itemCols);

		return true;

	}


	public void loadEvaluationContext(EvaluationContext c) {

		this.removeStyleName("loading");

		iterColumn.setProjectedSelection(c.getIterColumnName());
		valueTextBox.setValue(Integer.toString(c.getIterColumnNat()),true);

		iterUseValue.setValue(!c.isIterUseColumn(),true);
		userUseColumn.setValue(c.isIterUseColumn(),true);
		


		sortColumn.setProjectedSelection(c.getSortColumnName());

		for (int i=0;i<preDefinedSortingListBox.getItemCount();i++) {

			if (preDefinedSortingListBox.getValue(i).equals(c.getSortOrder())) preDefinedSortingListBox.setSelectedIndex(i);

		}
		
		sortOnColumn.setProjectedSelection(c.getSortOrderColumnOn());

		useSortPreDefButton.setValue(!c.isSortUseColumn(),true);
		useSortColumnButton.setValue(c.isSortUseColumn(),true);

		columnsAvailable.setProjectedSelection(c.getItemColumns());
		
	}



}
