package com.algebraweb.editor.client.graphcanvas;

import java.util.HashMap;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A simple coordinate with x and y
 * @author Patrick Brosi
 *
 */

public class Coordinate implements IsSerializable{

	private double x;
	private double y;	

	private HashMap<String,Coordinate[]> paths;

	public Coordinate() {

	}

	public Coordinate(double x,double y) {
		this.x=x;
		this.y=y;
	}

	public HashMap<String,Coordinate[]> getPaths() {

		return paths;

	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public void setPaths(HashMap<String,Coordinate[]> paths) {

		this.paths=paths;

	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}


}
