/**
 * 
 */
package com.googlecode.jumpnevolve.game;

import org.newdawn.slick.Input;

import com.googlecode.jumpnevolve.graphics.Pollable;

/**
 * @author Erik Wagner
 * 
 */
public interface SpecialPollable extends Pollable {

	public void startRound(Input input);

	public void endRound();
}
