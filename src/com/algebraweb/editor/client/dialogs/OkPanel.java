package com.algebraweb.editor.client.dialogs;

public class OkPanel extends YesNoPanel {

	public OkPanel(String msg, String title) {
		super(msg, title);
		super.no.removeFromParent();
		super.yes.setText("OK");
	}

}
