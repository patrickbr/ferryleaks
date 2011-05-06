package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A very simple sorter, for testing purposes
 * @author Patrick Brosi
 *
 */

public class SimpleSorter implements GraphSorter {
	

	public void doSort(ArrayList<GraphNode> nodes) {

		Iterator<GraphNode> i = nodes.iterator();

		GraphNode current;

		int x=30;
		int y=30;
		int c=0;

		while(i.hasNext()) {

			current=i.next();

			current.setX(x);
			current.setY(y);

			x+=current.getWidth() + 50;

			c++;

			if (c==4) {
				y = y+200;
				x=30;
				c=0;
			}
		}
	}

}
