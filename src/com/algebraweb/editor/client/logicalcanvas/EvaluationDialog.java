package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EvaluationDialog extends DialogBox {

	private RemoteManipulationServiceAsync manServ;
	
	private final SerializePanel sp;

	public EvaluationDialog(int pid, int nid, RemoteManipulationServiceAsync manServ) {

		super();
		super.setText("Evaluation");
		this.manServ=manServ;
		super.setAnimationEnabled(true);
		super.setModal(true);
		LayoutPanel p = new LayoutPanel();


		HorizontalPanel buttonsPanel = new HorizontalPanel();
		TabLayoutPanel t = new TabLayoutPanel(1.5, Unit.EM);
		t.setAnimationVertical(false);
		t.setAnimationDuration(500);
		t.setSize("550px", "350px");

		p.add(t);
		p.add(buttonsPanel);
		
		sp = new SerializePanel(pid,nid,manServ);
		
		
		manServ.getEvaluationContext(pid, nid, getContextCb);
		
		t.add(sp, "Serialization");


		Button cancelButton = new Button("Cancel");
		cancelButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				EvaluationDialog.this.hide();

			}
		});

		buttonsPanel.add(cancelButton);
		p.setWidgetBottomHeight(buttonsPanel, 0, Unit.PX, 50, Unit.PX);
		p.setSize("550px","400px");
		this.add(p);
		this.show();
		this.center();
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
