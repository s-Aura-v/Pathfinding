package com.aurav.pathfinding.utility;

import com.aurav.pathfinding.entities.Node;

import java.util.*;

import static com.aurav.pathfinding.utility.FileInput.readInput;

public class SearchAlgorithm {
    enum Direction {
        NORTH(0, -1),
        SOUTH(0, 1),
        EAST(-1, 0),
        WEST(1, 0);

        private int deltaX;
        private int deltaY;

        Direction(int deltaX, int deltaY) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

    }

    public static void main(String[] args) {
        int[][] map = readInput("assets/inputs/input3");
        System.gc();
        search(map, 0, 0, 5, 5);
    }

    /**
     * CURRENTLY BROKEN FOR LARGE INPUTS; FOR SOME REASOn, READINPUT IS NOT CLEARING ITS MEMORY...
     * AND PARENT IS BROKEN AND MAYBE THE COST CALCULATION TOO...
     * <p>
     * Given a source and destination, attempts to find the best path.
     * Uses a combination of Dijkstra's and A*
     * <a href="https://en.wikipedia.org/wiki/Taxicab_geometry">Manhattan Distance for A* Heuristic</a>
     * <a href="https://www.redblobgames.com/pathfinding/a-star/introduction.html">Algorithm Overview</a>
     * <a href="https://github.com/riscy/a_star_on_grids">More on A*</a>
     *
     * @param xSource the x position of the source cell
     * @param ySource the y position of the source cell
     * @param xDest   the x position of the destination cell
     * @param yDest   the y position of the destination cell
     */
    public static void search(int[][] map, int xSource, int ySource, int xDest, int yDest) {
        // A* Setup
        PriorityQueue<Node> open = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Integer.compare(o1.cost, o2.cost);
            }
        });
        Set<char[]> closed = new HashSet<>(); // can I use another data type for this comparison because String takes too much memory...

        Node startNode = new Node(xSource, ySource, 0);
        open.offer(startNode);

        while (!open.isEmpty()) {
            Node current = open.poll();
            if (current.xCoordinates == xDest && current.yCoordinates == yDest) {
                System.out.println(Arrays.toString(current.parent) + " at cost: " + current.cost);
                break;
            }

            String k = current.xCoordinates + "," + current.yCoordinates;
            char[] key = k.toCharArray();
            if (closed.contains(key)) continue;
            closed.add(key);

            // Explore neighbors
            for (Direction dir : Direction.values()) {
                try {
                    int xNeighbor = current.xCoordinates + dir.deltaX;
                    int yNeighbor = current.yCoordinates + dir.deltaY;
                    int distToDest = Math.abs(xDest - xNeighbor) + Math.abs(yDest - yNeighbor);
                    int distToSource = Math.abs(xNeighbor - xSource) + Math.abs(yNeighbor - ySource);

                    // Since the nodes have individual weights, use the lowest weight.
                    // Assumes the user is smart
                    int weight = Math.min(
                        map[current.xCoordinates][current.yCoordinates],
                        map[xNeighbor][yNeighbor]);
                    int newCost = current.cost + (weight * (distToDest + distToSource)); // i feel like this is off
                    // the website isn't helping a lot so  look it up later...

                    Node neighbor = new Node(xNeighbor, yNeighbor, newCost);
                    current.parent = new int[]{xNeighbor, yNeighbor};
                    open.offer(neighbor);

                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }

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


