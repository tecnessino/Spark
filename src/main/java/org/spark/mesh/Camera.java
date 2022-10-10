package org.spark.mesh;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
    public Vector3f position = new Vector3f(0,0,0), rotation = new Vector3f(0,0,0);

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
}
