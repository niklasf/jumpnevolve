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

import com.googlecode.jumpnevolve.graphics.world.BasicWorldFactory;

/**
 * Methoden die beim Erzeugen der Welt helfen, aber mehr aufs Spiel zielen, als
 * auf eine abstrakte Schnittstelle werden in dieser Klasse bereitgestellt.
 * 
 * @author Niklas Fiekas
 */
public class LevelWorldFactory extends BasicWorldFactory {
	public void addWood(float startX, float startY, float x, float y) {
		addAxisAlignedWall(new Wood(), startX, startY, x, y);
	}
	
	public void addStone(float startX, float startY, float x, float y) {
		addAxisAlignedWall(new Stone(), startX, startY, x, y);
	}
	
	public Body addFigure(float footX, float footY) {
		float oldMass = setMass(Figure.MASS);
		ROVector2f oldPosition = setPosition(new Vector2f(footX + Figure.WIDTH / 2.0f, footY - Figure.HEIGHT / 2.0f));
		Body figure = addAxisAlignedBox(new Figure(), Figure.WIDTH, Figure.HEIGHT);
		setMass(oldMass);
		setPosition(oldPosition);
		return figure;
	}
	
	public Body addSimpleFootSoldier(float footX, float footY, short dir, Float leftBorder, Float rightBorder) {
		float oldMass = setMass(SimpleFootSoldier.MASS);
		ROVector2f oldPosition = setPosition(new Vector2f(footX + SimpleFootSoldier.WIDTH / 2.0f, footY - SimpleFootSoldier.HEIGHT / 2.0f));
		Body soldier = addAxisAlignedBox(new SimpleFootSoldier(dir, leftBorder, rightBorder), SimpleFootSoldier.WIDTH, SimpleFootSoldier.HEIGHT);
		setMass(oldMass);
		setPosition(oldPosition);
		return soldier;
	}
	
	public Body addSimpleFootSoldier(float footX, float footY) {
		return addSimpleFootSoldier(footX, footY, SimpleFootSoldier.LEFT, null, null);
	}
	
	public Body addArmouredFootSoldier(float footX, float footY, short dir, Float leftBorder, Float rightBorder) {
		float oldMass = setMass(SimpleFootSoldier.MASS);
		ROVector2f oldPosition = setPosition(new Vector2f(footX + SimpleFootSoldier.WIDTH / 2.0f, footY - SimpleFootSoldier.HEIGHT / 2.0f));
		Body soldier = addAxisAlignedBox(new ArmouredFootSoldier(dir, leftBorder, rightBorder), SimpleFootSoldier.WIDTH, SimpleFootSoldier.HEIGHT);
		setMass(oldMass);
		setPosition(oldPosition);
		return soldier;
	}
	
	public Body addVerticalSlider(float footX, float footY, float x, float y, float upperBorder, float lowerBorder) {
		float oldMass = setMass(VerticalSlider.MASS);
		ROVector2f oldPosition = setPosition(new Vector2f(footX + x / 2.0f, footY + y / 2.0f));
		Body slider = addAxisAlignedBox(new VerticalSlider(upperBorder, lowerBorder), x, y);
		setMass(oldMass);
		setPosition(oldPosition);
		return slider;
	}
	
	public Body addHorizontalSlider(float footX, float footY, float x, float y, short dir, float leftBorder, float rightBorder) {
		float oldMass = setMass(HorizontalSlider.MASS);
		ROVector2f oldPosition = setPosition(new Vector2f(footX + x / 2.0f, footY + y / 2.0f));
		Body slider = addAxisAlignedWall(new HorizontalSlider(dir, leftBorder, rightBorder), x, y);
		setMass(oldMass);
		setPosition(oldPosition);
		return slider;
	}
	
	public Body addJumpingSoldier(float footX, float footY) {
		float oldMass = setMass(JumpingSoldier.MASS);
		ROVector2f oldPosition = setPosition(new Vector2f(footX + JumpingSoldier.WIDTH / 2.0f, footY - JumpingSoldier.HEIGHT / 2.0f));
		Body soldier = addAxisAlignedBox(new JumpingSoldier(), JumpingSoldier.WIDTH, JumpingSoldier.HEIGHT);
		setMass(oldMass);
		setPosition(oldPosition);
		return soldier;
	}
	
	public Body addDrop() {
		float oldMass = setMass(Drop.MASS);
		Body drop = addBall(new Drop(), Drop.RADIUS);
		setMass(oldMass);
		return drop;
	}
}
