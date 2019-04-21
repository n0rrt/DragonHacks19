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
	public String type;

	public Projectile(double x, double y, double xVel, double yVel, double width, double height, double damage, boolean canHarmPlayer, boolean canHarmEnemy, boolean hasGravity, int lifeTime, BufferedImage image, String type) {
		this.x = x;
		this.y = y;

		this.type = type;

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
		if (isOnScreen && type != "earth") {
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
			if(this.type == "fire"){
				explodeFire();
			}
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

		if (!Main.world.worldHitBox.intersects(hitBox)){
			if(this.type == "fire") {
				explodeFire();
				Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
			}
		}

		for(Enemy e:Main.world.currentFloor.currentRoom.enemies){
			if(this.type == "lightning"){
				if(e.topHitBox.intersects(hitBox) || e.bottomHitBox.intersects(hitBox) || e.leftHitBox.intersects(hitBox) || e.rightHitBox.intersects(hitBox)){
					e.currentHealth -= damage;
					Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
				}
			}
			if(this.type == "wind"){
				if(e.topHitBox.intersects(hitBox)){
					e.y += 160;
					Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
				}
				if(e.bottomHitBox.intersects(hitBox)){
					e.y -= 160;
					Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
				}
				if(e.leftHitBox.intersects(hitBox)){
					e.x += 160;
					Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
				}
				if(e.rightHitBox.intersects(hitBox)){
					e.x -= 160;
					Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
				}

				e.x = e.x<40?80:e.x;
				e.y = e.y<40?80:e.y;
				e.x = e.x>640?640:e.x;
				e.y = e.y>640?640:e.y;
			}

			if(this.type == "fire"){
				if(e.topHitBox.intersects(hitBox) || e.bottomHitBox.intersects(hitBox) || e.leftHitBox.intersects(hitBox) || e.rightHitBox.intersects(hitBox)){
					e.currentHealth -= damage;
					explodeFire();
					Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
				}
			}
			if(this.type == "firedrop"){
				if(e.topHitBox.intersects(hitBox) || e.bottomHitBox.intersects(hitBox) || e.leftHitBox.intersects(hitBox) || e.rightHitBox.intersects(hitBox)){
					e.currentHealth -= damage;
					Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
				}
			}
			if(this.type == "earth"){
				System.out.println("Making earth");
				double t1x = Main.world.player.x;
				double t1y = Main.world.player.y;
				double t2x = Main.world.player.x;
				double t2y = Main.world.player.y;

				if(Main.world.player.xDir == 0){
					t1x -= 20;
					t2x += 20;
					if(Main.world.player.yDir < 0){
						t1y -= 80;
						t2y -= 80;
					} else{
						t1y += 80;
						t2y += 80;
					}
				} else{
					t1y -= 20;
					t2y += 20;
					if(Main.world.player.xDir < 0){
						t1x -= 80;
						t2x -= 80;
					} else{
						t1x += 80;
						t2x += 80;
					}
				}

				System.out.println("Player: (" +Main.world.player.x +", " +Main.world.player.y +")");
				System.out.println("Tile 1: (" +t1x +", " +t1y +")");
				System.out.println("Tile 2: (" +t2x +", " +t2y +")");

				Tile t1 = new Tile(image, true, (int)t1y, (int)t1x, 200);
				Tile t2 = new Tile(image, true, (int)t2y, (int)t2x, 200);
				Main.world.currentFloor.currentRoom.tempTilesToAdd.add(t1);
				Main.world.currentFloor.currentRoom.tempTilesToAdd.add(t2);
				Main.world.currentFloor.currentRoom.projectilesToRemove.add(this);
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

	public void explodeFire(){
		Projectile f1 = new Projectile(x, y+41, 0, 5, width/2, height/2, damage/2, false, true, false, 5, image, "firedrop");
		Projectile f2 = new Projectile(x, y-41, 0, -5, width/2, height/2, damage/2, false, true, false, 5, image, "firedrop");
		Projectile f3 = new Projectile(x-41, y, -5, 0, width/2, height/2, damage/2, false, true, false, 5, image, "firedrop");
		Projectile f4 = new Projectile(x+41, y, 5, 0, width/2, height/2, damage/2, false, true, false, 5, image, "firedrop");
		Projectile f5 = new Projectile(x+41, y-41, 3, -3, width/2, height/2, damage/2, false, true, false, 5, image, "firedrop");
		Projectile f6 = new Projectile(x-41, y-41, -3, -3, width/2, height/2, damage/2, false, true, false, 5, image, "firedrop");
		Projectile f7 = new Projectile(x-41, y+41, -3, 3, width/2, height/2, damage/2, false, true, false, 5, image, "firedrop");
		Projectile f8 = new Projectile(x+41, y+41, 3, 3, width/2, height/2, damage/2, false, true, false, 5, image, "firedrop");

		Main.world.currentFloor.currentRoom.projectilesToAdd.add(f1);
		Main.world.currentFloor.currentRoom.projectilesToAdd.add(f2);
		Main.world.currentFloor.currentRoom.projectilesToAdd.add(f3);
		Main.world.currentFloor.currentRoom.projectilesToAdd.add(f4);
		Main.world.currentFloor.currentRoom.projectilesToAdd.add(f5);
		Main.world.currentFloor.currentRoom.projectilesToAdd.add(f6);
		Main.world.currentFloor.currentRoom.projectilesToAdd.add(f7);
		Main.world.currentFloor.currentRoom.projectilesToAdd.add(f8);
	}
}
