package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RawEdge implements IsSerializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5343591495878501668L;
	int to;
	int from;
	double[] anchors;
	
	public RawEdge() {
		
	}
	
	public RawEdge(int to, int from) {
		this.to=to;
		this.from=from;
	}
	
	public int getTo() {
		return to;
	}
	
	public int getFrom() {
		return from;
	}
	
	public void setAnchors(double[] anchors) {
		this.anchors=anchors;
	}
	
	public double[] getAnchors() {
		
		return anchors;
	}

}
