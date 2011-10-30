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
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

/**
 * @author niklas
 * 
 */
public class SlickEngine extends AppGameContainer implements AbstractEngine {

	private static SlickEngine instance;

	/**
	 * @return Die geteilte Instanz der Grafikengine.
	 * 
	 * @throws GraphicsError
	 *             Wenn ein OpenGL Fehler beim ersten Erzeugen der Instanz
	 *             auftrat
	 */
	public static SlickEngine getInstance() {
		if (instance == null) {
			try {
				instance = new SlickEngine(new StateBasedGame("") {
					@Override
					public void initStatesList(GameContainer container)
							throws SlickException {
					}
				});
			} catch (SlickException e) {
				throw new GraphicsError(e);
			}
			Engine.makeCurrent(instance);
		}
		return instance;
	}

	private StateBasedGame states;

	private SlickEngine(StateBasedGame states) throws SlickException {
		super(states);

		this.states = states;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.graphics.AbstractEngine#addState(com.googlecode
	 * .jumpnevolve.graphics.AbstractState)
	 */
	public void addState(AbstractState state) {
		if (!containsState(state)) {
			this.states.addState(state);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.graphics.AbstractEngine#containsState(com.
	 * googlecode.jumpnevolve.graphics.AbstractState)
	 */
	public boolean containsState(AbstractState state) {
		return this.states.getState(state.getID()) != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.googlecode.jumpnevolve.graphics.AbstractEngine#switchState(com.googlecode
	 * .jumpnevolve.graphics.AbstractState)
	 */
	public void switchState(AbstractState state) {
		if (this.states.getState(state.getID()) == null) {
			this.states.addState(state);
		}
		if (this.states.getCurrentStateID() != state.getID()) {
			this.states.enterState(state.getID());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.jumpnevolve.graphics.AbstractEngine#getCurrentState()
	 */
	public AbstractState getCurrentState() {
		GameState state = this.states.getCurrentState();
		if (!(state instanceof AbstractState)) {
			throw new RuntimeException(
					"The current state does not inherit AbstractState.");
		}
		return (AbstractState) state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.googlecode.jumpnevolve.graphics.AbstractEngine#start()
	 */
	@Override
	public void start() {
		// Vollbildmodus starten
		try {
			super.setDisplayMode(1000, 800, false);
		} catch (SlickException e) {
			throw new GraphicsError(e);
		}

		// SplashScreen schließen
		// TODO: Erst nach dem Laden erledigen
		SplashScreen splash = SplashScreen.getSplashScreen();
		if (splash != null) {
			try {
				splash.close();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}
		}

		// Anwendung starten
		try {
			super.start();
		} catch (SlickException e) {
			throw new GraphicsError(e);
		}
	}
}
