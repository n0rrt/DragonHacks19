package com.ui;

import com.core.Main;
import com.core.Window;
import com.map.World;

import java.awt.*;

public class PlayerUI {
	public static volatile double healthBorderX, healthBorderY, healthBorderWidth, healthBorderHeight, borderThickness;

	public PlayerUI() {
		healthBorderWidth = 300;
		healthBorderHeight = 30;
		borderThickness = 3;
	}

	public void render(Graphics2D g) {

		healthBorderX = Window.xOrigin + (Main.screenSize.width / 2 - healthBorderWidth / 2);
		healthBorderY = Window.yOrigin + Main.screenSize.height - healthBorderHeight - 10;
		

		
		Stroke oS = g.getStroke();
		g.setStroke(new BasicStroke((float) borderThickness));
		
		g.translate(healthBorderX, healthBorderY);
		g.setXORMode(Color.WHITE);
		//g.setColor(Color.WHITE);
		g.drawRect(0, 0, (int)healthBorderWidth, (int)healthBorderHeight);
		g.setPaintMode();
		g.translate(-healthBorderX, -healthBorderY);
		
		g.setStroke(oS);
		
		double healthPercentage = World.player.currentHealth/World.player.maxHealth;
		double innerWidth = (healthBorderWidth - borderThickness);
		
		g.translate(healthBorderX + borderThickness - 1, healthBorderY + borderThickness - 1);
		g.setColor(Color.RED);
		g.fillRect(0, 0, (int)((innerWidth * healthPercentage)), (int)(healthBorderHeight - borderThickness));
		g.translate(-(healthBorderX + borderThickness - 1), -(healthBorderY + borderThickness - 1));
		
		String forwardslashText = "/";
		String currentHealthText = (int)World.player.currentHealth +"";
		String maxHealthText =  (int)World.player.maxHealth +"";
		
		g.setFont(g.getFont().deriveFont((float)30));
		
		double forwardslashTextWidth = g.getFontMetrics().stringWidth(forwardslashText);
		double currentHealthTextWidth = g.getFontMetrics().stringWidth(currentHealthText);
		double textHeight = g.getFontMetrics().getHeight();
		double stringBuffer = 3.0;
		double middleOfMiddleX = healthBorderX + healthBorderWidth/2 - forwardslashTextWidth/2;
		double middleOfMiddleY = healthBorderY + healthBorderHeight/2 + textHeight/3.5;
		
		g.setXORMode(Color.BLUE);
		
		g.translate(middleOfMiddleX - stringBuffer - currentHealthTextWidth, middleOfMiddleY);
		g.drawString(currentHealthText, 0, 0);
		g.translate(-(middleOfMiddleX - stringBuffer - currentHealthTextWidth), -(middleOfMiddleY));
		
		g.translate(middleOfMiddleX, middleOfMiddleY);
		g.drawString(forwardslashText, 0, 0);
		g.translate(-(middleOfMiddleX), -(middleOfMiddleY));
		
		g.translate(middleOfMiddleX + stringBuffer*3.0, middleOfMiddleY);
		g.drawString(maxHealthText, 0, 0);
		g.translate(-(middleOfMiddleX + stringBuffer*3.0), -(middleOfMiddleY));
		
		g.setPaintMode();
	}

}
