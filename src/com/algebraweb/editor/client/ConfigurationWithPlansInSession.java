package com.algebraweb.editor.client;

public class ConfigurationWithPlansInSession extends Configuration {


	Integer pids[];

	public ConfigurationWithPlansInSession(Integer[] pids) {

		super();
		this.pids=pids;


	}


	public ConfigurationWithPlansInSession() {

		super();

	}

	public Integer[] getPlanIds() {

		return pids;

	}


}
