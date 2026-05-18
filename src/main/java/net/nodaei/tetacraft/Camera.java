package net.nodaei.tetacraft;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    public Vector3f position = new Vector3f(0,0,1);
    public float pitch, yaw;

    public float speed = 5.0f;

    public void update(long window, float deltaTime) {
        updateMovement(window, deltaTime);
    }

    // instead of moving the camera, we're moving the world.
    public void updateMovement(long window, float deltaTime) {
        float moveSpeed = speed * deltaTime;

        float yawRad = (float) Math.toRadians(yaw);
        float forwardX = (float) Math.sin(yawRad);
        float forwardZ = (float) -Math.cos(yawRad);

        // WASD logic
        if (glfwGetKey(window, GLFW_KEY_W) == GLFW_PRESS) { // forward
            position.x += forwardX * moveSpeed;
            position.z += forwardZ * moveSpeed;
        }

        if (glfwGetKey(window, GLFW_KEY_S) == GLFW_PRESS) { // backward
            position.x -= forwardX * moveSpeed;
            position.z -= forwardZ * moveSpeed;
        }

        if (glfwGetKey(window, GLFW_KEY_A) == GLFW_PRESS) { // leftward
            position.x += forwardZ * moveSpeed;
            position.z -= forwardX * moveSpeed;
        }

        if (glfwGetKey(window, GLFW_KEY_D) == GLFW_PRESS) { // rightward
            position.x -= forwardZ * moveSpeed;
            position.z += forwardX * moveSpeed;
        }

        // up and down
        if (glfwGetKey(window, GLFW_KEY_SPACE) == GLFW_PRESS) {
            position.y += moveSpeed; // downward
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            position.y -= moveSpeed; // downward
        }

        // Camera rotation; change later to use mouse.
        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {
            pitch -= moveSpeed * 50;
        }

        if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {
            pitch += moveSpeed * 50;
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT) == GLFW_PRESS) {
            yaw -= moveSpeed * 50;
        }

        if (glfwGetKey(window, GLFW_KEY_RIGHT) == GLFW_PRESS) {
            yaw += moveSpeed * 50;
        }
    }

    public Matrix4f getViewMatrix() {
        return new Matrix4f()
                .rotateX((float) Math.toRadians(pitch))
                .rotateY((float) Math.toRadians(yaw))
                .translate(-position.x, -position.y, -position.z);

    }
}
