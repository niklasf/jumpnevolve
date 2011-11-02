package com.googlecode.jumpnevolve.game.menu;

import java.util.ArrayList;

import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextButton;

public class SimpleSubMenu extends SubMenu {

	private ArrayList<InterfaceTextButton> entrys = new ArrayList<InterfaceTextButton>();

	public SimpleSubMenu(Menu parent, String name) {
		super(parent, new GridContainer(1, 1), name);
	}

	private void createCon() {
		GridContainer con = new GridContainer(this.entrys.size(), 1);
		for (int i = 0; i < this.entrys.size(); i++) {
			con.add(this.entrys.get(i), i, 0);
		}
		con.maximizeSize();
		this.setMainContainer(con);
	}

	public void addEntry(String name, InterfaceFunction function) {
		this.entrys.add(new InterfaceTextButton(function, name));
		this.createCon();
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
