package com.algebraweb.editor.client;

import java.io.Serializable;
import java.util.ArrayList;

/**
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

	private ArrayList<RawEdge> edgesTo = new ArrayList<RawEdge>();

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

	public int getColor() {
		return color;
	}

	public ArrayList<RawEdge> getEdgesToList() {
		return edgesTo;
	}

	/**
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

	public void setColor(int color) {
		this.color = color;
	}

	/**
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
