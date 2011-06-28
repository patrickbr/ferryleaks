package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.scheme.GoAble;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class SchemeEditPanel extends Composite{
	
	private LogicalSchemeTreeItem treeItem;
	private RemoteManipulationServiceAsync manServ;
	private NodeContent c;
	private GoAble scheme;
	
	private VerticalPanel p = new VerticalPanel();
	
	
	//TODO: propertyeditpanel and this should have a common subclass
	public SchemeEditPanel(LogicalSchemeTreeItem treeItem, RemoteManipulationServiceAsync manServ, GoAble scheme,PlanNode nodeContext) { 
		
		
		this.treeItem = treeItem;
		this.manServ=manServ;
		this.c=c;
		this.scheme=scheme;
		
		HTML title= new HTML(this.scheme.getHumanName());


		title.addStyleName("content-edit-panel-title");
		p.addStyleName("content-edit-panel");

		p.add(title);
		
		Button b = new Button("Add content");
		
		b.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				SchemeEditPanel.this.treeItem.addNewEmptyItem();
				
			}
			
		});
		
		p.add(b);
		
		initWidget(p);
	}

}
