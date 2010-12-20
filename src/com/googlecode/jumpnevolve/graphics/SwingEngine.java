package com.googlecode.jumpnevolve.graphics;

import org.newdawn.slick.CanvasGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class SwingEngine extends CanvasGameContainer implements AbstractEngine {

	private static final long serialVersionUID = -4831927693282260285L;
	
	private static SwingEngine instance;
	
	public static SwingEngine getInstance() {
		if(instance == null) {
			try {
				instance = new SwingEngine(new StateBasedGame("") {
					@Override
					public void initStatesList(GameContainer arg0)
							throws SlickException {
					}
				});
			} catch (SlickException e) {
				e.printStackTrace();
			}
			Engine.makeCurrent(instance);
		}
		return instance;
	}
	
	private StateBasedGame states;
	
	private SwingEngine(StateBasedGame states) throws SlickException {
		super(states);
		
		this.states = states;
		
		this.getContainer().setAlwaysRender(true);
	}

	@Override
	public void addState(AbstractState state) {
		if(!this.containsState(state)) {
			this.states.addState(state);
		}
	}

	@Override
	public boolean containsState(AbstractState state) {
		return this.states.getState(state.getID()) != null;
	}

	@Override
	public AbstractState getCurrentState() {
		return (AbstractState) this.states.getCurrentState();
	}

	@Override
	public int getScreenHeight() {
		return this.getContainer().getScreenHeight();
	}

	@Override
	public int getScreenWidth() {
		return this.getContainer().getScreenWidth();
	}

	@Override
	public void switchState(AbstractState state) {
		if (this.states.getState(state.getID()) == null) {
			this.states.addState(state);
		}
		if (this.states.getCurrentStateID() != state.getID()) {
			this.states.enterState(state.getID());
		}
	}

	@Override
	public void start() {
		try {
			super.start();
		} catch (SlickException e) {
			throw new GraphicsError(e);
		}
	}
}
