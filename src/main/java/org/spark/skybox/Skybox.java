package org.spark.skybox;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.spark.Game;
import org.spark.actor.ActorModule;
import org.spark.math.MatrixUtil;
import org.spark.mesh.Mesh;
import org.spark.mesh.MeshLoader;
import org.spark.mesh.MeshRender;
import org.spark.shader.impl.SkyboxShader;

public class Skybox extends ActorModule {

    public String path;
    public static float SIZE = 100;
    public Skybox(String path, float size) {
        this.path = path;
        Skybox.SIZE = size;
    }

    private static final float[] VERTICES = {
            -SIZE,  SIZE, -SIZE,
            -SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE, -SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,

            -SIZE, -SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE, -SIZE,  SIZE,
            -SIZE, -SIZE,  SIZE,

            -SIZE,  SIZE, -SIZE,
            SIZE,  SIZE, -SIZE,
            SIZE,  SIZE,  SIZE,
            SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE,  SIZE,
            -SIZE,  SIZE, -SIZE,

            -SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE, -SIZE,
            SIZE, -SIZE, -SIZE,
            -SIZE, -SIZE,  SIZE,
            SIZE, -SIZE,  SIZE
    };

    private static final String[] TEXTURE_FILES = {"posx", "negx", "posy", "negy", "posz", "negz"};

    public static Skybox instance;

    public Mesh cube;
    public int texture;
    public SkyboxShader shader;

    @Override
    public void onStart() {
        cube = getActor().getGame().getMeshLoader().createMeshFromVertices2(VERTICES,3);
        //cube = new MeshLoader().createMeshFromVertices2(VERTICES,3);
        texture = getActor().getGame().getMeshLoader().loadCubeMap(TEXTURE_FILES,path);
        //texture = new MeshLoader().loadCubeMap(TEXTURE_FILES);
        shader = (SkyboxShader) getActor().getGame().getShaders().get("Skybox");
        //shader = new SkyboxShader();

        Skybox.instance = this;
    }

    @Override
    public void onUpdate() {
        Matrix4f view = MatrixUtil.view(getActor().getGame().getCamera().getPosition(),getActor().getGame().getCamera().getRotation());

        //jestem glupi i nie umiem zrobic camery wiec narazie dam czysta macierz

        shader.start();
        shader.loadMatrix(shader.getUniformLocation("viewMatrix"),view);
        shader.loadMatrix(shader.getUniformLocation("projectionMatrix"),getActor().getGame().projectionMatrix);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP,texture);
        GL11.glDrawArrays(GL11.GL_TRIANGLES,0,cube.getVertexCount());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }
}
