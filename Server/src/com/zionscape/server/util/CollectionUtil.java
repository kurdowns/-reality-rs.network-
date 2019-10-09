package com.zionscape.server.util;

import java.util.List;

public class CollectionUtil {

	public static int getIndexOfValue(int[] arr, int n) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] == n) {
				return i;
			}
		}
		return -1;
	}

	public static <T> T getRandomElement(T[] arr) {
		return arr[Misc.random(arr.length - 1)];
	}

	public static int getRandomElement(int[] arr) {
		return arr[Misc.random(arr.length - 1)];
	}

	public static int[] getRandomElement(int[][] arr) {
		return arr[Misc.random(arr.length - 1)];
	}

	public static <T> T getRandomElement(List<T> list) {
		return list.get(Misc.random(list.size() - 1));
	}

}
