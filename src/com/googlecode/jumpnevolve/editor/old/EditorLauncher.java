package com.googlecode.jumpnevolve.editor.old;

import javax.swing.UIManager;

public class EditorLauncher {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			System.out.println("Ändern des LookAndFeel nicht möglich! " + e);
		}
		Editor x = new Editor();
	}

}
