package com.algebraweb.editor.client;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FlowPanel;

public class ZoomPanel extends FlowPanel {
	
	private Button zoomInButton = new Button("");
	private Button zoomOutButton = new Button("");
	
	public ZoomPanel() {
		super();
		zoomInButton.addStyleName("zoomInButton");
		zoomOutButton.addStyleName("zoomOutButton");
		this.addStyleName("zoompanel");
		this.add(zoomInButton);
		this.add(zoomOutButton);
	}
	
	public void registerZoomInHandler(ClickHandler handler) {
		zoomInButton.addClickHandler(handler);
	}
	
	public void registerZoomOutHandler(ClickHandler handler) {
		zoomOutButton.addClickHandler(handler);
	}

}
