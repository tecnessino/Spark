package org.spark.material;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.spark.actor.Actor;
import org.spark.light.Light;
import org.spark.math.MatrixUtil;
import org.spark.math.VectorUtil;
import org.spark.shader.ShaderProgram;
import org.spark.shader.impl.RenderShader;
import org.spark.skybox.Skybox;
import org.spark.texture.Texture;

import java.awt.*;
import java.util.Optional;

public class Material {

    public Color color;
    public Texture texture;

    public boolean useColor,disableLightReaction = false,useDevelopment = false;
    public boolean cubeMapReflections = false;
    public boolean useTexture;

    public float shineDamper = 1,reflectivity = 0;

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }

    public boolean isCubeMapReflections() {
        return cubeMapReflections;
    }

    public void setCubeMapReflections(boolean cubeMapReflections) {
        this.cubeMapReflections = cubeMapReflections;
    }

    public void use(Actor actor) {
        RenderShader shader = (RenderShader) actor.getGame().getShaders().get("Material");
        shader.start();
        shader.loadBoolean(shader.getUniformLocation("use_albedo_color"), isUseColor());
        shader.loadBoolean(shader.getUniformLocation("varying_albedo_color"), useDevelopment);
        if (isUseColor()) {
            shader.loadVector(shader.getUniformLocation("albedo_color"),
                    new Vector3f(
                            color.getRed() / 255,
                            color.getGreen() / 255,
                            color.getBlue() / 255
                    ));
        }

        shader.loadBoolean(shader.getUniformLocation("disable_light_reaction"),disableLightReaction);

        shader.loadBoolean(shader.getUniformLocation("use_texture"), isUseTexture());

        if (isUseTexture()) {
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        }

        //shader.loadInt(shader.getUniformLocation("enviroMap"),Skybox.instance.texture);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, Skybox.instance.texture);


        Matrix4f transform = MatrixUtil.createTransformationMatrix(
                actor.getPosition(),
                actor.getRotation().x,
                actor.getRotation().y,
                actor.getRotation().z,
                actor.getScale().getX()
        );

        Matrix4f view = MatrixUtil.view(actor.getGame().getCamera().getPosition(),actor.getGame().getCamera().getRotation());

        shader.loadMatrix(shader.getUniformLocation("transformMatrix"), transform);
        shader.loadMatrix(shader.getUniformLocation("projectionMatrix"), actor.getGame().projectionMatrix);
        shader.loadMatrix(shader.getUniformLocation("viewMatrix"), view);
        shader.loadVector(shader.getUniformLocation("cameraPosition"),actor.getGame().getCamera().getPosition());

        shader.addShine(shineDamper,reflectivity);
        Optional<Light> light = actor.getGame().getLights().stream().filter(l -> VectorUtil.distance(actor.position,l.position) <= 6.0).findFirst();
        if(light.isPresent()) {
            shader.addLight(light.get());
        }

        shader.loadBoolean(shader.getUniformLocation("cubemap_reflections"),cubeMapReflections);

    }

    public void secret() {
        useDevelopment = true;
    }
    public void unUse(Actor actor) {
        ShaderProgram shader = actor.getGame().getShaders().get("Material");
        shader.stop();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public boolean isUseColor() {
        return useColor;
    }

    public void setUseColor(boolean useColor) {
        this.useColor = useColor;
    }

    public boolean isUseTexture() {
        return useTexture;
    }

    public void setUseTexture(boolean useTexture) {
        this.useTexture = useTexture;
    }

    public void disableReactionToLight() {
        this.disableLightReaction = true;
    }
}
