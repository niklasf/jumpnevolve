package com.googlecode.jumpnevolve.tests.game;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

import com.googlecode.jumpnevolve.game.Level;
import com.googlecode.jumpnevolve.game.Levelloader;
import com.googlecode.jumpnevolve.graphics.SwingEngine;

public class SwingTest {

	public static void main(String[] args) {

		final SwingEngine engine = SwingEngine.getInstance();

		JFrame frame = new JFrame("Canvas test");
		frame.setSize(800, 600);
		frame.setVisible(true);
		frame.setLayout(new BorderLayout());

		JButton button = new JButton("Reset");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Level level = Levelloader
						.asyncLoadLevel("resources/levels/level.txt");
				engine.switchState(level);
				engine.requestFocus();
			}
		});
		frame.add(BorderLayout.EAST, button);

		frame.add(engine);
		engine.start();
	}
}
