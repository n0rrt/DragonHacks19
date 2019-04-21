package com.ui;

import com.core.Main;
import com.core.Window;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Menu {

	public volatile ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
	public volatile BufferedImage bg = null;
	public int buttonBuffer = 30;

	public Menu(BufferedImage bg) {
		this.bg = bg;
	}

	public void render(Graphics2D g) {
		g.drawImage(bg, (int) Main.window.xOrigin, (int) Main.window.yOrigin, Main.screenSize.width, Main.screenSize.height, null);
		for (int i = 0; i < buttons.size(); i++) {
			AffineTransform t = g.getTransform();
			MenuButton menuButton = buttons.get(i);
			menuButton.render(g);
			g.setTransform(t);
		}
	}

	public void update(Point mousePos) throws IOException {
		mousePos.x = mousePos.x;
		mousePos.y = mousePos.y;
		for (int i = 0; i < buttons.size(); i++) {
			MenuButton menuButton = buttons.get(i);
			if (menuButton.hitBox.contains(mousePos)) {
				menuButton.onClick();
			}
		}
	}

//	public void addButton(Object image, String id, double x, double y, double width, double height) {
//		buttons.add(new MenuButton(image, id, x, y, width, height));
//	}
	public void addButton(MenuButton mb){
		buttons.add(mb);
	}
}