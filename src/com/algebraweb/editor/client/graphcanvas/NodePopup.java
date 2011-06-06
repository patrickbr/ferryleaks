package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;

public class NodePopup extends PopupPanel {
	
	
	private int nodeid = -1;

    public NodePopup() {
      // PopupPanel's constructor takes 'auto-hide' as its boolean parameter.
      // If this is set, the panel closes itself automatically when the user
      // clicks outside of it.
     
      super(true);
      
      super.addStyleName("nodePopUp");
     
      super.setAnimationEnabled(true);
      super.setPixelSize(100, 160);
   


      // PopupPanel is a SimplePanel, so you have to set it's widget property to
      // whatever you want its contents to be.
    
    }
    
    public void showAt(int x, int y) {
    	
    	  this.nodeid=nodeid;
    	  super.setPopupPosition(x, y);
    	  super.clear();
    	  super.add(new Label("This is node #" + nodeid));
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

