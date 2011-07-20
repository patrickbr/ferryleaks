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

public class ContextMenu extends PopupPanel{
	
	private FlowPanel rows = new FlowPanel();
	private int x;
	private int y;
	
	public ContextMenu() {
		
		super(true);
		super.setModal(true);
		super.setAnimationEnabled(true);
		super.addStyleName("node-context-menu");
		
	}
	
	public void show(int x, int y) {
		
		super.setPopupPosition(x, y);
		this.x=x;
		this.y=y;
			
		this.clear();
		this.add(rows);
				
		super.show();
					
	}
	
	public void addSeperator() {
		
		HTML sep = new HTML();
		sep.addStyleName("node-context-line-break");
		rows.add(sep);
		
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
		
		rows.add(tmp);
		
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return y;
	}
	
	protected FlowPanel getRows() {
		
		return rows;
	}
	

}
