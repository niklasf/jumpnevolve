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

import net.phys2d.math.ROVector2f;
import net.phys2d.math.Vector2f;
import net.phys2d.raw.Body;
import net.phys2d.raw.World;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import com.googlecode.jumpnevolve.graphics.Drawable;
import com.googlecode.jumpnevolve.graphics.DrawablePollable;
import com.googlecode.jumpnevolve.graphics.ResourceManager;
import com.googlecode.jumpnevolve.graphics.effects.FogEmitterFactory;
import com.googlecode.jumpnevolve.graphics.effects.ParticleEffect;
import com.googlecode.jumpnevolve.graphics.effects.WaterfallEmitterFactory;
import com.googlecode.jumpnevolve.graphics.world.Camera;
import com.googlecode.jumpnevolve.graphics.world.SimulatedWorld;
import com.googlecode.jumpnevolve.graphics.world.WorldFactory;

/**
 * Ein einfaches Level zur Demonstration des Programms.
 * 
 * @author Niklas Fiekas
 */
public class DemoLevel implements WorldFactory {

	private LevelWorldFactory factory;
	
	private SimulatedWorld simulatedWorld;

	public DemoLevel(LevelWorldFactory world) {
		// Damit die Welt auf Anfrage zurückgeliefert werden kann
		this.factory = world;

		// Es folgt der Code, in dem Elemente in die Welt eingefügt werden.
		// Die WorldFactory Klassen sind geschachtelt:
		// - WorldFactory liefert keine Methoden
		// - BasicWorldFactory liefert einfache, allgemeine Methoden
		// - LevelWorldFactory liefert spielspezifische Methoden
		// - Diese Klasse erzeugt ein spezielles Level zur Demonstration
		
		// #1
		world.addWood(0, 0, 5f, 1.0f);
		
		// #2
		world.addWood(4.0f, 2.0f, 1.0f, 5.0f);
		world.addWood(5.0f, 6.0f, 3.0f, 1.0f);
		world.addWood(8.0f, 2.0f, 1.0f, 5.0f);
		
		// #3
		world.addWood(7.0f, 0.0f, 10.0f, 1.0f);
		world.addWood(7.0f, -3.2f, 5.0f, 1.0f);
		world.addSimpleFootSoldier(16.0f, 0.0f, SimpleFootSoldier.LEFT, 7.5f, 16.5f);
		world.addSimpleFootSoldier(11.0f, -3.2f);
		
		// #4
		world.addWood(17.0f, -2.0f, 1.0f, 1.0f);
		world.addWood(22.0f, -2.0f, 3.0f, 1.0f);
		world.addWood(26.0f, -2.0f, 1.0f, 1.0f);
		
		// #5
		world.addWood(29.0f, 0.0f, 5.0f, 1.0f);
		world.addSimpleFootSoldier(29.0f, 0.0f, SimpleFootSoldier.RIGHT, 29.5f, 33.5f);
		world.addSimpleFootSoldier(32.0f, 0.0f, SimpleFootSoldier.LEFT, 29.5f, 33.5f);
		world.addVerticalSlider(35.0f, 1.0f, 3.0f, 0.3f, 1.0f, 13.0f);
		
		// #6
		world.addWood(34.0f, -3.0f, 1.0f, 1.0f);
		world.addWood(35.5f, -4.0f, 1.0f, 1.0f);
		world.addWood(39.0f, -5.0f, 1.0f, 1.0f);
		
		// #7
		world.addStone(30.0f, 15.0f, 5.0f, 1.0f);
		world.addStone(37.0f, 18.0f, 1.0f, 1.0f);
		world.addStone(39.0f, 14.0f, 6.0f, 1.0f);
		world.addSimpleFootSoldier(39.0f, 14.0f, SimpleFootSoldier.RIGHT, 39.5f, 44.5f);
		world.addSimpleFootSoldier(44.0f, 14.0f, SimpleFootSoldier.LEFT, 39.5f, 44.5f);
		world.addStone(39.0f, 15.0f, 1.0f, 3.0f);
		world.addStone(44.5f, 18.0f, 1.0f, 1.0f);
		world.addHorizontalSlider(39.0f, 20.0f, 7.0f, 1.0f, HorizontalSlider.LEFT, 32.0f, 45.0f);
		
		// #8
		world.addStone(47.0f, 16.0f, 4.0f, 1.0f);
		world.addStone(53.0f, 15.0f, 2.0f, 2.0f);
		world.addStone(57.0f, 12.0f, 2.0f, 5.0f);
		
		// #9
		world.addStone(63.0f, 12.0f, 10.0f, 1.0f);
		world.addSimpleFootSoldier(63.0f, 12.0f, SimpleFootSoldier.RIGHT, 63.5f, null);
		world.addSimpleFootSoldier(67.0f, 12.0f);
		world.addStone(66.0f, 10.5f, 1.5f, 1.5f);
		world.addStone(70.0f, 10.5f, 2.5f, 1.5f);
		world.addStone(72.5f, 7.3f, 0.5f, 4.7f);
		world.addStone(79.0f, 12.0f, 1.0f, 1.0f);
		world.addStone(83.0f, 12.0f, 1.0f, 1.0f);
		
		// #10
		world.addStone(88.0f, 17.0f, 10.0f, 1.0f);
		world.addHorizontalSlider(73.0f, 20.0f, 4.0f, 1.0f, HorizontalSlider.LEFT, 73.0f, 90.0f);
		
		// #11
		world.addWood(40.0f, 0.0f, 4.0f, 1.0f);
		world.addWood(47.0f, -1.0f, 12.0f, 1.0f);
		
		// #12
		world.addWood(63.0f, -1.5f, 6.0f, 1.0f);
		world.addSimpleFootSoldier(63.0f, -1.5f, SimpleFootSoldier.RIGHT, 63.5f, null);
		world.addWood(68.0f, -2.5f, 2.0f, 1.0f);
		world.addWood(69.0f, -3.5f, 2.0f, 1.0f);
		world.addSimpleFootSoldier(69.0f, -3.5f, SimpleFootSoldier.RIGHT, 69.3f, null);
		world.addWood(70.0f, -4.5f, 3.0f, 1.0f);
		
		// #13
		world.addWood(76.0f, -4.5f, 4.0f, 1.0f);
		world.addWood(79.0f, -3.5f, 1.0f, 4.5f);
		world.addWood(79.0f, 1.0f, 6.0f, 1.0f);
		world.addWood(85.0f, -3.0f, 1.0f, 5.0f);
		world.addWood(85.0f, -4.0f, 12.0f, 1.0f);
		
		// #14
		world.addStone(97.0f, -1.0f, 1.0f, 1.0f);
		world.addVerticalSlider(98.0f, 15.0f, 2.0f, 0.3f, -1.0f, 17.0f);
		world.addSimpleFootSoldier(98.0f, -2.0f, SimpleFootSoldier.RIGHT, 98.5f, 99.5f);
		
		// #15
		world.addWood(98.0f, -4.0f, 4.0f, 1.0f);
		world.addWood(104.0f, -2.0f, 1.0f, 1.0f);
		world.addSimpleFootSoldier(104.0f, -2.0f, SimpleFootSoldier.RIGHT, 104.4f, 104.6f);
		world.addWood(106.0f, 0.0f, 1.0f, 1.0f);
		world.addWood(108.0f, 2.0f, 1.0f, 1.0f);
		world.addHorizontalSlider(108.0f, 1.0f, 4.0f, 1.0f, HorizontalSlider.LEFT, 110.0f, 130.0f);
		
		// #16
		world.addWood(117.0f, 5.0f, 1.0f, 4.0f);
		world.addWood(118.0f, 8.0f, 4.0f, 1.0f);
		world.addWood(122.0f, 5.0f, 1.0f, 4.0f);
		world.addWood(123.0f, 2.0f, 1.0f, 1.0f);
		world.addSimpleFootSoldier(119.0f, 4.0f, SimpleFootSoldier.RIGHT, null, null);
		world.addSimpleFootSoldier(122.0f, 4.0f, SimpleFootSoldier.RIGHT, null, null);
		
		// #17 - Ziel
		world.addWood(132.0f, 1.0f, 5.0f, 1.0f);
		world.addWood(133.0f, 0.0f, 4.0f, 1.0f);
		world.addJumpingSoldier(133.0f, 0.0f);
		
		// #18
		world.addHorizontalSlider(40.0f, -7.0f, 2.0f, 1.0f, HorizontalSlider.RIGHT, 40.0f, 46.0f);
		world.addWood(46.0f, -8.0f, 4.0f, 1.0f);
		
		// # 19
		world.addWood(48.0f, -11.0f, 1.5f, 1.0f);
		world.addWood(50.0f, -14.0f, 1.0f, 2.0f);
		world.addWood(51.0f, -13.0f, 4.0f, 1.0f);
		world.addJumpingSoldier(51.2f, -13.0f);
		world.addWood(58.0f, -13.0f, 4.0f, 1.0f);
		world.addVerticalSlider(61.0f, -14.0f, 1.0f, 0.3f, -23.0f, 0.0f);
		world.addSimpleFootSoldier(61.0f, -14.1f, SimpleFootSoldier.RIGHT, 61.4f, 61.6f);
		
		// #20
		world.addWood(62.0f, -25.0f, 18.0f, 1.0f);
		world.addWood(65.0f, -27.0f, 1.0f, 2.0f);
		world.addWood(79.0f, -28.0f, 1.0f, 3.0f);
		world.addSimpleFootSoldier(66.0f, -25.0f);
		world.addSimpleFootSoldier(68.0f, -25.0f);
		world.addSimpleFootSoldier(75.0f, -25.0f);
		world.addSimpleFootSoldier(78.0f, -25.0f);
		
		// #21
		world.addWood(85.0f, -26.0f, 1.0f, 1.0f);
		world.addWood(88.0f, -26.0f, 2.0f, 1.0f);
		world.addSimpleFootSoldier(88.0f, -26.0f, SimpleFootSoldier.RIGHT, 88.5f, 89.5f);
		world.addWood(93.0f, -27.0f, 2.0f, 1.0f);
		// Wasserfall
		world.setPosition(new Vector2f(97.5f, -40.0f));
		world.addDrop();
		world.setPosition(new Vector2f(97.6f, -41.0f));
		world.addDrop();
		
		final Body figure = world.addFigure(0, 0);
		
		// Nachdem die Einstellungen für die physikalische Welt erledigt sind,
		// kommen nun Einstellungen für das Level
		this.simulatedWorld = new SimulatedWorld(this);
		
		// Wasserfalleffekt einfügen
		this.simulatedWorld.add(new ParticleEffect(new Vector2f(97.5f, -1.0f), new FogEmitterFactory()));
		this.simulatedWorld.add(new ParticleEffect(new Vector2f(97.5f, -40.0f), new WaterfallEmitterFactory(36.0f)));
		
		// Unteres Wasser hinzufügen
		this.simulatedWorld.add(new DrawablePollable() {
			private float y = 0.0f;
			private float timeout = 0.0f;
			public void draw(Graphics g) {
				Color oldColor = g.getColor();
				g.setColor(new Color(0.0f, 0.0f, 1.0f, 0.4f));
				g.fillRect(-50.0f, 20.2f + this.y, 200.0f, 3.0f);
				g.setColor(oldColor);
			}

			public void poll(Input input, float secounds) {
				this.timeout -= secounds;
				if(this.timeout <= 0) {
					this.timeout = 0.2f;
					this.y = (float) ((Math.random() - 0.5) * 0.1);
				}
			}
		});
		
		// Hintergrund hinzufügen
		this.simulatedWorld.addBackground(new Drawable() {
			public void draw(Graphics g) {
				g.pushTransform();
				DemoLevel.this.simulatedWorld.doCameraTranslation(g);
			 	g.texture(new Rectangle(-50.0f, -40.0f, 200.0f, 60.0f), ResourceManager.getInstance().getImage("landscape.png"), true);
			 	g.popTransform();
			}
		});
		
		// Spielfigur
		
		// final Body figure = world.addFigure(117.0f, -30.0f);
		
		// Kamera Einstellung
		this.simulatedWorld.setCamera(new Camera() {
			public ROVector2f getPosition() {
				return figure.getPosition();
			}

			public void poll(Input input, float secounds) { }
		});
	}
	
	public SimulatedWorld getSimulatedWorld() {
		return this.simulatedWorld;
	}
	
	public World getWorld() {
		return this.factory.getWorld();
	}
}
