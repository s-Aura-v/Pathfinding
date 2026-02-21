package com.aurav.pathfinding;

import com.aurav.pathfinding.screens.MazeScreen;

public class Pathfinder extends BaseGame {

    @Override
    public void create() {
        super.create();
        setActiveScreen(new MazeScreen());
    }
}
