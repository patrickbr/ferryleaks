package com.algebraweb.editor.client;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

/**
 * A plan's tab bar button
 * @author Patrick Brosi
 *
 */
public class PlanSwitchButton extends Composite {

	private Button button;
	private HTML errorLabel = new HTML("<span class='error-count'></span>");

	public PlanSwitchButton(int pid) {
		button = new Button();
		button.setText("Plan " + pid);
		FlowPanel p = new FlowPanel();
		p.addStyleName("planswitchbutton");
		p.add(errorLabel);
		p.add(button);
		initWidget(p);
	}

	public Button getButton() {
		return button;
	}

	public void setErrorCount(int c) {
		if (c > 0) {
			errorLabel.setHTML("<span class='error-count'>(" + c + ")</span>");
		} else {
			errorLabel.setHTML("<span class='error-count'></span>");
		}
	}
}
