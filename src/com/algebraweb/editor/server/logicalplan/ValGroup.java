package com.algebraweb.editor.server.logicalplan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A group of node values with no own parameters. On editing 
 * a node, the corresponding GoInto will be presented by an
 * own tab in the editor window and this group will be filled
 * with the values provided there.
 * @author Patrick Brosi
 *
 */

public class ValGroup implements NodeContent, ContentNode{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4630462466854800864L;

	protected ArrayList<NodeContent> childs = new ArrayList<NodeContent>();

	private PropertyMap attributes = new PropertyMap();

	private String name;
	private String internalName;
	
	public ValGroup(String name) {
		
		this.name=name;
		
		//TODO: this should be different in the future
		this.internalName = name;
				
	}

	public ArrayList<NodeContent> getContent() {
		return childs;
	}

	public void setChilds(ArrayList<NodeContent> childs) {
		this.childs = childs;
	}

	@Override
	public PropertyMap getAttributes() {
		return attributes;
	}

	@Override
	public void setAttributes(PropertyMap attributes) {
		this.attributes = attributes;
	}

	public String getName() {
		return name;
	}

	
	//TODO: experimental
	
	public String toString() {
		
		String ret = "{VALGROUP '" + name + "' childs:";
		Iterator<NodeContent> i = childs.iterator();
				
		while (i.hasNext()) {
			
			ret+=i.next().toString();
			
		}
		
		return ret + "}";
	}
	
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
	
	public boolean removeContent(NodeContent con) {


		Iterator<NodeContent> i = childs.iterator();

		while (i.hasNext()) {

			NodeContent c = i.next();
			if (c == con) {
				i.remove();
				return true;
			}

			//go into child
			return c.removeContent(con);

		}

		return false;

	}

	@Override
	public ArrayList<NodeContent> getDirectContentWithInternalName(String name) {
		ArrayList<NodeContent> temp = new ArrayList<NodeContent>();
		
		Iterator<NodeContent> i = childs.iterator();
				
		while (i.hasNext()) {
			
			NodeContent c = i.next();
			if (c.getInternalName().equals(name)) temp.add(c);
								
		}
		
		return temp;
	}

	@Override
	public String getInternalName() {
		
		return internalName;
	}



}
