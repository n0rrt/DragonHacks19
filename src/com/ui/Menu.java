package com.ui;

import com.core.Main;
import com.core.Util;
import com.core.Window;
import com.map.World;
import res.menu.AbstractMenu;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Menu extends AbstractMenu {

//	public volatile ArrayList<MenuButton> buttons = new ArrayList<MenuButton>();
//	public volatile BufferedImage bg = null;
	public int buttonBuffer = 30;

	public Menu(BufferedImage bg) {
		super(bg);

		// Create Some Buttons
		Runnable selectAction = new Runnable() {
			@Override
			public void run(){
				try {
					Main.world = new World();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Main.isInLevel = true;
				Main.currentMenu = null;
			}
		};
		try {
			MenuButton selectLevelButton = new MenuButton(Util.loadImg("res/menu/menuButton.png"), "selectlevel", Main.screenSize.getWidth() / 2 - 100, Main.screenSize.getHeight() / 2 - 110, 200, 100, selectAction);
			addButton(selectLevelButton);
		}catch(IOException e){
			e.printStackTrace();
		}

		Runnable exitAction = new Runnable() {
			@Override
			public void run(){
				System.exit(0);
			}
		};
		try{
			MenuButton exitGameButton = new MenuButton(Util.loadImg("res/menu/menuButton.png"), "exitgame", Main.screenSize.getWidth() / 2 - 100, Main.screenSize.getHeight() / 2 + 50, 200, 100,exitAction);
			addButton(exitGameButton);
		}catch( IOException e){
			e.printStackTrace();
		}

	}

//	public void render(Graphics2D g) {
//		g.drawImage(bg, (int) Main.window.xOrigin, (int) Main.window.yOrigin, Main.screenSize.width, Main.screenSize.height, null);
//		for (int i = 0; i < buttons.size(); i++) {
//			AffineTransform t = g.getTransform();
//			MenuButton menuButton = buttons.get(i);
//			menuButton.render(g);
//			g.setTransform(t);
//		}
//	}
//
//	public void update(Point mousePos) throws IOException {
//		mousePos.x = mousePos.x;
//		mousePos.y = mousePos.y;
//		for (int i = 0; i < buttons.size(); i++) {
//			MenuButton menuButton = buttons.get(i);
//			if (menuButton.hitBox.contains(mousePos)) {
//				menuButton.onClick();
//			}
//		}
//	}

//	public void addButton(Object image, String id, double x, double y, double width, double height) {
//		buttons.add(new MenuButton(image, id, x, y, width, height));
//	}
	public void addButton(MenuButton mb){
		buttons.add(mb);
	}
}