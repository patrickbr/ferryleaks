package com.algebraweb.editor.client.graphcanvas;

import com.hydro4ge.raphaelgwt.client.Raphael.Shape;


public class ConnectedShape {


	private int x;
	private int y;
	private Shape shape;
	
	public ConnectedShape(Shape shape, int x, int y) {
		
		this.x=x;
		this.y=y;
		this.shape=shape;
		
	}

	public int getX() {
		return x;
	}


	public int getY() {
		return y;
	}


	public Shape getShape() {
		return shape;
	}
	

}
