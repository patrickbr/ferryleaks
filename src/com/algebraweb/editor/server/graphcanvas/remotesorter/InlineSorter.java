package com.algebraweb.editor.server.graphcanvas.remotesorter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Coordinate;


public class InlineSorter implements RemoteSorter {
	
	public HashMap<Integer,Coordinate> getCoordinateHashMap(ArrayList<RawNode> nodes,ArrayList<RawEdge> edges) {

		Iterator<RawNode> i = nodes.iterator();

		RawNode current;

	
		HashMap<Integer,Coordinate> ret = new HashMap<Integer,Coordinate>();


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
