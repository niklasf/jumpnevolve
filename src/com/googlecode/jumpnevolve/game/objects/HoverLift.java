package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Luft-Lift, der normalerweise ein Bestreben nach oben hat.
 * <p>
 * Er kann eine gewisse Masse tragen, ist die auf ihm lastende Masse größer als
 * diese, so sinkt er nach unten. Ist sie kleiner steigt er immer noch auf, aber
 * langsamer.
 * 
 * @author Erik Wagner
 * 
 *         FIXME: Der Lift bleibt bei einem Objekt mit mass == portableMass noch
 *         nicht stehen, sondern bewegt sich teilweise leicht, da das Objekt
 *         immer wieder nach oben geschoben wird
 */
public class HoverLift extends ObjectTemplate implements Blockable {

	private static final long serialVersionUID = -5540543276796403277L;

	private final Vector upForce;

	public HoverLift(World world, Vector position, Vector dimension,
			float portableMass) {
		super(world, ShapeFactory.createRectangle(position, dimension), 2.0f);
		this.upForce = Vector.UP.mul(GRAVITY * portableMass);
	}

	public HoverLift(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments.split(",")[0]),
				Float.parseFloat(arguments.split(",")[1]));
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
	public void onGeneralCrash(AbstractObject other, CollisionResult colResult) {
		byte blocked = colResult.getIsOverlap().toShapeDirection();
		if (other.isMoveable()
				&& (blocked == Shape.DOWN || blocked == Shape.DOWN_LEFT || blocked == Shape.DOWN_RIGHT)) {
			// Kraft nach unten entsprechend der Masse des Objekts, das auf
			// diesem steht
			this.applyForce(Vector.DOWN.mul(other.getMass() * GRAVITY));
		}
	}

	@Override
	public void draw(Graphics g) {
		GraphicUtils.texture(g, this.getShape(), ResourceManager.getInstance()
				.getImage("textures/aluminium.png"), true);
	}
}
