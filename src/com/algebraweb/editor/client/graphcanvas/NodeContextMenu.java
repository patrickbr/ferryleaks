package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NodeContextMenu extends ContextMenu{

	private GraphNode n;


	public NodeContextMenu() {

		super();

	}

	public void show(GraphNode n, int x, int y) {

		super.show(x,y);

		this.n=n;


	}

	
	public void addItem(final NodeContextMenuItem i) {
		
		final FlowPanel tmp = new FlowPanel();
		
		tmp.addStyleName("node-context-menu-item");
		tmp.addDomHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				i.onClick(NodeContextMenu.this.n.getId());
				NodeContextMenu.this.hide();
				tmp.removeStyleName("hover");
				
			}
		}, ClickEvent.getType());
		
		tmp.addDomHandler(new MouseMoveHandler() {
			
			@Override
			public void onMouseMove(MouseMoveEvent event) {
				
				tmp.addStyleName("hover");
				
			}
		},MouseMoveEvent.getType());
		
		
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


}