package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Masses;
import com.googlecode.jumpnevolve.util.Parameter;

public class FakeGround extends ObjectTemplate {

	public FakeGround(World world, Vector position, Vector dimension) {
		super(world, ShapeFactory.createRectangle(position, dimension),
				Masses.NO_MASS);
	}

	public FakeGround(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		// Nichts tun
	}

	public void draw(Graphics g) {
		GraphicUtils.texture(g, getShape(), ResourceManager.getInstance()
				.getImage("textures/stone.png"), false);
		GraphicUtils
				.fill(g, this.getShape(), new Color(0.3f, 0.3f, 0.3f, 0.5f));
	}
}
