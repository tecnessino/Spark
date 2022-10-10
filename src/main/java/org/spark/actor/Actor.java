package org.spark.actor;

import org.spark.Game;

import org.lwjgl.util.vector.Vector3f;

import java.util.ArrayList;
import java.util.List;

public class Actor {
    public List<ActorModule> actorModuleList = new ArrayList<>();

    public Game game;
    public Vector3f position = new Vector3f(0,0,0),rotation = new Vector3f(0,0,0),scale = new Vector3f(1,1,1);


    public Game getGame() {
        return game;
    }

    public Vector3f getPosition() {
        return position;
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    public Vector3f getRotation() {
        return rotation;
    }

    public void setRotation(Vector3f rotation) {
        this.rotation = rotation;
    }

    public Vector3f getScale() {
        return scale;
    }

    public void setScale(Vector3f scale) {
        this.scale = scale;
    }

    public void addModule(ActorModule actorModule) {
        actorModule.actor = this;
        actorModule.onStart();
        actorModuleList.add(actorModule);
    }
}
