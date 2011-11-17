package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.game.player.PlayerFigure;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.Timer;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.Moving;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Masses;
import com.googlecode.jumpnevolve.util.Parameter;

public class FallingGround extends ObjectTemplate implements Blockable {

	private static final long serialVersionUID = 1531381990047043934L;
	private final Timer falling = new Timer(
			Parameter.OBJECTS_FALLINGGROUND_FALLINGTIME);
	private final Vector startPosition;

	public FallingGround(World world, Vector position, Vector dimension) {
		super(world, ShapeFactory.createRectangle(position, dimension),
				Masses.FALLING_GROUND);
		this.startPosition = position;
	}

	public FallingGround(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments));
	}

	@Override
	public boolean wantBlock(Blockable other) {
		return true;
	}

	@Override
	public boolean canBeBlockedBy(Blockable other) {
		return !other.isMoveable();
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.falling.isRunning()) {
			this.applyForce(Vector.DOWN.mul(this.getMass()
					* Parameter.GAME_ABSTRACTOBJECT_GRAVITY));
		}
		if (this.falling.didFinish()) {
			this.setPosition(this.startPosition);
			this.falling.setTime(Parameter.OBJECTS_FALLINGGROUND_FALLINGTIME);
		}
	}

	@Override
	public void onGeneralCrash(AbstractObject other, CollisionResult colResult) {
		if (other instanceof PlayerFigure) {
			this.falling.start();
		}
	}

	@Override
	public void poll(Input input, float secounds) {
		super.poll(input, secounds);
		this.falling.poll(input, secounds);
	}

	public void draw(Graphics g) {
		GraphicUtils.texture(g, getShape(), ResourceManager.getInstance()
				.getImage("textures/stone.png"), false);
	}
}
