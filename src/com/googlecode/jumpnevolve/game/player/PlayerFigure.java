/**
 * 
 */
package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.Blockable;
import com.googlecode.jumpnevolve.graphics.world.Damageable;
import com.googlecode.jumpnevolve.graphics.world.Fighting;
import com.googlecode.jumpnevolve.graphics.world.GravityActing;
import com.googlecode.jumpnevolve.graphics.world.Jumping;
import com.googlecode.jumpnevolve.graphics.world.Living;
import com.googlecode.jumpnevolve.graphics.world.Moving;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.NextCollision;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class PlayerFigure extends AbstractObject implements Fighting,
		Activating, GravityActing, Moving, Jumping, Blockable {

	private final Player parent;

	private Vector curDirection = Vector.ZERO;

	private boolean jumps;

	/**
	 * 
	 * @param world
	 * @param position
	 * @param parent
	 */
	public PlayerFigure(World world, Vector position, Player parent) {
		super(world, ShapeFactory.createCircle(position, 10), 5.0f);
		this.parent = parent;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.isAlive() == false) {
			this.setPosition(this.parent.getLastSave());
			// Zurücksetzen zum letzten Speicherort

			this.setAlive(true); // Wiederbeleben

		}
		if (this.isWayBlocked(Shape.DOWN) == false) {
			this.jumps = false;
		}
		// TODO: Tod verarbeiten
	}

	public boolean isLiving() {
		return this.parent.getCurPlayable().isLiving();
	}

	public boolean isDamageable() {
		return this.parent.getCurPlayable().isDamageable();
	}

	public void doubleJump() {
		// TODO Auto-generated method stub

	}

	public void jump() {
		this.jumps = true;
		/*
		 * if (this.wasWayBlocked(Shape.DOWN)) {
		 * this.setVelocity(this.getVelocity().modifyY(
		 * -this.parent.getCurPlayable().getJumpingHeight() * 98.1f
		 * this.getMass())); }
		 */
	}

	public void run(int direction) {
		switch (direction) {
		case Playable.DIRECTION_LEFT:
			/*
			 * this.setVelocity(this.getVelocity().modifyX(
			 * -this.parent.getCurPlayable().getRunningSpeed()));
			 */
			/*
			 * this.applyForce(Vector.ZERO.modifyX((-this.parent.getCurPlayable()
			 * .getWalkingSpeed() - this.getVelocity().x) 1.5f *
			 * this.getMass()));
			 */
			// Nach links laufen
			this.curDirection = Vector.LEFT;
			break;
		case Playable.DIRECTION_RIGHT:
			/*
			 * this.setVelocity(this.getVelocity().modifyX(
			 * this.parent.getCurPlayable().getRunningSpeed()));
			 */
			/*
			 * this.applyForce(Vector.ZERO.modifyX((+this.parent.getCurPlayable()
			 * .getWalkingSpeed() - this.getVelocity().x) 1.5f *
			 * this.getMass()));
			 */
			// Nach rechts laufen
			this.curDirection = Vector.RIGHT;
			break;
		case Playable.STAY:
			this.curDirection = Vector.ZERO;
			// this.setVelocity(this.getVelocity().modifyX(0));
		default:
			break;
		}
	}

	public void draw(Graphics g) {
		this.parent.getCurPlayable().draw(g);
	}

	@Override
	public boolean canDamage(NextCollision col) {
		return col.isBlocked(Shape.DOWN) && col.isBlocked(Shape.RIGHT) == false
				&& col.isBlocked(Shape.LEFT) == false;
	}

	@Override
	public int getDamage() {
		return 1;
	}

	@Override
	public int getKindOfDamage() {
		return DAMAGE_NORMAL;
	}

	@Override
	public boolean wantDamaging(Living object) {
		return object.getCompany() == COMPANY_ENEMY && this.isDamageable();
	}

	@Override
	public int getCompany() {
		return COMPANY_PLAYER;
	}

	@Override
	public void damage(Damageable damager) {
		if (this.isLiving()) {
			this.killed();
		}
	}

	@Override
	public int getDeff(int kindOfDamage) {
		return 0;
	}

	@Override
	public int getHP() {
		return 1;
	}

	@Override
	public void killed() {
		this.setAlive(false);
	}

	@Override
	public void hasActivated(Activable object) {
		// Nichts tun
	}

	@Override
	public void hasDeactivated(Activable object) {
		// Nichts tun
	}

	@Override
	public boolean wantActivate(Activable object) {
		return true;
	}

	@Override
	public boolean wantDeactivate(Activable object) {
		return false;
	}

	@Override
	public Vector getMovingDirection() {
		return this.curDirection;
	}

	@Override
	public float getMovingSpeed() {
		return this.parent.getCurPlayable().getWalkingSpeed();
	}

	@Override
	public float getJumpingHeight() {
		if (this.jumps) {
			return this.parent.getCurPlayable().getJumpingHeight();
		} else {
			return 0.0f;
		}
	}

	@Override
	public boolean wantBlock(Blockable other) {
		return true;
	}

	@Override
	public boolean canBeBlockedBy(Blockable other) {
		return true;
	}

}
