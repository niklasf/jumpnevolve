package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.game.Figure;
import com.googlecode.jumpnevolve.game.VorlageGegner;
import com.googlecode.jumpnevolve.game.VorlageLandschaft;
import com.googlecode.jumpnevolve.game.VorlageObjekte;
import com.googlecode.jumpnevolve.graphics.world.AbstractObject;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

public class NormalSoldier extends VorlageGegner {

	public NormalSoldier(Vector position, Vector dimension, Vector force,
			World worldOfThis) {
		super(AbstractObject.TYPE_BOX, position, dimension, force, worldOfThis);
		// TODO Auto-generated constructor stub
	}

	public NormalSoldier(Vector position, Vector dimension, World worldOfThis) {
		this(position, dimension, Vector.ZERO, worldOfThis);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void crashedByEnemy(VorlageGegner gegner) {
		gegner
				.blockWay(gegner.getShape().getTouchedSideOfThis(
						this.getShape()));// Weg des anderen blockieren
		this.setVelocity(this.getVelocity().neg()); // Bewegungsrichtung
		// umkehren
	}

	@Override
	protected void crashedByGround(VorlageLandschaft ground) {
		this.setVelocity(this.getVelocity().neg()); // Bewegungsrichtung
		// umkehren

	}

	@Override
	protected void crashedByObjekt(VorlageObjekte objekt) {
		if (objekt.isMoveable()) {
			objekt.blockWay(objekt.getShape().getTouchedSideOfThis(
					this.getShape()));
		}
		this.setVelocity(this.getVelocity().neg()); // Bewegungsrichtung
		// umkehren
	}

	@Override
	protected void crashedByPlayer(Figure player) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStandardsPerRound() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(Graphics g) {
		// TODO Auto-generated method stub

	}

}
