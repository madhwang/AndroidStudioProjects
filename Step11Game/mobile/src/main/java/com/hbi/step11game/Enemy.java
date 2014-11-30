package com.hbi.step11game;

/**
 * Created by lee on 2014-11-27.
 */
public class Enemy {
    private int imgIndex;
    private int x, y;
    private int energy;
    private boolean isDead;

    public Enemy() {
    }

    public Enemy(int imgIndex, int x, int y, int energy, boolean isDead) {
        this.imgIndex = imgIndex;
        this.x = x;
        this.y = y;
        this.energy = energy;
        this.isDead = isDead;
    }

    public int getImgIndex() {
        return imgIndex;
    }

    public void setImgIndex(int imgIndex) {
        this.imgIndex = imgIndex;
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

    public int getEnergy() {
        return energy;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean isDead) {
        this.isDead = isDead;
    }
}
