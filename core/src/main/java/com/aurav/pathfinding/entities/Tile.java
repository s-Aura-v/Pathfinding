package com.aurav.pathfinding.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Tile {
    short posX;
    short posY;
    short weight;
    Texture texture;

    public Tile(short posX, short posY, short weight) {
        this.posX = posX;
        this.posY = posY;
        this.weight = weight;
        texture = new Texture("maps/tile.png");
    }

    public Texture getTexture() {
        return texture;
    }

    public short getPosX() {
        return posX;
    }

    public short getPosY() {
        return posY;
    }

    public short getWeight() {
        return weight;
    }
}
