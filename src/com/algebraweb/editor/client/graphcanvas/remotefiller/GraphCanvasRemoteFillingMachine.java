package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.ArrayList;
import java.util.Iterator;


import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;
import com.algebraweb.editor.client.graphcanvas.remotesorter.RemoteSorter;

public class GraphCanvasRemoteFillingMachine {

	/**
	 * A filling machine for the GraphCanvas. Takes a GraphCanvasFiller-object 
	 * and puts its content on the canvas.
	 */


	private GraphCanvas c;


	public GraphCanvasRemoteFillingMachine(GraphCanvas graphCanvas) {

		this.c = graphCanvas;

	}


	public void fillWith(ArrayList<RawNode> nodes) {

		c.clear();


		Iterator<RawNode> ni = nodes.iterator();
		Iterator<RawNode> ne = nodes.iterator();

		while (ni.hasNext()) {

			RawNode current = ni.next();
			c.addNode(current.getNid(),current.getColor(),current.getWidth(), current.getHeight(), current.getText());

		}

		while (ne.hasNext()) {

			RawNode current = ne.next();

			Iterator<RawEdge> nf = current.getEdgesToList().iterator();

			while (nf.hasNext()) {

				c.createEdge(c.getGraphNodeById(current.getNid()), c.getGraphNodeById(nf.next().getTo()),true);


			}

		}

	
		GraphCanvas.hideLoading();
				
		//automatically sort...
		c.sort(new RemoteSorter("dot"));
	
		

	}


	public void fill(RemoteFiller filler, GraphManipulationCallback cb) {

		GraphCanvas.showLoading("Loading...");
		filler.init(this,cb);


	}



}
