package com.aurav.pathfinding.entities;

public class Node {
    public int[] parent;
    public int xCoordinates;
    public int yCoordinates;
    public int cost;

    public Node(int xCoordinates, int yCoordinates, int cost) {
        this.xCoordinates = xCoordinates;
        this.yCoordinates = yCoordinates;
        this.cost = cost;
    }
}
