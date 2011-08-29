package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;

/**
 * The dialog for SQL listener editing
 * @author Patrick Brosi
 *
 */
public class EditSQLListenerDialog extends AddSQListenerDialog{

	private SQLBubble b;

	public EditSQLListenerDialog(SQLBubble b, RemoteManipulationServiceAsync rmsa, LogicalCanvas canvas) {
		super(b.getNid(), rmsa, canvas);
		this.b=b;
		super.getSerializationPanel().loadEvaluationContext(b.getEvaluationContext());
		super.getDatabaseConfigPanel().loadEvaluationContext(b.getEvaluationContext());
	}

	@Override
	protected void fillEvalContext() {

	}

	@Override
	protected void submit() {
		EvaluationContext c = saveContext();
		b.setEvaluationContext(c);
		b.update();
		this.hide();
	}	
}
