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

import com.googlecode.jumpnevolve.game.menu.MainMenu;
import com.googlecode.jumpnevolve.graphics.AbstractEngine;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.util.Parameter;

/**
 * @author niklas
 * 
 */
public class Jumpnevolve {

	/**
	 * Startet das Spiel.
	 * 
	 * @param args
	 *            Kommandozeilenargumente
	 */
	public static void main(String[] args) {
		AbstractEngine engine = Engine.getInstance();

		engine.setTargetFrameRate(Parameter.GAME_FPS_TARGET);
		engine.switchState(new MainMenu("resources/levels/"));
		engine.start();
	}

}
