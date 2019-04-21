package com.entities;

import com.core.Main;
import com.core.Util;
import com.core.Window;
import com.map.Tile;
import com.map.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player {

	public volatile BufferedImage image;
	public volatile double x, y, yVel, xVel, baseSpeed, effectiveSpeed, jumpPower, collisionBuffer, width, height, playerBuffer, throwXPower, throwYPower, attackWidth, attackHeight, attackDamage, maxHealth, currentHealth;
	public volatile Rectangle topHitBox, bottomHitBox, leftHitBox, rightHitBox;
	public volatile boolean isFalling, isStoppedTop, isStoppedBottom, isStoppedLeft, isStoppedRight, canAttack;
	public int yDir, xDir, attackSpeed, attackCooldown, hitBoxBuffer = 3;

	public Player(int x, int y) {

		playerBuffer = 3;

		width = Main.tileSize;
		height = Main.tileSize*1.5;

		collisionBuffer = 2;

		baseSpeed = 3;
		effectiveSpeed = baseSpeed;
		jumpPower = 8;

		isFalling = true;

		throwXPower = 3.5;
		throwYPower = -5;

		attackWidth = Main.tileSize;
		attackHeight = Main.tileSize;

		attackSpeed = 100;
		attackCooldown = 0;

		attackDamage = 1;

		maxHealth = 100.0;
		currentHealth = maxHealth;

		yDir = 1;
		xDir = 0;

		try {
			this.image = Util.loadImg("res/player/player_front.png");
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.x = x + Main.window.xOrigin*2;
		this.y = y + Main.window.yOrigin*2;

		topHitBox = new Rectangle(x + hitBoxBuffer, y, (int) width - hitBoxBuffer * 2, hitBoxBuffer);
		bottomHitBox = new Rectangle(x + hitBoxBuffer, y + (int) height - hitBoxBuffer, (int) width - hitBoxBuffer * 2, hitBoxBuffer);
		leftHitBox = new Rectangle(x, y + hitBoxBuffer, hitBoxBuffer, (int) height - hitBoxBuffer * 2);
		rightHitBox = new Rectangle(x + (int) width - hitBoxBuffer, y + hitBoxBuffer, hitBoxBuffer, (int) height - hitBoxBuffer * 2);

	}

	public void render(Graphics2D g) {
		g.translate(x, y);
		g.drawImage(image, 0, 0, (int) width, (int) height, null);
		g.translate(-x, -y);
//		g.setColor(Color.YELLOW);
//		g.draw(topHitBox);
//		g.setColor(Color.ORANGE);
//		g.draw(bottomHitBox);
//		g.setColor(Color.BLUE);
//		g.draw(leftHitBox);
//		g.setColor(Color.GREEN);
//		g.draw(rightHitBox);
	}

	public void update() {
		if (attackCooldown <= 0) {
			canAttack = true;
		} else {
			attackCooldown -= 1;
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

		isStoppedTop = !topHitBox.intersects(Main.world.worldHitBox) || isStoppedTop;
		isStoppedBottom = !bottomHitBox.intersects(Main.world.worldHitBox) || isStoppedBottom;
		isStoppedLeft = !leftHitBox.intersects(Main.world.worldHitBox) || isStoppedLeft;
		isStoppedRight = !rightHitBox.intersects(Main.world.worldHitBox) || isStoppedRight;

		if ((isStoppedLeft && isStoppedRight && isStoppedBottom)) {
			y -= collisionBuffer;
		}

		xVel = 0;

//		if (isStoppedBottom || isStoppedTop) {
//			yVel = 0;
//			if (isStoppedTop) {
//				y += collisionBuffer;
//			}
//		}

		//---=== MOVEMENT ===---
		if (Window.keys[KeyEvent.VK_W] && !isStoppedTop) {
			yVel = -effectiveSpeed;
			yDir = -1;
			xDir = 0;
			try {
				this.image = Util.loadImg("res/player/player_back.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (Window.keys[KeyEvent.VK_S] && !isStoppedBottom) {
			yVel = effectiveSpeed;
			yDir = 1;
			xDir = 0;
			try {
				this.image = Util.loadImg("res/player/player_front.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (Window.keys[KeyEvent.VK_A] && !isStoppedLeft) {
			xVel = -effectiveSpeed;
			xDir = -1;
			yDir = 0;
			try {
				this.image = Util.loadImg("res/player/player_left.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (Window.keys[KeyEvent.VK_D] && !isStoppedRight) {
			xVel = effectiveSpeed;
			xDir = 1;
			yDir = 0;
			try {
				this.image = Util.loadImg("res/player/player_right.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (canAttack) {
			if(Window.keys[KeyEvent.VK_NUMPAD0]) {
				makeAttack(0);
			} else if(Window.keys[KeyEvent.VK_NUMPAD1]) {
				makeAttack(1);
			} else if(Window.keys[KeyEvent.VK_NUMPAD2]) {
				makeAttack(2);
			} else if(Window.keys[KeyEvent.VK_NUMPAD3]) {
				makeAttack(3);
			}

		}
		x += xVel;
		y += yVel;

		xVel = 0;
		yVel = 0;

//		Main.window.xOrigin = x - Main.screenSize.width / 2;
//		Main.window.yOrigin = y - (Main.screenSize.height / 3) * 2;

		topHitBox.setLocation((int) x + hitBoxBuffer, (int) y);
		bottomHitBox.setLocation((int) x + hitBoxBuffer, (int) y + (int) height - hitBoxBuffer);
		leftHitBox.setLocation((int) x, (int) y + hitBoxBuffer);
		rightHitBox.setLocation((int) x + (int) width - hitBoxBuffer, (int) y + hitBoxBuffer);
	}

	public void makeAttack(int type){
		if(type == 0){
			BufferedImage fireImage = null;
			try {
				fireImage = Util.loadImg("res/spells/fireball.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Projectile fireProjectile = new Projectile(x, y, 6*xDir, 6*yDir, attackWidth, attackHeight, 2, false, true, false, 40, fireImage, "fire");
			Main.world.currentFloor.currentRoom.projectilesToAdd.add(fireProjectile);
		} else if(type == 1){
			BufferedImage lightningImage = null;
			try {
				lightningImage = Util.loadImg("res/spells/lightningbolt.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Projectile lightningProjectile = new Projectile(x, y, 6*xDir, 6*yDir, attackWidth, attackHeight, 4, false, true, true, 40, lightningImage, "lightning");
			Main.world.currentFloor.currentRoom.projectilesToAdd.add(lightningProjectile);
		} else if(type == 2){
			BufferedImage windImage = null;
			try {
				windImage = Util.loadImg("res/spells/wind.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Projectile windProjectile = new Projectile(x, y, 6*xDir, 6*yDir, attackWidth, attackHeight, 0, false, true, true, 25, windImage, "wind");
			Main.world.currentFloor.currentRoom.projectilesToAdd.add(windProjectile);
		} else if(type == 3){
			BufferedImage earthImage = null;
			try {
				earthImage = Util.loadImg("res/spells/stone.png");
			} catch (IOException e) {
				e.printStackTrace();
			}
			Projectile earthProjectile = new Projectile(x, y, 1*xDir, 1*yDir, attackWidth, attackHeight, attackDamage, false, true, true, 1500, earthImage, "earth");
			Main.world.currentFloor.currentRoom.projectilesToAdd.add(earthProjectile);
		}
			canAttack = false;
		attackCooldown = attackSpeed;
	}
}