package com.aurav.pathfinding.utility;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FileInput {

    public static int[][] readInput(String path) {
        path = "assets/inputs/input1";

        HashMap<Integer, Integer> teleporterIndex = new HashMap();
        int[][] tempTiles = new int[10000][10000]; // using max possible size
        int rowIndex = 0;
        int column = 0;

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
                            // TODO: Teleporter incomplete for now...
                            tempTiles[rowIndex][columnIndex] = 11;
                            int index = Integer.parseInt(rowLine[columnIndex].substring(1));
//                            System.out.println(teleIndex);
                        }
                    }
                }
                rowIndex++;
                column = rowLine.length;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        // Optimization:
        // Then we cut that array down to the proper size; to save memory.
        int[][] tiles = new int[rowIndex][column];
        for (int i = 0; i < rowIndex; i++) {
            System.arraycopy(tempTiles[i], 0, tiles[i], 0, column);
        }
        System.out.println(tiles.length + " by " + tiles[0].length);

        System.out.println(Arrays.deepToString(tiles));
        return tiles;
    }
}
