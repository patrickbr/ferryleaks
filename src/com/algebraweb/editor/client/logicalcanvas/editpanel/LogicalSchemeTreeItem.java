package com.algebraweb.editor.client.logicalcanvas.editpanel;

import java.util.Iterator;

import com.algebraweb.editor.client.services.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.node.ContentNode;
import com.algebraweb.editor.shared.node.ContentVal;
import com.algebraweb.editor.shared.node.LabelAttrIdentifierOb;
import com.algebraweb.editor.shared.node.LabelContentIdentifierOb;
import com.algebraweb.editor.shared.node.LabelOb;
import com.algebraweb.editor.shared.node.LabelStringOb;
import com.algebraweb.editor.shared.node.NodeContent;
import com.algebraweb.editor.shared.node.PropertyValue;
import com.algebraweb.editor.shared.node.ValGroup;
import com.algebraweb.editor.shared.scheme.Field;
import com.algebraweb.editor.shared.scheme.GoAble;
import com.algebraweb.editor.shared.scheme.GoInto;
import com.algebraweb.editor.shared.scheme.NodeScheme;
import com.algebraweb.editor.shared.scheme.Value;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TreeItem;

public class LogicalSchemeTreeItem extends NodeTreeItem {
	protected GoAble scheme;
	protected ContentNode content;
	protected RemoteManipulationServiceAsync manServ;

	public LogicalSchemeTreeItem(RemoteManipulationServiceAsync manServ,
			GoAble scheme, ContentNode content) {

		this.scheme = scheme;
		this.content = content;
		this.manServ = manServ;

		this.setWidget(new HTML("<b>" + scheme.getHumanName() + "</b>"));
		processItems();

		super.setState(true, true);

	}

	private TreeItem addContent(NodeContent current) {
		NodeTreeItem cntt = new ContentNodeTreeItem(manServ, current, scheme);

		Iterator<GoAble> schemas = scheme.getSchema().iterator();

		while (schemas.hasNext()) {

			GoAble currentSchema = schemas.next();

			cntt.addItem(new LogicalSchemeTreeItem(manServ, currentSchema,
					current));
			cntt.setState(true, true);

		}

		this.addItem(cntt);
		return cntt;

	}

	public void addNewEmptyItem() {

		NodeContent n = null;

		if (scheme instanceof Value) {

			n = new ContentVal(((Value) scheme).getValName(), ((Value) scheme)
					.getXmlObject(), null);

			Iterator<Field> it = scheme.getFields().iterator();

			while (it.hasNext()) {

				Field current = it.next();

				if (current.hasMustBe()) {
					((ContentVal) n).getAttributes().put(
							current.getName(),
							new PropertyValue(current.getMust_be(), current
									.getType()));
				} else {
					((ContentVal) n).getAttributes().put(
							current.getName(),
							new PropertyValue("(" + current.getType() + ")",
									current.getType()));
				}

			}

			if (((Value) scheme).hasVal()) {
				((ContentVal) n)
						.setValue(new PropertyValue("(null)", "string"));
			}

			parseLabelSchema(n, ((Value) scheme).getNameToPrint());

		} else if (scheme instanceof GoInto) {
			n = new ValGroup(scheme.getXmlObject());
		}

		this.content.getContent().add(n);

		addContent(n);
		this.setState(true);
	}

	public boolean deleteContent(ContentNodeTreeItem current) {

		boolean success = this.content.getContent().remove(
				current.getContentNode());

		current.remove();

		return success;

	}

	public GoAble getScheme() {
		return scheme;
	}

	private void parseLabelSchema(NodeContent retEl, String schema) {
		LabelOb c = new LabelStringOb("");

		for (int i = 0; i < schema.length(); i++) {

			if (schema.substring(i, i + 1).equals("{")) {

				if (c != null) {
					retEl.addLabelOb(c);
				}
				c = new LabelContentIdentifierOb("");

			} else if (schema.substring(i, i + 1).equals("}")
					|| schema.substring(i, i + 1).equals("]")) {

				if (c != null) {
					retEl.addLabelOb(c);
				}
				c = new LabelStringOb("");

			} else if (schema.substring(i, i + 1).equals("[")) {

				if (c != null) {
					retEl.addLabelOb(c);
				}
				c = new LabelAttrIdentifierOb("");

			} else {

				c.addChar(schema.substring(i, i + 1));

			}

		}

		if (!(c == null) && !c.getVal().equals("")) {
			retEl.addLabelOb(c);
		}
	}

	private void processItems() {

		if (scheme instanceof Value || scheme instanceof GoInto) {

			Iterator<NodeContent> it = content.getDirectNodeContentByScheme(
					scheme).iterator();

			while (it.hasNext()) {

				NodeContent current = it.next();
				addContent(current);

			}

		} else if (scheme instanceof NodeScheme) {

			Iterator<GoAble> schemas = scheme.getSchema().iterator();

			while (schemas.hasNext()) {

				GoAble currentSchema = schemas.next();

				if (currentSchema.isEditable()) {
					this.addItem(new LogicalSchemeTreeItem(manServ,
							currentSchema, content));
				}

			}

		}
	}

	@Override
	public void setSelected(boolean selected) {

		if (selected) {
			this.getWidget().getElement().getStyle().setBackgroundColor("#CCC");
		} else {
			this.getWidget().getElement().getStyle().setBackgroundColor("");
		}

	}

}
