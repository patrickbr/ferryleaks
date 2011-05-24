package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;



/**
 * A simple circle sorter.... for testing purposes
 * @author Patrick Brosi
 *
 */

public class CircleSorter implements RemoteSorter {


	public HashMap<Integer,Coordinate> getCoordinateHashMap(ArrayList<RawNode> nodes,ArrayList<RawEdge> edges) {

		Iterator<RawNode> i = nodes.iterator();

		RawNode current;

		double c=0;

		double rad = 20 * nodes.size();

		int x=100 + (20 * nodes.size());
		int y=100 + (20 * nodes.size());

		HashMap<Integer,Coordinate> ret = new HashMap<Integer,Coordinate>();

		while(i.hasNext()) {

			current=i.next();

			Coordinate cord = new Coordinate((x+ Math.sin(c) * rad) - current.getWidth()/2,(y+ Math.cos(c) * rad)  + current.getHeight()/2);

			ret.put(current.getNid(),cord);
			
			c=c+(Math.PI *2)/nodes.size();

		}

		return ret;
	}

}
