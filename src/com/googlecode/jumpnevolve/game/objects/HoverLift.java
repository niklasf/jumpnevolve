package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.Client;
import com.googlecode.jumpnevolve.graphics.world.ObjectGroup;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Masses;

/**
 * Ein Luft-Lift, der normalerweise ein Bestreben nach oben hat.
 * <p>
 * Er kann eine gewisse Masse tragen, ist die auf ihm lastende Masse größer als
 * diese, so sinkt er nach unten. Ist sie kleiner steigt er immer noch auf, aber
 * langsamer.
 * 
 * @author Erik Wagner
 */
public class HoverLift extends ObjectTemplate implements Blockable,
		ObjectGroup, Client {

	private static final long serialVersionUID = -5540543276796403277L;

	private final Vector upForce;

	private final AbstractObject[] objects;

	public HoverLift(World world, Vector position, Vector dimension,
			float portableMass) {
		super(world, ShapeFactory.createRectangle(position, dimension),
				Masses.HOVER_LIFT);
		this.upForce = Vector.UP.mul(GRAVITY * portableMass);
		ScoutObject scout = new ScoutObject(world, this.getShape(), this);
		scout.moveCenter(Vector.UP.mul(dimension.y * 0.3f));
		this.objects = new AbstractObject[] { scout };
	}

	public HoverLift(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments.split(",")[0]),
				Float.parseFloat(arguments.split(",")[1]));
	}

	private void applyDownForce(float value) {
		this.applyForce(Vector.DOWN.mul(value));
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
		// Kraft nach oben, stellt das Bestreben nach oben dar
		this.applyForce(this.upForce);
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.texture(g, this.getShape(), ResourceManager.getInstance()
				.getImage("textures/aluminium.png"), true);
	}

	@Override
	public AbstractObject[] getObjects() {
		return this.objects;
	}

	@Override
	public void informAboutCrash(AbstractObject other, CollisionResult colRe,
			ScoutObject scout) {
		if (other.isMoveable()) {
			this.applyDownForce(other.getMass() * GRAVITY);
		}
	}
}
