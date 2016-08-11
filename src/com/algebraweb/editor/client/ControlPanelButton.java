package com.algebraweb.editor.client;

import com.google.gwt.user.client.ui.Button;

/**
 * A button for the control panel. Will be styled accordingly.
 *
 * @author Patrick Brosi
 *
 */
public class ControlPanelButton extends Button {
	public ControlPanelButton(String desc) {
		super();

		this.addStyleName("controllpanel-button");
		super.getElement().setAttribute("title", desc);
		this.setWidth("39px");
		this.setHeight("39px");
	}

	public ControlPanelButton(String desc, String styleClass) {
		this(desc);
		this.addStyleName("controllbutton-" + styleClass);
	}
}
