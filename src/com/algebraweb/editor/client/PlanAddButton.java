package com.algebraweb.editor.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;

public class PlanAddButton extends Composite {

	private Button button;
	
	public PlanAddButton() {
		
		button = new Button();
		button.setText("+");
		FlowPanel p = new FlowPanel();
		p.addStyleName("planswitchbutton");
		p.add(button);
				
		initWidget(p);
	}
	
	public Button getButton() {
		return button;
	}
	
}
