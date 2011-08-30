package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.graphcanvas.GraphCanvasErrorDialogBox;
import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

/**
 * The dialog for evaluation context creation.
 * 
 * @author Patrick Brosi
 * 
 */
public class CreateEvaluationContextDialog extends TabbedDialog {

	private RemoteManipulationServiceAsync manServ;
	private final SerializePanel sp;
	private EvaluationContext loadedContext = new EvaluationContext();
	private int pid;
	private int nid;
	private AsyncCallback<EvaluationContext> getContextCb = new AsyncCallback<EvaluationContext>() {
		@Override
		public void onFailure(Throwable caught) {
			new GraphCanvasErrorDialogBox(caught.getMessage());
		}

		@Override
		public void onSuccess(EvaluationContext result) {
			loadedContext = result;
			processContextResult(result);
		}
	};

	public CreateEvaluationContextDialog(int pid, int nid,
			RemoteManipulationServiceAsync manServ) {
		super();
		super.setText("Evaluation");
		this.manServ = manServ;
		this.pid = pid;
		this.nid = nid;
		sp = new SerializePanel(pid, nid, manServ);
		fillEvalContext();
		addTab(sp, "Serialization");

		Button cancelButton = new Button("Cancel");
		cancelButton.addStyleName("cancelbutton");
		cancelButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CreateEvaluationContextDialog.this.hide();

			}
		});

		Button okButton = new Button("Ok");
		okButton.addStyleName("okbutton");
		okButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				CreateEvaluationContextDialog.this.submit();

			}
		});

		addButton(okButton);
		addButton(cancelButton);
	}

	protected void fillEvalContext() {
		manServ.getEvaluationContext(pid, nid, getContextCb);
	}

	public EvaluationContext getLoadedContext() {
		return loadedContext;
	}

	/**
	 * Get the manipulation service
	 * 
	 * @return the manServ
	 */
	protected RemoteManipulationServiceAsync getManServ() {
		return manServ;
	}

	/**
	 * Return the node id this evaluation context is for
	 * 
	 * @return the nid
	 */
	protected int getNid() {
		return nid;
	}

	/**
	 * Return the plan id this evaluation context is for
	 * 
	 * @return the pid
	 */
	protected int getPid() {
		return pid;
	}

	/**
	 * Returns the serialize panel used by this dialog
	 * 
	 * @return the serialize panel
	 */
	protected SerializePanel getSerializationPanel() {
		return sp;
	}

	/**
	 * Loads the evaluation context <i>result</i> into the dialog
	 * 
	 * @param result
	 *            the evContext to load
	 */
	protected void processContextResult(EvaluationContext result) {
		sp.loadEvaluationContext(result);
	}

	/**
	 * Saves the dialog to the loaded evaluation context
	 * 
	 * @return the re-filled evaluation context
	 */
	protected EvaluationContext saveContext() {
		EvaluationContext c = getLoadedContext();
		sp.fillEvaluationContext(c);
		return c;
	}

	protected void submit() {
		hide();
	}
}
