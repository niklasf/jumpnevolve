package com.googlecode.jumpnevolve.game;

import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Die Vorlage für alle vom Spieler gesteuerten Figuren.
 * 
 * Figuren zeichnen sich dadurch aus, dass sie sich bewegen und lebend sind. Sie
 * können blockbar und in der Lage sein, zu töten. Sie können nich geschoben
 * oder aktiviert werden.
 * 
 * @author Erik Wagner
 * 
 */
public abstract class FigureTemplate extends AbstractObject implements Playable {

	private static final long serialVersionUID = 1396199934877069892L;

	private Vector save;

	public FigureTemplate(World world, Shape shape, float mass,
			boolean blockable, boolean living, boolean killable) {
		super(world, shape, mass, blockable, false, living, false, killable);
		this.setNewSave(shape.getCenter());
	}

	public Vector getLastSave() {
		return this.save;
	}

	public void setNewSave(Vector save) {
		this.save = save;
	}

	protected void replace(Vector newPlace) {
		this.setPosition(newPlace);
	}

	/**
	 * Synchronisiert die Werte dieses Objekts mit denen einer anderen Figur
	 * 
	 * @param other
	 *            Die andere Figur, deren Werte übernommen werden
	 */
	public final void synchronize(FigureTemplate other) {
		this.setPosition(other.getPosition());
		this.setVelocity(other.getVelocity());
		this.setForce(other.getForce());
		this.setCollision(other.getCollision());
		this.setOldCollision(other.getOldCollision());
	}

}
