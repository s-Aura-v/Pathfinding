package com.aurav.pathfinding.utility;

import java.io.*;
import java.security.KeyPair;
import java.util.Arrays;
import java.util.HashMap;

public class FileInput {

    public static HashMap<Integer, int[]> teleportPair = new HashMap<>();

    public FileInput() {

    }

    /**
     * Teleport Logic:
     * Each teleport has a tag, "T1, T2, ... TN". These teleports are in pairs.
     * If we get the tag #, {1, 2, ...}, then add a 100 for their weight, we can make sure the A* doesn't explore it.
     * AND, we can make sure we can find the pairs from the HashMap.
     */
    public int[][] readInput(String path) {
        int[][] tempTiles = new int[10000][10000]; // using max possible size
        int rowIndex = 0;
        int column = 0;

        teleportPair = new HashMap<>();

        // Initialization:
        // First, we just put everything in an array with the worst case scenario.
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] rowLine = line.split(" ");
                for (int columnIndex = 0; columnIndex < rowLine.length; columnIndex++) {
                    try {
                        tempTiles[rowIndex][columnIndex] = Integer.parseInt(rowLine[columnIndex]);
                    } catch (NumberFormatException e) {
                        if (rowLine[columnIndex].equals("F")) {
                            tempTiles[rowIndex][columnIndex] = 10;
                        } else if (rowLine[columnIndex].charAt(0) == 'T') {
                            // If it's a teleporter, let's save the teleporter index and coordinates;
                            int tag = Integer.parseInt(rowLine[columnIndex].substring(1)) + 100;
                            if (teleportPair.containsKey(tag)) {
                                int[] arr = teleportPair.get(tag);
                                arr[2] = rowIndex;
                                arr[3] = columnIndex;
                            } else {
                                teleportPair.put(tag, new int[]{rowIndex, columnIndex, 0, 0});
                            }
                            tempTiles[rowIndex][columnIndex] = tag;
                        }
                    }
                }
                rowIndex++;
                column = rowLine.length;
                rowLine = null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Optimization:
        // Then we cut that array down to its true size; to save memory.
        int[][] tiles = new int[rowIndex][column];
        for (int i = 0; i < rowIndex; i++) {
            System.arraycopy(tempTiles[i], 0, tiles[i], 0, column);
        }

        System.out.println(tiles.length + " by " + tiles[0].length);
        for (int[] arr : teleportPair.values()) {
            System.out.println(Arrays.toString(arr));
        }
        return tiles;
    }
}
