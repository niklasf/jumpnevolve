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

import com.jdotsoft.jarloader.JarClassLoader;

/**
 * Lädt und startet die Anwendung aus dem bin-Verzeichnis oder einem Archiv.
 * 
 * @author Niklas Fiekas
 */
public class JumpnevolveLauncher {

	/**
	 * @param args
	 *            Kommandozeilenargumente werden an {@link Jumpnevolve}
	 *            weitergegeben.
	 */
	public static void main(String[] args) {

		// Der JarClassLoader kann auch native Bibliotheken aus einem Archiv
		// laden, wenn das nötig ist.
		try {
			new JarClassLoader().invokeMain(
					"com.googlecode.jumpnevolve.game.Jumpnevolve", args);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

}
