package com.map;

import com.core.Util;
import com.entities.Enemy;
import com.entities.Projectile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Room {
    public static volatile Tile[][] tiles = null;

    public static final int TILELEGNTH = 13;

    public static volatile ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    public static volatile ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
    public static volatile ArrayList<Projectile> projectilesToAdd = new ArrayList<Projectile>();

    public static volatile ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public static volatile ArrayList<Enemy> enemiesToAdd = new ArrayList<Enemy>();
    public static volatile ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();

    public void genTiles(){
        for( int r = 0; r < TILELEGNTH; r++)
            for ( int c = 0; c < TILELEGNTH; c++){
                try{
                    tiles[r][c] = new Tile(Util.loadImg("res/Time/tilePlaceHolder.png"),false,r* Tile.TILESIZE,c * Tile.TILESIZE);
                }catch(IOException e1){
                    e1.printStackTrace();
                }
            }
    }

    public void render(Graphics2D g) throws IOException {
        for (Tile[] ta : tiles) {
            for (Tile t : ta) {
                if (t.isOnScreen) {
                    t.render(g);
                }
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
                t.isOnScreen = World.worldHitBox.contains(t.x, t.y);
            }
        }

        for (Projectile p : projectiles) {
            p.isOnScreen = World.worldHitBox.contains(p.x, p.y);
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
