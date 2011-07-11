package com.algebraweb.editor.client.graphcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.hydro4ge.raphaelgwt.client.AnimationCallback;
import com.hydro4ge.raphaelgwt.client.Raphael.Circle;

public class GraphNodeModifier {



	private GraphCanvas c;

	public GraphCanvas getCanvas() {
		return c;
	}


	public GraphNodeModifier(GraphCanvas c) {

		this.c=c;

	}

	protected void animateTo(GraphNode n,double x, double y) {

		n.setAniLock();

		n.setX(x);
		n.setY(y);	

		checkDimension(n,x, y);

		update(n,true,true);

		JSONObject newAttrs = new JSONObject();

		newAttrs.put("x", new JSONNumber(x));
		newAttrs.put("y", new JSONNumber(y));

		JSONObject newAttrsText = new JSONObject();

		newAttrsText.put("x", new JSONNumber(x + n.getWidth()/2));
		newAttrsText.put("y", new JSONNumber(y + n.getHeight()/2));

		n.getShape().animate(newAttrs,1000, "backIn",buildAnimationCallback(n));
		n.getText().animate(newAttrsText,1000, "backIn");

		Iterator<ConnectedShape> it = n.getConnectedShapes().values().iterator();

		while (it.hasNext()) {

			ConnectedShape current = it.next();

			JSONObject newAttrsShape = new JSONObject();

			newAttrsShape.put("x", new JSONNumber(x + current.getX()));
			newAttrsShape.put("y", new JSONNumber(y + current.getY()));

			current.getShape().animate(newAttrsShape,1000, "backIn");

		}

	}


	protected void checkDimension(GraphNode n,double x, double y) {
		/**
		 * TODO: scale!!
		 */

		if ((x/c.getScale())+(n.getWidth() / c.getScale()) > c.getWidth()) {
			c.setWidth((int)(c.getWidth() + (50 / c.getScale()) + ((x/c.getScale())+(n.getWidth() / c.getScale()) - c.getWidth())));
		}

		if ((y/c.getScale())+(n.getHeight() / c.getScale())> c.getHeight()) {
			c.setHeight((int)(c.getHeight() + (50 / c.getScale()) + ((y/c.getScale())+(n.getHeight() / c.getScale()) - c.getHeight())));
		}
	}

	protected void removeEdge(GraphNode n,GraphEdge e) {

		n.getEdgesTo().remove(e);
		n.getEdgesFrom().remove(e);
		getOffset(n,e.getOrientationFrom(),null,false,-1,true,false,false);
		getOffset(n,e.getOrientation(),null,false,-1,true,false,false);

	}

	protected void dimOut(GraphNode n) {

		JSONObject newAttrs = new JSONObject();
		newAttrs.put("opacity", new JSONNumber(0));
		n.getShape().animate(newAttrs,1000);
		n.getText().animate(newAttrs,1000);

		Iterator<ConnectedShape> it = n.getConnectedShapes().values().iterator();

		while (it.hasNext()) {

			ConnectedShape current = it.next();

			current.getShape().animate(newAttrs,1000);

		}

	}

	protected void hideEdges(GraphNode n, boolean animated) {

		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();
		while (j.hasNext()) c.getGraphEdgeModifier().snakeIn(j.next(),animated);

	}

	protected void showEdges(GraphNode n, boolean animated) {


		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();
		while (j.hasNext()) c.getGraphEdgeModifier().snakeOut(j.next(),animated);

	}

	protected void edgesToFront(GraphNode n) {

		Iterator<GraphEdge> i = n.getEdgesTo().iterator();
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();

		while (i.hasNext()) c.getGraphEdgeModifier().toFront(i.next());
		while (j.hasNext()) c.getGraphEdgeModifier().toFront(j.next());

	}


	protected void setSelected(GraphNode n) {

		JSONObject newAttrs = new JSONObject();

		newAttrs.put("stroke-width", new JSONNumber(2));

		n.getShape().animate(newAttrs, 300);


		Iterator<GraphEdge> i = n.getEdgesTo().iterator();
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();

		while (i.hasNext()) c.getGraphEdgeModifier().setSelected(i.next());
		while (j.hasNext())  c.getGraphEdgeModifier().setSelected(j.next());

	}

	protected void setNotSelected(GraphNode n) {

		JSONObject newAttrs = new JSONObject();

		newAttrs.put("stroke-width", new JSONNumber(1));

		n.getShape().animate(newAttrs, 300);

		Iterator<GraphEdge> i = n.getEdgesTo().iterator();
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();

		while (i.hasNext()) c.getGraphEdgeModifier().setNotSelected(i.next());
		while (j.hasNext())  c.getGraphEdgeModifier().setNotSelected(j.next());

	}


	protected void update(GraphNode n,boolean quiet,boolean animated) {

		Iterator<GraphEdge> i = n.getEdgesTo().iterator();
		Iterator<GraphEdge> j = n.getEdgesFrom().iterator();

		while (i.hasNext()) c.getGraphEdgeModifier().update(i.next(),quiet,animated);
		while (j.hasNext()) c.getGraphEdgeModifier().update(j.next(),quiet,animated);

	}


	protected void moveTo(GraphNode n,double x, double y) {

		if ((n.getX() != x || n.getY()!= x) && x >=0 && y>=0) {


			checkDimension(n,x, y);

			n.getShape().attr("x", x);
			n.getShape().attr("y", y);

			n.getText().attr("x", x + n.getWidth()/2);
			n.getText().attr("y", y + n.getHeight()/2);


			Iterator<ConnectedShape> it = n.getConnectedShapes().values().iterator();

			while (it.hasNext()) {

				ConnectedShape current = it.next();

				current.getShape().attr("x", x + current.getX());
				current.getShape().attr("y", y + current.getY());

			}


			n.setX(x);
			n.setY(y);

			update(n,false,false);
		}
	}

