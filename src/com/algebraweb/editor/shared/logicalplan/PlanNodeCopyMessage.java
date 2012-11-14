package com.algebraweb.editor.shared.logicalplan;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A message providing information on how to put a node into the clipboard
 * 
 * @author Patrick Brosi
 * 
 */
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
		return this.id;
	}

	/**
	 * @return the pos
	 */
	public Tuple getPos() {
		return this.pos;
	}
}
