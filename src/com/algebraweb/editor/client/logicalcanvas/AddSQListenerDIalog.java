package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;

public class AddSQListenerDIalog extends EvaluationDialog{

	private LogicalCanvas c;
	private int nid;

	public AddSQListenerDIalog(int nid,RemoteManipulationServiceAsync manServ, LogicalCanvas c) {
		super(c.getId(), nid, manServ);
		super.setText("Add SQL listener");
		this.nid=nid;
		
		this.c=c;
	
	}
	
	
	@Override
	protected void submit() {
		
		
		EvaluationContext c = saveContext();
		
		this.c.addSQLListener(nid,getManServ(),c);
		this.hide();
		
	}
	
	

}
