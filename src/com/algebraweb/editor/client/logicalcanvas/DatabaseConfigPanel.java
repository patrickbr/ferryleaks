package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.PasswordTextBox;
import com.google.gwt.user.client.ui.TextBox;

public class DatabaseConfigPanel extends LayoutPanel{

	private final TextBox serverAdress = new TextBox();
	private final TextBox serverPort = new TextBox();
	private final TextBox dbName = new TextBox();
	
	private final TextBox userName = new TextBox();
	private final PasswordTextBox password = new PasswordTextBox();

	public DatabaseConfigPanel(int pid, int nid, RemoteManipulationServiceAsync manServ) {

		super();


		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName("database-panel-inner");

		mainPanel.add(new Label("Connection:"));

		mainPanel.add(new InlineHTML("Host: "));
		mainPanel.add(serverAdress);
		mainPanel.add(new InlineHTML(" Port: "));
		mainPanel.add(serverPort);
		mainPanel.add(new InlineHTML("<br>"));
		mainPanel.add(new InlineHTML("Database name: "));
		mainPanel.add(dbName);

		
		
		mainPanel.add(new Label("User:"));

		mainPanel.add(new InlineHTML("Name: "));
		mainPanel.add(userName);
		mainPanel.add(new InlineHTML("<br>"));
		mainPanel.add(new InlineHTML("Password: "));
		mainPanel.add(password);
		


		this.add(mainPanel);

	}


}
