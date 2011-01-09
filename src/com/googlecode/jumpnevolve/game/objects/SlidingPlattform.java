package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.game.ObjectTemplate;
import com.googlecode.jumpnevolve.graphics.GraphicUtils;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Rectangle;
import com.googlecode.jumpnevolve.math.Vector;

/**
 * Eine sich in horizontaler Richtung bewegende Plattform.
 * 
 * @author Erik Wagner
 * 
 */
public class SlidingPlattform extends ObjectTemplate {

	private final float leftEnd, rightEnd;

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
		super(world, new Rectangle(position, dimension), 5.0f, true, false);
		if (end1 > end2) {
			this.leftEnd = end2;
			this.rightEnd = end1;
		} else {
			this.rightEnd = end2;
			this.leftEnd = end1;
		}
	}

	@Override
	protected void specialSettingsPerRound(Input input) {
		if (this.getPosition().x <= this.leftEnd) {
			this.setVelocity(Vector.RIGHT.mul(50.0f));
		}
		if (this.getPosition().x >= this.rightEnd) {
			this.setVelocity(Vector.LEFT.mul(50.0f));
		}
		if (this.getVelocity().x == 0) {
			this.setVelocity(Vector.LEFT.mul(50.0f));
		}
	}

	public void draw(Graphics g) {
		GraphicUtils.texture(g, this.getShape(), ResourceManager.getInstance()
				.getImage("textures/aluminium.png"), true);
	}
}
