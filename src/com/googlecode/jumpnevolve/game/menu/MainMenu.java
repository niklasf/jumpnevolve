package com.googlecode.jumpnevolve.game.menu;

import java.io.IOException;

import com.googlecode.jumpnevolve.editor2.Editor2;
import com.googlecode.jumpnevolve.graphics.Engine;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;

public class MainMenu extends Menu {

	private SimpleSubMenu mainState;

	public MainMenu(String levelPath) {
		super();
		this.mainState = new SimpleSubMenu(this, "MainMenu");
		super.addSubMenu(mainState);
		this.addSubMenu(new LevelSelection(this, levelPath));
		this.addDirectButton("Editor", InterfaceFunctions.MENU_EDITOR);
		// this.switchTo("LevelSelection1");
	}

	public void addSubMenu(SubMenu menu) {
		this.mainState.addEntry(menu.name, menu);
		super.addSubMenu(menu);
	}

	public void addDirectButton(String name, InterfaceFunction function) {
		this.mainState.addEntry(name, function);
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		// Zum SubMenu switchen, wenn die Funktion des Objekts das SubMenu ist
		// TODO: Besser beim Loslassen der Maustaste die Aktion ausführen
		InterfaceFunction function = object.getFunction();
		if (function instanceof SubMenu) {
			this.switchTo(function.getFunctionName().split("_")[1]);
		} else if (function == InterfaceFunctions.MENU_EDITOR) {
			try {
				Engine.getInstance()
						.switchState(
								new Editor2(this, this.getWidth(), this
										.getHeight(), 1));
			} catch (IOException e) {
				System.out.println("Fehler beim Erstellen des Editors: "
						+ e.getMessage());
				e.printStackTrace();
			}
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
