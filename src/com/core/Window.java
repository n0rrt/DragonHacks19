package com.core;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ConcurrentModificationException;

@SuppressWarnings("serial")
public class Window extends JPanel {

	public static volatile double xOrigin, yOrigin = 0;
	public static volatile boolean[] keys = new boolean[65536];

	public Window() {

	}

	@Override
	protected void paintComponent(Graphics gr) {
		Graphics2D g = (Graphics2D) gr;
		super.paintComponent(g);
		g.translate(-xOrigin, -yOrigin);
		try {
			if (Main.isInLevel) {
				try {
					Main.world.render(g);
				} catch (ConcurrentModificationException e) {

				}
			} else {
				AffineTransform t = g.getTransform();
				if (Main.isPaused) {
					Main.world.render(g);
				}
				g.setTransform(t);
				Main.currentMenu.render(g);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}