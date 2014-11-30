package com.hbi.step11game;

/**
 * Created by user on 2014-11-30.
 */
public class Missile {
	private int x;
	private int y;
	private boolean isDead;

	public Missile(){}

	public Missile(int x,int y,boolean isDead)
	{
		this.x = x;
		this.y = y;
		this.isDead = isDead;
	}


	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public boolean isDead() {
		return isDead;
	}

	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
}
