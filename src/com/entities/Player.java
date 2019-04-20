package com.entities;

import com.core.Main;
import com.core.Window;
import com.map.Tile;
import com.map.World;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

public class Player {

	public volatile BufferedImage image;
	public volatile double x, y, yVel, xVel, baseSpeed, effectiveSpeed, jumpPower, collisionBuffer, width, height, playerBuffer, throwXPower, throwYPower, attackWidth, attackHeight, attackDamage, maxHealth, currentHealth;
	public volatile Rectangle topHitBox, bottomHitBox, leftHitBox, rightHitBox;
	public volatile boolean isFalling, isStoppedTop, isStoppedBottom, isStoppedLeft, isStoppedRight, canAttack;
	public int moving, attackSpeed, attackCooldown, hitBoxBuffer = 3;

	public Player(BufferedImage image, int x, int y) {

		playerBuffer = 3;

		width = Main.tileSize/1.5; //playerBuffer;
		height = Main.tileSize/1.5; //* 1.25 - playerBuffer;

		collisionBuffer = 2;

		baseSpeed = 1.75;
		effectiveSpeed = baseSpeed;
		jumpPower = 8;

		isFalling = true;

		throwXPower = 3.5;
		throwYPower = -5;

		attackWidth = Main.tileSize / 2;
		attackHeight = Main.tileSize / 2;

		attackSpeed = 10;
		attackCooldown = 0;

		attackDamage = 1;

		maxHealth = 100.0;
		currentHealth = maxHealth;

		moving = 1;

		this.image = image;
		this.x = x;
		this.y = y;

		topHitBox = new Rectangle(x + hitBoxBuffer, y, (int) width - hitBoxBuffer * 2, hitBoxBuffer);
		bottomHitBox = new Rectangle(x + hitBoxBuffer, y + (int) height - hitBoxBuffer, (int) width - hitBoxBuffer * 2, hitBoxBuffer);
		leftHitBox = new Rectangle(x, y + hitBoxBuffer, hitBoxBuffer, (int) height - hitBoxBuffer * 2);
		rightHitBox = new Rectangle(x + (int) width - hitBoxBuffer, y + hitBoxBuffer, hitBoxBuffer, (int) height - hitBoxBuffer * 2);

	}

	public void render(Graphics2D g) {
		g.translate(x, y);
		g.drawImage(image, 0, 0, (int) width, (int) height, null);
		g.translate(-x, -y);
		//g.setColor(Color.YELLOW);
		//g.draw(topHitBox);
		//g.setColor(Color.ORANGE);
		//g.draw(bottomHitBox);
		//g.setColor(Color.BLUE);
		//g.draw(leftHitBox);
		//g.setColor(Color.GREEN);
		//g.draw(rightHitBox);
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
		for (Tile[] ta : World.tiles) {
			for (Tile t : ta) {
				if (t.hasHitBox && t.isOnScreen) {
					isStoppedTop = topHitBox.intersects(t.hitBox) || isStoppedTop;
					isStoppedBottom = bottomHitBox.intersects(t.hitBox) || isStoppedBottom;
					isStoppedLeft = leftHitBox.intersects(t.hitBox) || isStoppedLeft;
					isStoppedRight = rightHitBox.intersects(t.hitBox) || isStoppedRight;
				}

			}
		}
		if ((isStoppedLeft && isStoppedRight && isStoppedBottom)) {
			y -= collisionBuffer;
		}

		xVel = 0;

		if (isStoppedBottom || isStoppedTop) {
			yVel = 0;
			if (isStoppedTop) {
				y += collisionBuffer;
			}
		}
		if (isFalling) {
			yVel -= World.gravity;
		}

		if (Window.keys[KeyEvent.VK_W] && !isStoppedTop && isStoppedBottom) {
			yVel = -jumpPower;
		}
		if (Window.keys[KeyEvent.VK_A] && !isStoppedLeft) {
			xVel = -effectiveSpeed;
			moving = -1;
		}
		if (Window.keys[KeyEvent.VK_D] && !isStoppedRight) {
			xVel = effectiveSpeed;
			moving = 1;
		}
		if (Window.keys[KeyEvent.VK_SPACE] && canAttack) {
			World.projectilesToAdd.add(new Projectile(x, y, throwXPower * moving, throwYPower - (Math.random() + 1) * 2, attackWidth, attackHeight, attackDamage, true, true, true, 150, image));
			canAttack = false;
			attackCooldown = attackSpeed;

		}
		x += xVel;
		y += yVel;

		isFalling = !isStoppedBottom;

		Window.xOrigin = x - Main.screenSize.width / 2;
		Window.yOrigin = y - (Main.screenSize.height / 3) * 2;

		topHitBox.setLocation((int) x + hitBoxBuffer, (int) y);
		bottomHitBox.setLocation((int) x + hitBoxBuffer, (int) y + (int) height - hitBoxBuffer);
		leftHitBox.setLocation((int) x, (int) y + hitBoxBuffer);
		rightHitBox.setLocation((int) x + (int) width - hitBoxBuffer, (int) y + hitBoxBuffer);
	}
}