

package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.graphcanvas.GraphCanvasErrorDialogBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

public class CreateEvaluationContextDialog extends TabbedDialog{

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


	public CreateEvaluationContextDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {


		super();
		super.setText("Evaluation");
		this.manServ=manServ;

		this.pid=pid;
		this.nid=nid;

		sp = new SerializePanel(pid,nid,manServ);

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
	 * @return the manServ
	 */
	protected RemoteManipulationServiceAsync getManServ() {
		return manServ;
	}

	/**
	 * @return the nid
	 */
	protected int getNid() {
		return nid;
	}

	/**
	 * @return the pid
	 */
	protected int getPid() {
		return pid;
	}

	protected SerializePanel getSerializationPanel() {
		return sp;
	}

	protected void processContextResult(EvaluationContext result) {

		sp.loadEvaluationContext(result);

	}


	protected EvaluationContext saveContext() {

		EvaluationContext c = getLoadedContext();
		sp.fillEvaluationContext(c);
		return c;

	}

	protected void submit() {



	}


}
