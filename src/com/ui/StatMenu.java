package com.ui;

import com.core.Util;
import res.menu.AbstractMenu;

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

    public StatMenu(BufferedImage bg){
        super(bg);

        makeLightningBranch();


    }

    private void makeLightningBranch(){
        Runnable lightningAction = new Runnable(){
            @Override
            public void run(){
                ;
            }
        };
        try {
            lightningBranch = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", 240, 60, 120, 90, lightningAction, null);
            lightningBranch.left = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", 230-1*width-10, 80, width, height, lightningAction, null);
            lightningBranch.left.left = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", 230-2*width-20, 80, width, height, lightningAction, null);
            lightningBranch.right = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", 220+60+1*width+10, 80, width, height, lightningAction, null);
            lightningBranch.right.right = new nodeStatButton(Util.loadImg("res/menu/DebugShape.png"), "lightning upgrade button lv 1", 220+60+2*width+20, 80, width, height, lightningAction, null);

        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void render(Graphics2D g){
        super.render(g);
        nodeRender(g,lightningBranch);
//        nodeRender(g,fireBranch);
//        nodeRender(g,airBranch);
//        nodeRender(g,earthBranch);

    }

    private void nodeRender(Graphics2D g, nodeStatButton sb){
        if(sb == null)
            return;
        sb.render(g);
        nodeRender(g,sb.left);
        nodeRender(g,sb.right);
    }
}
