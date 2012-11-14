package com.algebraweb.editor.shared.node;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A lightweight object holding edge information for drawing. Will be used for
 * transfers
 * 
 * @author Patrick Brosi
 * 
 */
public class RawEdge implements IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5343591495878501668L;
	int to;
	int from;
	int fixedParentPos = -1;
	double[] anchors;

	public RawEdge() {

	}

	public RawEdge(int to, int from) {
		this.to = to;
		this.from = from;
	}

	public double[] getAnchors() {
		return anchors;
	}

	/**
	 * Returns the parent position of this edge
	 * 
	 * @return the fixedParentPos
	 */
	public int getFixedParentPos() {
		return fixedParentPos;
	}

	/**
	 * Returns the id of the from-node
	 * 
	 * @return the nid
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * Returns the id of the to-node
	 * 
	 * @return the nid
	 */
	public int getTo() {
		return to;
	}

	/**
	 * Could later be used to set anchor points. NOT IMPLEMENTED YET!
	 * 
	 * @param anchors
	 */
	public void setAnchors(double[] anchors) {
		this.anchors = anchors;
	}

	/**
	 * Sets the fixed parent pos of this node
	 * 
	 * @param fixedParentPos
	 *            the fixedParentPos to set
	 */
	public void setFixedParentPos(int fixedParentPos) {
		this.fixedParentPos = fixedParentPos;
	}

}
