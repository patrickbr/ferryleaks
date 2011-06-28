package com.algebraweb.editor.client;

import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.PushButton;

public class ControllPanelButton extends Button {




	public ControllPanelButton(String desc,String styleClass) {

		this(desc);
		this.addStyleName("controllbutton-" +styleClass);
		
		
		
	}

	public ControllPanelButton(String desc) {

		super();
	
		
		
		this.addStyleName("controllpanel-button");
		super.getElement().setAttribute("title", desc);
		this.setWidth("39px");
		this.setHeight("39px");

	}
	




}
