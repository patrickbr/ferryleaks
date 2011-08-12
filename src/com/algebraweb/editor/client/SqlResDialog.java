package com.algebraweb.editor.client;

import java.util.List;
import java.util.Map;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * A simple dialog to present sql results provided by a
 * JDBC instance using a SqlResTable
 * 
 * @author Patrick Brosi
 *
 */
public class SqlResDialog extends DialogBox {

	/**
	 *  @param result the JDBC sql result
	 */
	public SqlResDialog(List<Map<String, String>> result) {

		this.setText("Evaluation results");

		SqlResTable t = new SqlResTable(result.size()+1,result.get(0).size());
		t.fill(result);

		VerticalPanel p = new VerticalPanel();
		ScrollPanel s = new ScrollPanel();

		s.add(t);
		p.add(s);
		Button ok = new Button("Close");

		ok.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				SqlResDialog.this.hide();

			}
		});

		p.add(ok);

		this.add(p);

		this.center();
		this.show();
	}

}
