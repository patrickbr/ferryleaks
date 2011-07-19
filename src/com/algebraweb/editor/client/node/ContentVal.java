package com.algebraweb.editor.client.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.algebraweb.editor.client.scheme.Field;
import com.algebraweb.editor.client.scheme.GoAble;
import com.algebraweb.editor.client.scheme.Value;

public class ContentVal extends NodeContent {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -168461026763593678L;

	

	private PropertyValue value;


	public ContentVal(String name, String internalName,PropertyValue value) {
		
		this.name=name;
		this.value=value;
		this.internalName = internalName;
		
	}
	
	public ContentVal() {
		
		
	}


	public ArrayList<NodeContent> getContent() {
		return childs;
	}


	public void setChilds(ArrayList<NodeContent> childs) {
		this.childs = childs;
	}




	public PropertyValue getValue() {
		return value;
	}


	public void setValue(PropertyValue value) {
		this.value = value;
	}
	
	public String toString() {
		
		String ret = "[CONTENTVAL '" + name + "' value:" + value + " childs:(";
		
		Iterator<NodeContent> i = childs.iterator();
		
		
		while (i.hasNext()) {
			
			ret+=i.next().toString();
			
		}
		
		ret +=") attributes:";
		
		Set s = attributes.keySet();
		
		Iterator<String> a = s.iterator();
		
		while (a.hasNext()) {
			
			String n = a.next();
			
			ret+="{" + n + "="  + attributes.get(n) + "}";
			
		}
		
		
		return ret+"]";
	}
	
	/**
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
	**/
	/**
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
**/

	/**
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
	*/


	@Override
	public String getInternalName() {
		return internalName;
	}
	
	/**
	public ArrayList<NodeContent> getDirectNodeContentByScheme(GoAble g) {

		ArrayList<NodeContent> res = new ArrayList<NodeContent>();

		//TODO: !!!!! maybe this should be flat!!
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

						if ((!nodeVal.getAttributes().containsKey(att) ||
								(current.hasMustBe() && !current.getMust_be().equals(nodeVal.getAttributes().get(att).getVal())))){

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

	*/
}
