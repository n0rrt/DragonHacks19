package com.ui;

import com.core.Main;
import com.core.Window;
import com.map.World;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class MenuButton {
	public volatile String id, imgString = "";
	public volatile double x, y, width, height, stringWidth, stringHeight, stringButtonBuffer;
	public static volatile double fontSize = 108;
	public volatile Rectangle hitBox = null;
	public volatile BufferedImage image = null;
	public volatile boolean shouldRender, isImage = false;

	public MenuButton(Object image, String id, double x, double y, double width, double height) {
		if (image instanceof String) {
			isImage = false;
			this.imgString = (String) image;
		} else {
			isImage = true;
			this.image = (BufferedImage) image;
		}
		this.shouldRender = true;
		this.id = id;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.hitBox = new Rectangle((int) this.x , (int) this.y, (int) this.width, (int) this.height);
	}

	public void render(Graphics2D g) {
		if (Main.isLevelSelectionScreen && id.startsWith("level:")) {
			Rectangle r = new Rectangle(0, 100, (int) Main.screenSize.getWidth(), (int) Main.screenSize.getHeight() - 300 - (int)fontSize);
			shouldRender = r.contains(new Point((int) x, (int) y));
		}else {
			
		}
		if (shouldRender) {
			AffineTransform old = g.getTransform();
			
			g.translate(x + Window.xOrigin, y + Window.yOrigin);
			if (!isImage) {
				g.setColor(Color.ORANGE.darker());
				g.setFont(g.getFont().deriveFont((float) fontSize));
				g.drawString(imgString, 0, g.getFontMetrics().getHeight() / 2 + 10);
				hitBox.setBounds((int) this.x, (int) this.y, g.getFontMetrics().stringWidth(imgString),
						g.getFontMetrics().getHeight() - 30);
			} else {
				g.drawImage(image, 0, 0, (int) width, (int) height, null);
			}
			g.setTransform(old);
		}
	}

	public void onClick() throws IOException {
		if (id.toLowerCase().trim().equals("selectlevel")) {
			Main.currentMenu = null;
			Main.loadLevelSelectMenu();
		} else if (id.toLowerCase().trim().equals("levelselectback")) {
			Main.currentMenu = null;
			Main.loadMainMenu();
		} else if (id.toLowerCase().trim().equals("exitgame")) {
			System.exit(0);
		} else if (id.toLowerCase().trim().equals("resumegame")) {
			Main.pause();
		} else if (id.toLowerCase().trim().equals("mainmenu")) {
			Main.isPaused = false;
			Main.isInLevel = false;
			Main.currentMenu = null;
			Main.loadMainMenu();
			World.clear();
		} else if (id.toLowerCase().trim().startsWith("level:")) {
			Main.loadWorld(id.substring(6));
			Main.isPaused = false;
			Main.isLevelSelectionScreen = false;
			Main.isInLevel = true;
			Main.currentMenu = null;
		}
	}
}