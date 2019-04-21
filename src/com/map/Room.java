package com.map;

import com.core.Main;

import com.core.Util;
import com.entities.Enemy;
import com.entities.Projectile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Room {
    public int id;

    public static volatile Tile[][] tiles = null;

    public int width;
    public int height;

    public volatile ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    public volatile ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
    public volatile ArrayList<Projectile> projectilesToAdd = new ArrayList<Projectile>();

    public static volatile ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public static volatile ArrayList<Enemy> enemiesToAdd = new ArrayList<Enemy>();
    public static volatile ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();

    public Room(int id, int width, int height){
        this.id = id;
        this.width = width;
        this.height = height;
    }

    public void genContents(){
        tiles = new Tile[width][height];
        for(int x = 0; x < width; x++){
            for(int y = 0; y < height; y++){
                BufferedImage tempTileImage = null;
                try {
                    String imgStr = "res/level/tile" +((int)(Math.random() * 4) + 1) +".png";
                    tempTileImage = Util.loadImg(imgStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Tile tempTile = new Tile(tempTileImage, false, Main.tileSize*x, Main.tileSize*y);
                tiles[x][y] = tempTile;
            }
        }
    }

    public void render(Graphics2D g) throws IOException {
        for (Tile[] ta : tiles) {
            for (Tile t : ta) {
                t.render(g);
            }
        }

        for (Projectile p : projectiles) {
            p.render(g);
        }
        for (Enemy e : enemies) {
            e.render(g);
        }
    }
    public void update() throws InterruptedException {
        for (Projectile p : projectiles) {
            p.isOnScreen = Main.world.worldHitBox.contains(p.x, p.y);
            p.update();
        }

        for (Enemy e : enemies) {
            e.update();
        }

        for (Projectile p : projectilesToRemove) {
            projectiles.remove(p);
        }

        for (Projectile p : projectilesToAdd) {
            projectiles.add(p);
            System.out.println(p);
            System.out.println(projectiles.toString());
        }

        for (Enemy e : enemiesToAdd) {
            enemies.add(e);
        }

        for (Enemy e : enemiesToRemove) {
            enemies.remove(e);
        }

        projectilesToRemove.clear();
        projectilesToAdd.clear();
        enemiesToAdd.clear();
        enemiesToRemove.clear();
    }

}
