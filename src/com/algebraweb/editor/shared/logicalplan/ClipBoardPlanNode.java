package com.algebraweb.editor.shared.logicalplan;

import com.algebraweb.editor.client.graphcanvas.Tuple;
import com.algebraweb.editor.shared.node.PlanNode;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A PlanNode within the clip board. Holds an additional position.
 * 
 * @author Patrick Brosi
 * 
 */
public class ClipBoardPlanNode implements IsSerializable {
	private PlanNode p;
	private Tuple pos;

	public ClipBoardPlanNode() {

	}

	public ClipBoardPlanNode(PlanNode p, Tuple pos) {
		this.p = p;
		this.pos = pos;
	}

	/**
	 * Returns the plan node
	 * 
	 * @return the p
	 */
	public PlanNode getPlanNode() {
		return p;
	}

	/**
	 * Returns the position
	 * 
	 * @return the pos
	 */
	public Tuple getPos() {
		return pos;
	}
}