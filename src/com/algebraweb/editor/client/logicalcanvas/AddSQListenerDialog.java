package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.AlgebraEditorCanvasView;
import com.algebraweb.editor.client.RemoteManipulationServiceAsync;

public class AddSQListenerDialog extends EvaluationDialog{

	private AlgebraEditorCanvasView c;
	private int nid;

	public AddSQListenerDialog(int nid,RemoteManipulationServiceAsync manServ, AlgebraEditorCanvasView algebraEditorView) {
		super(algebraEditorView.getId(), nid, manServ);
		super.setText("Add SQL listener");
		this.nid=nid;
		this.c=algebraEditorView;
	}

	@Override
	protected void submit() {
		EvaluationContext c = saveContext();
		//TODO: not good...
		this.c.addSQLListener(nid,getManServ(),c);
		this.hide();
	}
}
