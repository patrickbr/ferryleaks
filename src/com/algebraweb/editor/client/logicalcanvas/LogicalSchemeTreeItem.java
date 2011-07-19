package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.RemoteManipulationService;
import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFillingService;
import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.ContentVal;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.node.ValGroup;
import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.GoInto;
import com.algebraweb.editor.client.scheme.NodeScheme;
import com.algebraweb.editor.client.scheme.Value;
import com.algebraweb.editor.client.validation.ValidationError;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TreeItem;

public class LogicalSchemeTreeItem extends NodeTreeItem{



	protected GoAble scheme;
	protected ContentNode content;
	protected RemoteManipulationServiceAsync manServ;


	public LogicalSchemeTreeItem(RemoteManipulationServiceAsync manServ,GoAble scheme, ContentNode content) {

		this.scheme=scheme;
		this.content=content;
		this.manServ = manServ;


		this.setWidget(new HTML("<b>" + scheme.getHumanName() + "</b>"));
		processItems();

		super.setState(true,true);

	}


	@Override
	public void setSelected(boolean selected) {

		if (selected) {
			this.getWidget().getElement().getStyle().setBackgroundColor("#CCC");
		}else{
			this.getWidget().getElement().getStyle().setBackgroundColor("");
		}

	}


	public void addNewEmptyItem() {


		NodeContent n = null;

		if (scheme instanceof Value) {

			n = new ContentVal(((Value)scheme).getValName(),((Value)scheme).getXmlObject(),null);


			Iterator<Field> it = scheme.getFields().iterator();

			while (it.hasNext()) {

				Field current = it.next();

				if (current.hasMustBe()) {
					((ContentVal)n).getAttributes().put(current.getVal(), new PropertyValue(current.getMust_be(), current.getType()));
				}else{
					((ContentVal)n).getAttributes().put(current.getVal(), new PropertyValue("(" + current.getType() + ")", current.getType()));

				}

			}

		}else if (scheme instanceof GoInto) {

			n = new ValGroup(scheme.getXmlObject());

		}


		this.content.getContent().add(n);

		
		addContent(n);
		this.setState(true);
	}



	private void processItems() {


		if (scheme instanceof Value || scheme instanceof GoInto) {

			Iterator<NodeContent> it = content.getDirectNodeContentByScheme(scheme).iterator();


			while (it.hasNext()) {

				NodeContent current = it.next();
				addContent(current);

			}


		}else if (scheme instanceof NodeScheme){


			Iterator<GoAble> schemas = scheme.getSchema().iterator();

			while (schemas.hasNext()) {

				GoAble currentSchema = schemas.next();

				if (currentSchema.isEditable()) this.addItem(new LogicalSchemeTreeItem(manServ,currentSchema, content));


			}


		}
	}

	public boolean deleteContent(ContentNodeTreeItem current) {
		
		boolean success = this.content.getContent().remove(current.getContentNode());
		
		current.remove();
		
		
		return success;
		
		
	}

	private TreeItem addContent(NodeContent current) {
		NodeTreeItem cntt = new ContentNodeTreeItem(manServ,current,scheme);

		Iterator<GoAble> schemas = scheme.getSchema().iterator();

		while (schemas.hasNext()) {

			GoAble currentSchema = schemas.next();

			cntt.addItem(new LogicalSchemeTreeItem(manServ,currentSchema, current));
			cntt.setState(true, true);

		}

		this.addItem(cntt);
		return cntt;
		
	}

	public GoAble getScheme() {
		return scheme;
	}








}
