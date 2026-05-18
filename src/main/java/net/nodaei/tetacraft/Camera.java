package net.nodaei.tetacraft;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {
    public Vector3f position = new Vector3f(0, 0, 3);

    public float pitch;
    public float yaw;

    public Matrix4f getViewMatrix() {
        return new Matrix4f()
                .rotateX((float) Math.toRadians(pitch))
                .rotateY((float) Math.toRadians(yaw))
                .translate(-position.x, -position.y, -position.z);
    }
}
