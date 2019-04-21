package com.map;

import com.core.Main;
import com.core.Util;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Tile {

	public volatile BufferedImage image;
	public volatile boolean hasHitBox, isOnScreen;
	public volatile int x, y;
	public volatile Rectangle hitBox;
	public int lifeTime = 0;
	public Runnable touchAction;

	public Tile(BufferedImage image, boolean hasHitBox, int x, int y) {
		this.image = image;
		this.hasHitBox = hasHitBox;
		this.x = x;//+80;
		this.y = y;//+80;

		//if (hasHitBox) {
		hitBox = new Rectangle(this.x, this.y, Main.tileSize, Main.tileSize);

		//}
	}

	public Tile(BufferedImage image, boolean hasHitBox, int x, int y, Runnable touchAction) {
		this.image = image;
		this.hasHitBox = hasHitBox;
		this.x = x;//+80;
		this.y = y;//+80;
		this.touchAction = touchAction;
		//if (hasHitBox) {
		hitBox = new Rectangle(this.x, this.y, Main.tileSize, Main.tileSize);

		//}
	}

	public Tile(BufferedImage image, boolean hasHitBox, int x, int y, int lifeTime) {
		this.image = image;
		this.hasHitBox = hasHitBox;
		this.x = x;//+80;
		this.y = y;//+80;
		this.lifeTime = lifeTime;
		//if (hasHitBox) {
			hitBox = new Rectangle(this.x, this.y, Main.tileSize, Main.tileSize);

		//}
	}

	public void render(Graphics2D g) {
		Util.drawImage(g, image, x, y, Main.tileSize, Main.tileSize);
		//if(hasHitBox){
			g.setColor(Color.RED);
			g.draw(hitBox);
		//}
	}
	public void update(){
		if(lifeTime < 0){
			Main.world.currentFloor.currentRoom.tempTilesToRemove.add(this);
		} else{
			lifeTime--;
		}
	}
}