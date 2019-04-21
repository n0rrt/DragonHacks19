package com.controls;
//
//import com.core.Main;


import com.core.Main;

import java.util.ArrayList;
import java.util.Collections;
public class SpellCast {
	static double velXi = 0;
	static double velYi = 0;
	static double velZi = 0;

	static double velXf;
	static double velYf;
	static double velZf;

	static double posXi = 0;
	static double posYi = 0;
	static double posZi = 0;

	static double posXf;
	static double posYf;
	static double posZf;

	static double timeStep = 0.1;

	static final int FIREID = 0;
	static final int LIGHTNINGID = 1;
	static final int WINDID = 2;
	static final int EARTHID = 3;

	static int[] posTuple = new int[3];
	static ArrayList<Integer> confidenceValues= new ArrayList<Integer>();
	public static void trackSpell(int x, int y, int z) {
		//int[] startPos = {0, 0, 0};
		int[] threshold = { 1, 1, 1 };

		int lightConfidence = 0;
		int fireConfidence = 0;
		int earthConfidence = 0;
		int windConfidence = 0;

		int[] lightA = { 2, -2, -2 };
		int[] lightB = { lightA[0], lightA[1] + 2, lightA[2] + 2 };
		int[] lightC = { lightB[0], lightB[1] - 4, lightB[2] - 3 };
		int[][] lightning = {lightA, lightB, lightC };

		int[] fireA = {0, -4, -4};
		int[] fireB = {fireA[0], fireA[1] +3, fireA[2] + 3};
		int[] fireC = {fireB[0], fireA[1] + 3, fireA[2] + 3};
		int[][] fire = {fireA, fireB, fireC};

		int[] earthA = {4, 3, 3};
		int[] earthB = {earthA[0] + 4, earthA[1] - 4, earthA[2] - 4};
		int[] earthC = {earthB[0] +2, earthB[1] -1, earthB[2] - 1};
		int[][] earth = {earthA, earthB, earthC};

		int[] windA = {1, -4, -4};
		int[] windB = {windA[0] + 2, windA[1], windA[2]};
		int[] windC = {windB[0] + 4, windB[1] - 2, windB[2] -2};
		int[][] wind = {windA, windB, windC};

		for (int i = 0; i < 3; i++) {
			if (SerialParse.prevValues.get(i).equals((lightning[i]))
					|| lessThan((sub(add(SerialParse.prevValues.get(i), threshold), lightning[i])), threshold)
					|| lessThan((sub(sub(SerialParse.prevValues.get(i), threshold), lightning[i])), threshold)) {
				lightConfidence++;

			}
			if(SerialParse.prevValues.get(i).equals((fire[i]))
					|| lessThan((sub(add(SerialParse.prevValues.get(i), threshold), fire[i])), threshold)
					|| lessThan((sub(sub(SerialParse.prevValues.get(i), threshold), fire[i])), threshold))
			{
				fireConfidence++;

			}
			if(SerialParse.prevValues.get(i).equals((earth[i]))
					|| lessThan((sub(add(SerialParse.prevValues.get(i), threshold), earth[i])), threshold)
					|| lessThan((sub(sub(SerialParse.prevValues.get(i), threshold), earth[i])), threshold))
			{
				earthConfidence++;

			}
			if(SerialParse.prevValues.get(i).equals((wind[i]))
					|| lessThan((sub(add(SerialParse.prevValues.get(i), threshold), wind[i])), threshold)
					|| lessThan((sub(sub(SerialParse.prevValues.get(i), threshold), wind[i])), threshold))
			{
				windConfidence++;

			}

		}
		confidenceValues.add(fireConfidence);
		confidenceValues.add(lightConfidence);
		confidenceValues.add(windConfidence);
		confidenceValues.add(earthConfidence);

		int maxVal = confidenceValues.indexOf(Collections.max(confidenceValues));

		System.out.println(maxVal);

		Main.world.player.caster.castSpell(maxVal);

	}

	public static int[] add(int[] first, int[] second) {
		int length = first.length < second.length ? first.length : second.length;
		int[] result = new int[length];

		for (int i = 0; i < length; i++) {
			result[i] = first[i] + second[i];
		}

		return result;
	}

	public static int[] sub(int[] first, int[] second) {
		int length = first.length < second.length ? first.length : second.length;
		int[] result = new int[length];

		for (int i = 0; i < length; i++) {
			result[i] = first[i] - second[i];
		}

		return result;
	}

	public static boolean lessThan(int[] first, int[] second) {
		int length = first.length < second.length ? first.length : second.length;

		for (int i = 0; i < length; i++) {
			if (first[i] > second[i]) {
				return false;
			}
		}
		return true;
	}

	public static int[] getPos(int accX, int accY, int accZ) {
		if (SerialParse.runCount == 20) {
			posXf = 0;
			posYf = 0;
			posZf = 0;
			posXi = 0;
			posYi = 0;
			posZi = 0;
			velXi = 0;
			velYi = 0;
			velZi = 0;
			velXf = 0;
			velYf = 0;
			velZf = 0;
			SerialParse.runCount = 0;
		}
		velXf = velXi + accX * timeStep;
		velYf = velYi + accY * timeStep;
		velZf = velZi + accZ * timeStep;

		posXf = posXi + velXf * timeStep;
		posYf = posYi + velYf * timeStep;
		posZf = posZi + velYf * timeStep;

		posXi = posXf;
		posYi = posYf;
		posZi = posZf;

		posTuple[0] = (int) posXf;
		posTuple[1] = (int) posYf;
		posTuple[2] = (int) posZf;

		return posTuple;
	}
}
