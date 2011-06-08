package com.algebraweb.editor.server.logicalplan;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A ContentNode is, contrary to a NodeContent, a Node which can <i>hold</i>
 * NodeContens.
 * @author patrick
 *
 */

public interface ContentNode extends Serializable{
		
	/**
	 * Returns all values in this ContentNode with a given
	 * internal name (as specified in the scheme XML file)
	 * @param name
	 * @return
	 */
	public ArrayList<NodeContent> getValuesByInternalName(String name);
	
	/**
	 * returns the contents of this ContentNode
	 * @return
	 */
	public ArrayList<NodeContent> getContent();

}
