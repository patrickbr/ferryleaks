package com.algebraweb.editor.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.StackLayoutPanel;
import com.google.gwt.user.client.ui.Widget;


public class NumberedStackLayoutPanel extends StackLayoutPanel{

	public NumberedStackLayoutPanel(Unit unit) {
		super(unit);

		super.addSelectionHandler(new SelectionHandler<Integer>() {

			@Override
			public void onSelection(SelectionEvent<Integer> event) {


				for (int i=0;i<getWidgetCount();i++) {

					getHeaderWidget(i).removeStyleName("selected");

				}

				getHeaderWidget(event.getSelectedItem()).addStyleName("selected");

			}

		});



	}

	@Override
	public void add(Widget widget,String header, double headerSize) {

		super.add(widget, header, headerSize);
		updateClass();

	}

	@Override
	public void add(IsWidget widget,IsWidget header, double headerSize) {

		super.add(widget, header, headerSize);
		updateClass();

	}

	@Override
	public void add(Widget widget,SafeHtml header, double headerSize) {

		super.add(widget, header, headerSize);
		updateClass();

	}

	@Override
	public void add(Widget widget,Widget header, double headerSize) {

		super.add(widget, header, headerSize);
		updateClass();

	}

	@Override
	public void add(IsWidget widget,String header, boolean asHtml,double headerSize) {

		super.add(widget, header, asHtml,headerSize);
		updateClass();

	}

	@Override
	public void add(Widget widget,String header, boolean asHtml,double headerSize) {

		super.add(widget, header, asHtml,headerSize);

		updateClass();

	}




	private void updateClass() {



		if (super.getWidgetCount() <= 1) {

			super.getHeaderWidget(super.getWidgetCount()-1).addStyleName("first");



		}else{

			super.getHeaderWidget(super.getWidgetCount()-2).removeStyleName("last");
			super.getHeaderWidget(super.getWidgetCount()-1).addStyleName("last");


		}

	}

}
