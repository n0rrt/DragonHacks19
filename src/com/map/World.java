package com.map;

import com.core.Main;
import com.core.Util;
import com.core.Window;
import com.entities.Enemy;
import com.entities.Player;
import com.entities.Projectile;
import com.ui.PlayerUI;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class World {

	public static volatile Tile[][] tiles = null;

	public static volatile ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public static volatile ArrayList<Projectile> projectilesToRemove = new ArrayList<Projectile>();
	public static volatile ArrayList<Projectile> projectilesToAdd = new ArrayList<Projectile>();

	public static volatile ArrayList<Enemy> availableEnemies = new ArrayList<Enemy>();
	public static volatile ArrayList<Enemy> enemies = new ArrayList<Enemy>();
	public static volatile ArrayList<Enemy> enemiesToAdd = new ArrayList<Enemy>();
	public static volatile ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();

	public String level, path = "";

	public static Player player = null;
	public static PlayerUI playerUI = null;

	public static volatile double gravity = 0;

	public static volatile Rectangle worldHitBox = null;

	public World(String level) throws IOException {
		this.level = level;
		path = "Levels/" + this.level + "/";
		String[] levelInfo = parseLevelData();
		gravity = -(Double.parseDouble(levelInfo[2]) / (Main.tileSize / 10));
		loadTiles(path + "world/tiles.txt");
		loadPlayer(path + "res/player.png", Integer.parseInt(levelInfo[0]), Integer.parseInt(levelInfo[1]));
		worldHitBox = new Rectangle((int) Window.xOrigin - Main.tileSize, (int) Window.yOrigin - Main.tileSize, Main.screenSize.width + Main.tileSize, Main.screenSize.height + Main.tileSize);
	}

	public String[] parseLevelData() throws IOException {
		String content = Util.readFile(path + "LevelData.txt");
		String[] temp = content.split("-\r?\n");
		String[] levelData = temp[0].split(":");
		String[] enemyData = temp[1].split("\\|");
		for (String s : enemyData) {
			String[] tempE = s.split(":");
			availableEnemies.add(new Enemy(Util.loadImg(path + "res/" + tempE[0]), Util.readFile(path + "res/" + tempE[1]), Util.readFile(path + "res/" + tempE[2]), this));
		}
		return levelData;
	}

	public void render(Graphics2D g) throws IOException {
		g.translate(Window.xOrigin - 1, Window.yOrigin - 1);
		g.drawImage(Util.loadImg(path + "/res/bg.png"), 0, 0, Main.screenSize.width + 2, Main.screenSize.height + 2, null);
		g.translate(-(Window.xOrigin - 1), -(Window.yOrigin - 1));

		player.render(g);

		playerUI.render(g);

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

	public void loadTiles(String path) throws IOException {
		String fileContents = Util.readFile(path);
		String[] fileContentsHolding = fileContents.split("-");

		String[][] references = Util.splitAtLinesAndString(fileContentsHolding[0], ":");
		String[][] tileMap = Util.splitAtLinesAndString(fileContentsHolding[1], " ");

		tiles = new Tile[tileMap.length][tileMap[0].length];

		for (int i = 0; i < tileMap.length; i++) {
			String[] currentTileRow = tileMap[i];
			for (int j = 0; j < currentTileRow.length; j++) {
				String currentTile = currentTileRow[j];
				compareTileToReference(references, currentTile, j, i);
			}
		}
	}

	public void compareTileToReference(String[][] references, String tile, int x, int y) throws IOException {
		for (int i = 0; i < references.length; i++) {
			String currentReference = references[i][0];
			if (tile.trim().equals(currentReference.trim())) {
				tiles[y][x] = new Tile(Util.loadImg(path + "/res/" + references[i][1]), references[i][2].trim().equals("t"), x * Main.tileSize, y * Main.tileSize);
			}
		}
	}

	public void loadEnemies() throws IOException {

	}

	public void loadItems(String path) throws IOException {

	}

	public void loadPlayer(String path, int x, int y) throws IOException {
		player = new Player(Util.loadImg(path), x, y);
		playerUI = new PlayerUI();
	}

	public void update() throws InterruptedException {
		worldHitBox.setLocation((int) Window.xOrigin - Main.tileSize, (int) Window.yOrigin - Main.tileSize);
		worldHitBox.setSize(Main.screenSize.width + Main.tileSize, Main.screenSize.height + Main.tileSize);
		for (Tile[] ta : tiles) {
			for (Tile t : ta) {
				t.isOnScreen = worldHitBox.contains(t.x, t.y);
			}
		}
		for (Projectile p : projectiles) {
			p.isOnScreen = worldHitBox.contains(p.x, p.y);
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
		player.update();
	}

	public static void clear() {
		tiles = null;
		player = null;
		projectiles.clear();
		projectilesToAdd.clear();
		projectilesToRemove.clear();
		availableEnemies.clear();
		enemies.clear();
		enemiesToAdd.clear();
		enemiesToRemove.clear();
	}
}