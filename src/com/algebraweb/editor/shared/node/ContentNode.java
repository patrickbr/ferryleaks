package com.algebraweb.editor.shared.node;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.algebraweb.editor.shared.scheme.Field;
import com.algebraweb.editor.shared.scheme.GoAble;
import com.algebraweb.editor.shared.scheme.Value;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A ContentNode is, contrary to a NodeContent, a Node that can <i>hold</i>
 * NodeContents.
 *
 * @author patrick
 *
 */

public abstract class ContentNode implements IsSerializable,Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 3587617554315766060L;
	protected ArrayList<NodeContent> childs = new ArrayList<NodeContent>();
	protected ArrayList<LabelOb> labelScheme = new ArrayList<LabelOb>();

	/**
	 * Adds a label object to this ContentNode
	 *
	 * @param ob
	 */
	public void addLabelOb(LabelOb ob) {
		labelScheme.add(ob);
	}

	/**
	 * Clears this node's label objects
	 */
	public void clearLabelOb() {
		labelScheme.clear();
	}

	/**
	 * Returns ALL values in this ContentNode with a given internal name (as
	 * specified in the scheme XML file) Goes into content childs!
	 *
	 * @param name
	 * @return
	 */
	public List<NodeContent> getAllContentWithInternalName(String name) {
		List<NodeContent> temp = new ArrayList<NodeContent>();
		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {
			NodeContent c = i.next();
			if (c.getInternalName().equals(name)) {
				temp.add(c);
			}
			temp.addAll(c.getAllContentWithInternalName(name));
		}
		return temp;
	}

	/**
	 * Returns ALL values in this ContentNode with a given internal name (as
	 * specified in the scheme XML file) Goes into content childs!
	 *
	 * @param name
	 * @return
	 */
	public List<NodeContent> getAllContentWithValName(String name) {
		List<NodeContent> temp = new ArrayList<NodeContent>();
		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {
			NodeContent c = i.next();
			if (c.getName().equals(name)) {
				temp.add(c);
			}
			temp.addAll(c.getAllContentWithValName(name));
		}
		return temp;
	}

	/**
	 * returns the contents of this ContentNode
	 *
	 * @return
	 */
	public abstract List<NodeContent> getContent();

	public List<NodeContent> getContentWithAttributeValue(String attr,
			String value) {
		List<NodeContent> ret = new ArrayList<NodeContent>();
		if (this instanceof NodeContent) {
			Iterator<String> itP = ((NodeContent) this).getAttributes()
					.keySet().iterator();
			while (itP.hasNext()) {
				String current = itP.next();
				if (current.equals(attr)
						&& ((NodeContent) this).getAttributes().get(current)
								.getVal().equals(value)) {
					ret.add(((NodeContent) this));
				}
			}
		}
		Iterator<NodeContent> it = this.getContent().iterator();
		while (it.hasNext()) {
			ret.addAll(it.next().getContentWithAttributeValue(attr, value));
		}
		return ret;
	}

	/**
	 * Returns values in this ContentNode with a given internal name (as
	 * specified in the scheme XML file) Stays flat, does NOT go into content
	 * childs!
	 *
	 * @param name
	 * @return
	 */
	public List<NodeContent> getDirectContentWithInternalName(String name) {
		List<NodeContent> temp = new ArrayList<NodeContent>();
		Iterator<NodeContent> i = childs.iterator();
		while (i.hasNext()) {
			NodeContent c = i.next();
			if (c.getInternalName().equals(name)) {
				temp.add(c);
			}
		}
		return temp;
	}

	/**
	 * Returns contents in this ContentNode that fit a given scheme. Stays flat,
	 * doet NOT go into content childs!
	 *
	 * @param g
	 *            the scheme to look for
	 * @return a list of NodeContents fitting the scheme
	 */
	public List<NodeContent> getDirectNodeContentByScheme(GoAble g) {
		List<NodeContent> res = new ArrayList<NodeContent>();
		Iterator<NodeContent> it = getDirectContentWithInternalName(
				g.getXmlObject()).iterator();
		while (it.hasNext()) {
			NodeContent node = it.next();
			if (g instanceof Value) {
				boolean fail = false;
				if (!(node instanceof ContentVal)) {
					fail = true;
				} else {
					ContentVal nodeVal = (ContentVal) node;
					List<Field> fields = ((Value) g).getFields();
					Iterator<Field> i = fields.iterator();
					while (i.hasNext()) {
						Field current = i.next();
						String att = current.getName();
						if (!nodeVal.getAttributes().containsKey(att)
								|| current.hasMustBe()
								&& !current.getMust_be().equals(
										nodeVal.getAttributes().get(att)
												.getVal())) {
							fail = true;
						}
					}
				}
				if (!fail) {
					res.add(node);
				}
			} else {
				res.add(node);
			}
		}
		return res;
	}

	/**
	 * Return the internal name of this content node. This is usually the XML
	 * object's name.
	 *
	 * @return the internal name
	 */
	public abstract String getInternalName();

	/**
	 * Returns the <i>label</i> of this content node. This is the label built
	 * from the information given in the node scheme file.
	 *
	 * @return the assembled label
	 */
	public abstract String getLabel();

	/**
	 * Removes a NodeContent object from this content node or from any of its
	 * childs (does not stay flat).
	 *
	 * @param con
	 *            the NodeContent object to remove
	 * @return true if the deletion was successfull
	 */
	public boolean removeContent(NodeContent con) {
		Iterator<NodeContent> i = childs.iterator();
		while (i.hasNext()) {
			NodeContent c = i.next();
			if (c == con) {
				i.remove();
				return true;
			}
			if (c.removeContent(con)) {
				return true;
			}
		}
		return false;
	}
}