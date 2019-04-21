package com.map;

import com.core.Main;
import com.core.Util;
import com.entities.Enemy;
import com.entities.Projectile;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Room {
    public int id;

    public static volatile Tile[][] tiles = null;

    public static volatile ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    public static volatile ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
    public static volatile ArrayList<Projectile> projectilesToAdd = new ArrayList<Projectile>();

    public static volatile ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public static volatile ArrayList<Enemy> enemiesToAdd = new ArrayList<Enemy>();
    public static volatile ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();

    public Room(int id){
        this.id = id;
    }

    public void genContents(){
        tiles = new Tile[13][13];
        for(int x = 0; x < 13; x++){
            for(int y = 0; y < 13; y++){
                BufferedImage tempTileImage = null;
                try {
                    tempTileImage = Util.loadImg("res/level/tile.png");
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
        for (Tile[] ta : tiles) {
            for (Tile t : ta) {
                t.isOnScreen = Main.world.worldHitBox.contains(t.x, t.y);
            }
        }

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
