package org.spark;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.spark.actor.Actor;
import org.spark.actor.ActorModule;
import org.spark.display.DisplayManager;
import org.spark.light.Light;
import org.spark.math.MatrixUtil;
import org.spark.mesh.Camera;
import org.spark.mesh.MeshLoader;
import org.spark.shader.Shaders;

import java.util.ArrayList;
import java.util.List;

public class Game {

    public List<Actor> actorList = new ArrayList<>();

    public Shaders shaders;
    public Camera camera;
    public MeshLoader meshLoader;
    public Matrix4f projectionMatrix;

    public List<Light> lights = new ArrayList<>();

    public Actor CreateActor() {
        Actor actor = new Actor();
        actor.game = this;
        actorList.add(actor);
        return actor;
    }

    public Light CreateLight() {
        Light light = new Light(new Vector3f(0,0,0),new Color(255,255,255));
        lights.add(light);
        return light;
    }

    public MeshLoader getMeshLoader() { return meshLoader; }
    public Shaders getShaders() { return shaders; }
    public Camera getCamera() { return camera; }
    public List<Light> getLights() { return lights; }

    public static void startGame(Game g) {
        DisplayManager.createDisplay();

        g.meshLoader = new MeshLoader();
        g.shaders = new Shaders();
        g.shaders.initalize();
        g.camera = new Camera();
        g.projectionMatrix = MatrixUtil.createProjectionMatrix();

        g.onStart();

        while(!Display.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            for(Actor actor : g.actorList) {
                for(ActorModule module : actor.actorModuleList) {
                    module.onUpdate();
                }
            }

            g.onUpdate();

            DisplayManager.updateDisplay();
        }
    }

    public void onStart() {

    }

    public void onUpdate() {

    }
}
