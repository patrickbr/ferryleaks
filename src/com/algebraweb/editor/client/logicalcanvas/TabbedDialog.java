package com.algebraweb.editor.client.logicalcanvas;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class TabbedDialog extends DialogBox{
	
	private HorizontalPanel buttonsPanel;
	private TabLayoutPanel t;
	private LayoutPanel p;
	
	public TabbedDialog() {
		
		super();
		
		super.setAnimationEnabled(true);
		super.setModal(true);
		p = new LayoutPanel();
		t = new TabLayoutPanel(1.5, Unit.EM);
		
		t.setAnimationVertical(false);
		t.setAnimationDuration(500);
		t.setSize("550px", "350px");


		buttonsPanel = new HorizontalPanel();
	
		p.add(t);
		p.add(buttonsPanel);
				
		p.setSize("550px","400px");
		
		this.add(p);
		this.show();
		this.center();
		
	}
	
	
	protected void addTab(Widget w,String title) {
		getTabPanel().add(w, title);
	}
	
	protected void addButton(Button b) {
		getButtonsPanel().add(b);
		p.setWidgetBottomHeight(buttonsPanel, 0, Unit.PX, 50, Unit.PX);
	}

	/**
	 * @return the buttonsPanel
	 */
	protected HorizontalPanel getButtonsPanel() {
		return buttonsPanel;
	}

	
	/**
	 * @return the t
	 */
	protected TabLayoutPanel getTabPanel() {
		return t;
	}




	
}
