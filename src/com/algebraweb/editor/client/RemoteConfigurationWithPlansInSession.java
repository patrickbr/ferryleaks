package com.algebraweb.editor.client;

/**
 * A RemoteConfiguration holding information on already loaded plans on the
 * server
 * 
 * @author Patrick Brosi
 * 
 */
public class RemoteConfigurationWithPlansInSession extends RemoteConfiguration {
	private Integer pids[];

	public RemoteConfigurationWithPlansInSession() {
		super();
	}

	public RemoteConfigurationWithPlansInSession(Integer[] pids) {
		super();
		this.pids = pids;
	}

	public Integer[] getPlanIds() {
		return pids;
	}
}