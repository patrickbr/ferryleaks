package com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.logicalcanvas.GraphNotConnectedException;
import com.algebraweb.editor.client.logicalcanvas.PlanHasCycleException;
import com.algebraweb.editor.shared.node.NodeContent;
import com.algebraweb.editor.shared.node.PlanNode;
import com.algebraweb.editor.shared.node.QueryPlan;

public class EvaluationContextProvider {

	private HttpSession session;

	public EvaluationContextProvider(HttpSession session) {
		this.session=session;
	}

	/**
	 * Loads an evaluation context into a query plan
	 * @param p the query plan
	 * @throws GraphNotConnectedException
	 * @throws PlanHasCycleException
	 */
	public void fillEvaluationContext(QueryPlan p) throws GraphNotConnectedException, PlanHasCycleException {

		PlanNode root;

		try {
			root = p.getRootNode(false);
		}catch(Exception e) {
			root = null;
		}
		EvaluationContext c = new EvaluationContext();
	
		c.setDatabase((String)session.getAttribute("databaseName"));
		c.setDatabasePassword((String)session.getAttribute("databasePw"));
		c.setDatabasePort((session.getAttribute("databasePort") != null?(Integer) session.getAttribute("databasePort"):0));
		c.setDatabaseServer((String)session.getAttribute("databaseHost"));
		c.setDatabaseUser((String)session.getAttribute("databaseUser"));

		if (root != null && root.getKind().equals("serialize relation")) {

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
			List<ItemCol> itemCols = new ArrayList<ItemCol>();

			while (items.hasNext()) {
				NodeContent current = items.next();
				itemCols.add(new ItemCol(current.getAttributes().get("name").getVal(), Integer.parseInt(current.getAttributes().get("position").getVal())));
			}
			
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
	
	/**
	 * A private class for comparing columns
	 * @author patrick
	 *
	 */
	private class ItemCol implements Comparable<ItemCol> {

		private int pos;
		private String name;

		public ItemCol(String name, int pos) {
			this.name=name;
			this.pos=pos;
		}

		@Override
		public int compareTo(ItemCol arg0) {
			return this.pos = arg0.getPosition();
		}

		/**
		 * Returns the name of this ItemColumn
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Returns the position of this ItemColumn
		 * @return the position
		 */
		public int getPosition() {
			return pos;
		}
	}

}
