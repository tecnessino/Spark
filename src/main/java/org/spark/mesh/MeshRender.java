package org.spark.mesh;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.Color;
import org.lwjgl.util.vector.Vector3f;
import org.spark.actor.ActorModule;
import org.spark.light.Light;
import org.spark.material.Material;
import org.spark.shader.ShaderProgram;

public class MeshRender extends ActorModule {

    public Mesh mesh;
    public Material material;
    public MeshRender(Mesh mesh,Material material) {
        this.mesh = mesh;
        this.material = material;
    }

    @Override
    public void onStart() {
        super.onStart();
    }


    @Override
    public void onUpdate() {
        super.onUpdate();

        GL30.glBindVertexArray(mesh.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        material.use(getActor());
        GL11.glDrawElements(GL11.GL_TRIANGLES,mesh.getVertexCount(),GL11.GL_UNSIGNED_INT,0);
        material.unUse(getActor());
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
