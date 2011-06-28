package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.PlanModelManipulator;
import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.TextArea;


public class NodeEditSourcePanel extends LayoutPanel {
	
	private TextArea editArea = new TextArea();
	private RemoteManipulationServiceAsync manServ;
	private PlanModelManipulator pmm;
	private int nid;
	
	public NodeEditSourcePanel(int nid,RemoteManipulationServiceAsync manServ,PlanModelManipulator pmm) {
		
		super();
		
		this.manServ = manServ;
		this.pmm=pmm;
		this.nid=nid;
		
		this.manServ.getXMLFromPlanNode(0, nid, xmlCb);
		editArea.addStyleName("sourceedit");
		super.add(editArea);
		
		
	}
	
	
	public void save() {
		
		//TODO: planid!
		
		pmm.updateNodeContent(0, nid, editArea.getText());
		
		
		
	}
	
	
	private AsyncCallback<String> xmlCb = new AsyncCallback<String>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(String result) {
			editArea.setText(result);
			
		}
		
	};
	

}
