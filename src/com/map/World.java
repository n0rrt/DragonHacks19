package com.map;

import com.core.Main;
import com.core.Util;
import com.entities.Player;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class World {

	public volatile Player player = null;

	public volatile double gravity = 0;

	public volatile Rectangle worldHitBox = null;

	public volatile Floor currentFloor = null;
	public volatile ArrayList<Floor> floors = new ArrayList<>();

	public World() throws IOException {
		genFloors();
		currentFloor = floors.get(0);
		worldHitBox = new Rectangle((int) Main.window.xOrigin - Main.tileSize, (int) Main.window.yOrigin - Main.tileSize, Main.screenSize.width + Main.tileSize, Main.screenSize.height + Main.tileSize);
		loadPlayer(currentFloor.currentRoom.tiles[6][6].x, currentFloor.currentRoom.tiles[6][6].y);
	}

//	public String[] parseLevelData() throws IOException {
//		String content = Util.readFile(path + "LevelData.txt");
//		String[] temp = content.split("-\r?\n");
//		String[] levelData = temp[0].split(":");
//		String[] enemyData = temp[1].split("\\|");
//		for (String s : enemyData) {
//			String[] tempE = s.split(":");
//		}
//		return levelData;
//	}

	public void genFloors(){
		for(int i = 0; i < 10; i++){
			Floor tempFloor = new Floor(i);
			tempFloor.genRooms(1);
			floors.add(tempFloor);
		}
	}

	public void render(Graphics2D g) throws IOException {
		//g.translate(Window.xOrigin - 1, Window.yOrigin - 1);
		//g.drawImage(Util.loadImg("/res/level/levelBG.png"), 0, 0, Main.screenSize.width + 2, Main.screenSize.height + 2, null);
		//g.translate(-(Window.xOrigin - 1), -(Window.yOrigin - 1));

		currentFloor.render(g);

		player.render(g);
	}

//	public void loadTiles(String path) throws IOException {
//		String fileContents = Util.readFile(path);
//		String[] fileContentsHolding = fileContents.split("-");
//
//		String[][] references = Util.splitAtLinesAndString(fileContentsHolding[0], ":");
//		String[][] tileMap = Util.splitAtLinesAndString(fileContentsHolding[1], " ");
//
//		//tiles = new Tile[tileMap.length][tileMap[0].length];
//
//		for (int i = 0; i < tileMap.length; i++) {
//			String[] currentTileRow = tileMap[i];
//			for (int j = 0; j < currentTileRow.length; j++) {
//				String currentTile = currentTileRow[j];
//				compareTileToReference(references, currentTile, j, i);
//			}
//		}
//	}

//	public void compareTileToReference(String[][] references, String tile, int x, int y) throws IOException {
//		for (int i = 0; i < references.length; i++) {
//			String currentReference = references[i][0];
//			if (tile.trim().equals(currentReference.trim())) {
//				tiles[y][x] = new Tile(Util.loadImg(path + "/res/" + references[i][1]), references[i][2].trim().equals("t"), x * Main.tileSize, y * Main.tileSize);
//			}
//		}
//	}

	public void loadPlayer(int x, int y) throws IOException {
		player = new Player(Util.loadImg("res/player/player.png"), x, y);
	}

	public void update() throws InterruptedException {
		worldHitBox.setLocation((int) Main.window.xOrigin - Main.tileSize, (int) Main.window.yOrigin - Main.tileSize);
		worldHitBox.setSize(Main.screenSize.width + Main.tileSize, Main.screenSize.height + Main.tileSize);

		player.update();
	}

	public void clear() {
		this.player = null;
		this.currentFloor = null;
	}
}