package org.spark.mesh;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.spark.texture.Texture;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MeshLoader {

    public List<Integer> vaos = new ArrayList<>();
    public List<Integer> vbos = new ArrayList<>();
    public Mesh createMeshFromVertices(float[] positions,float[] textureCoords,float[] normals, int[] indices) {
        int vaoID = genVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,textureCoords);
        storeDataInAttributeList(2,3,normals);
        unbindVAO();
        return new Mesh(vaoID,indices.length);
    }

    public Mesh loadObjModel(String fileName) {
        InputStream fr = null;
        fr = this.getClass().getResourceAsStream(fileName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(fr));

        String line;
        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();
        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray = null;
        int[] indicesArray = null;
        try {

            while (true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if (line.startsWith("v ")) {
                    Vector3f vertex = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    vertices.add(vertex);
                } else if (line.startsWith("vt ")) {
                    Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]));
                    textures.add(texture);
                } else if (line.startsWith("vn ")) {
                    Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]),
                            Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                    normals.add(normal);
                } else if (line.startsWith("f ")) {
                    textureArray = new float[vertices.size() * 2];
                    normalsArray = new float[vertices.size() * 3];
                    break;
                }
            }

            while (line != null) {
                if (!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");
                String[] vertex1 = currentLine[1].split("/");
                String[] vertex2 = currentLine[2].split("/");
                String[] vertex3 = currentLine[3].split("/");

                processVertex(vertex1,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex2,indices,textures,normals,textureArray,normalsArray);
                processVertex(vertex3,indices,textures,normals,textureArray,normalsArray);
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size()*3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vector3f vertex:vertices){
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for(int i=0;i<indices.size();i++){
            indicesArray[i] = indices.get(i);
        }
        return createMeshFromVertices(verticesArray, textureArray,normalsArray, indicesArray);

    }

    private static void processVertex(String[] vertexData, List<Integer> indices,
                                      List<Vector2f> textures, List<Vector3f> normals, float[] textureArray,
                                      float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);
        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1])-1);
        textureArray[currentVertexPointer*2] = currentTex.x;
        textureArray[currentVertexPointer*2+1] = 1 - currentTex.y;
        Vector3f currentNorm = normals.get(Integer.parseInt(vertexData[2])-1);
        normalsArray[currentVertexPointer*3] = currentNorm.x;
        normalsArray[currentVertexPointer*3+1] = currentNorm.y;
        normalsArray[currentVertexPointer*3+2] = currentNorm.z;
    }

    public Mesh createMeshFromVertices(float[] positions,int[] indices) {
        int vaoID = genVAO();
        bindIndicesBuffer(indices);
        storeDataInAttributeList(0,3,positions);
        storeDataInAttributeList(1,2,new float[] {
                0
        });
        unbindVAO();
        return new Mesh(vaoID,indices.length);
    }

    public Mesh createMeshFromVertices2(float[] positions,int dimesions) {
        int vaoID = genVAO();
        storeDataInAttributeList(0,dimesions,positions);
        unbindVAO();
        return new Mesh(vaoID,positions.length/dimesions);
    }


    public void gracefulClose() {
        for(int i : vaos) {
            GL30.glDeleteVertexArrays(i);
        }
        for(int i : vbos) {
            GL15.glDeleteBuffers(i);
        }
    }

    public int genVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);
        return vaoID;
    }
    public void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    public void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER,vboID);
        IntBuffer buffer = storeDataInIntBuf(indices);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
    }

    public int loadCubeMap(String[] textures,String path) {
        int texID = GL11.glGenTextures();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP,texID);

        for(int i = 0;i<textures.length;i++) {
            Texture texture = Texture.createTexture(path + textures[i] + ".jpg");
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X+i,0,GL11.GL_RGBA,texture.width,texture.height,0,GL11.GL_RGBA,GL11.GL_UNSIGNED_BYTE,texture.buffer1);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MAG_FILTER,GL11.GL_LINEAR);
            GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP,GL11.GL_TEXTURE_MIN_FILTER,GL11.GL_LINEAR);
        }

        return texID;
    }

    public void storeDataInAttributeList(int attributeNumber,int coordinateSize,float[] data) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,vboID);
        FloatBuffer buffer = storeDataInFloatBuf(data);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER,buffer,GL15.GL_STATIC_DRAW);
        GL20.glVertexAttribPointer(attributeNumber,coordinateSize, GL11.GL_FLOAT,false,0,0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER,0);
    }

    public FloatBuffer storeDataInFloatBuf(float[] data) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }

    public IntBuffer storeDataInIntBuf(int[] data) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data);
        buffer.flip();
        return buffer;
    }
}
