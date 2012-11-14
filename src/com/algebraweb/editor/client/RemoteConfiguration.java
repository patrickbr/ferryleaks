package com.algebraweb.editor.client;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Holds the server configuration.
 * 
 * @author Patrick Brosi
 * 
 */
public class RemoteConfiguration implements IsSerializable {

	private int keepAliveInterval;
	private boolean loadEmptyCanvas;
	private boolean invertArrows;

	public RemoteConfiguration() {
	}

	/**
	 * The keep alive internval in ms.
	 * 
	 * @return the keepAliveInterval
	 */
	public int getKeepAliveInterval() {
		return keepAliveInterval;
	}

	/**
	 * If canvas arrows should be inverted
	 * 
	 * @return the invertArrows
	 */
	public boolean isInvertArrows() {
		return invertArrows;
	}

	/**
	 * Tells whether a empty canvas should be loaded on start
	 * 
	 * @return the loadEmptyCanvas
	 */
	public boolean isLoadEmptyCanvas() {
		return loadEmptyCanvas;
	}

	/**
	 * @param invertArrows
	 *            the invertArrows to set
	 */
	public void setInvertArrows(boolean invertArrows) {
		this.invertArrows = invertArrows;
	}

	/**
	 * @param keepAliveInterval
	 *            the keepAliveInterval to set
	 */
	public void setKeepAliveInterval(int keepAliveInterval) {
		this.keepAliveInterval = keepAliveInterval;
	}

	/**
	 * @param loadEmptyCanvas
	 *            the loadEmptyCanvas to set
	 */
	public void setLoadEmptyCanvas(boolean loadEmptyCanvas) {
		this.loadEmptyCanvas = loadEmptyCanvas;
	}

}
