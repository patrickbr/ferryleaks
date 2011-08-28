package com.algebraweb.editor.client.graphcanvas.remotefiller;

import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.RawEdge;
import com.algebraweb.editor.client.RawNode;
import com.algebraweb.editor.client.graphcanvas.Fillable;
import com.algebraweb.editor.client.graphcanvas.GraphCanvas;
import com.algebraweb.editor.client.graphcanvas.GraphManipulationCallback;


/**
 * A filling machine for the GraphCanvas. Takes a GraphCanvasFiller-object 
 * and puts its content on the canvas.
 * @author Patrick Brosi
 *
 */
public class GraphCanvasRemoteFillingMachine {
	private Fillable c;

	public GraphCanvasRemoteFillingMachine(Fillable graphCanvas) {
		this.c = graphCanvas;
	}

	/**
	 * Starts the filling with a given remote filler. Calls GraphManipulationCallback
	 * after filling has ended
	 * @param filler the filler to use
	 * @param cb the GraphManipulationCallback to call
	 */
	public void fill(RemoteFiller filler, GraphManipulationCallback cb) {
		GraphCanvas.showLoading("Loading...");
		filler.init(this,cb);
	}

	/**
	 * Fill the canvas with a given list of RawNodes
	 * @param nodes the nodes to use
	 */
	protected void fillWith(List<RawNode> nodes) {
		c.clear();

		Iterator<RawNode> ni = nodes.iterator();
		Iterator<RawNode> ne = nodes.iterator();

		while (ni.hasNext()) {
			RawNode current = ni.next();
			c.addNode(current.getNid(),current.getColor(),current.getWidth(), current.getHeight(), current.getText(),current.getFixedChildCount());
		}

		while (ne.hasNext()) {
			RawNode current = ne.next();
			Iterator<RawEdge> nf = current.getEdgesToList().iterator();
			while (nf.hasNext()) {
				RawEdge cur = nf.next();
				c.createEdge(current.getNid(), cur.getTo(),cur.getFixedParentPos(),true);
			}
		}
		GraphCanvas.hideLoading();
	}
}
