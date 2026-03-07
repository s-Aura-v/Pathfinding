package com.aurav.pathfinding.utility;

import com.aurav.pathfinding.entities.Node;

import java.util.*;

import static com.aurav.pathfinding.utility.FileInput.teleportPair;

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
        int[][] map = FileInput.readInput("assets/inputs/input3");
        Node n = search(map, 0, 0, 9999, 9999);
        System.out.println(getPath(n));
    }

    static int sizeX;
    static int sizeY;

    /**
     * CURRENTLY BROKEN FOR LARGE INPUTS;
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
    public static Node search(int[][] map, int xSource, int ySource, int xDest, int yDest) {
        PriorityQueue<Node> open = new PriorityQueue<>(new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                return Integer.compare(o1.fCost, o2.fCost);
            }
        });
        boolean[][] visited = new boolean[map.length][map[0].length];
        int[][] bestGCost = new int[map.length][map[0].length];
        for (int[] row : bestGCost) Arrays.fill(row, Integer.MAX_VALUE);

        Node startNode = new Node(xSource, ySource);
        startNode.gCost = 0;
        bestGCost[xSource][ySource] = 0;
        open.offer(startNode);

        Node bestNode = null;

        sizeX = map[0].length;
        sizeY = map.length;

        while (!open.isEmpty()) {
            Node current = open.poll();
            visited[current.x][current.y] = true;

            if (current.x == xDest && current.y == yDest) {
                bestNode = current;
                break;
//                return current;
            }

            for (Direction dir : Direction.values()) {
                try {
                    int xNeighbor = current.x + dir.deltaX;
                    int yNeighbor = current.y + dir.deltaY;

                    if (map[xNeighbor][yNeighbor] == 10) continue;
                    if (visited[xNeighbor][yNeighbor]) continue;
                    if (map[xNeighbor][yNeighbor] > 100) {
                        int[] teleporters = teleportPair.get(map[xNeighbor][yNeighbor]);
                        if (teleporters[0] == xNeighbor && teleporters[1] == yNeighbor) {
                            xNeighbor = teleporters[2];
                            yNeighbor = teleporters[3];
                        } else {
                            xNeighbor = teleporters[0];
                            yNeighbor = teleporters[1];
                        }
                    }

                    // Pick the lowest cost between the nodes
                    // gCost = [cost to current node] + [cost to get to neighbor node]
                    int gCost = current.gCost + Math.min(map[current.x][current.y], map[xNeighbor][yNeighbor]);
                    if (gCost < bestGCost[xNeighbor][yNeighbor]) {
                        bestGCost[xNeighbor][yNeighbor] = gCost;
                        Node neighbor = new Node(xNeighbor, yNeighbor);
                        neighbor.parent = current;
                        neighbor.gCost = gCost;
                        neighbor.fCost = gCost + (Math.abs(xDest - xNeighbor) + Math.abs(yDest - yNeighbor));
                        open.add(neighbor);
                    }
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
            }
        }

        // If we haven't found the best node, then let's start looking at the teleports exclusively. again
        if (bestNode == null) {
//            teleportSearch();
        }

//        int bestTCost = teleportSearch(xDest, yDest, xSource, ySource, map);
//        if (bestTCost < bestNode.gCost) {
        // do some wizardly
//        }
        return bestNode;
    }

    /**
     * My initial plan for handling teleports, but I've decided against it, because I found an easier solution that works mostly.
     * This method attempts to find the closest teleport to the goal,
     * then see if we can get a shorter path from there.
     * Use the cost for A*, but ignore it for the pathfinding.
     * Basically running BFS, but keeping track of cost to see if it gives us a better path.
     * <a href="https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm">...</a>
     */
    public static int teleportSearch(int xSource, int ySource, int xDest, int yDest, int[][] map) {
        Queue<Integer[]> queue = new PriorityQueue<>();
        queue.add(new Integer[]{xDest, yDest});

        // Memory efficient because doing 2, 2d int arrays is better than storing 100 million Node objects
        boolean[][] visited = new boolean[map.length][map[0].length];
        int[][] bestCost = new int[map.length][map[0].length];
        for (int[] row : bestCost) Arrays.fill(row, Integer.MAX_VALUE);
        bestCost[xDest][yDest] = 0;

        while (!queue.isEmpty()) {
            Integer[] node = queue.poll();

            for (Direction d : Direction.values()) {
                int xNeighbor = node[0] + d.deltaX;
                int yNeighbor = node[1] + d.deltaY;

                int cost = map[xNeighbor][yNeighbor];
                if (cost < bestCost[xNeighbor][yNeighbor]) {
                    bestCost[xNeighbor][yNeighbor] = cost;
                }

                // Works in conjunction with the file input logic
                if (map[xNeighbor][yNeighbor] > 100) {
                    int[] teleporters = teleportPair.get(map[xNeighbor][yNeighbor]);
                    if (teleporters[0] == xNeighbor && teleporters[1] == yNeighbor) {
                        search(map, xNeighbor, yNeighbor, xSource, ySource);
                    } else {
                        search(map, teleporters[2], teleporters[3], xSource, ySource);
                    }
                }
            }
        }

        return 0;
    }

    public static boolean[][] getPath(Node n) {
        boolean[][] path = new boolean[sizeY][sizeX];
        if (n == null) {
            return path;
        }
        Node current = n;
        while (current.parent != null) {
            path[current.x][current.y] = true;
            current = current.parent;
        }
        return path;
    }

}


