package com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.GraphNotConnectedException;
import com.algebraweb.editor.client.node.NodeContent;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.node.QueryPlan;

public class EvaluationContextProvider {


	public EvaluationContextProvider() {

	}

	//TODO should be in a special pipeline kind of class
	//maybe interface

	public void fillEvaluationContext(QueryPlan p) throws GraphNotConnectedException {


		PlanNode root = p.getRootNode(false);
		EvaluationContext c = new EvaluationContext();

		c.setDatabase("");
		c.setDatabasePassword("");
		c.setDatabasePort(5432);
		c.setDatabaseServer("localhost");
		c.setDatabaseUser("");

		if (root.getKind().equals("serialize relation")) {

			//TODO: what about erroneous nodes?
			if (root.getContentWithAttributeValue("function", "iter").size() > 0) {
				c.setIterColumnName(root.getContentWithAttributeValue("function", "iter").get(0).getAttributes().get("name").getVal());
				c.setIterUseColumn(true);
			}else{
				c.setIterUseColumn(false);
			}

			c.setIterColumnNat(1);

			if (root.getContentWithAttributeValue("function", "pos").size() > 0) {
				c.setSortColumnName(root.getContentWithAttributeValue("function", "pos").get(0).getAttributes().get("name").getVal());
				c.setSortUseColumn(true);
			}else{
				c.setSortUseColumn(false);
			}

			c.setSortOrder("ASCENDING");


			Iterator<NodeContent> items = root.getContentWithAttributeValue("function", "item").iterator();

			ArrayList<ItemCol> itemCols = new ArrayList<ItemCol>();


			while (items.hasNext()) {


				NodeContent current = items.next();

				itemCols.add(new ItemCol(current.getAttributes().get("name").getVal(), Integer.parseInt(current.getAttributes().get("position").getVal())));

			}

			//sort to match the position ints given in the plan
			Collections.sort(itemCols);

			Iterator<ItemCol> itCol = itemCols.iterator();
			String[] colsString = new String[itemCols.size()];
			int i=0;

			while (itCol.hasNext()) {

				ItemCol current = itCol.next();

				colsString[i] = current.getName();
				i++;

			}

			c.setItemColumns(colsString);

		}else{

			c.setIterColumnName("");
			c.setIterColumnNat(1);
			c.setIterUseColumn(false);
			c.setSortColumnName("");
			c.setSortOrder("ASCENDING");
			c.setSortUseColumn(false);

		}

		p.setEvContext(c);


	}


	private class ItemCol implements Comparable<ItemCol> {



		private int pos;
		private String name;


		public ItemCol(String name, int pos) {

			this.name=name;
			this.pos=pos;

		}


		@Override
		public int compareTo(ItemCol arg0) {
			return this.pos = arg0.getPos();
		}


		/**
		 * @return the pos
		 */
		public int getPos() {
			return pos;
		}


		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}





	}



}
