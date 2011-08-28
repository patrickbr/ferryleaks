package com.algebraweb.editor.shared.logicalplan;

import com.algebraweb.editor.client.graphcanvas.Coordinate;
import com.algebraweb.editor.shared.node.PlanNode;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A PlanNode within the clip board. Holds an additional position.
 * @author Patrick Brosi
 *
 */
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
	 * Returns the plan node
	 * @return the p
	 */
	public PlanNode getPlanNode() {
		return p;
	}

	/**
	 * Returns the position
	 * @return the pos
	 */
	public Coordinate getPos() {
		return pos;
	}
}
