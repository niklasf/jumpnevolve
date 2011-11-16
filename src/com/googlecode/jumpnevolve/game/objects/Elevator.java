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
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;
import com.googlecode.jumpnevolve.util.Masses;

/**
 * 
 * Beschreibung: Eine Plattform, die sich rauf und runter bewegt
 * 
 * Spezifikationen: blockbar, nicht schiebbar
 * 
 * Bewegungen: Masse = 1; vertikal in einem bestimmten Bereich --> 50 Pixel pro
 * Sekunde
 * 
 * Aggressivitäten: keine
 * 
 * Immunitäten: keine
 * 
 * Aktivierung: keine
 * 
 * Besonderheiten: keine
 * 
 * @author Erik Wagner
 * 
 */
public class Elevator extends ObjectTemplate implements Moving, Blockable {

	private static final long serialVersionUID = 4385912397697222758L;

	private final float upEnd, downEnd;
	private Vector curDirection = Vector.UP;

	public Elevator(World world, Vector position, Vector dimension,
			float downEnd, float upEnd) {
		super(world, ShapeFactory.createRectangle(position, dimension),
				Masses.ELEVATOR);
		if (upEnd > downEnd) {
			this.upEnd = downEnd;
			this.downEnd = upEnd;
		} else {
			this.upEnd = upEnd;
			this.downEnd = downEnd;
		}
	}

	public Elevator(World world, Vector position, String arguments) {
		this(world, position, Vector.parseVector(arguments.split(",")[0]),
				Float.parseFloat(arguments.split(",")[1]), Float
						.parseFloat(arguments.split(",")[2]));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.getPosition().y <= this.upEnd) {
			this.curDirection = Vector.DOWN;
		}
		if (this.getPosition().y >= this.downEnd) {
			this.curDirection = Vector.UP;
		}
		if (this.getVelocity().y == 0) {
			this.curDirection = this.curDirection.neg();
		}
	}

	public void blockWay(Blockable blocker, CollisionResult colRe) {
		if (blocker instanceof AbstractObject) {
			if (((AbstractObject) blocker).isMoveable() == false) {
				super.blockWay(blocker, colRe);
			}
		}
	}

	@Override
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
		return MOVE_ELEVATOR;
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
