package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;

public class EditSQLListenerDialog extends AddSQListenerDIalog{
	
	

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
