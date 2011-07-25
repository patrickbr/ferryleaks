package com.hydro4ge.raphaelgwt.client;

public abstract class AnimationCallback {
  static public void fire(AnimationCallback c) {
	c.onComplete();
  }
  public abstract void onComplete();
}

