package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.VerticalPanel;

public class PropertyEditPanel extends Composite{


	private NodeContent c;
	private GoAble scheme;
	
	private ContentNodeTreeItem treeItem;
	private int pid;

	protected RemoteManipulationServiceAsync manServ;
	private FlowPanel p = new FlowPanel();
	private ArrayList<PropertyEditField> fields = new ArrayList<PropertyEditField>();

	public PropertyEditPanel(int pid, ContentNodeTreeItem treeItem, RemoteManipulationServiceAsync manServ,NodeContent c, GoAble scheme,PlanNode nodeContext) {

		this.c=c;
		this.pid=pid;
		this.scheme=scheme;
		this.treeItem=treeItem;
		
		this.manServ=manServ;

		HTML title= new HTML("Edit " + this.c.getInternalName());


		title.addStyleName("content-edit-panel-title");
		p.addStyleName("content-edit-panel");

		p.add(title);
		
		Button deleteButton = new Button("X");
		
		deleteButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {

				((LogicalSchemeTreeItem)PropertyEditPanel.this.treeItem.getParentItem()).deleteContent(PropertyEditPanel.this.treeItem);

				
			}
			
		});
		
		deleteButton.addStyleName("delete-button");
		p.add(deleteButton);
		
		

		if (this.scheme.hasFields()) {
			Iterator<Field> it = scheme.getFields().iterator();

			while (it.hasNext()) {

				Field current = it.next();

				PropertyValue pv;

				if (c.getAttributes().containsKey(current.getVal())) {

					pv=c.getAttributes().get(current.getVal());

				}else{

					pv=c.getAttributes().put(current.getVal(), new PropertyValue("", current.getType()));

				}

				PropertyEditField f;

				if (current.getType().matches("__COLUMN[\\{]?[0-9]*[\\}]?") || current.getType().equals("__COLUMN_REMOVE")) {

					f = new PropertyEditFieldGivenValues(current);

					manServ.getReferencableColumnsWithoutAdded(nodeContext.getId(), pid, refColsLoadCallBack((PropertyEditFieldGivenValues)f,pv));


				}else{

					f = new PropertyEditField(current);
					f.bindToPropertyVal(pv);
					f.drawField();

				}





				if (current.hasMustBe()) f.setLocked(true);

				fields.add(f);
				p.add(f);


			}



		}




		initWidget(p);



	}

	public void save() {

		GWT.log("Saving panel");

		Iterator<PropertyEditField> it = fields.iterator();

		while (it.hasNext()) {
			it.next().save();
		}



	}
	


	private AsyncCallback<ArrayList<Property>> refColsLoadCallBack(final PropertyEditFieldGivenValues field,final PropertyValue pv) {



		return new AsyncCallback<ArrayList<Property>>() {


			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(ArrayList<Property> result) {


				field.setPossibleValues(result);
				field.drawField();
				field.bindToPropertyVal(pv);


			}

		};

	}




}
