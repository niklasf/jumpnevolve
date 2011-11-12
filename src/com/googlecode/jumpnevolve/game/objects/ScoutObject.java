package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.Client;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.CollisionResult;
import com.googlecode.jumpnevolve.math.NextShape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Ein Späher für andere Objekte.
 * <p>
 * So können Kollisionen mit Objekten im Vorherein beachtet werden, ohne sie
 * beispielsweise durch die Eigenschaft "Blockable" zu blockieren.
 * <p>
 * Siehe {@link Client} für weitere Informationen
 * 
 * @author Erik Wagner
 * 
 */
public class ScoutObject extends ObjectTemplate {

	private static final long serialVersionUID = 3526762798248615817L;

	private final Client parent;
	private final boolean moveWithParent;
	private Vector lastParentPosition;

	/**
	 * Erzeugt einen neuen Späher für ein bestimmtes Objekt, der diesem Objekt
	 * in seiner Bewegung folgt
	 * 
	 * @param world
	 *            Die Welt für das Objekt
	 * @param shape
	 *            Das Shape, für das Kollisionen erkannt werden soll
	 * @param parent
	 *            Der Client, den der Späher informieren soll
	 */
	public ScoutObject(World world, NextShape shape, Client parent) {
		this(world, shape, parent, true);
	}

	/**
	 * Erzeugt einen neuen Späher für ein bestimmtes Objekt
	 * 
	 * @param world
	 *            Die Welt für das Objekt
	 * @param shape
	 *            Das Shape, für das Kollisionen erkannt werden soll
	 * @param parent
	 *            Der Client, den der Späher informieren soll
	 * @param moveWithParent
	 *            Ob der Späher dem Parent folgen soll oder nicht
	 */
	public ScoutObject(World world, NextShape shape, Client parent,
			boolean moveWithParent) {
		super(world, shape, 0.0f);
		this.parent = parent;
		this.lastParentPosition = parent.getPosition();
		this.moveWithParent = moveWithParent;
	}

	/**
	 * Bewegt den Späher um einen bestimmten Betrag
	 * 
	 * @param deltaPos
	 */
	public void moveCenter(Vector deltaPos) {
		this.setPosition(this.getPosition().add(deltaPos));
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.moveWithParent) {
			// Position anhand der Bewegung des Parents ändern, wenn das
			// gewünscht ist
			this.moveCenter(this.parent.getPosition().sub(
					this.lastParentPosition));
			this.lastParentPosition = this.parent.getPosition();
		}
	}

	@Override
	public void onGeneralCrash(AbstractObject other, CollisionResult colResult) {
		if (other != parent) {
			this.parent.informAboutCrash(other, colResult, this);
		}
	}

	@Override
	public void draw(Graphics g) {
		// Nichts zeichnen, da dieses Objekt nur intern Informationen liefern
		// soll, aber nicht für den Nutzer sichtbar ist
	}
}
