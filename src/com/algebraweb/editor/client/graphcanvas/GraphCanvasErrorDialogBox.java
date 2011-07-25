package com.algebraweb.editor.client.graphcanvas;

import com.algebraweb.editor.client.AlgebraEditor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;

public class GraphCanvasErrorDialogBox extends DialogBox {
	
	
	public GraphCanvasErrorDialogBox(String msg) {
		
		AlgebraEditor.log("ERROR: " + msg);
		
		super.setText("Error");
		Button ok = new Button("OK");
		ok.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				hide();
			}
		});
		;
		VerticalPanel p = new VerticalPanel();
	
		HTML inner = new HTML(msg);
		inner.addStyleName("error-inner");
		p.add(inner);
		p.add(ok);
		ok.getElement().getStyle().setMargin(20, Unit.PX);
		p.setCellHorizontalAlignment(ok, HasHorizontalAlignment.ALIGN_CENTER);

		super.add(p);



		center();
		show();
		
		
	}

}
