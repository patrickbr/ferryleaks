package com.algebraweb.editor.server.logicalplan.xmlplanloader.planparser;

import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.QueryPlan;

public class EvaluationContextProvider {


	public EvaluationContextProvider() {

	}

	//TODO should be in a special pipeline kind of class
	//maybe interface

	public void fillEvaluationContext(QueryPlan p) {


		PlanNode root = p.getRoot();
		EvaluationContext c = new EvaluationContext();

		c.setDatabase("bugferrytest");
		c.setDatabasePassword("test");
		c.setDatabasePort(5432);
		c.setDatabaseServer("localhost");
		c.setDatabaseUser("bugferry");

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



}
