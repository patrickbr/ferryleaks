package com.algebraweb.editor.server.logicalplan.validation.validators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.algebraweb.editor.server.logicalplan.ContentVal;
import com.algebraweb.editor.server.logicalplan.NodeContent;
import com.algebraweb.editor.server.logicalplan.PlanNode;
import com.algebraweb.editor.server.logicalplan.Property;
import com.algebraweb.editor.server.logicalplan.PropertyValue;
import com.algebraweb.editor.server.logicalplan.QueryPlan;
import com.algebraweb.editor.server.logicalplan.validation.ValidationResult;
import com.algebraweb.editor.server.logicalplan.validation.Validator;
import com.ibm.icu.impl.PropsVectors;

public class ReferencedColumnsValidator implements Validator {

	@Override
	public ValidationResult validate(PlanNode p) {


		ArrayList<Property> refedCols = p.getReferencedColumns();
		ArrayList<Property> refAbleCols = new ArrayList<Property>();

		Iterator<PlanNode> childIt = p.getChilds().iterator();

		while (childIt.hasNext()) {
			
			PlanNode current = childIt.next();

			validate(current);
		
			refAbleCols.addAll(current.getReferencableColumnsFromValues());

		}

		Iterator<Property> refIt = refedCols.iterator();

		while (refIt.hasNext()) {


			Property currentCol = refIt.next();

			if (!(containsPropertyByVal(currentCol,refAbleCols))) {
				
				
				System.out.println("Node #" + p.getId() + " (" + p.getKind() + ") referes to non existing column '" + currentCol.getPropertyVal().getVal() + "'");
				
				
			}


		}
		
		
		return null;

	}
	
	
	private boolean containsPropertyByVal(Property p, ArrayList<Property> props) {
		
		
		Iterator<Property> it = props.iterator();
		
		while (it.hasNext()) {
			
			if (p.getPropertyVal().getVal().equals(it.next().getPropertyVal().getVal())) return true;
			
			
		}
		
		
		return false;
		
		
		
	}


}




