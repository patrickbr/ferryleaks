package com.algebraweb.editor.client.logicalcanvas;

import com.algebraweb.editor.client.RemoteManipulationServiceAsync;
import com.algebraweb.editor.shared.logicalplan.EvaluationContext;
import com.google.gwt.user.client.ui.CheckBox;
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
	private final CheckBox saveGlobal = new CheckBox();

	public DatabaseConfigPanel(int pid, int nid, RemoteManipulationServiceAsync manServ) {
		super();

		FlowPanel mainPanel = new FlowPanel();
		mainPanel.addStyleName("database-panel-inner");
		mainPanel.add(new Label("Connection:"));
		mainPanel.add(new InlineHTML("Host: "));
		mainPanel.add(serverAdress);
		mainPanel.add(new InlineHTML(" Port: "));
		mainPanel.add(serverPort);
		mainPanel.add(new InlineHTML("Database name: "));
		mainPanel.add(dbName);
		mainPanel.add(new Label("User:"));
		mainPanel.add(new InlineHTML("Name: "));
		mainPanel.add(userName);
		mainPanel.add(new InlineHTML("Password: "));
		mainPanel.add(password);

		InlineHTML saveGlobalT = new InlineHTML("Set as session default");
		mainPanel.add(saveGlobalT);
		saveGlobal.addStyleName("set-save-global");
		saveGlobalT.addStyleName("set-save-global-t");
		mainPanel.add(saveGlobal);

		this.add(mainPanel);
	}


	protected void fillEvaluationContext(EvaluationContext c) {
		c.setDatabaseServer(serverAdress.getText());
		c.setDatabasePort(Integer.parseInt(serverPort.getText()));
		c.setDatabase(dbName.getText());
		c.setDatabaseUser(userName.getText());
		c.setDatabasePassword(password.getText());
		c.setDatabaseSetGlobal(saveGlobal.getValue());
	}


	protected void loadEvaluationContext(EvaluationContext c) {
		serverAdress.setText(c.getDatabaseServer());
		serverPort.setText(Integer.toString(c.getDatabasePort()));
		dbName.setText(c.getDatabase());
		userName.setText(c.getDatabaseUser());
		password.setText(c.getDatabasePassword());
	}
}
