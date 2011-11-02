package com.googlecode.jumpnevolve.game.menu;

import com.googlecode.jumpnevolve.graphics.gui.Informable;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunction;
import com.googlecode.jumpnevolve.graphics.gui.InterfaceFunctions;
import com.googlecode.jumpnevolve.graphics.gui.InterfacePart;
import com.googlecode.jumpnevolve.graphics.gui.container.GridContainer;
import com.googlecode.jumpnevolve.graphics.gui.container.HeadlineContainer;
import com.googlecode.jumpnevolve.graphics.gui.container.InterfaceContainer;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceObject;
import com.googlecode.jumpnevolve.graphics.gui.objects.InterfaceTextButton;
import com.googlecode.jumpnevolve.math.Shape;
import com.googlecode.jumpnevolve.math.Vector;

/**
 *
 * @author Erik Wagner
 *
 *         TODO: Einträge in ein SubMenu ermöglichen (TextButtonList)
 */
public abstract class SubMenu extends HeadlineContainer implements Informable,
		InterfaceFunction {

	public final Menu parent;
	public final String name;

	public SubMenu(Menu parent, InterfaceContainer mainCon, String name) {
		super(new GridContainer(1, 1, GridContainer.MODUS_X_RIGHT,
				GridContainer.MODUS_Y_UP), mainCon);
		this.parent = parent;
		this.name = name;
		InterfaceTextButton b = new InterfaceTextButton(
				InterfaceFunctions.MENU_BACKTOMAINMENU, "Hauptmenü");
		b.addInformable(this);
		((GridContainer) this.getHeadlineContainer()).add(b, 0, 0);
		mainCon.maximizeSize();
		this.getHeadlineContainer().maximizeXRange();
	}

	@Override
	public void mouseClickedAction(InterfaceObject object) {
		if (object.function == InterfaceFunctions.MENU_BACKTOMAINMENU) {
			this.parent.switchBackToMainState();
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
	public String getKindOfParent() {
		return "SubMenu";
	}

	@Override
	public String getFunctionName() {
		return "SubMenu_" + this.name;
	}

	@Override
	public String getClassNameForEditor() {
		return null;
	}
}
