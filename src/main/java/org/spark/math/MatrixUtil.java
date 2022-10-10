package org.spark.math;


import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Matrix4f;

import java.awt.*;

public class MatrixUtil {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1f;
    private static final float FAR_PLANE = 1000;
    public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry,
                                                      float rz, float scale) {
        Matrix4f matrix = new Matrix4f();
        matrix.setIdentity();
        Matrix4f.translate(translation, matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rx), new Vector3f(1,0,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(ry), new Vector3f(0,1,0), matrix, matrix);
        Matrix4f.rotate((float) Math.toRadians(rz), new Vector3f(0,0,1), matrix, matrix);
        Matrix4f.scale(new Vector3f(scale,scale,scale), matrix, matrix);
        return matrix;
    }

   /* public static Matrix4f view(Vector3f position, Vector3f rotation) {
        Matrix4f result = new Matrix4f();
        result.setIdentity();

        Vector3f negative = new Vector3f(-position.getX(), -position.getY(), -position.getZ());
        Matrix4f translationMatrix = new Matrix4f().translate(negative.);
        Matrix4f rotXMatrix = new Matrix4f().rotate(rotation.getX(), new Vector3f(1, 0, 0));
        Matrix4f rotYMatrix = new Matrix4f().rotate(rotation.getY(), new Vector3f(0, 1, 0));
        Matrix4f rotZMatrix = new Matrix4f().rotate(rotation.getZ(), new Vector3f(0, 0, 1));

        //Matrix4f rotationMatrix = Matrix4f.multiply(rotZMatrix, Matrix4f.multiply(rotYMatrix, rotXMatrix));

        Matrix4f temp  = new Matrix4f();
        temp.setIdentity();
        Matrix4f.mul(rotYMatrix,rotXMatrix,temp);

        Matrix4f rotationMatrix = new Matrix4f();
        rotationMatrix.setIdentity();
        Matrix4f.mul(rotZMatrix,temp,rotationMatrix);

        Matrix4f.mul(translationMatrix,rotationMatrix,result);

        return result;
    }*/

    public static Matrix4f view(Vector3f position, Vector3f rotation) {
        Matrix4f viewMatrix = new Matrix4f();
        viewMatrix.setIdentity();
        Matrix4f.rotate(rotation.getX(), new Vector3f(1, 0, 0), viewMatrix,
                viewMatrix);
        Matrix4f.rotate(rotation.getY(), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
        Vector3f cameraPos = position;
        Vector3f negativeCameraPos = new Vector3f(-cameraPos.x,-cameraPos.y,-cameraPos.z);
        Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
        return viewMatrix;
    }


    public static Matrix4f createProjectionMatrix(){
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.setIdentity();
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV / 2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;

        return projectionMatrix;
    }


    public static int getRainbow(float seconds, float saturation, float brightness) {
        float hue = (System.currentTimeMillis() % (int) (seconds * 1000)) / (float) (seconds * 1000);
        int color = Color.HSBtoRGB (hue, saturation, brightness);
        return color;
    }
}
