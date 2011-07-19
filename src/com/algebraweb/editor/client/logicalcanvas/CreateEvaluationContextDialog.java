

package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;

public class CreateEvaluationContextDialog extends TabbedDialog{

	private RemoteManipulationServiceAsync manServ;
	private final SerializePanel sp;

	private int pid;
	private int nid;
	private CheckBox cb;


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

	protected SerializePanel getSerializationPanel() {
		return sp;
	}

	protected void fillEvalContext() {

		GWT.log("gurr");
		manServ.getEvaluationContext(pid, nid, getContextCb);

	}



	/**
	 * @return the manServ
	 */
	protected RemoteManipulationServiceAsync getManServ() {
		return manServ;
	}

	/**
	 * @return the pid
	 */
	protected int getPid() {
		return pid;
	}

	/**
	 * @return the nid
	 */
	protected int getNid() {
		return nid;
	}

	protected void submit() {


	}

	private AsyncCallback<EvaluationContext> getContextCb = new AsyncCallback<EvaluationContext>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onSuccess(EvaluationContext result) {

			sp.loadEvaluationContext(result);

		}

	};



}
