package com.algebraweb.editor.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * A Web Editor for the Table Algebra
 * @author Patrick Brosi
 *
 */

public class AlgebraWebEditor implements EntryPoint {


	/**
	 * Everything begins here...
	 */
	public void onModuleLoad() {
				
		GraphCanvas graphCanvas = new GraphCanvas(1800,1600);
		
		DragPanel d = new DragPanel();
		
		d.add(graphCanvas);
		
		RootPanel.get().add(d);
		RootPanel.get().add(new ControllPanel(300,300,graphCanvas));
		
		d.center();
				
	
	}
}
