package com.algebraweb.editor.client;

import java.io.Serializable;



public class RawNode implements Serializable  {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2613849213898458764L;
	private int nid;
	private String text;
	private String color;
	
	private int width;
	private int height;
	
	
	public RawNode(int nid, String text, String color, int width, int height) {
		
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


	public String getColor() {
		return color;
	}


	public void setColor(String color) {
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
	
	

}
