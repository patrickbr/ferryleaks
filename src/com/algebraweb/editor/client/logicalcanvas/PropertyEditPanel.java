package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.node.ContentVal;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.Value;
import com.algebraweb.editor.client.validation.ValidationError;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;

public class PropertyEditPanel extends Composite{


	private NodeContent c;
	private GoAble scheme;

	private ContentNodeTreeItem treeItem;

	protected RemoteManipulationServiceAsync manServ;
	private FlowPanel p = new FlowPanel();
	private ArrayList<PropertyEditField> fields = new ArrayList<PropertyEditField>();

	public PropertyEditPanel(int pid, ContentNodeTreeItem treeItem, RemoteManipulationServiceAsync manServ,NodeContent c, GoAble scheme,PlanNode nodeContext) {

		this.c=c;
		this.scheme=scheme;
		this.treeItem=treeItem;

		this.manServ=manServ;

		HTML title= new HTML("Edit " + this.c.getInternalName());



		title.addStyleName("content-edit-panel-title");
		p.addStyleName("content-edit-panel");

		p.add(title);

		if (treeItem.getErrors() != null) {

			HTML errorP = new HTML();
			String tmpRet ="";
			Iterator<ValidationError> i = treeItem.getErrors().iterator();
			while (i.hasNext()) tmpRet += i.next().getErrorMsg() + "<br>";

			errorP.addStyleName("error-msg-edit-panel");
			errorP.setHTML(tmpRet);

			p.add(errorP);

		}

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

					f = new PropertyEditFieldAvailableColumns(pid,nodeContext.getId(), manServ, current);


				}else{

					f = new PropertyEditTextField(current);
				}

				f.bindToPropertyVal(pv);
				if (current.hasMustBe()) f.setLocked(true);

				fields.add(f);
				p.add(f);


			}

		}
		
		if (this.scheme instanceof Value && c instanceof ContentVal && ((Value)this.scheme).hasVal()) {
			
			PropertyEditField f;
			f = new PropertyEditTextField("Value");
			f.bindToPropertyVal(((ContentVal)c).getValue());
			fields.add(f);
			p.add(f);
			
		}
		
	
		initWidget(p);

	}

	public void save() {

		Iterator<PropertyEditField> it = fields.iterator();

		while (it.hasNext()) {
			it.next().save();
		}



	}



}
