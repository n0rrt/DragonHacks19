package com.entities;

import com.core.Main;
import com.core.Util;
import com.map.Tile;
import com.map.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Enemy {
	public BufferedImage image, attackImage;

	public String tempGeneralAttributes, tempMotionCycle;
	ArrayList<String> sizeAttributes, motionCycle, currentMotionCycle;

	public volatile double x, y, yVel, xVel, baseSpeed, effectiveSpeed, jumpPower, collisionBuffer, width, height, throwXPower, throwYPower, attackWidth, attackHeight, attackDamage, maxHealth, currentHealth = 1000000;
	public volatile Rectangle topHitBox, bottomHitBox, leftHitBox, rightHitBox;
	public volatile boolean isFalling, isStoppedTop, isStoppedBottom, isStoppedLeft, isStoppedRight, canAttack, shouldGoRight, shouldGoLeft, shoulfGoDown, shouldFollowPlayer, shouldJump, hasGravity, hasHitBox, isDeadly, despawnOnHit;
	public int moving, attackSpeed, spawnRate, spawnAmount, attackCooldown, attackAmount, touchDamage, heightInTiles, widthInTiles, attackLifetime, hitBoxBuffer = 3;

	public Enemy(BufferedImage image, int x, int y) {
		this.x = x;
		this.y = y;

		this.image = image;

		hitBoxBuffer = 3;



		heightInTiles = (int) Math.ceil(height / (double) Main.tileSize);
		widthInTiles = (int) Math.ceil(width / (double) Main.tileSize);

		width = Main.tileSize;
		height = Main.tileSize;
		attackCooldown = 100;
		maxHealth = 3;
		currentHealth = maxHealth;
		attackDamage = 3;
		touchDamage = 3;
		hasHitBox = true;
		isDeadly = true;
		attackWidth = Main.tileSize;
		attackHeight = Main.tileSize;
		baseSpeed = 1;
		effectiveSpeed = baseSpeed;
//		try {
//			attackImage = Util.loadImg(world.path + "res/" + atts[18][1].trim());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		attackLifetime = 500;
		despawnOnHit = true;

		if (hasHitBox) {
			topHitBox = new Rectangle((int) x + hitBoxBuffer, (int) y, (int) width - hitBoxBuffer * 2, hitBoxBuffer);
			bottomHitBox = new Rectangle((int) x + hitBoxBuffer, (int) y + (int) height - hitBoxBuffer, (int) width - hitBoxBuffer * 2, hitBoxBuffer);
			leftHitBox = new Rectangle((int) x, (int) y + hitBoxBuffer, hitBoxBuffer, (int) height - hitBoxBuffer * 2);
			rightHitBox = new Rectangle((int) x + (int) width - hitBoxBuffer, (int) y + hitBoxBuffer, hitBoxBuffer, (int) height - hitBoxBuffer * 2);
		}

	}

	public String toString(){
		return "(" +x +", " +y +")";
	}

	private void refine(World world) {
		width = Main.tileSize;
		height = Main.tileSize;
		attackCooldown = 100;
		maxHealth = 3;
		attackDamage = 3;
		touchDamage = 3;
		hasHitBox = true;
		isDeadly = true;
		attackWidth = Main.tileSize;
		attackHeight = Main.tileSize;
		baseSpeed = 2.5;
		effectiveSpeed = baseSpeed;
		currentHealth = maxHealth;
//		try {
//			attackImage = Util.loadImg(world.path + "res/" + atts[18][1].trim());
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		attackLifetime = 500;
		despawnOnHit = true;
	}

	public void render(Graphics2D g) {
		g.translate(x, y);
		g.drawImage(image, 0, 0, (int) width, (int) height, null);
		g.translate(-x, -y);

		// g.setColor(Color.YELLOW);
		// g.draw(topHitBox);
		// g.setColor(Color.ORANGE);
		// g.draw(bottomHitBox);
		// g.setColor(Color.BLUE);
		// g.draw(leftHitBox);
		// g.setColor(Color.GREEN);
		// g.draw(rightHitBox);
	}

	public void update() {
		if (currentHealth <= 0) {
			Main.world.currentFloor.currentRoom.enemiesToRemove.add(this);
		}

		isStoppedTop = false;
		isStoppedBottom = false;
		isStoppedLeft = false;
		isStoppedRight = false;

		for (Tile[] ta : Main.world.currentFloor.currentRoom.tiles) {
			for (Tile t : ta) {
				if (t.hasHitBox) {
					isStoppedTop = topHitBox.intersects(t.hitBox) || isStoppedTop;
					isStoppedBottom = bottomHitBox.intersects(t.hitBox) || isStoppedBottom;
					isStoppedLeft = leftHitBox.intersects(t.hitBox) || isStoppedLeft;
					isStoppedRight = rightHitBox.intersects(t.hitBox) || isStoppedRight;

				}

			}
		}

		double playerXDiff = Main.world.player.x - x;
		double playerYDiff = Main.world.player.y - y;

		xVel = playerXDiff<0?-baseSpeed/1.41:baseSpeed/1.41;
		yVel = playerYDiff<0?-baseSpeed/1.41:baseSpeed/1.41;

		if(xVel < 0 && isStoppedLeft){
			xVel = 0;
		}
		if(xVel > 0 && isStoppedRight){
			xVel = 0;
		}
		if(xVel < 0 && isStoppedTop){
			yVel = 0;
		}
		if(xVel > 0 && isStoppedBottom){
			yVel = 0;
		}

		x += xVel;
		y += yVel;

		xVel = 0;
		yVel = 0;

		topHitBox.setLocation((int) x + hitBoxBuffer, (int) y);
		bottomHitBox.setLocation((int) x + hitBoxBuffer, (int) y + (int) height - hitBoxBuffer);
		leftHitBox.setLocation((int) x, (int) y + hitBoxBuffer);
		rightHitBox.setLocation((int) x + (int) width - hitBoxBuffer, (int) y + hitBoxBuffer);

	}

	public boolean spawn() {
		int ix = (int) (Math.random() * Main.world.currentFloor.currentRoom.tiles.length);
		int iy = (int) (Math.random() * Main.world.currentFloor.currentRoom.tiles[0].length);
		Tile spawnTile = Main.world.currentFloor.currentRoom.tiles[ix][iy];
		if (spawnTile.hasHitBox) {
			return false;
		} else {
			try {
				for (int i = iy; i <= iy + heightInTiles; i++) {
					for (int ii = ix; ii <= ix + widthInTiles; ii++) {
						if (Main.world.currentFloor.currentRoom.tiles[i][ii].hasHitBox) {
							return false;
						}
					}
				}

			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}
		y = spawnTile.x;
		x = spawnTile.y;
		Main.world.currentFloor.currentRoom.enemiesToAdd.add(this);
		return true;

	}
}