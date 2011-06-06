package com.algebraweb.editor.client;


import java.io.Serializable;
import java.util.ArrayList;



public class RawNode implements Serializable  {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2613849213898458764L;
	private int nid;
	private String text;
	private int color;
	
	private int width;
	private int height;
	
	private ArrayList<RawEdge> edgesTo = new ArrayList<RawEdge>();
	
	
	public RawNode(int nid, String text, int color, int width, int height) {
		
		this.nid=nid;
		this.text=text;
		this.color=color;
		this.width=width;
		this.height=height;
				
	}
	
	public RawNode() {
		
	}


	public int getNid() {
		return nid;
	}


	public void setNid(int nid) {
		this.nid = nid;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public int getColor() {
		return color;
	}


	public void setColor(int color) {
		this.color = color;
	}


	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}


	public int getHeight() {
		return height;
	}


	public void setHeight(int height) {
		this.height = height;
	}
	
	public ArrayList<RawEdge> getEdgesToList() {
		return edgesTo;
	}
	
	

}
