package com.ui;

import com.core.Main;
import com.core.Util;
import com.map.World;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class StatMenu extends AbstractMenu {

    public nodeStatButton lightningBranch;
    public nodeStatButton fireBranch;
    public nodeStatButton airBranch;
    public nodeStatButton earthBranch;

    private static int width = 90;
    private static int height = 60;

    public StatMenu(BufferedImage bg) {
        super(bg);

        makeLightningBranch();
        makeFireBranch();


    }

    private final static int focalNode = 280;

    private final static int BigWidth = 120;
    private final static int BigHeight = 150;

    private final static int SmallWidth = 70;
    private final static int SmallHeight = 120;

    private final static int margin = 15;
    private final static int marginButtom = 20;

    private final static int fromBigToSmall = 15;

    private final static int padding = 10;

    private void makeLightningBranch() {
        Runnable lightningAction = new Runnable() {
            @Override
            public void run() {
                System.out.println("Just checking");
                try {
                    Main.world = new World();
                    Main.world.currentFloor.currentRoom.spawnEnemies();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Main.isInLevel = true;
                Main.currentMenu = null;
            }
        };
        try {
            lightningBranch = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode, padding, BigWidth, BigHeight, lightningAction, null);
            lightningBranch.left = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode - (SmallWidth + margin), padding + fromBigToSmall, SmallWidth, SmallHeight, lightningAction, lightningBranch);
            lightningBranch.left.left = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode - (2 * SmallWidth + 2 * margin), padding + fromBigToSmall, SmallWidth, SmallHeight, lightningAction, null);
            lightningBranch.left.left.left = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode - (3 * SmallWidth + 3 * margin), padding + fromBigToSmall, SmallWidth, SmallHeight, lightningAction, null);
            lightningBranch.right = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode + BigWidth + margin, padding + fromBigToSmall, SmallWidth, SmallHeight, lightningAction, lightningBranch);
            lightningBranch.right.right = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode + BigWidth + SmallWidth + 2 * margin, padding + fromBigToSmall, SmallWidth, SmallHeight, lightningAction, null);
            lightningBranch.right.right.right = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode + BigWidth + 2 * SmallWidth + 3 * margin, padding + fromBigToSmall, SmallWidth, SmallHeight, lightningAction, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeFireBranch() {
        Runnable lightningAction = new Runnable() {
            @Override
            public void run() {
                ;
            }
        };
        try {
            fireBranch = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode, padding + fromBigToSmall + BigHeight + marginButtom, BigWidth, BigHeight, lightningAction, null);
            fireBranch.left = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode - (SmallWidth + margin), padding + 2 * fromBigToSmall + BigHeight + marginButtom, SmallWidth, SmallHeight, lightningAction, null);
            fireBranch.left.left = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode - (2 * SmallWidth + 2 * margin), padding + 2 * fromBigToSmall + BigHeight + marginButtom, SmallWidth, SmallHeight, lightningAction, null);
            fireBranch.left.left.left = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode - (3 * SmallWidth + 3 * margin), padding + 2 * fromBigToSmall + BigHeight + marginButtom, SmallWidth, SmallHeight, lightningAction, null);
            fireBranch.right = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode + BigWidth + margin, padding + 2 * fromBigToSmall + BigHeight + marginButtom, SmallWidth, SmallHeight, lightningAction, null);
            fireBranch.right.right = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode + BigWidth + SmallWidth + 2 * margin, padding + 2 * fromBigToSmall + BigHeight + marginButtom, SmallWidth, SmallHeight, lightningAction, null);
            fireBranch.right.right.right = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", focalNode + BigWidth + 2 * SmallWidth + 3 * margin, padding + 2 * fromBigToSmall + BigHeight + marginButtom, SmallWidth, SmallHeight, lightningAction, null);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void render(Graphics2D g) {
        super.render(g);
        nodeRender(g, lightningBranch);
        nodeRender(g, fireBranch);
//        nodeRender(g,airBranch);
//        nodeRender(g,earthBranch);

    }

    private void nodeRender(Graphics2D g, nodeStatButton sb) {
        if (sb == null)
            return;
        sb.render(g);
        nodeRender(g, sb.left);
        nodeRender(g, sb.right);
    }

    public void update(Point mousePos) throws IOException{
        mousePos.x = mousePos.x;
        mousePos.y = mousePos.y;
        NodeUpdate(mousePos, lightningBranch);
        NodeUpdate(mousePos, fireBranch);
    }

    public void NodeUpdate(Point mousePos, nodeStatButton sb)throws IOException{
        if( sb == null){
            return;
        }
        if (sb.hitBox.contains(mousePos)) {
            sb.onClick();
        }
        NodeUpdate(mousePos, sb.left);
        NodeUpdate(mousePos, sb.right);
    }
//    public void update(Point mousePos) throws IOException{
//        mousePos.x = mousePos.x;
//        mousePos.y = mousePos.y;
//        for (int i = 0; i < buttons.size(); i++) {
//            MenuButton menuButton = buttons.get(i);
//            if (menuButton.hitBox.contains(mousePos)) {
//                menuButton.onClick();
//            }
//        }
//    }
}

