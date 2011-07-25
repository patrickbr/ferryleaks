package com.algebraweb.editor.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A simple Yes/No Panel to provide the user a possibility to
 * answer a simple question
 * 
 * @author Patrick Brosi
 *
 */
public class YesNoPanel extends DialogBox{

	private Button yes = new Button("Yes");
	private Button no = new Button("No");

	/**
	 * @param msg the question to be asked
	 * @param title the title of the popup
	 */
	public YesNoPanel(String msg, String title) {

		super();
		super.setText(title);
		super.setAnimationEnabled(true);
		yes.setWidth("70px");
		no.setWidth("70px");
		VerticalPanel p = new VerticalPanel();

		HorizontalPanel h = new HorizontalPanel();
		HorizontalPanel ht = new HorizontalPanel();

		yes.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		no.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});

		HTML text = new HTML(msg);
		ht.add(text);
		p.add(ht);
		p.add(h);
		h.setSpacing(10);
		ht.setSpacing(10);

		p.setCellHorizontalAlignment(h, HasHorizontalAlignment.ALIGN_CENTER);
		p.setCellVerticalAlignment(h, HasVerticalAlignment.ALIGN_MIDDLE);
		h.add(yes);
		h.add(no);
		this.add(p);
	}

	/**
	 * Adds a ClickHandler to the "No"-Button
	 * @param h the handler to add
	 */
	public void registerNoClickHandler(ClickHandler h) {
		no.addClickHandler(h);
	}

	/**
	 * Adds a ClickHandler to the "Yes"-Button
	 * @param h the handler to add
	 */
	public void registerYesClickHandler(ClickHandler h) {
		yes.addClickHandler(h);
	}

	@Override
	public void show() {
		super.show();
		yes.setFocus(true);
	}


}
