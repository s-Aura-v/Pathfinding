package com.aurav.pathfinding.utility;

import static com.aurav.pathfinding.utility.FileInput.readInput;

public class SearchAlgorithm {
    enum Direction {
        NORTH(0, 1),
        SOUTH(0, -1),
        EAST(1, 0),
        WEST(-1, 0);

        private int deltaX;
        private int deltaY;

        Direction(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

    }

    public static void main(String[] args) {
        int[][] map = readInput("assets/inputs/input1");
        search(map, 0, 0, 1, 2);
    }

    /**
     * Given a source and destination, attempts to find the best path.
     * Uses a combination of Dijkstra's and A*
     * <a href="https://en.wikipedia.org/wiki/Taxicab_geometry">Manhattan Distance for A* Heuristic</a>
     *
     * @param xSource the x position of the source cell
     * @param ySource the y position of the source cell
     * @param xDest   the x position of the destination cell
     * @param yDest   the y position of the destination cell
     */
    public static void search(int[][] map, int xSource, int ySource, int xDest, int yDest) {
        int xCurrent = xSource;
        int yCurrent = ySource;

        int distToSource = 0;
        int distToDest = 0;

        boolean destFound = false;
        while (!destFound) {
            int bestCost = Integer.MAX_VALUE;

            // Step 1: Find the best neighbor
            int bestNeighborX = 0;
            int bestNeighborY = 0;
            for (Direction dir : Direction.values()) {
                try {
                    int xNeighbor = xCurrent + dir.deltaX;
                    int yNeighbor = yCurrent + dir.deltaY;

                    distToDest = Math.abs(xDest - xNeighbor) + Math.abs(yDest - yNeighbor);
                    distToSource = Math.abs(xNeighbor - xSource) + Math.abs(yNeighbor - ySource);

                    // To fix the two node error, we'll just use the lowest weight.
                    // It's a game bruh, it's about what feels good, not what is good.
                    int weight = map[xNeighbor][yNeighbor];
                    if (map[xCurrent][yCurrent] < weight) {
                        weight = map[xCurrent][yCurrent];
                    }

                    int cost = weight * distToDest * distToSource;

                    if (cost < bestCost) {
                        bestCost = cost;
                        bestNeighborX = xCurrent;
                        bestNeighborY = yCurrent;
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }

            // Step 2: See if it's the edge
            if (xCurrent == xDest && yCurrent == yDest) destFound = true;

            // Step 3: Move to the best neighbor
            xCurrent = bestNeighborX;
            yCurrent = bestNeighborY;
        }

        System.out.println(distToDest);
    }

    /**
     * Compares the weight of source and destination, then returns the lower of the two.
     * The map contains a weight for each node; this implementation takes the lower weight as the true weight.
     */
    private int getLowestWeight(int[][] map, int xCurrent, int yCurrent, int xOther, int yOther) {
        int current = map[xCurrent][yCurrent];
        int other = map[xOther][yOther];
        return Math.min(current, other);
    }

}


