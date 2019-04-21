package com.ui;

import java.awt.*;
import java.io.IOException;

public class nodeStatButton extends MenuButton {

    public nodeStatButton parent;
    public nodeStatButton left;
    public nodeStatButton right;

    public boolean gotActivated;

    public nodeStatButton(Object image, String id, double x, double y, double width, double height, Runnable clickAction, nodeStatButton parent){
        super(image, id, x, y, width, height, clickAction);
        this.parent = parent;
        gotActivated = false;

    }
//    public void onClick() throws IOException {
//        System.out.println("Hello");
//        //if( parent == null || parent.gotActivated){
//            super.onClick();
//        //}
//    }

//    public void render(Graphics2D g){
//
//    }
}
