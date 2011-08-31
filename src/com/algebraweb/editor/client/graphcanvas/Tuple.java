package com.algebraweb.editor.client.graphcanvas;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A simple tuple with x and y
 * 
 * @author Patrick Brosi
 * 
 */

public class Tuple implements IsSerializable {

	private double x;
	private double y;

	public Tuple() {

	}

	public Tuple(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

}
