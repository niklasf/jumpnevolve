/**
 * 
 */
package com.googlecode.jumpnevolve.game.player;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.EnemyTemplate;
import com.googlecode.jumpnevolve.game.Playable;
import com.googlecode.jumpnevolve.game.Player;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Circle;
import com.googlecode.jumpnevolve.math.Collision;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * @author Erik Wagner
 * 
 */
public class PlayerFigure extends AbstractObject {

	private final Player parent;

	private Vector save;

	/**
	 * 
	 * @param world
	 * @param position
	 * @param parent
	 */
	public PlayerFigure(World world, Vector position, Player parent) {
		super(world, new Circle(position, 10.0f), 5.0f, true, true, true,
				false, true);
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

	@Override
	public boolean isLiving() {
		return this.parent.getCurPlayable().isLiving();
	}

	@Override
	public boolean isKillable() {
		return this.parent.getCurPlayable().isKillable();
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
	public void onLivingCrash(AbstractObject other) {
		Collision col = this.getShape().getCollision(other.getShape(),
				other.isMoveable(), true);
		if (other instanceof EnemyTemplate && col.isBlocked(Shape.DOWN)
				&& col.isBlocked(Shape.RIGHT) == false
				&& col.isBlocked(Shape.LEFT) == false) {
			other.kill(this);
		}
	}

	@Override
	public void kill(AbstractObject killer) {
		// Sterben
		this.setAlive(false);
	}

}
