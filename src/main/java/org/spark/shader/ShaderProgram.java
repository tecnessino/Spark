package org.spark.shader;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import java.nio.FloatBuffer;
import java.util.Scanner;

public abstract class ShaderProgram {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    public ShaderProgram(String vertexFile,String fragmentFile){
        vertexShaderID = loadShader(vertexFile,GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader(fragmentFile,GL20.GL_FRAGMENT_SHADER);
        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
    }


    private static FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);
    public void start(){
        GL20.glUseProgram(programID);
    }

    public void stop(){
        GL20.glUseProgram(0);
    }

    public void cleanUp(){
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    protected abstract void bindAttributes();

    public int getUniformLocation(String uniformName){
        return GL20.glGetUniformLocation(programID,uniformName);
    }


    public void bindAttribute(int attribute, String variableName){
        GL20.glBindAttribLocation(programID, attribute, variableName);
    }

    public void loadFloat(int location, float value){
        GL20.glUniform1f(location, value);
    }

    public void loadInt(int location,int value) {
        GL20.glUniform1i(location,value);
    }

    public void loadVector(int location, Vector3f vector){
        GL20.glUniform3f(location,vector.x,vector.y,vector.z);
    }

    public void loadBoolean(int location, boolean value){
        float toLoad = 0;
        if(value){
            toLoad = 1;
        }
        GL20.glUniform1f(location, toLoad);
    }

    public void loadMatrix(int location, Matrix4f matrix){
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(location, false, matrixBuffer);
    }

    private static int loadShader(String file, int type){
        StringBuilder shaderSource = new StringBuilder();

        Scanner s = new Scanner(ShaderProgram.class.getResourceAsStream(file));
        while(s.hasNextLine()) {
            shaderSource.append(s.nextLine()).append("\n");
        }

        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS )== GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }

}