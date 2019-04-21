package com.entities;

import com.core.Main;
import com.core.Util;
import com.map.World;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class Red_Wizard extends Enemy {

    public Red_Wizard(BufferedImage im, int x, int y){
        super(im, x, y);

        tickTillNextAttack = 0;
    }

    private int AttackTick = 60;
    private int tickTillNextAttack;

    public void update(){
        super.update();

        if(tickTillNextAttack >= AttackTick){
            fireBolt();
            tickTillNextAttack = 0;
        }
        tickTillNextAttack++;
    }

    public void fireBolt(){

        BufferedImage fireImage = null;
        try {
            fireImage = Util.loadImg("res/spells/fireball.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        double xdif = (x - Main.world.player.x);
        double ydif = (y - Main.world.player.y);
        double angle = Math.atan(ydif / xdif);
        if((xdif < 0 && ydif < 0) ||xdif < 0)
            angle = Math.PI;
        //double hyp = Math.sqrt(xdif*xdif + ydif*ydif);
        int bX,bY;
        bX = (int)(Math.cos(angle)*5);
        bY = (int)(Math.sin(angle)*5);

        Projectile fireProjectile = new Projectile(x, y, -1*bX, -1*bY, attackWidth, attackHeight, attackDamage, true, false, false, 40, fireImage, "fire");
        Main.world.currentFloor.currentRoom.projectilesToAdd.add(fireProjectile);
    }
}
