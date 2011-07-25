package com.algebraweb.editor.client;

public class ConfigurationWithPlansInSession extends Configuration {

	Integer pids[];

	public ConfigurationWithPlansInSession() {
		super();
	}

	public ConfigurationWithPlansInSession(Integer[] pids) {
		super();
		this.pids=pids;
	}

	public Integer[] getPlanIds() {
		return pids;
	}

}
