package com.aurav.pathfinding.utility;

import com.aurav.pathfinding.entities.Node;

import java.util.*;

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
        FileInput fs = new FileInput();
        int[][] map = fs.readInput("assets/inputs/input3");
        System.gc();
        search(map, 0, 0, 5, 5);
    }

    /**
     * CURRENTLY BROKEN FOR LARGE INPUTS; FOR SOME REASOn, READINPUT IS NOT CLEARING ITS MEMORY...
     * AND PARENT IS BROKEN AND MAYBE THE COST CALCULATION TOO...
     * <p>
     * Given a source and destination, attempts to find the best path.
     * Uses a combination of Dijkstra's and A*
     * <a href="https://en.wikipedia.org/wiki/A*_search_algorithm">Pseudo Code for A*</a>
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
                return Integer.compare(o1.fCost, o2.fCost);
            }
        });
        boolean[][] visited = new boolean[map.length][map[0].length];

        Node startNode = new Node(xSource, ySource);
        startNode.gCost = 0;
        open.offer(startNode);

        while (!open.isEmpty()) {
            // Polling the lowest cost node
            Node current = open.poll();
            visited[current.x][current.y] = true;
            if (current.x == xDest && current.y == yDest) {
                System.out.println(Arrays.toString(current.parent) + ": " + current.gCost);
                break;
            }

            // Explore neighbors
            for (Direction dir : Direction.values()) {
                try {
                    int xNeighbor = current.x + dir.deltaX;
                    int yNeighbor = current.y + dir.deltaY;

                    // Pass Cases:
                    if (map[xNeighbor][yNeighbor] == 10) continue;
                    if (visited[xNeighbor][yNeighbor]) continue;

                    Node neighbor = new Node(xNeighbor, yNeighbor);
                    // gcost = [cost to current npde] + [cost to get to neighbor node]
                    int gCost = current.gCost + Math.min(map[current.x][current.y], map[xNeighbor][yNeighbor]);
                    if (gCost < neighbor.gCost) {
                        neighbor.parent = new int[]{current.x, current.y};
                        neighbor.gCost = gCost;
                        neighbor.fCost = gCost + (Math.abs(xDest - xNeighbor) + Math.abs(yDest - yNeighbor));
                        if (!open.contains(neighbor)) open.add(neighbor);
                    }

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


