package com.googlecode.jumpnevolve.game.objects;

import org.newdawn.slick.Graphics;

import com.googlecode.jumpnevolve.game.Figure;
import com.googlecode.jumpnevolve.game.VorlageGegner;
import com.googlecode.jumpnevolve.game.VorlageLandschaft;
import com.googlecode.jumpnevolve.game.VorlageObjekte;
import com.googlecode.jumpnevolve.graphics.world.World;
import com.googlecode.jumpnevolve.math.Vector;

public class NormalGround extends VorlageLandschaft {

	public NormalGround(byte type, Vector position, Vector dimension,
			World worldOfThis) {
		super(type, position, dimension, worldOfThis);
	}

	@Override
	protected void crashedByEnemy(VorlageGegner gegner) {
		gegner
				.blockWay(gegner.getShape().getTouchedSideOfThis(
						this.getShape()));
	}

	@Override
	protected void crashedByGround(VorlageLandschaft ground) {
		// Nichts tun

	}

	@Override
	protected void crashedByObjekt(VorlageObjekte objekt) {
		if (objekt.isMoveable()) {
			objekt.blockWay(objekt.getShape().getTouchedSideOfThis(
					this.getShape()));
		}
		// Sonst nichts tun
	}

	@Override
	protected void crashedByPlayer(Figure player) {
		player
				.blockWay(player.getShape().getTouchedSideOfThis(
						this.getShape()));

	}

	@Override
	public void setStandardsPerRound() {
		// Nichts tun

	}

	@Override
	public void draw(Graphics g) {
		// TODO Zeichnen mit Textur und nach jeweiliger Größe

	}

}
