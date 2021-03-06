package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.node.RawNode;

/**
 * A simple inline sorter
 * 
 * @author Patrick Brosi
 * 
 */
public class InlineSorter implements RemoteSorter {
	public Map<Integer, Tuple> getCoordinateHashMap(List<RawNode> nodes) {
		Iterator<RawNode> i = nodes.iterator();
		RawNode current;
		Map<Integer, Tuple> ret = new HashMap<Integer, Tuple>();

		int x = 30;
		int y = 30;
		int c = 0;

		while (i.hasNext()) {
			current = i.next();
			Tuple coord = new Tuple(x, y);
			x += current.getWidth() + 50;
			c++;
			if (c == 4) {
				y = y + 200;
				x = 30;
				c = 0;
			}
			ret.put(current.getNid(), coord);
		}
		return ret;
	}
}