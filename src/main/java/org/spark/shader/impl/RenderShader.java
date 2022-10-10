package org.spark.shader.impl;

import org.lwjgl.util.vector.Vector3f;
import org.spark.light.Light;
import org.spark.shader.ShaderProgram;

public class RenderShader extends ShaderProgram {

    public RenderShader() {
        super("/Spark/Shaders/MaterialRender.vert", "/Spark/Shaders/MaterialRender.frag");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
        super.bindAttribute(1,"textureCoords");
        super.bindAttribute(2,"normal");
    }

    public void addLight(Light light) {
        loadVector(getUniformLocation("lightPosition"),light.position);
        loadVector(getUniformLocation("lightColor"),new Vector3f(
                light.color.getRed()/255,
                light.color.getGreen()/255,
                light.color.getBlue()/255
        ));
    }

    public void addShine(float damper,float reflectivity) {
        loadFloat(getUniformLocation("shineDamper"),damper);
        loadFloat(getUniformLocation("reflectivity"),reflectivity);
    }
}
