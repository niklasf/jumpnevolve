package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.Moving;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine sich in horizontaler Richtung bewegende Plattform.
 * 
 * @author Erik Wagner
 * 
 */
public class SlidingPlattform extends ObjectTemplate implements Moving,
		Blockable {

	private static final long serialVersionUID = -3698619613073930651L;

	private final float leftEnd, rightEnd;
	private Vector curDirection = Vector.LEFT;

	/**
	 * Erzeugt eine neue Plattform, die sich nach rechts und links bewegt
	 * 
	 * @param world
	 *            Die Welt in die dieses Objekt geaddet wird
	 * @param position
	 *            Die Start-Position der Plattform
	 * @param dimension
	 *            Die Größe der Plattform in Form eines Vektors vom Zentrum der
	 *            Plattform zu einer Ecke der Plattform
	 * @param end1
	 *            Die eine Position, an welcher die Plattform die Richtung
	 *            ändert
	 * @param end2
	 *            Die andere Position, an welcher die Plattform die Richtung
	 *            ändert
	 */
	public SlidingPlattform(World world, Vector position, Vector dimension,
			float end1, float end2) {
		super(world, ShapeFactory.createRectangle(position, dimension), 5.0f);
		if (end1 > end2) {
			this.leftEnd = end2;
			this.rightEnd = end1;
		} else {
			this.rightEnd = end2;
			this.leftEnd = end1;
		}
	}

	public SlidingPlattform(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments.split(",")[0]),
				Float.parseFloat(arguments.split(",")[1]), Float
						.parseFloat(arguments.split(",")[2]));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.getPosition().x <= this.leftEnd) {
			this.curDirection = Vector.RIGHT;
		}
		if (this.getPosition().x >= this.rightEnd) {
			this.curDirection = Vector.LEFT;
		}
		if (this.getVelocity().x == 0) {
			this.curDirection = this.curDirection.neg();
		}
	}

	public void draw(Graphics g) {
		GraphicUtils.texture(g, this.getShape(), ResourceManager.getInstance()
				.getImage("textures/aluminium.png"), true);
	}

	@Override
	public Vector getMovingDirection() {
		return curDirection;
	}

	@Override
	public float getMovingSpeed() {
		return 50.0f;
	}

	@Override
	public boolean canBeBlockedBy(Blockable other) {
		return !other.isMoveable();
	}

	@Override
	public boolean wantBlock(Blockable other) {
		return true;
	}
}
