package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class NodePopup extends PopupPanel {
	
	
	private int nodeid = -1;

    public NodePopup() {
     
      super(true);
      
      super.addStyleName("nodePopUp");
     
      super.setAnimationEnabled(true);
   
   

    }
    
    protected void render() {
    	
    	
    	
    }
    
    public void showAt(int x, int y) {
    	
          super.clear();
    	  render();
    	  super.setPopupPosition(x, y);
    	  super.show();
    	    	
    }
    
    @Override
    public void hide() {
    	
    	this.nodeid=-1;
    	super.hide();
    	
    }
    
    public int getNodeId() {
    	return nodeid;
    }
    
    public void setNodeId(int id) {
    	nodeid = id;
    }
  }

