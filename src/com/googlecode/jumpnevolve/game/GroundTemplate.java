package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.BackgroundDrawable;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.NextShape;

/**
 * Die Vorlage für jede Art von Landschaft.
 * 
 * Landschaft ziechnet sich dadurch aus, dass sie sich niemals verändert
 * (unbeweglich, tot, nicht tötungsfähig und nicht aktivierbar). Sie sind jedoch
 * blockbar, was bedeutet, dass sie andere Objekte blkockieren können.
 * <p>
 * Landschaft wird im Hintergrund gezeichnet.
 * 
 * @author Erik Wagner
 * 
 */
public abstract class GroundTemplate extends AbstractObject implements
		Blockable, BackgroundDrawable {

	private static final long serialVersionUID = 7287324851357837067L;

	public GroundTemplate(World world, NextShape shape) {
		super(world, shape, 0.0f);
	}

	public boolean wantBlock(Blockable other) {
		return true;
	}

	public boolean canBeBlockedBy(Blockable other) {
		return false;
	}

	@Override
	public int getCompany() {
		return COMPANY_GROUND;
	}
}
