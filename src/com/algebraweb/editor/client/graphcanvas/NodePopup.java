package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A popup appearing on node hovering.
 * @author Patrick Brosi
 *
 */
public class NodePopup extends PopupPanel {

	private int nodeid = -1;

	public NodePopup() {
		super(true);
		super.addStyleName("nodePopUp");
		super.setAnimationEnabled(true);
	}

	protected void correctPosition() {

		int top = this.getPopupTop() - Window.getScrollTop();
		int left = this.getPopupLeft() - Window.getScrollLeft();

		if (this.getOffsetHeight() + top > Window.getClientHeight()) {

			int l = this.getElement().getAbsoluteLeft();
			int h = this.getElement().getAbsoluteTop();
			super.getElement().getStyle().setTop(h - this.getOffsetHeight(),
					Unit.PX);
			super.getElement().getStyle().setLeft(l, Unit.PX);

		}
		if (this.getOffsetWidth() + left > Window.getClientWidth()) {

			int l = super.getAbsoluteLeft();
			int h = this.getAbsoluteTop();

			super.setPopupPosition(super.getAbsoluteLeft()
					- this.getOffsetWidth(), this.getAbsoluteTop());
			super.setPopupPosition(l - this.getOffsetWidth(), h);

		}
	}

	public int getNodeId() {
		return nodeid;
	}

	@Override
	public void hide() {
		this.nodeid = -1;
		super.hide();
	}

	protected void render() {
	}

	public void setNodeId(int id) {
		nodeid = id;
	}

	public void showAt(int x, int y) {
		super.clear();
		render();
		super.setPopupPosition(x, y);
		super.show();
	}
}
