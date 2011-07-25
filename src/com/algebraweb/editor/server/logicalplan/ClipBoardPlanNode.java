package com.algebraweb.editor.server.logicalplan;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.client.node.PlanNode;
import com.google.gwt.user.client.rpc.IsSerializable;

public class ClipBoardPlanNode implements IsSerializable{

	private PlanNode p;
	private Coordinate pos;

	public ClipBoardPlanNode() {

	}

	public ClipBoardPlanNode(PlanNode p, Coordinate pos) {

		this.p=p;
		this.pos=pos;

	}

	/**
	 * @return the p
	 */
	public PlanNode getP() {
		return p;
	}

	/**
	 * @return the pos
	 */
	public Coordinate getPos() {
		return pos;
	}



}
