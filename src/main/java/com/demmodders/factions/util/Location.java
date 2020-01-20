package com.demmodders.factions.util;

public class Location {
    String world;
    float x;
    float y;
    float z;
    float pitch;
    float yaw;
    public Location(String world, float x, float y, float z, float pitch, float yaw){
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }
}

