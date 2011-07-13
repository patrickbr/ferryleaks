package com.algebraweb.editor.server.logicalplan.xmlbuilder;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;

import com.algebraweb.editor.client.logicalcanvas.EvaluationContext;
import com.algebraweb.editor.client.node.ContentVal;
import com.algebraweb.editor.client.node.PlanNode;
import com.algebraweb.editor.client.node.Property;
import com.algebraweb.editor.client.node.PropertyMap;
import com.algebraweb.editor.client.node.PropertyValue;
import com.algebraweb.editor.client.node.QueryPlan;
import com.algebraweb.editor.client.node.ValGroup;
import com.algebraweb.editor.client.scheme.NodeScheme;

public class SerializeRelationBuilder {


	private EvaluationContext c;
	private ServletContext context;

	public SerializeRelationBuilder(EvaluationContext c, ServletContext context) {

		this.c=c;
		this.context=context;
		

	}


	public PlanNode addSerializRelation(PlanNode root) {


		String dummyIterColumn = "iter_bugferry_dummy_iter_col";
		String dummySortColumn = "pos_bugferry_dummy_sort_col";

		HashMap<String,NodeScheme> schemes = (HashMap<String,NodeScheme>)context.getAttribute("nodeSchemes");

		PlanNode newRootNode = root;

		ArrayList<Integer> nids = new ArrayList<Integer>();

		if (!c.isIterUseColumn()) {


			PlanNode attachNode = new PlanNode(root.getMother().getFreeId(nids), schemes.get("attach"),root.getMother());
			nids.add(attachNode.getId());
			ValGroup contentGroup = new ValGroup("content");
			attachNode.getContent().add(contentGroup);

			ContentVal attachColumn = new ContentVal("column", "column", "");
			PropertyMap attachColumnAttrs = new PropertyMap();
			attachColumnAttrs.put(new Property("name", new PropertyValue(dummyIterColumn,"string")));
			attachColumnAttrs.put(new Property("new", new PropertyValue("true","boolean")));
			attachColumn.setAttributes(attachColumnAttrs);

			contentGroup.getContent().add(attachColumn);

			ContentVal attachColumnValue = new ContentVal("value","value",Integer.toString(c.getIterColumnNat()));
			PropertyMap attachColumnValueAttrs = new PropertyMap();
			attachColumnValueAttrs.put(new Property("type", new PropertyValue("nat","string")));
			attachColumnValue.setAttributes(attachColumnValueAttrs);
			attachColumn.getContent().add(attachColumnValue);


			createEdgeTo(attachNode, newRootNode);
			newRootNode = attachNode;


		}

		PlanNode serializeRel = new PlanNode(root.getMother().getFreeId(nids), schemes.get("serialize relation"),root.getMother());
		nids.add(serializeRel.getId());

		ValGroup contentGroup = new ValGroup("content"); 
		serializeRel.getContent().add(contentGroup);

		//fill the iter column field
		ContentVal iter = new ContentVal("column", "column", "");
		PropertyMap attributes = new PropertyMap();

		attributes.put(new Property("new", new PropertyValue("false","boolean")));
		attributes.put(new Property("function", new PropertyValue("iter","string")));

		if (c.isIterUseColumn()) {
			attributes.put(new Property("name", new PropertyValue(c.getIterColumnName(),"string")));
		}else{
			attributes.put(new Property("name", new PropertyValue(dummyIterColumn,"string")));
		}
		iter.setAttributes(attributes);
		contentGroup.getContent().add(iter);


		//fill the pos column field
		ContentVal pos = new ContentVal("column","column","");
		PropertyMap attributesPos = new PropertyMap();

		attributesPos.put(new Property("new", new PropertyValue("false","boolean")));
		attributesPos.put(new Property("function", new PropertyValue("pos","string")));

		if (c.isSortUseColumn()) {
			attributesPos.put(new Property("name", new PropertyValue(c.getSortColumnName(),"string")));
		}else{
			attributesPos.put(new Property("name", new PropertyValue(dummySortColumn,"string")));
		}


		pos.setAttributes(attributesPos);
		contentGroup.getContent().add(pos);


		//fill the item colum fields

		//documentation wiki says 0 here, but pf wont like it...
		int i=1;

		for (String itemCol : c.getItemColumns()) {

			ContentVal item = new ContentVal("column","column","");
			PropertyMap attributesItem = new PropertyMap();

			attributesItem.put(new Property("new", new PropertyValue("false","boolean")));
			attributesItem.put(new Property("function", new PropertyValue("item","string")));
			attributesItem.put(new Property("position", new PropertyValue(Integer.toString(i),"int")));
			attributesItem.put(new Property("name", new PropertyValue(itemCol,"string")));

			item.setAttributes(attributesItem);
			contentGroup.getContent().add(item);
			i++;

		}
		
		PlanNode nilNode = new PlanNode(root.getMother().getFreeId(nids), schemes.get("nil"),root.getMother());

		createEdgeTo(serializeRel, nilNode);
		createEdgeTo(serializeRel, newRootNode);
		
		
		
		newRootNode = serializeRel;

		

		return newRootNode;

	}




	private void createEdgeTo(PlanNode from, PlanNode to) {

		from.getChilds().add(to);

		ContentVal edge = new ContentVal("edge", "edge", "");

		PropertyMap attributesItem = new PropertyMap();
		attributesItem.put(new Property("to", new PropertyValue(Integer.toString(to.getId()),"int")));

		edge.setAttributes(attributesItem);
		from.getContent().add(edge);

	}





}
