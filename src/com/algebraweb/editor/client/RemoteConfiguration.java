package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.IsSerializable;

public class RemoteConfiguration implements IsSerializable{
	
	private int keepAliveInterval;
	private boolean loadEmptyCanvas;
	private boolean invertArrows;
	
	public RemoteConfiguration() {
	}

	/**
	 * @return the keepAliveInterval
	 */
	public int getKeepAliveInterval() {
		return keepAliveInterval;
	}

	/**
	 * @param keepAliveInterval the keepAliveInterval to set
	 */
	public void setKeepAliveInterval(int keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
	}

	/**
	 * @return the loadEmptyCanvas
	 */
	public boolean isLoadEmptyCanvas() {
		return loadEmptyCanvas;
	}

	/**
	 * @param loadEmptyCanvas the loadEmptyCanvas to set
	 */
	public void setLoadEmptyCanvas(boolean loadEmptyCanvas) {
		this.loadEmptyCanvas = loadEmptyCanvas;
	}

	/**
	 * @return the invertArrows
	 */
	public boolean isInvertArrows() {
		return invertArrows;
	}

	/**
	 * @param invertArrows the invertArrows to set
	 */
	public void setInvertArrows(boolean invertArrows) {
		this.invertArrows = invertArrows;
	}
	
	
	
	
	
}
