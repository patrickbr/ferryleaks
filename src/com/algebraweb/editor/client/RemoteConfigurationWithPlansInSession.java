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
	private boolean fromPost = false;

	public RemoteConfigurationWithPlansInSession() {
		super();
	}

	public RemoteConfigurationWithPlansInSession(Integer[] pids, boolean fromPost) {
		super();
		this.pids = pids;
		this.fromPost=fromPost;
	}

	public Integer[] getPlanIds() {
		return pids;
	}
	
	public boolean isFromPost() {
		return fromPost;
	}

}
