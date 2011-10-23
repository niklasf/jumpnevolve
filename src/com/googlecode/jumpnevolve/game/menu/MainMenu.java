package com.googlecode.jumpnevolve.game.menu;

import com.googlecode.jumpnevolve.graphics.gui.InterfaceObject;
import com.googlecode.jumpnevolve.math.Shape;

public class MainMenu extends Menu {

	private SimpleSubMenu mainState;

	public MainMenu(String levelPath) {
		super();
		this.mainState = new SimpleSubMenu(this, "MainMenu");
		super.addSubMenu(mainState);
		this.addSubMenu(new LevelSelection(this, levelPath));
		// this.switchTo("LevelSelection1");
	}

	public void addSubMenu(SubMenu menu) {
		this.mainState.addEntry(menu.name, menu);
		super.addSubMenu(menu);
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		// Zum SubMenu switchen, wenn die Funktion des Objekts das SubMenu ist
		// TODO: Besser beim Loslassen der Maustaste die Aktion ausführen
		if (object.function instanceof SubMenu) {
			this.switchTo(object.function.getFunctionName().split("_")[1]);
		}
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		// Nichts tun
	}

	@Override
	public void objectIsSelected(InterfaceObject object) {
		// Nichts tun
	}

	@Override
	public void switchBackToMainState() {
		this.curState = this.mainState;
	}

}
