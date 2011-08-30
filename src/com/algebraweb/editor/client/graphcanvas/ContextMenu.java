package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;

/**
 * A context menu.
 * 
 * @author Patrick Brosi
 *
 */
public class ContextMenu extends PopupPanel {

	private FlowPanel rows = new FlowPanel();
	private int x;
	private int y;

	public ContextMenu() {
		super(true);
		super.setModal(true);
		super.setAnimationEnabled(true);
		super.addStyleName("node-context-menu");
		GraphCanvas.preventTextSelection(getElement(), true);
	}

	public void addItem(final ContextMenuItem i) {
		final FlowPanel tmp = new FlowPanel();
		tmp.addStyleName("node-context-menu-item");
		tmp.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				i.onClick();
				ContextMenu.this.hide();
				tmp.removeStyleName("hover");

			}
		}, ClickEvent.getType());

		GraphCanvas.preventTextSelection(tmp.getElement(), true);
		tmp.addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();

			}
		}, ContextMenuEvent.getType());
		tmp.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {

				tmp.addStyleName("hover");

			}
		}, MouseMoveEvent.getType());
		tmp.addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				tmp.removeStyleName("hover");

			}
		}, MouseOutEvent.getType());

		InlineHTML text = new InlineHTML(i.getItemTitle());
		text.sinkEvents(Event.MOUSEEVENTS);

		tmp.add(text);
		rows.add(tmp);
	}

	/**
	 * Adds a line to the context menu.
	 */
	public void addSeperator() {
		HTML sep = new HTML();
		sep.addStyleName("node-context-line-break");
		rows.add(sep);
	}

	protected FlowPanel getRows() {
		return rows;
	}

	/**
	 * Returns the X position of this menu
	 * @return the x position
	 */
	public int getX() {
		return x;
	}

	/**
	 * Returns the Y position of this menu
	 * @return the y position
	 */
	public int getY() {
		return y;
	}

	public void show(int x, int y) {
		super.setPopupPosition(x, y);
		this.x = x;
		this.y = y;

		this.clear();
		this.add(rows);
		super.show();
	}

}
