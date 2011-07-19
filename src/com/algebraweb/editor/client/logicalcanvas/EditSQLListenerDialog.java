package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.core.client.GWT;

public class EditSQLListenerDialog extends AddSQListenerDIalog{
	
	

	private SQLBubble b;
	
	public EditSQLListenerDialog(SQLBubble b, RemoteManipulationServiceAsync rmsa, LogicalCanvas canvas) {
		
		super(b.getNid(), rmsa, canvas);
		this.b=b;
		super.getSerializationPanel().loadEvaluationContext(b.getEvaluationContext());

		
	}
	
	@Override
	protected void fillEvalContext() {

	}

	
	
	@Override
	protected void submit() {
		
		
		EvaluationContext c = new EvaluationContext();
		getSerializationPanel().fillEvaluationContext(c);
	
		b.setEvaluationContext(c);
		b.update();
		this.hide();
		
	}
	

}
