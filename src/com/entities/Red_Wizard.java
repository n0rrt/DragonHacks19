package com.entities;

import com.core.Main;
import com.core.Util;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Red_Wizard extends Enemy {

    public Red_Wizard(BufferedImage im, int x, int y){
        super(im, x, y);


    }

    public void fireBolt(){

        BufferedImage fireImage = null;
        try {
            fireImage = Util.loadImg("res/spells/fireball.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Projectile fireProjectile = new Projectile(x, y, 6*xDir, 6*yDir, attackWidth, attackHeight, 2, false, true, false, 40, fireImage, "fire");
        //Main.world.currentFloor.currentRoom.projectilesToAdd.add(fireProjectile);
    }
}
