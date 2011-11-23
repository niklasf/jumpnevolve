package com.googlecode.jumpnevolve.game.menu;

import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.container.TextButtonList;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextButton;

public class SimpleSubMenu extends SubMenu {

	private TextButtonList buttonList = new TextButtonList(6, 10);

	public SimpleSubMenu(Menu parent, String name) {
		super(parent, new GridContainer(1, 1), name);
		((GridContainer) this.getMainContainer()).add(this.buttonList, 0, 0);
	}

	public void addEntry(String name, InterfaceFunction function) {
		this.buttonList.addTextButton(new InterfaceTextButton(function, name));
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		// Nichts tun
	}

	@Override
	public void mouseOverAction(InterfaceObject object) {
		// Nichts tun
	}

	@Override
	public void objectIsSelected(InterfaceObject object) {
		// Nichts tun
	}
}
