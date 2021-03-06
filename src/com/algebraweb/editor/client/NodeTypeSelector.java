package com.algebraweb.editor.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A dialog asking the user for a node type. Will enter edge adding mode.
 * 
 * @author Patrick Brosi
 * 
 */
public class NodeTypeSelector extends DialogBox {
	private ListBox b;
	private Button ok = new Button("OK");
	private Button cancel = new Button("Cancel");

	public NodeTypeSelector(String[] schemes,
			final AlgebraEditorCanvasView algebraEditorView) {

		this.setAnimationEnabled(true);
		this.setModal(true);
		this.setGlassEnabled(true);

		super.setText("Select type");
		VerticalPanel p = new VerticalPanel();
		b = new ListBox();

		for (String scheme : schemes) {
			b.addItem(scheme);
		}
		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				algebraEditorView.enterNodeAddingMode(getSelectedScheme());
				hide();
			}
		});
		cancel.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		p.add(b);
		HorizontalPanel h = new HorizontalPanel();
		h.add(cancel);
		p
				.setCellHorizontalAlignment(cancel,
						HasHorizontalAlignment.ALIGN_RIGHT);
		cancel.getElement().getStyle().setMargin(10, Unit.PX);
		cancel.getElement().getStyle().setMarginRight(0, Unit.PX);

		h.add(ok);
		p.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_RIGHT);
		ok.getElement().getStyle().setMargin(10, Unit.PX);
		p.add(h);
		p.getElement().getStyle().setPadding(20, Unit.PX);
		this.add(p);
		this.center();
		this.show();
	}

	public String getSelectedScheme() {
		return b.getItemText(b.getSelectedIndex());
	}
}