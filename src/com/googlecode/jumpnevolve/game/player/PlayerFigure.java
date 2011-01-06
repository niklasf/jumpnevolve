/**
 * 
 */
package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Activable;
import com.googlecode.jumpnevolve.graphics.world.Activating;
import com.googlecode.jumpnevolve.graphics.world.Damageable;
import com.googlecode.jumpnevolve.graphics.world.Fighting;
import com.googlecode.jumpnevolve.graphics.world.Living;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Collision;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class PlayerFigure extends AbstractObject implements Fighting,
		Activating {

	private final Player parent;

	private Vector save;

	/**
	 * 
	 * @param world
	 * @param position
	 * @param parent
	 */
	public PlayerFigure(World world, Vector position, Player parent) {
		super(world, new Circle(position, 10.0f), 5.0f, true, true);
		this.parent = parent;
		this.save = this.getPosition();
		// TODO Auto-generated constructor stub
	}

	public Vector getLastSave() {
		return this.save;
	}

	public void setNewSave(Vector save) {
		this.save = save;
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.isAlive() == false) {
			this.setPosition(this.getLastSave());
			// Zurücksetzen zum letzten Speicherort

			this.setAlive(true); // Wiederbeleben

		}

		// Schwerkraft
		this.applyForce(Vector.DOWN.mul(98.1f * this.getMass()));
		// TODO: Tod verarbeiten
	}

	@Override
	public boolean isBlockable() {
		return this.parent.getCurPlayable().isBlockable();
	}

	@Override
	public boolean isPushable() {
		return this.parent.getCurPlayable().isPushable();
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
		if (this.wasWayBlocked(Shape.DOWN)) {
			this.setVelocity(new Vector(this.getVelocity().x, -this.parent
					.getCurPlayable().getJumpingHeight()
					* 98.1f * this.getMass()));
		}
	}

	public void run(int direction) {
		switch (direction) {
		case Playable.DIRECTION_LEFT:
			this.setVelocity(this.getVelocity().modifyX(
					-this.parent.getCurPlayable().getRunningSpeed()));
			// Nach links laufen
			break;
		case Playable.DIRECTION_RIGHT:
			this.setVelocity(this.getVelocity().modifyX(
					this.parent.getCurPlayable().getRunningSpeed()));
			// Nach rechts laufen
			break;
		case Playable.STAY:
			this.setVelocity(this.getVelocity().modifyX(0));
		default:
			break;
		}
	}

	public void draw(Graphics g) {
		this.parent.getCurPlayable().draw(g);
	}

	@Override
	public boolean canDamage(Collision col) {
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

}
