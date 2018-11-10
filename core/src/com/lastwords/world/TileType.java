package com.lastwords.world;

import java.util.HashMap;

public enum TileType {

    GRASS(1, true, "Grass"),
    DIRT(2, true, "Dirt"),
    SKY(3, false, "Sky"),
    LAVA(4, true, "Lava"),
    CLOUD(5, true, "Cloud"),
    STONE(6, true, "Stone");

    public static final int TILE_SIZE = 16;

    private final int id;
    private final boolean collidable;
    private final String name;
    private final float damage;


    private TileType(int id, boolean collidable, String name) {
        this(id, collidable, name, 0);
    }

    private TileType(int id, boolean collidable, String name, float damage) {
        this.id = id;
        this.collidable = collidable;
        this.name = name;
        this.damage = damage;
    }

    public int getId() {
        return id;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public String getName() {
        return name;
    }

    public float getDamage() {
        return damage;
    }

    public static TileType byId(int id) {
        for (TileType tileType: values()) {
            if (tileType.id == id) {
                return tileType;
            }
        }
        return null;
    }

}