	private AnimationCallback buildAnimationCallback(final GraphNode n) {

		return new AnimationCallback() {

			@Override
			public void onComplete() {
				//update(n,true);
				n.unsetAniLock();
				showEdges(n,true);
			}
		};
	}

	protected int getOffset(GraphNode n, int orientation,GraphEdge e, boolean hasChanged, int oldOrientation) {
		return getOffset(n, orientation,e, hasChanged, oldOrientation, false,false,false);	
	}


	private int getGurrByOrientation(GraphNode n,int o) {

		if (o == 0 || o == 1) {
			return (int) n.getShape().getBBox().width();
		}else{
			return (int) n.getShape().getBBox().height();
		}
	}


	protected int getOffset(GraphNode n,int orientation,GraphEdge e, boolean hasChanged, int oldOrientation, boolean forceall,boolean quiet,boolean animated) {

		if (hasChanged) {
			getOffset(n, oldOrientation,null,false,-1,true,quiet,animated);
		}

		int ret = -1;

		int gurr = getGurrByOrientation(n,orientation);

		ArrayList<GraphEdge> edges = getEdgesByOrientation(orientation,n.getEdgesTo());
		ArrayList<GraphEdge> edgesF = getEdgesByOrientationFrom(orientation,n.getEdgesFrom());

		int length = edges.size() + edgesF.size();
		//if (edgesF.size() > 0) {
		//	length++;
		//}

		Iterator<GraphEdge> i = edges.iterator();
		Iterator<GraphEdge> a = edgesF.iterator();

		int c=1;
		int step = Math.round(gurr /(length+1));

		GraphEdge current;

		//TODO: make this configurable somehow



		while (i.hasNext()) {

			current = i.next();

			if (forceall || !current.equals(e)) {
				this.c.getGraphEdgeModifier().updateOffset(current,step * c,quiet,animated);

			}else{
				ret = step * c;
			}
			c++;

		}



		while (a.hasNext()) {

			current = a.next();

			if (forceall || !current.equals(e)) {

				if (current.getFrom().getFixedChildCount() != -1 && current.getFixedParentPos() != -1) {

					step = Math.round(gurr /(current.getFrom().getFixedChildCount()+1));
					c=current.getFixedParentPos();
					
					
					this.c.getGraphEdgeModifier().updateOffsetFrom(current,step * c,quiet,animated);
					

				}else{

					this.c.getGraphEdgeModifier().updateOffsetFrom(current,step * c,quiet,animated);
					c++;
				}

			}else{

				if (current.getFrom().getFixedChildCount() != -1 && current.getFixedParentPos() != -1) {

					step = Math.round(gurr /(current.getFrom().getFixedChildCount()+1));
									
					ret = step * current.getFixedParentPos();

				}else{

					ret = step * c;
					c++;

				}
			}


		}




		return ret;

	}

	private ArrayList<GraphEdge> getEdgesByOrientation(int orientation,ArrayList<GraphEdge> e) {

		Iterator<GraphEdge> i = e.iterator();

		ArrayList<GraphEdge> temp = new ArrayList<GraphEdge>();

		while (i.hasNext()) {

			GraphEdge current = i.next();

			if (current.getOrientation() == orientation) {
				temp.add(current);
			}

		}

		return temp;
	}

	private ArrayList<GraphEdge> getEdgesByOrientationFrom(int orientation,ArrayList<GraphEdge> e) {

		Iterator<GraphEdge> i = e.iterator();

		ArrayList<GraphEdge> temp = new ArrayList<GraphEdge>();

		while (i.hasNext()) {

			GraphEdge current = i.next();

			if (current.getOrientationFrom() == orientation) {
				temp.add(current);
			}

		}

		return temp;
	}

	protected void kill(GraphNode n) {

		n.getText().remove();
		n.getShape().remove();

		Iterator<ConnectedShape> it = n.getConnectedShapes().values().iterator();

		while (it.hasNext()) {

			ConnectedShape current = it.next();

			current.getShape().remove();

		}
	}

	protected void connectShapeToNode(String identifier,ConnectedShape s, GraphNode n) {

		n.getConnectedShapes().put(identifier, s);
	
		if (s.getShape() instanceof Circle) {
			
			
			s.getShape().attr("cx",n.getX() + s.getX());
			s.getShape().attr("cy",n.getY() + s.getY());	
			
		}else{
			
			
			s.getShape().attr("x",n.getX() + s.getX());
			s.getShape().attr("y",n.getY() + s.getY());	
			
		}
		
	}

	protected void removeShapeFromNode(String identifier,GraphNode n) {

		ConnectedShape s = n.getConnectedShapes().get(identifier);
		if (s != null ) {
			s.getShape().remove();
			n.getConnectedShapes().remove(identifier);
		}
		

	}

	public static MouseMoveHandler mouseMoveHandlerBuilder(final GraphNode n) {

		return new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {

				FullScreenDragPanel.preventDrag();

				if (!n.aniLock()) {

					JSONObject newAttrs = new JSONObject();

					newAttrs.put("stroke", new JSONString("#000"));

					n.getShape().animate(newAttrs, 100);


				}

			}
		};
	}


	public static MouseOutHandler mouseOutHandlerBuilder(final GraphNode n) {

		return new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {

				FullScreenDragPanel.unPreventDrag();

				if (!n.aniLock()) {

					JSONObject newAttrs = new JSONObject();
					newAttrs.put("stroke", new JSONString("#555"));
					n.getShape().animate(newAttrs, 100);

				}
			}

		};

	}


}
