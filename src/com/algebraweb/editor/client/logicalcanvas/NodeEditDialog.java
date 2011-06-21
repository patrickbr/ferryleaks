package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.PlanModelManipulator;
import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.validation.ValidationError;
import com.algebraweb.editor.client.validation.ValidationResult;
import com.google.gwt.cell.client.TextCell;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.HorizontalSplitPanel;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Tree;
import com.google.gwt.user.client.ui.TreeItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.TreeViewModel;

public class NodeEditDialog extends DialogBox{
	
	
	
	private RemoteManipulationServiceAsync manServ;
	private PlanModelManipulator pmm;
	private int nid;
	private int pid;
	
	private NodeEditTree tree = new NodeEditTree();
	private PlanNode node = null;
	private Widget editPanelWidget=null;
	
	private HashMap<TreeItem,PropertyEditPanel> panels = new HashMap<TreeItem,PropertyEditPanel>();
	
	HorizontalSplitPanel p = new HorizontalSplitPanel();
    VerticalPanel v = new VerticalPanel();
    HorizontalPanel h = new HorizontalPanel();
	
	public NodeEditDialog(PlanModelManipulator pmm,RemoteManipulationServiceAsync manServ, int nid, int pid) {
		
		super();
		super.setAnimationEnabled(true);
		super.setGlassEnabled(true);
		this.manServ=manServ;
		this.pmm=pmm;
		this.nid=nid;
		this.pid=pid;
		super.setText("Edit node #" + nid);

	    
	    h.add(new Button("+"));
	    	    
	 	    
	    
	    p.setSize("500px", "300px");
	    
	
	    p.add(tree);
	    
	    tree.addSelectionHandler(new SelectionHandler<TreeItem>() {
			
			@Override
			public void onSelection(SelectionEvent<TreeItem> event) {
				
				if (event.getSelectedItem() instanceof ContentNodeTreeItem) {
					
					if (panels.containsKey(event.getSelectedItem())) {
						
						NodeEditDialog.this.setEditWidget(panels.get(event.getSelectedItem()));
						
					}else{
						
					PropertyEditPanel p = new PropertyEditPanel(((ContentNodeTreeItem)event.getSelectedItem()).getContentNode(),((ContentNodeTreeItem)event.getSelectedItem()).getScheme());
					panels.put(event.getSelectedItem(),p);
					
					NodeEditDialog.this.setEditWidget(p);
					
					}
					
				}
				
				
			}
		});
	    
	    v.add(h);
	    v.add(p);
	    
	    Button ok = new Button("Save");
	    Button cancel = new Button("cancel");
	    HorizontalPanel hh = new HorizontalPanel();
	    
	    cancel.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				NodeEditDialog.this.hide();
				
			}
		});
	    
	    ok.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				NodeEditDialog.this.save();
				
			}
		});
	    
	    hh.add(ok);
	    hh.add(cancel);
	    v.add(hh);
		
		super.add(v);
		super.show();
		super.center();
		
		load();
				
	}
	
	
	
	private void load() {
		
		
		manServ.getPlanNode(nid,pid, nodeLoadCallBack);
		
		
		
	}
	
	public void setEditWidget(Widget w) {
		
		clearEditWidget();
		p.add(w);
	
		editPanelWidget = w;
		
		
		
	}
	
	public void clearEditWidget() {
		
		
		if (editPanelWidget != null) p.remove(editPanelWidget);
		editPanelWidget = null;
		
	}
	
	public void save() {
		
		
		Iterator<PropertyEditPanel> it = panels.values().iterator();
		
		while (it.hasNext()) {
			
			it.next().save();
			
		}
		
		//TODO!!!: plan id!!
		
		pmm.updateNodeContent(0, node);
		
		
		this.hide();
		
		
	}
	
	//TODO: this should be in the PlanModelManipulator
	
	private AsyncCallback<PlanNode> nodeLoadCallBack = new AsyncCallback<PlanNode>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onSuccess(PlanNode result) {

			node = result;
			
			Iterator<GoAble> schemas = result.getScheme().getSchema().iterator();

			while (schemas.hasNext()) {

				GoAble currentSchema = schemas.next();


				tree.addItem(new LogicalSchemeTreeItem(manServ,currentSchema, result));


			}
						

		}

	};
	

	

	
	

}
