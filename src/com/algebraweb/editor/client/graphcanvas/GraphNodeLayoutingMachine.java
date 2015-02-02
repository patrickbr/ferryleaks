package com.algebraweb.editor.client.graphcanvas;

import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.client.AlgebraEditor;
import com.google.gwt.user.client.Window;

/**
 * Provides animated layouting after sorting by a GraphSorter
 * 
 * @author Patrick Brosi
 * 
 */

public class GraphNodeLayoutingMachine {

	protected List<GraphNode> nodes;
	protected List<GraphEdge> edges;
	protected GraphNodeModifier gnm;
	protected GraphCanvas c;
	private static int offsetX = 600;
	private static int offsetY = 500;

	public GraphNodeLayoutingMachine(GraphCanvas c, GraphNodeModifier gnm) {
		this.gnm = gnm;
		this.c = c;
	}

	public void layout(List<GraphNode> nodes2, boolean animate) {
		AlgebraEditor.log("Layouting with animate = "
				+ Boolean.toString(animate));
		Iterator<GraphNode> a = nodes2.iterator();

		int width = 0;
		int height = 0;

		int negW = 0;
		int negH = 0;

		while (a.hasNext()) {
			GraphNode current = a.next();
			if (current.getX() < negW) {
				negW = (int) current.getX();
			}
			if (current.getY() < negW) {
				negH = (int) current.getY();
			}

			if (current.getX() + Math.abs(negW) > width) {
				width = Math.abs(negW) + (int) current.getX();
			}
			if (current.getY() + Math.abs(negH) > height) {
				height = Math.abs(negH) + (int) current.getY();
			}
		}

		int newH = Window.getClientHeight() - 30;
		int newW = Window.getClientWidth() - 30;

		if (height / gnm.getCanvas().getScale() > newH) {
			newH = (int) (height / gnm.getCanvas().getScale());
		}
		if (width / gnm.getCanvas().getScale() > newW) {
			newW = (int) (width / gnm.getCanvas().getScale());
		}

		gnm.getCanvas().setSize(newW, newH);

		offsetX = 180;
		offsetY = 100;

		a = nodes2.iterator();

		while (a.hasNext()) {
			GraphNode current = a.next();

			if (!c.isNotActive() && animate) {
				gnm.animateTo(current, current.getX() + offsetX, current.getY()
						+ offsetY);
			} else {
				gnm.moveTo(current, current.getX() + offsetX, current.getY()
						+ offsetY);
			}
		}

		if (gnm.getCanvas().getNodes().size() > 0) {
			Tuple scrollTo = gnm.getCanvas().getScrollIntoViewPos(
					gnm.getCanvas().getNodes().get(
							gnm.getCanvas().getNodes().size() - 1).getId());

			if (!gnm.getCanvas().isNotActive()) {
				Window.scrollTo((int) scrollTo.getX(), (int) scrollTo.getY());
				((FullScreenDragPanel) gnm.getCanvas().getParent())
						.changeSavedScrollPos((int) scrollTo.getX(),
								(int) scrollTo.getY());
			}
		}
	}

	/**
	 * Sort the given nodes with the GraphSorter sorter
	 * 
	 * @param nodes
	 * @param sorter
	 */

	public void sortGraph(List<GraphNode> nodes, List<GraphEdge> edges,
			GraphSorter sorter, final boolean layout) {

		this.nodes = nodes;
		this.edges = edges;

		Iterator<GraphNode> i = nodes.iterator();
		GraphNode current;

		while (i.hasNext()) {
			current = i.next();
			gnm.hideEdges(current, !c.isNotActive());
		}

		GraphCanvas.showLoading("Layouting...");
		sorter.doSort(nodes, edges, new GraphManipulationCallback() {
			@Override
			public void onComplete() {
				GraphCanvas.hideLoading();
				if (layout) {
					layout(GraphNodeLayoutingMachine.this.nodes, true);
				}
			}
		});
	}
}