package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.AlgebraEditorCanvasView;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;

/**
 * The dialog for adding SQL listeners.
 * 
 * @author Patrick Brosi
 * 
 */
public class AddSQListenerDialog extends EvaluationDialog {

	private AlgebraEditorCanvasView c;
	private int nid;

	public AddSQListenerDialog(int nid, RemoteManipulationServiceAsync manServ,
			AlgebraEditorCanvasView algebraEditorView) {
		super(algebraEditorView.getId(), nid, manServ);
		super.setText("Add SQL listener");
		this.nid = nid;
		this.c = algebraEditorView;
	}

	@Override
	protected void submit() {
		EvaluationContext c = saveContext();
		this.c.addSQLListener(nid, c);
		this.hide();
	}
}
