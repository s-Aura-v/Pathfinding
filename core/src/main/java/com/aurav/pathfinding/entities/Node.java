package com.aurav.pathfinding.entities;

public class Node {
    public Node parent;
    public int x;
    public int y;
    public int gCost; // the cost from start to current node
    public int fCost; // the cost from start to current node + heuristic

    /**
     * The node representing the values required for an A* algorithm.
     *
     * @param x     the x position coordinate
     * @param y     the y position coordinate
     */
    public Node(int x, int y) {
        this.x = x;
        this.y = y;
        this.gCost = Integer.MAX_VALUE;
    }
}
