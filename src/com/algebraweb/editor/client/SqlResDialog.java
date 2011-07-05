package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;


public class SqlResDialog extends DialogBox {


	public SqlResDialog(ArrayList<HashMap<String,String>> res) {

		this.setText("Evaluation results");
		
		int row=1;
		int col=0;
		
		if (res.size()>0) {
			
			
			Grid t = new Grid(res.size()+1,res.get(0).size());
			
			
			Iterator<String> a = res.get(0).keySet().iterator();
			
			while(a.hasNext()) {
				
				String curS = a.next();
				t.setHTML(0, col, "<div class='header-cell'>" + curS + "</div>");
				col++;
				
			}
			
			col=0;
			Iterator<HashMap<String,String>> it = res.iterator();
			
			
			
			while (it.hasNext()) {
				
				HashMap<String,String> cur = it.next();
			
				Iterator<String> i = cur.keySet().iterator();
				
				
				
				while(i.hasNext()) {
					
					String curS = i.next();
					GWT.log(Integer.toString(col));
					t.setHTML(row, col, "<div class='res-cell'>" + cur.get(curS) + "</div>");
					col++;
					
				}
			
				col=0;
				row++;
			
				
			}
			
						
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

}
