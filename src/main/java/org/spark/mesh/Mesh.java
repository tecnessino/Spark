package org.spark.mesh;

public class Mesh {
    public int vaoID,vertexCount;

    public Mesh(int vaoID, int vertexCount) {
        this.vaoID = vaoID;
        this.vertexCount = vertexCount;
    }

    public int getVaoID() {
        return vaoID;
    }

    public void setVaoID(int vaoID) {
        this.vaoID = vaoID;
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }
}
