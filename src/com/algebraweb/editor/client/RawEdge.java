package com.algebraweb.editor.client;

import java.io.Serializable;



public class RawEdge implements Serializable {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int fromId;
	private int toId;

	public RawEdge(int fromId, int toId) {

		this.fromId = fromId;
		this.toId=toId;


	}
	
	public RawEdge() {
		
	}


	public int getFrom() {
		return fromId;
	}

	public void setFrom(int from) {
		this.fromId = from;
	}

	public int getTo() {
		return toId;
	}

	public void setTo(int to) {
		this.toId = to;
	}

}
