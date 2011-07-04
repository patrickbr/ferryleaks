package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PushButton;

public class PlanSwitcher extends AbsolutePanel{
	
	
	private HashMap<Integer, Button> buttons = new HashMap<Integer, Button>();
	private AlgebraEditor editor;
	private FlowPanel p = new FlowPanel();
	
	public PlanSwitcher(AlgebraEditor e) {
		
		super();
		this.editor=e;
		
		this.setStylePrimaryName("switcher");

		this.getElement().getStyle().setPosition(Position.FIXED);
		
		this.add(p);
		
		
				
	}
	
	public void addPlan(final int pid) {
				
		GWT.log("adding new plan");
		Button newB = new Button();
		buttons.put(pid,newB);
		
		newB.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				
				editor.changeCanvas(pid);
				
			}
			
		});
		
		newB.setText("Plan " +pid);
		p.add(newB);
		
				
	}
	
	public void removePlan(int pid) {
		
		p.remove(buttons.get(pid));
		buttons.remove(pid);
		
	}
	

}
