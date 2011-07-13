package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;

public class CreateXMLDialog extends TabbedDialog{

	private RemoteManipulationServiceAsync manServ;
	private final SerializePanel sp;
	
	private int pid;
	private int nid;
	

	public CreateXMLDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {


		super();
		super.setText("Evaluation");
		this.manServ=manServ;
		
		this.pid=pid;
		this.nid=nid;

		sp = new SerializePanel(pid,nid,manServ);

		manServ.getEvaluationContext(pid, nid, getContextCb);

		addTab(sp, "Serialization");

		Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CreateXMLDialog.this.hide();

			}
		});
		
		Button okButton = new Button("Ok");
		okButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				CreateXMLDialog.this.submit();

			}
		});

		addButton(okButton);
		addButton(cancelButton);


	}
	
	protected SerializePanel getSerializationPanel() {
		return sp;
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
		
		
		EvaluationContext c = new EvaluationContext();
		
		sp.fillEvaluationContext(c);
		
		manServ.getXMLLogicalPlanFromRootNode(pid, nid, c, xmlCb);
		
		
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
	
	private AsyncCallback<String> xmlCb = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(String result) {

			Window.alert(result);

		}

	};


}
