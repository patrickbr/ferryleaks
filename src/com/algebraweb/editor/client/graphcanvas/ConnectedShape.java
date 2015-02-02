package com.algebraweb.editor.client.graphcanvas;

import com.hydro4ge.raphaelgwt.client.Raphael.Shape;

/**
 * A shape connected to any given node. Any instance of this class can be given
 * to an actual node on the canvas. It will be moved, animated and removed
 * according to its node parent.
 * 
 * @author Patrick Brosi
 * 
 */

public class ConnectedShape {
	private int x;
	private int y;
	private Shape shape;

	/**
	 * @param shape
	 * @param x
	 * @param y
	 */

	public ConnectedShape(Shape shape, int x, int y) {
		this.x = x;
		this.y = y;
		this.shape = shape;
	}

	public Shape getShape() {
		return shape;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}