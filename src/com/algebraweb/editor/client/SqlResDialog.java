package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.Grid;


public class SqlResDialog extends DialogBox {


	public SqlResDialog(ArrayList<HashMap<String,String>> res) {


		if (res.size()>0) {
			Grid t = new Grid(res.size(),res.get(0).size());
			
			GWT.log("Row count: " + res.get(0).size());
			
			Iterator<HashMap<String,String>> it = res.iterator();
			
			int row=0;
			int col=0;
			
			while (it.hasNext()) {
				
				HashMap<String,String> cur = it.next();
			
				Iterator<String> i = cur.keySet().iterator();
				GWT.log("Column count: " +  cur.keySet().size());
				
				
				while(i.hasNext()) {
					
					String curS = i.next();
					GWT.log(Integer.toString(col));
					t.setHTML(row, col, cur.get(curS));
					col++;
					
				}
			
				col=0;
				row++;
			
				
			}
			
			this.add(t);
			this.center();
			this.show();
		}
		
		

	}

}
