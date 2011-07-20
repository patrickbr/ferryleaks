package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PlanNodeCopyMessage implements IsSerializable{

	private Coordinate pos;
	private int id;

	public PlanNodeCopyMessage() {

	}

	public PlanNodeCopyMessage(int id, Coordinate pos) {

		this.pos=pos;
		this.id=id;
			
	}

	/**
	 * @return the pos
	 */
	public Coordinate getPos() {
		return pos;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	

}
