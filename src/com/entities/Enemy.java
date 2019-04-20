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

	public volatile double x, y, yVel, xVel, baseSpeed, effectiveSpeed, jumpPower, collisionBuffer, width, height, throwXPower, throwYPower, attackWidth, attackHeight, attackDamage, maxHealth, currentHealth;
	public volatile Rectangle topHitBox, bottomHitBox, leftHitBox, rightHitBox;
	public volatile boolean isFalling, isStoppedTop, isStoppedBottom, isStoppedLeft, isStoppedRight, canAttack, shouldGoRight, shouldGoLeft, shoulfGoDown, shouldFollowPlayer, shouldJump, hasGravity, hasHitBox, isDeadly, despawnOnHit;
	public int moving, attackSpeed, spawnRate, spawnAmount, attackCooldown, attackAmount, touchDamage, heightInTiles, widthInTiles, attackLifetime, hitBoxBuffer = 3;

	public Enemy(BufferedImage image, String tempGeneralAttributes, String tempMotionCycle, World world) {
		this.image = image;
		this.tempGeneralAttributes = tempGeneralAttributes;
		this.tempMotionCycle = tempMotionCycle;

		refine(world);

		hitBoxBuffer = 3;

		currentHealth = maxHealth;

		heightInTiles = (int) Math.ceil(height / (double) Main.tileSize);
		widthInTiles = (int) Math.ceil(width / (double) Main.tileSize);

		if (hasHitBox) {
			topHitBox = new Rectangle((int) x + hitBoxBuffer, (int) y, (int) width - hitBoxBuffer * 2, hitBoxBuffer);
			bottomHitBox = new Rectangle((int) x + hitBoxBuffer, (int) y + (int) height - hitBoxBuffer, (int) width - hitBoxBuffer * 2, hitBoxBuffer);
			leftHitBox = new Rectangle((int) x, (int) y + hitBoxBuffer, hitBoxBuffer, (int) height - hitBoxBuffer * 2);
			rightHitBox = new Rectangle((int) x + (int) width - hitBoxBuffer, (int) y + hitBoxBuffer, hitBoxBuffer, (int) height - hitBoxBuffer * 2);
		}

	}

	private void refine(World world) {
		String[][] atts = Util.splitAtLinesAndString(tempGeneralAttributes, ":");
		spawnAmount = Integer.parseInt(atts[0][1].trim());
		spawnRate = Integer.parseInt(atts[1][1].trim());
		width = Integer.parseInt(atts[2][1].trim());
		height = Integer.parseInt(atts[3][1].trim());
		throwXPower = Integer.parseInt(atts[4][1].trim());
		throwYPower = Integer.parseInt(atts[5][1].trim());
		attackAmount = Integer.parseInt(atts[6][1].trim());
		attackCooldown = Integer.parseInt(atts[7][1].trim());
		jumpPower = Integer.parseInt(atts[8][1].trim());
		maxHealth = Integer.parseInt(atts[9][1].trim());
		attackDamage = Integer.parseInt(atts[10][1].trim());
		touchDamage = Integer.parseInt(atts[11][1].trim());
		hasGravity = Boolean.parseBoolean(atts[12][1].trim());
		hasHitBox = Boolean.parseBoolean(atts[13][1].trim());
		isDeadly = Boolean.parseBoolean(atts[14][1].trim());
		attackWidth = Integer.parseInt(atts[15][1].trim());
		attackHeight = Integer.parseInt(atts[16][1].trim());
		baseSpeed = Integer.parseInt(atts[17][1].trim());
		effectiveSpeed = baseSpeed;
		try {
			attackImage = Util.loadImg(world.path + "res/" + atts[18][1].trim());
		} catch (IOException e) {
			e.printStackTrace();
		}
		attackLifetime = Integer.parseInt(atts[19][1].trim());
		despawnOnHit = Boolean.parseBoolean(atts[20][1].trim());
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
			World.enemiesToRemove.add(this);
		}
		isStoppedTop = false;
		isStoppedBottom = false;
		isStoppedLeft = false;
		isStoppedRight = false;

		for (Tile[] ta : World.tiles) {
			for (Tile t : ta) {
				if (t.hasHitBox) {
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

		x += xVel;
		y += yVel;

		isFalling = !isStoppedBottom;

		topHitBox.setLocation((int) x + hitBoxBuffer, (int) y);
		bottomHitBox.setLocation((int) x + hitBoxBuffer, (int) y + (int) height - hitBoxBuffer);
		leftHitBox.setLocation((int) x, (int) y + hitBoxBuffer);
		rightHitBox.setLocation((int) x + (int) width - hitBoxBuffer, (int) y + hitBoxBuffer);

	}

	public boolean spawn() {
		int ix = (int) (Math.random() * World.tiles.length);
		int iy = (int) (Math.random() * World.tiles[0].length);
		Tile spawnTile = World.tiles[ix][iy];
		if (spawnTile.hasHitBox) {
			return false;
		} else {
			try {
				for (int i = iy; i <= iy + heightInTiles; i++) {
					for (int ii = ix; ii <= ix + widthInTiles; ii++) {
						if (World.tiles[i][ii].hasHitBox) {
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
		World.enemiesToAdd.add(this);
		return true;

	}
}