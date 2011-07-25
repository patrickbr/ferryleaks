package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.ui.Grid;

/**
 * A simple grid table to present sql query results
 * @author patrick
 *
 */
public class SqlResTable extends Grid{

	/**
	 * @param x number of columns
	 * @param y number of rows
	 */
	public SqlResTable(int x, int y) {
		super(x,y);
	}

	/**
	 * Fills the grid with an sql result provided by a JDBC-instance
	 * @param res the sql result as an ArrayList of HashMaps
	 */
	public void fill(ArrayList<HashMap<String,String>> res) {

		if (res.size()<1) return;

		int row=1;
		int col=0;

		Iterator<String> a = res.get(0).keySet().iterator();
		while(a.hasNext()) {
			String curS = a.next();
			this.setHTML(0, col, "<div class='header-cell'>" + curS + "</div>");
			col++;
		}

		col=0;
		Iterator<HashMap<String,String>> it = res.iterator();
		while (it.hasNext()) {
			HashMap<String,String> cur = it.next();
			Iterator<String> i = cur.keySet().iterator();
			while(i.hasNext()) {
				String curS = i.next();
				this.setHTML(row, col, "<div class='res-cell'>" + cur.get(curS) + "</div>");
				col++;
			}
			col=0;
			row++;
		}
	}
}
