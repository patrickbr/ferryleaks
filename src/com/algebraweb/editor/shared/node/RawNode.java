package com.algebraweb.editor.shared.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A lightweight object holding drawing information for nodes. Is used for
 * transfers.
 *
 * @author Patrick Brosi
 *
 */
public class RawNode implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 2613849213898458764L;
	private int nid;
	private String text;
	private int color;
	private int fixedChildCount = -1;
	private int width;
	private int height;
	private List<RawEdge> edgesTo = new ArrayList<RawEdge>();

	public RawNode() {

	}

	public RawNode(int id) {
		this.nid = id;
	}

	public RawNode(int nid, String text, int color, int width, int height) {
		this.nid = nid;
		this.text = text;
		this.color = color;
		this.width = width;
		this.height = height;
	}

	/**
	 * Returns the node's color
	 *
	 * @return the color as a hex int
	 */
	public int getColor() {
		return color;
	}

	/**
	 * Returns the list of edges pointing to this node
	 *
	 * @return the list of edges
	 */
	public List<RawEdge> getEdgesToList() {
		return edgesTo;
	}

	/**
	 * Returns the fixed child count of this node
	 *
	 * @return the fixedChildCount
	 */
	public int getFixedChildCount() {
		return fixedChildCount;
	}

	public int getHeight() {
		return height;
	}

	public int getNid() {
		return nid;
	}

	public String getText() {
		return text;
	}

	public int getWidth() {
		return width;
	}

	/**
	 * Sets the color of this node.
	 *
	 * @param color
	 *            the color as a hex integer.
	 */
	public void setColor(int color) {
		this.color = color;
	}

	/**
	 * Sets the number of children this node has to hold.
	 *
	 * @param fixedChildCount
	 *            the fixedChildCount to set
	 */
	public void setFixedChildCount(int fixedChildCount) {
		this.fixedChildCount = fixedChildCount;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setNid(int nid) {
		this.nid = nid;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setWidth(int width) {
		this.width = width;
	}
}