package com.algebraweb.editor.client;

public class RemoteConfigurationWithPlansInSession extends RemoteConfiguration {

	Integer pids[];

	public RemoteConfigurationWithPlansInSession() {
		super();
	}

	public RemoteConfigurationWithPlansInSession(Integer[] pids) {
		super();
		this.pids=pids;
	}

	public Integer[] getPlanIds() {
		return pids;
	}

}
