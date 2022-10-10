package org.spark.math;

import org.lwjgl.util.vector.Vector3f;

public class VectorUtil {
    public static double distance(Vector3f from,Vector3f to) {
        double dx = from.x - to.x;
        double dy = from.y - to.y;
        double dz = from.z - to.z;
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
}
