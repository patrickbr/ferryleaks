package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.ContextMenuEvent;
import com.google.gwt.event.dom.client.ContextMenuHandler;
import com.google.gwt.event.dom.client.MouseDownEvent;
import com.google.gwt.event.dom.client.MouseDownHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.PushButton;

public class PlanSwitcher extends AbsolutePanel{


	private HashMap<Integer, PlanSwitchButton> buttons = new HashMap<Integer, PlanSwitchButton>();
	private AlgebraEditor editor;
	private FlowPanel p = new FlowPanel();
	private int active = -1;
	private PlanAddButton addPlanButton;

	public PlanSwitcher(final AlgebraEditor e) {

		super();
		addPlanButton = new PlanAddButton();
		addPlanButton.addStyleName("add-plan-button");

		addPlanButton.getButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				e.createNewPlan(false);
			
			}
		});
		

		this.editor=e;

		this.setStylePrimaryName("switcher");
		p.add(addPlanButton);

		this.getElement().getStyle().setPosition(Position.FIXED);
		this.add(p);

	}

	public PlanSwitchButton addPlan(final int pid) {

		GWT.log("adding new plan");
		PlanSwitchButton newB = new PlanSwitchButton(pid);
		buttons.put(pid,newB);
		p.remove(addPlanButton);

		newB.getButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				editor.changeCanvas(pid);


			}

		});

		newB.getButton().addMouseDownHandler(new MouseDownHandler() {

			@Override
			public void onMouseDown(MouseDownEvent event) {


				if (event.getNativeButton() == NativeEvent.BUTTON_MIDDLE) {

					event.preventDefault();
					editor.removePlan(pid);

				}

			}
		});
		
		newB.getButton().addDomHandler(new ContextMenuHandler() {

			@Override
			public void onContextMenu(ContextMenuEvent event) {
			
				event.preventDefault();
				editor.getTabContextMenu().show(pid, event.getNativeEvent().getClientX()+Window.getScrollLeft(), event.getNativeEvent().getClientY()+Window.getScrollTop());
				
			}
		},ContextMenuEvent.getType());


		p.add(newB);
		p.add(addPlanButton);

		return newB;

	}

	public void removePlan(int pid) {

		p.remove(buttons.get(pid));
		buttons.remove(pid);
		if (active == pid) active = -1;

	}

	public void setActive(int pid) {

		if (active != -1) buttons.get(active).removeStyleName("active");
		buttons.get(pid).addStyleName("active");
		active=pid;

	}


}
