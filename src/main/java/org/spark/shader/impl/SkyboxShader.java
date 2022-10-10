package org.spark.shader.impl;

import org.spark.shader.ShaderProgram;

public class SkyboxShader extends ShaderProgram {

    public SkyboxShader() {
        super("/Spark/Shaders/Skybox.vert", "/Spark/Shaders/Skybox.frag");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0,"position");
    }
}
