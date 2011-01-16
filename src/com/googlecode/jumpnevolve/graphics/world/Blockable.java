package com.googlecode.jumpnevolve.graphics.world;

/**
 * @author Erik Wagner
 * 
 */
public interface Blockable extends Accompanying {

	public boolean wantBlock(Blockable other);

	public boolean canBeBlockedBy(Blockable other);
}
