package com.algebraweb.editor.client.graphcanvas;

import com.algebraweb.editor.client.TabContextMenuItem;
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
import com.google.gwt.user.client.ui.InlineHTML;

public class TabContextMenu extends ContextMenu{

	private int pid;


	public TabContextMenu() {

		super();

	}

	public void addItem(final TabContextMenuItem i) {

		final FlowPanel tmp = new FlowPanel();
		GraphCanvas.preventTextSelection(tmp.getElement(), true);
		
		tmp.addStyleName("node-context-menu-item");
		
		tmp.addDomHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				i.onClick(TabContextMenu.this.pid);
				TabContextMenu.this.hide();
				tmp.removeStyleName("hover");

			}
		}, ClickEvent.getType());

		tmp.addDomHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {

				tmp.addStyleName("hover");

			}
		},MouseMoveEvent.getType());

		tmp.addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {
				event.preventDefault();

			}
		},ContextMenuEvent.getType());

		tmp.addDomHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				tmp.removeStyleName("hover");

			}
		},MouseOutEvent.getType());

		InlineHTML text = new InlineHTML(i.getItemTitle());
		text.sinkEvents(Event.MOUSEEVENTS);

		tmp.add(text);

		super.getRows().add(tmp);

	}


	public void show(int pid, int x, int y) {

		super.show(x,y);
		this.pid=pid;

	}


}
