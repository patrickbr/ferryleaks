package com.algebraweb.editor.client.node;

import java.util.ArrayList;
import java.util.Iterator;

import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.Value;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * A ContentNode is, contrary to a NodeContent, a Node which can <i>hold</i>
 * NodeContents.
 * @author patrick
 *
 */

public abstract class ContentNode implements IsSerializable{

	protected ArrayList<NodeContent> childs = new ArrayList<NodeContent>();
	protected ArrayList<LabelOb> labelScheme = new ArrayList<LabelOb>();

	
	public void addLabelOb(LabelOb ob) {

		labelScheme.add(ob);

	}
	
	public void clearLabelOb() {

		labelScheme.clear();

	}

	
	
	/**
	 * Returns ALL values in this ContentNode with a given
	 * internal name (as specified in the scheme XML file)
	 * Goes into content childs!
	 * @param name
	 * @return
	 */
	public ArrayList<NodeContent> getAllContentWithInternalName(String name) {

		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();

		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c.getInternalName().equals(name)) temp.add(c);

			temp.addAll(c.getAllContentWithInternalName(name));

		}

		return temp;

	}


	/**
	 * Returns ALL values in this ContentNode with a given
	 * internal name (as specified in the scheme XML file)
	 * Goes into content childs!
	 * @param name
	 * @return
	 */
	public ArrayList<NodeContent> getAllContentWithValName(String name) {

		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();

		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			
			if (c.getName().equals(name)) temp.add(c);

			temp.addAll(c.getAllContentWithValName(name));

		}

		return temp;

	}
	
	


	/**
	 * returns the contents of this ContentNode
	 * @return
	 */
	public abstract ArrayList<NodeContent> getContent();

	public ArrayList<NodeContent> getContentWithAttributeValue(String attr, String value) {


		ArrayList<NodeContent> ret = new ArrayList<NodeContent>();


		if (this instanceof NodeContent) {

			Iterator<String> itP = ((NodeContent)this).getAttributes().keySet().iterator();

			while (itP.hasNext()) {
				
				String current = itP.next();
				
				
				if (current.equals(attr) && ((NodeContent)this).getAttributes().get(current).getVal().equals(value)) {
					
					
					ret.add(((NodeContent)this));
					
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
	 * Returns values in this ContentNode with a given
	 * internal name (as specified in the scheme XML file)
	 * Stays flat, does NOT go into content childs!
	 * @param name
	 * @return
	 */
	public ArrayList<NodeContent> getDirectContentWithInternalName(String name) {

		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();

		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c.getInternalName().equals(name)) temp.add(c);

		}

		return temp;
	}


	public ArrayList<NodeContent> getDirectNodeContentByScheme(GoAble g) {



		ArrayList<NodeContent> res = new ArrayList<NodeContent>();


		Iterator<NodeContent> it = getDirectContentWithInternalName(g.getXmlObject()).iterator();

		while(it.hasNext()) {

			NodeContent node = it.next();

			if (g instanceof Value) {

				boolean fail = false;

				if (!(node instanceof ContentVal)) {
					fail=true;
				}else{

					ContentVal nodeVal = (ContentVal) node;

					ArrayList<Field> fields = ((Value)g).getFields();
					Iterator<Field> i = fields.iterator();


					while (i.hasNext()) {

						Field current = i.next();
						String att = current.getVal();

						if (!nodeVal.getAttributes().containsKey(att) ||
								(current.hasMustBe() && !current.getMust_be().equals(nodeVal.getAttributes().get(att).getVal()))) {

							fail=true;

						}					
					}
				}

				if (!fail) res.add(node);

			} else {
				res.add(node);
			}
		}

		return res;
	}


	public abstract String getInternalName();
	
	
	public abstract String getLabel();
	
	public boolean removeContent(NodeContent con) {


		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c == con) {
				i.remove();
				return true;
			}

			//go into child
			if (c.removeContent(con)) return true;

		}

		return false;

	}
	



}
