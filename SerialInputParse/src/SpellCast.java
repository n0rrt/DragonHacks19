import java.util.Arrays;

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

	public static String trackSpell(int x, int y, int z) {
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
		int[] earthB = {};
		int[] earthC = {};
		int[][] earth = {earthA, earthB, earthC};
		
		int[] windA = {};
		int[] windB = {};
		int[] windC = {};
		int[][] wind = {windA, windB, windC};

		for (int i = 0; i < 3; i++) {
			if (SerialParse.prevValues.get(i).equals((lightning[i]))
					|| lessThan((sub(add(SerialParse.prevValues.get(i), threshold), lightning[i])), threshold)
					|| lessThan((sub(sub(SerialParse.prevValues.get(i), threshold), lightning[i])), threshold)) {
				lightConfidence++;
			}
//			else if()
//			{
//				
//			}
		}

		return "";

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
