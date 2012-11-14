package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PlanNodeCopyMessage implements IsSerializable {

	private Tuple pos;
	private int id;

	public PlanNodeCopyMessage() {

	}

	public PlanNodeCopyMessage(int id, Tuple pos) {

		this.pos = pos;
		this.id = id;

	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the pos
	 */
	public Tuple getPos() {
		return pos;
	}

}
