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

package com.googlecode.jumpnevolve.graphics;

import java.awt.SplashScreen;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.util.Log;

/**
 * @author niklas
 * 
 */
public class Engine extends AppGameContainer {
	
	/**
	 * Die gewünschte Anzahl an Frames pro Sekunde.
	 */
	public static final int TARGET_FPS = 60;

	private static Engine instance;

	/**
	 * @return Die geteilte Instanz der Grafikengine.
	 * 
	 * @throws GraphicsError
	 *             Wenn ein OpenGL Fehler beim ersten Erzeugen der Instanz
	 *             auftrat
	 */
	public static Engine getInstance() {
		if (instance == null) {
			try {
				instance = new Engine(new StateBasedGame("") {
					@Override
					public void initStatesList(GameContainer container)
							throws SlickException {
					}
				});
			} catch (SlickException e) {
				throw new GraphicsError(e);
			}
		}
		return instance;
	}

	private boolean fullscreen = true;

	private StateBasedGame states;

	private Engine(StateBasedGame states) throws SlickException {
		// Mit leerer Zustandsliste initialisieren
		super(states);
		this.states = states;

		// Einstellungen
		setTargetFrameRate(TARGET_FPS);
	}

	/**
	 * Fügt einen Zustand hinzu, sodass er später aufgerufen werden kann. Wird
	 * ein existierender Zustand hinzugefügt, wird keine Aktion ausgeführt.
	 * 
	 * @param state
	 *            Neuer Zustand der Grafikengine.
	 */
	public void addState(AbstractState state) {
		if (!containsState(state)) {
			this.states.addState(state);
		}
	}

	/**
	 * Fragt ab, ob ein Zustand bereits hinzugefügt wurde.
	 * 
	 * @param state
	 *            Der zu prüfende Zustand
	 * @return {@code true}, wenn der Zustand hinzugefügt wurde.
	 */
	public boolean containsState(AbstractState state) {
		return this.states.getState(state.getID()) != null;
	}

	@Override
	public void start() {
		// Vollbildmodus starten
		try {
			super.setDisplayMode(800, 600, this.fullscreen);
		} catch (SlickException e) {
			throw new GraphicsError(e);
		}
		
		// SplashScreen schließen
		SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash != null) {
			// Künstlich warten
			if(this.fullscreen) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException ex) {	}
			}
			
			try {
				splash.close();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		} else {
			Log.warn("No Java Splash Screen had been created");
		}

		// Anwendung starten
		try {
			super.start();
		} catch (SlickException e) {
			throw new GraphicsError(e);
		}
	}

	/**
	 * Bereitet den neuen Zustand vor, beendet den alten und startet den neuen.
	 * Der alte Zustand wird nicht neu initialisiert, wenn neuer und alter
	 * Zustand die selben sind.
	 * 
	 * @param state
	 *            Der neue Zustand
	 */
	public void switchState(AbstractState state) {
		// Zustand laden
		if (this.states.getState(state.getID()) == null) {
			this.states.addState(state);
		}
		
		// Zustand wechseln
		// Das passiert mit einem weißen Blitz.
		// TODO: Übergang allgemeiner machen
		if (this.states.getCurrentStateID() != state.getID()) {
			this.states.enterState(state.getID(), null, new FadeInTransition(Color.white, 1000));
		}
	}
}
