package com.algebraweb.editor.client.logicalcanvas;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.client.RemoteManipulationService;
import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.client.graphcanvas.remotefiller.RemoteFillingService;
import com.algebraweb.editor.client.node.ContentNode;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.ValGroup;
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


		this.setWidget(new HTML("<b><i>" + scheme.getHumanName() + "</b></i>"));
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



	private void processItems() {


		if (scheme instanceof Value || scheme instanceof GoInto) {




			Iterator<NodeContent> it = content.getDirectNodeContentByScheme(scheme).iterator();


			while (it.hasNext()) {

				NodeContent current = it.next();


				NodeTreeItem cntt = new ContentNodeTreeItem(manServ,current,scheme);

				Iterator<GoAble> schemas = scheme.getSchema().iterator();

				while (schemas.hasNext()) {

					GoAble currentSchema = schemas.next();

					cntt.addItem(new LogicalSchemeTreeItem(manServ,currentSchema, current));
					cntt.setState(true, true);

				}

				this.addItem(cntt);


			}


		}else if (scheme instanceof NodeScheme){


			Iterator<GoAble> schemas = scheme.getSchema().iterator();

			while (schemas.hasNext()) {

				GoAble currentSchema = schemas.next();


				this.addItem(new LogicalSchemeTreeItem(manServ,currentSchema, content));


			}


		}/**else{


			Iterator<GoAble> schemas = scheme.getSchema().iterator();

			while (schemas.hasNext()) {

				GoAble currentSchema = schemas.next();

				Iterator<NodeContent> it = content.getDirectNodeContentByScheme(scheme).iterator();

				while (it.hasNext()) {

					this.addItem(new LogicalSchemeTreeItem(currentSchema, it.next()));
				}

			}

		}**/
	}







}
