package org.spark.shader;

import org.spark.shader.impl.RenderShader;
import org.spark.shader.impl.SkyboxShader;

import java.util.HashMap;

public class Shaders {
    public HashMap<String,ShaderProgram> shaderProgramHashMap = new HashMap<>();

    public ShaderProgram get(String name) {
        return shaderProgramHashMap.get(name);
    }



    public void initalize() {
        shaderProgramHashMap.put("Material",new RenderShader());
        shaderProgramHashMap.put("Skybox",new SkyboxShader());
    }
}
