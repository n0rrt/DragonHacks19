package com.core;

import com.map.World;
import com.ui.Menu;
import com.ui.MenuButton;
import com.ui.StatMenu;
import res.menu.AbstractMenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Main {

	public static Dimension monitorSize = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds().getSize();
	public static Dimension screenSize = new Dimension(680, 780);

	public static volatile AbstractMenu menus[] = new AbstractMenu[2];

	public static JFrame frame = null;
	public static Window window = null;

	public static volatile int tickSpeed = 10;
	public static volatile int renderSpeed = 1;

	public static volatile int tileSize = 40;

	public static volatile Point mousePos = new Point(0, 0);

	public static volatile World world = null;
	public static volatile AbstractMenu currentMenu = null;

	public static volatile boolean isPaused, isInPauseMenu, isInLevel, isLevelSelectionScreen;

	public static void main(String[] args) throws InterruptedException, IOException {
		System.setProperty("sun.java2d.opengl", "true");

		menus[0] = new Menu(Util.loadImg("res/menu/menuBG.png"));
		menus[1] = new StatMenu(Util.loadImg("res/menu/menuBG.png"));

		loadMainMenu();
		makeWindow();

		render.start();
		game.start();
	}

	public static void makeWindow() {
		frame = new JFrame("SuperMeatMan");
		window = new Window();
		frame.setUndecorated(true);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.getContentPane().add(window);
		frame.setBounds((int)(monitorSize.getWidth()/2 - screenSize.getWidth()/2), (int)(monitorSize.getHeight()/2 - screenSize.getHeight()/2), (int)screenSize.getWidth(), (int)screenSize.getHeight());
		frame.setVisible(true);
		frame.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {

			}

			@Override
			public void keyReleased(KeyEvent e) {
				Window.keys[e.getKeyCode()] = false;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				Window.keys[e.getKeyCode()] = true;
				if (Window.keys[KeyEvent.VK_ESCAPE] && isInLevel) {
					System.exit(0);
					//pause();
				} else if (Window.keys[KeyEvent.VK_ESCAPE] && isPaused) {
					pause();
				}
			}
		});
		frame.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isInLevel) {
					try {
						currentMenu.update(mousePos);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		frame.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				mousePos.setLocation(e.getPoint());
			}

			@Override
			public void mouseDragged(MouseEvent e) {
			}
		});
		frame.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {

			}
		});

	}

	public static void loadMainMenu() throws IOException {
		isInLevel = false;
		currentMenu = menus[0];

	}

//	public static void loadLevelSelectMenu() throws IOException {
//		isLevelSelectionScreen = true;
//		currentMenu = new Menu(Util.loadImg("menuRes/levelSelectionBg.png"));
//
//		//String path = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath() + "Levels";
//		String path = "C:\\Users\\Plane\\Desktop\\My Stuff\\Programming\\Misc\\SideScroller\\src\\levels";
//		System.out.println(path);
//		File f = new File(path);
//		File[] fileArray = f.listFiles();
//		int tempY = 200;
//		for (File fi : fileArray) {
//			currentMenu.addButton(fi.getName(), "level:" + fi.getName(), 100, tempY, 200, 100);
//			tempY += MenuButton.fontSize + 10;
//		}
//		currentMenu.addButton(Util.loadImg("MenuRes/levelSelectionBack.png"), "levelselectback", 0, screenSize.getHeight() - 100, 200, 100);
//	}

	public static void loadWorld() throws IOException {
		world = new World();
	}

	public static void pause() {
		if (isPaused) {
			currentMenu = null;
			isInLevel = true;
			isInPauseMenu = false;
			isPaused = false;
		} else {
			currentMenu = menus[0];//new Menu(null);

			try {
				Runnable playAction = new Runnable() {
					@Override
					public void run(){
						//play actions
					}
				};
				MenuButton playButton = new MenuButton(Util.loadImg("menuRes/pauseMenuMainMenu.png"), "mainmenu", screenSize.getWidth() / 2 - 100, screenSize.getHeight() / 2 + 50, 200, 100, playAction);
//				currentMenu.addButton(playButton);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				Runnable resumeAction = new Runnable() {
					@Override
					public void run() {
						// Resume Game Action
					}
				};
				MenuButton resumeButton = new MenuButton(Util.loadImg("menuRes/pauseMenuResume.png"), "resumegame", screenSize.getWidth() / 2 - 100, screenSize.getHeight() / 2 - 150, 200, 100,resumeAction);
//				currentMenu.addButton(resumeButton);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			isInPauseMenu = true;
			isPaused = true;
			isInLevel = false;
		}

	}

	static Thread render = new Thread(new Runnable() {
		@Override
		public void run() {
			while (true) {
				try {
					Thread.sleep(renderSpeed);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				window.repaint();
			}
		}
	});

	static Thread game = new Thread(new Runnable() {
		@Override
		public void run() {
			long startTime = 0;
			long endTime = 0;
			int elapsedTime = 0;
			while (true) {
				try {
					if (tickSpeed - elapsedTime > 0) {
						Thread.sleep(tickSpeed - elapsedTime);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				startTime = System.currentTimeMillis();
				try {
					if (isInLevel && !isPaused) {
						world.update();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				endTime = System.currentTimeMillis();
				elapsedTime = (int) (endTime - startTime);
			}
		}
	});
}
