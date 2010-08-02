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

package com.googlecode.jumpnevolve.tests.graphics;

import java.net.MalformedURLException;
import java.net.URL;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

/**
 * <p>
 * Testfall für die Slick Soundbibliothek. Dieser Test kann nicht automatisch
 * durchgeführt werden.
 * </p>
 * 
 * <p>
 * Erwartetes Verhalten: Der Beispielsound wird abgespielt.
 * </p>
 * 
 * @author Niklas Fiekas
 */
public class SoundTest {
	public static void main(String[] args) {
		try {
			new Sound(
					new URL("http://upload.wikimedia.org/wikipedia/commons/c/c8/Example.ogg")).play();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
