package com.entities;

import com.core.Main;
import com.map.Tile;
import com.map.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Projectile {

	public double x, y, width, height, xVel, yVel, damage;
	public boolean hasGravity, isOnScreen, shouldSpawn, canHarmPlayer, canHarmEnemy, live;
	public BufferedImage image;
	public Rectangle hitBox;
	public int lifeTime;

	public Projectile(double x, double y, double xVel, double yVel, double width, double height, double damage, boolean canHarmPlayer, boolean canHarmEnemy, boolean hasGravity, int lifeTime, BufferedImage image) {
		this.x = x;
		this.y = y;

		this.xVel = xVel;
		this.yVel = yVel;

		this.width = width;
		this.height = height;

		this.damage = damage;

		this.canHarmPlayer = canHarmPlayer;
		this.canHarmEnemy = canHarmEnemy;

		this.hasGravity = hasGravity;

		this.lifeTime = lifeTime;

		this.image = image;

		hitBox = new Rectangle((int) x, (int) y, (int) width, (int) height);

		shouldSpawn = true;

		live = true;
	}

	public void render(Graphics2D g) {
		if (isOnScreen) {
			g.translate(x, y);
			g.drawImage(image, 0, 0, (int) width, (int) height, null);
			g.translate(-x, -y);
			//g.setColor(Color.CYAN);
			//g.draw(hitBox);
		}
	}

	Random r = new Random();

	public void update() {
		if (lifeTime <= 0) {
			Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
			return;
		}

		lifeTime -= 1;
		if ((hitBox.intersects(Main.world.player.topHitBox) || hitBox.intersects(Main.world.player.bottomHitBox) || hitBox.intersects(Main.world.player.leftHitBox) || hitBox.intersects(Main.world.player.rightHitBox)) && canHarmPlayer && live) {
			live = false;
			if (Main.world.player.currentHealth - damage >= Main.world.player.maxHealth) {
				Main.world.player.currentHealth = Main.world.player.maxHealth;
			} else if (Main.world.player.currentHealth - damage <= 0) {
				Main.world.player.currentHealth = 0;
			} else {
				Main.world.player.currentHealth -= damage;
			}
		}

		for(Enemy e:Main.world.currentFloor.currentRoom.enemies){
			if(e.topHitBox.intersects(hitBox) || e.bottomHitBox.intersects(hitBox) || e.leftHitBox.intersects(hitBox) || e.rightHitBox.intersects(hitBox)){
				e.currentHealth -= damage;
			}
		}

		if (true) {
			for (Tile[] ta : Main.world.currentFloor.currentRoom.tiles) {
				for (Tile t : ta) {
					if (t.hasHitBox) {
						if (t.hitBox.intersects(hitBox)) {
							xVel = 0;
							yVel = 0;
							live = false;
						}
					}
				}
			}
			y += yVel;
			x += xVel;
			hitBox.setBounds((int) x, (int) y, (int) width, (int) height);
		}
	}
}
