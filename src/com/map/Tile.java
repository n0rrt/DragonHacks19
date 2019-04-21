package com.map;

import com.core.Main;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

	public volatile BufferedImage image;
	public volatile boolean hasHitBox, isOnScreen;
	public volatile int x, y;
	public volatile Rectangle hitBox;

	public static final int TILESIZE = 40;

	public Tile(BufferedImage image, boolean hasHitBox, int x, int y) {
		this.image = image;
		this.hasHitBox = hasHitBox;
		this.x = x;
		this.y = y;

		if (hasHitBox) {
			hitBox = new Rectangle(this.x, this.y, Main.tileSize, Main.tileSize);
			
		}
	}

	public void render(Graphics2D g) {
		g.drawImage(image, x, y, Main.tileSize, Main.tileSize, null);
		//if(hasHitBox){
			//g.setColor(Color.RED);
			//g.draw(hitBox);
		//}
	}
}