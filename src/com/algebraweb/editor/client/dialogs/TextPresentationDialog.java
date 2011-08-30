package com.algebraweb.editor.client.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A simple dialog box with a close button to present plain text
 * 
 * @author Patrick Brosi
 * 
 */
public class TextPresentationDialog extends DialogBox {

	private VerticalPanel p = new VerticalPanel();
	private TextArea ta = new TextArea();

	/**
	 * @param title
	 *            the title of the dialog
	 */
	public TextPresentationDialog(String title) {
		this(title, "");
	}

	/**
	 * @param title
	 *            the title of the dialog
	 * @param content
	 *            the plain text to present
	 */
	public TextPresentationDialog(String title, String content) {

		super();
		super.setText(title);
		super.setAnimationEnabled(true);
		ta.setWidth("500px");
		ta.setHeight("200px");

		p.add(ta);
		ta.setReadOnly(true);
		ta.setText(content);

		Button closeButton = new Button("Close");
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		p.add(closeButton);

		add(p);
		center();
		show();

	}

	/**
	 * Load plain text into the box
	 * 
	 * @param t
	 *            the plain text to load
	 */
	public void loadText(String t) {
		ta.setText(t);
	}

}
