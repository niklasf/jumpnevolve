package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;

public class Waterpit extends ObjectTemplate {

	private static final long serialVersionUID = -6308218436976045606L;

	public Waterpit(World world, Shape shape, float mass) {
		super(world, shape, mass);
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// TODO Auto-generated method stub

	}

}
