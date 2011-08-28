package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;

/**
 * A simple inline sorter
 * @author Patrick Brosi
 *
 */
public class InlineSorter implements RemoteSorter {

	public Map<Integer,Coordinate> getCoordinateHashMap(List<RawNode> nodes) {
		Iterator<RawNode> i = nodes.iterator();
		RawNode current;
		Map<Integer,Coordinate> ret = new HashMap<Integer,Coordinate>();

		int x=30;
		int y=30;
		int c=0;

		while(i.hasNext()) {
			current=i.next();
			Coordinate coord = new Coordinate(x,y);
			x+=current.getWidth() + 50;
			c++;
			if (c==4) {
				y = y+200;
				x=30;
				c=0;
			}			
			ret.put(current.getNid(), coord);
		}
		return ret;
	}
}
