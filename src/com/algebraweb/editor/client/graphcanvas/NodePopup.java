package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
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

		//correctPosition();

	}


	protected void correctPosition() {

		int top = this.getPopupTop()-Window.getScrollTop();
		int left = this.getPopupLeft()-Window.getScrollLeft();

		if (this.getOffsetHeight() + top > Window.getClientHeight()) {
			
	
			int l = this.getElement().getAbsoluteLeft();
			int h = this.getElement().getAbsoluteTop();			
			GWT.log((this.getElement().getStyle().getTop()));
			//super.setPopupPosition(this.getAbsoluteLeft(),getAbsoluteTop()-this.getOffsetHeight());
			//super.setPopupPosition(l,h-this.getOffsetHeight());
			super.getElement().getStyle().setTop(h-this.getOffsetHeight(), Unit.PX);
			super.getElement().getStyle().setLeft(l, Unit.PX);
			

		}
		if (this.getOffsetWidth() + left > Window.getClientWidth()) {
			
			int l = super.getAbsoluteLeft();
			int h = this.getAbsoluteTop();
			
			super.setPopupPosition(super.getAbsoluteLeft()-this.getOffsetWidth(), this.getAbsoluteTop());
			super.setPopupPosition(l-this.getOffsetWidth(), h);
			

		}


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

