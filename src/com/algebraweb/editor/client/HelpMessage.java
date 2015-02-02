package com.algebraweb.editor.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class HelpMessage extends FlowPanel{

	
	public HelpMessage(final AlgebraEditor editor) {		
		super();
		
		super.addStyleName("helpbar");		
		super.add(new HTML("<span class='questionmark'>?</span>"));		
		super.add(new HTML("<div class='helpmsg'>No idea what this is about? You probably want to load an example plan.</div>"));
		
		Button yesButton = new Button("Yes.");
		Button noButton = new Button("No, thank you.");
		
		super.add(yesButton);
		super.add(noButton);
		
		noButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {				
				HelpMessage.this.removeFromParent();				
			}
		});
		
		yesButton.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				HelpMessage.this.removeFromParent();
				editor.loadExamplePlanFromServer("/examples/example2.xml");				
			}
		});		
	}	
}