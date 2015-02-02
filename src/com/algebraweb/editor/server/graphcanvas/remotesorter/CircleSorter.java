package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.node.RawNode;

/**
 * A simple circle sorter.... for testing purposes
 * 
 * @author Patrick Brosi
 * 
 */
public class CircleSorter implements RemoteSorter {

	public Map<Integer, Tuple> getCoordinateHashMap(List<RawNode> nodes) {
		Iterator<RawNode> i = nodes.iterator();
		RawNode current;

		double c = 0;
		double rad = 20 * nodes.size();

		int x = 100 + 20 * nodes.size();
		int y = 100 + 20 * nodes.size();

		Map<Integer, Tuple> ret = new HashMap<Integer, Tuple>();

		while (i.hasNext()) {
			current = i.next();
			Tuple cord = new Tuple(x + Math.sin(c) * rad - current.getWidth()
					/ 2, y + Math.cos(c) * rad + current.getHeight() / 2);
			ret.put(current.getNid(), cord);
			c = c + Math.PI * 2 / nodes.size();
		}
		return ret;
	}
}