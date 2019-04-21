package com.core;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.Buffer;
import java.util.ArrayList;

public class Util {

	public static ArrayList<BufferedImage> cachedImages = new ArrayList<BufferedImage>();
	private static ArrayList<String> cachedImagePaths = new ArrayList<String>();

	public static BufferedImage loadImg(String img) throws IOException {
		BufferedImage out, convertedImage = null;

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gd.getDefaultConfiguration();

		boolean isCached = false;
		int cachedImageIdex = 0;
		for (int i = 0; i < cachedImagePaths.size(); i++) {
			String currentImagePath = cachedImagePaths.get(i);
			if (img.equals(currentImagePath)) {
				isCached = true;
				cachedImageIdex = i;
				break;
			}
		}

		if (isCached) {
			out = cachedImages.get(cachedImageIdex);
			return out;
		} else {
			out = ImageIO.read(Window.class.getResource("/" + img));
			cachedImagePaths.add(img);
			convertedImage = gc.createCompatibleImage(out.getWidth(), out.getHeight(), out.getTransparency());
			Graphics2D g2d = convertedImage.createGraphics();
			g2d.drawImage(out, 0, 0, out.getWidth(), out.getHeight(), null);
			g2d.dispose();
			cachedImages.add(convertedImage);
			return convertedImage;
		}
	}

	public static String readFile(String path) throws IOException {
		String out = "";
		InputStream input = Util.class.getResourceAsStream("/" + path);
		out = convertStreamToString(input);
		input.close();
		return out;
	}

	public void writeFile(String fileName, String input) {

	}

	static String convertStreamToString(InputStream is) {
		@SuppressWarnings("resource")
		java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
		return s.hasNext() ? s.next() : "";
	}

	public static String[][] splitAtLinesAndString(String unrefinedArray, String split) {
		String[] arrayInRows = unrefinedArray.trim().split("\r\n");
		String[][] refinedArray = new String[arrayInRows.length][];
		for (int i = 0; i < arrayInRows.length; i++) {
			String currentRow = arrayInRows[i];
			refinedArray[i] = currentRow.split(split);
		}
		return refinedArray;
	}

	public static void drawImage(Graphics2D g, BufferedImage img, int x, int y, int width, int height){
		g.drawImage(img, (int)(x), (int)(y), width, height, null);
	}

}