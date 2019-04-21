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
    public static volatile int boardOffset = 80;


    public int width;
    public int height;

    public volatile ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
    public volatile ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
    public volatile ArrayList<Projectile> projectilesToAdd = new ArrayList<Projectile>();

    public static volatile ArrayList<Enemy> enemies = new ArrayList<Enemy>();
    public static volatile ArrayList<Enemy> enemiesToAdd = new ArrayList<Enemy>();
    public static volatile ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();

    public static volatile ArrayList<Tile> tempTiles = new ArrayList<Tile>();
    public static volatile ArrayList<Tile> tempTilesToAdd = new ArrayList<Tile>();
    public static volatile ArrayList<Tile> tempTilesToRemove = new ArrayList<Tile>();

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
                Tile tempTile = new Tile(tempTileImage, false, Main.tileSize*x + boardOffset, Main.tileSize*y + boardOffset);
                tiles[x][y] = tempTile;
            }
        }
    }

    public void spawnEnemies(){
        try {// This was more just memeing about
            BufferedImage temp_im = Math.random() > .5 ? Util.loadImg("res/enemy/red_wizard.png") : Util.loadImg("res/enemy/dog.png");
            Enemy e = new Enemy(temp_im, Main.world.currentFloor.currentRoom.tiles[14][14].x, Main.world.currentFloor.currentRoom.tiles[14][14].y);
            temp_im = Math.random() > .5 ? Util.loadImg("res/enemy/red_wizard.png") : Util.loadImg("res/enemy/dog.png");
            Enemy e1 = new Enemy(temp_im, Main.world.currentFloor.currentRoom.tiles[0][0].x, Main.world.currentFloor.currentRoom.tiles[0][0].y);
            temp_im = Math.random() > .5 ? Util.loadImg("res/enemy/red_wizard.png") : Util.loadImg("res/enemy/dog.png");
            Enemy e2 = new Enemy(temp_im, Main.world.currentFloor.currentRoom.tiles[14][0].x, Main.world.currentFloor.currentRoom.tiles[14][0].y);
            temp_im = Math.random() > .5 ? Util.loadImg("res/enemy/red_wizard.png") : Util.loadImg("res/enemy/dog.png");
            Enemy e3 = new Enemy(temp_im, Main.world.currentFloor.currentRoom.tiles[0][14].x, Main.world.currentFloor.currentRoom.tiles[0][14].y);
            enemiesToAdd.add(e);
            enemiesToAdd.add(e1);
            enemiesToAdd.add(e2);
            enemiesToAdd.add(e3);
        } catch (IOException e1) {
            e1.printStackTrace();
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
        for (Tile t : tempTiles) {
            t.render(g);
        }
    }
    public void update() throws InterruptedException {

        if(enemies.size() == 0){
            Runnable ladderAction = new Runnable() {
                @Override
                public void run() {
                    Main.isInLevel = false;
                    Main.currentMenu = Main.menus[1];
                }
            };
            try {
                Tile ladderTile = new Tile(Util.loadImg("res/level/ladder.png"), true,580, 580, ladderAction);
                tempTilesToAdd.add(ladderTile);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        for (Projectile p : projectiles) {
            p.isOnScreen = Main.world.worldHitBox.contains(p.x, p.y);
            p.update();
        }

        for (Enemy e : enemies) {
            e.update();
        }

        for (Tile t : tempTiles) {
            t.update();
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

        for (Tile t : tempTilesToAdd) {
            tempTiles.add(t);
        }

        for (Tile t : tempTilesToRemove) {
            tempTiles.remove(t);
        }

        projectilesToRemove.clear();
        projectilesToAdd.clear();
        enemiesToAdd.clear();
        enemiesToRemove.clear();
        tempTilesToAdd.clear();
        tempTilesToRemove.clear();
    }

}
