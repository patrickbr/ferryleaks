package com.algebraweb.editor.client.graphcanvas;


import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class LoadingMessagePopUp extends PopupPanel {
	
	
	private Label l;
	private FlowPanel p = new FlowPanel();
	
	public LoadingMessagePopUp() {
						
		super();
		//super.setAnimationEnabled(true);

	    l = new Label();
	    HTML img = new HTML("<img src='loading.gif'>");
	    img.setStyleName("loadingImg");
	    l.addStyleName("loadingmsg");
	    
	    p.add(img);
	    p.add(l);
	    l.addStyleName("loadingmsg");
	    p.addStyleName("loadingWrap");
	    setWidget(p);
	 
		super.addStyleName("loadingPopUp");
		super.setModal(true);
			
	}
		
	public void show(String msg) {
				
		
		l.addStyleName("loadingmsg");
		l.setText(msg);
		super.center();
		
	}
	

}
