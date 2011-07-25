package com.algebraweb.editor.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TextPresentationDialog extends DialogBox {
	
	
	private VerticalPanel p = new VerticalPanel();
	private TextArea ta = new TextArea();
	
	public TextPresentationDialog(String title) {
		this(title,"");
	}
	
	public TextPresentationDialog(String title,String content) {
		
		super();
		super.setText(title);
		super.setAnimationEnabled(true);
		ta.setWidth("500px");
		ta.setHeight("200px");
		
		p.add(ta);
		ta.setReadOnly(true);
		ta.setText(content);
		
		
		Button closeButton = new Button("Close");
		closeButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				hide();				
			}
		});
		p.add(closeButton);
		
		add(p);
		center();
		show();
		
	}
	
	public void loadText(String t) {
		
		ta.setText(t);
		
	}
	

}
