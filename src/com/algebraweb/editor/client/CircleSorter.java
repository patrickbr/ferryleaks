package com.algebraweb.editor.client;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * A simple circle sorter.... for testint purposes
 * @author Patrick Brosi
 *
 */

public class CircleSorter implements GraphSorter {

	
	public void doSort(ArrayList<GraphNode> nodes) {

		Iterator<GraphNode> i = nodes.iterator();

		GraphNode current;

		double c=0;
		
		double rad = 20 * nodes.size();
	
		int x=100 + (20 * nodes.size());
		int y=100 + (20 * nodes.size());

		while(i.hasNext()) {

			current=i.next();
			
			current.setX((x+ Math.sin(c) * rad) - current.getWidth()/2);
			current.setY((y+ Math.cos(c) * rad)  + current.getHeight()/2);

			c=c+(Math.PI *2)/nodes.size();

		}
	}

}
