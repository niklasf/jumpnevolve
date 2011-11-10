/**
 *
 */
package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.ForegroundDrawable;
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
import com.googlecode.jumpnevolve.math.NextCollision;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.ShapeFactory;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine Spielerfigur nimmt die Eigenschaften eines {@link Playable} an.
 * <p>
 * Sie wird stets im Vordergrund gezeichnet.
 * 
 * @author Erik Wagner
 * 
 */
public class PlayerFigure extends AbstractObject implements Fighting,
		Activating, GravityActing, Moving, Jumping, Blockable,
		ForegroundDrawable {

	private static final long serialVersionUID = -861952852028764393L;

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
		super(world, ShapeFactory.createCircle(position, 10), 4.0f);
		this.parent = parent;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.isAlive() == false) {
			this.setPosition(this.parent.getLastSave());
			// Zurücksetzen zum letzten Speicherort

			this.setAlive(true); // Wiederbeleben

			// TODO: Punkte abziehen o.Ä.
		}
		if (this.isWayBlocked(Shape.DOWN) == false) {
			this.jumps = false;
		}
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
	}

	public void run(int direction) {
		switch (direction) {
		case Playable.DIRECTION_LEFT:
			// Nach links laufen
			this.curDirection = Vector.LEFT;
			break;
		case Playable.DIRECTION_RIGHT:
			// Nach rechts laufen
			this.curDirection = Vector.RIGHT;
			break;
		case Playable.STAY:
			// Stehen bleiben
			this.curDirection = Vector.ZERO;
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
