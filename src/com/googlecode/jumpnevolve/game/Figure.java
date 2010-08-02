/*
 * Copyright (C) 2010 Erik Wagner and Niklas Fiekas
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, see <http://www.gnu.org/licenses/>.
 */

package com.googlecode.jumpnevolve.game;

import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.BodyList;
import net.phys2d.raw.CollisionEvent;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.Log;

import com.googlecode.jumpnevolve.graphics.AbstractState;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.world.BasicEntity;

/**
 * @author Erik Wagner
 */
public class Figure extends BasicEntity {

	public static final float MASS = 80.0f;
	public static final float WIDTH = 0.5f;
	public static final float HEIGHT = 1.5f;

	private boolean couldTryToJump = false;

	@Override
	public void draw(Graphics g) {
		g.pushTransform();
		
		// Die Figur zeichnen
		g.scale(0.6f / AbstractState.ZOOM, 0.49f / AbstractState.ZOOM);
		if (this.body.getVelocity().getX() >= 0) { // Bewegung lank rechts
			g.drawImage(ResourceManager.getInstance().getImage(
					"figure-cross.png"), -64, -60);
		} else { // Bewegung nach links
			g.drawImage(ResourceManager.getInstance().getRevertedImage(
					"figure-cross.png"), -66, -60);
		}
		
		g.popTransform();
	}

	@Override
	public void poll(Input input, float secounds) {
		// TODO: Programmsteuerung hier entfernen
		
		// Programm beenden
		if (input.isKeyDown(Input.KEY_ESCAPE)) {
			Log.info("Exit scheduled.");
			Engine.getInstance().exit();
		}
		
		// Neu anfangen
		if (input.isKeyDown(Input.KEY_0)) {
			Engine.getInstance().switchState(
					new DemoLevel(new LevelWorldFactory()).getSimulatedWorld());
		}

		// Nach rechts gehen
		if (input.isKeyDown(Input.KEY_RIGHT)) {
			this.body.addForce(new Vector2f(240.0f * 3, 0.0f));
		}
		
		// Nach unten gehen
		if (input.isKeyDown(Input.KEY_LEFT)) {
			this.body.addForce(new Vector2f(-240.0f * 3, 0.0f));
		}
		
		// Springen
		if (input.isKeyDown(Input.KEY_SPACE)) {
			boolean canJump = false;

			// Man kann nur Springen, wenn man auf einem anderen Objekt steht
			if (this.couldTryToJump) {
				BodyList touching = this.body.getTouching();
				for (int i = 0; i < touching.size(); i++) {
					if (touching.get(i).getPosition().getY() > this.body
							.getPosition().getY() + 0.7f) {
						canJump = true;
						break;
					}
				}
			}

			// Große Kraft nach oben einmalig anwenden
			if (canJump) {
				this.body.addForce(new Vector2f(0.0f, -40000.0f));
				this.couldTryToJump = false;
			}
		}

		// Untere Grenze:
		// Man stirbt, wenn man unten ins Wasser fällt.
		if (this.body.getPosition().getY() > 20.0f) {
			Engine.getInstance().switchState(
					new DemoLevel(new LevelWorldFactory()).getSimulatedWorld());
		}
	}

	@Override
	public void collisionOccured(CollisionEvent event, Body other) {
		// Wenn man von oben auf ein Objekt trifft, kann man möglicherweise
		// springen.
		if (event.getPoint().getY() > this.body.getPosition().getY() + 0.5f
				&& Math.abs(event.getNormal().getX()) != 1.0f) {
			this.couldTryToJump = true;
		}
		
		if (this.world.entityForBody(other) instanceof ArmouredFootSoldier) {
			// Gegner, bei dem man stirbt, sobald man ihn berührt
			Engine.getInstance().switchState(
					new DemoLevel(new LevelWorldFactory()).getSimulatedWorld());
			
		} else if (this.world.entityForBody(other) instanceof SimpleFootSoldier) {
			// Man stirbt, wenn man nicht richtig trifft
			if (Math.abs(event.getNormal().getX()) == 1
					|| other.getPosition().getY() < this.body.getPosition()
							.getY()) {
				Engine.getInstance().switchState(
						new DemoLevel(new LevelWorldFactory())
								.getSimulatedWorld());
			
				// Und überlebt, wenn man draufspringt
			} else {
				this.world.remove(other);
			}
		} else if (this.world.entityForBody(other) instanceof JumpingSoldier) {
			
			// Wenn man einen springenden Gegner trifft, stirbt man auch
			Engine.getInstance().switchState(
					new DemoLevel(new LevelWorldFactory()).getSimulatedWorld());
		}
	}

	@Override
	public void init(Body body) {
		super.init(body);
		body.setCanRest(false);
		body.setMaxVelocity(3.0f, 100.0f);
	}
}
