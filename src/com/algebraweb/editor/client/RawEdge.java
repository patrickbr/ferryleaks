package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RawEdge implements IsSerializable  {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5343591495878501668L;
	int to;
	int from;
	int fixedParentPos=-1;
	double[] anchors;
	
	public RawEdge() {
		
	}
	
	public RawEdge(int to, int from) {
		this.to=to;
		this.from=from;
	}
	
	public double[] getAnchors() {
		
		return anchors;
	}
	
	/**
	 * @return the fixedParentPos
	 */
	public int getFixedParentPos() {
		return fixedParentPos;
	}
	
	public int getFrom() {
		return from;
	}
	
	public int getTo() {
		return to;
	}

	public void setAnchors(double[] anchors) {
		this.anchors=anchors;
	}

	/**
	 * @param fixedParentPos the fixedParentPos to set
	 */
	public void setFixedParentPos(int fixedParentPos) {
		this.fixedParentPos = fixedParentPos;
	}
	
}
