package com.algebraweb.editor.client.graphcanvas;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * A very simple inline sorter, for testing purposes
 * @author Patrick Brosi
 *
 */

public class InlineSorter  implements GraphSorter {

	public void doSort(ArrayList<GraphNode> nodes,ArrayList<GraphEdge> edges,GraphManipulationCallback cb) {

		Iterator<GraphNode> i = nodes.iterator();

		GraphNode current;

		int x=430;
		int y=200;
		int c=1;


		while(i.hasNext()) {

			current=i.next();

			current.setY(y);
			current.setX(x + (360 * c));

			y+=current.getHeight() + 50;

			
			c = -c;

		}
		
		cb.onComplete();
	}
}



