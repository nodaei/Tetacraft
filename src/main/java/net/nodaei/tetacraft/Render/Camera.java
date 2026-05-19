package net.nodaei.tetacraft.Render;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static org.lwjgl.glfw.GLFW.*;

public class Camera {
    // camera movement
    public Vector3f position = new Vector3f(0,0,1);
    private double lastMouseX, lastMouseY;
    private float pitch, yaw;

    // mouse lock logic boolean's
    private boolean firstMouse = true;
    private boolean mouseLocked = true;
    private boolean escWasPressed = false;

    // speed variables
    public float speed = 5.0f;
    public float mouseSensitivity = 0.5f;

    public void update(long window, float deltaTime) {
        updateMouseLock(window);
        if (mouseLocked) updateCamera(window);
        updateMovement(window, deltaTime);
    }

    // connects the mouse to the camera's yaw and pitch
    public void updateCamera(long window) {
        double[] mouseX = new double[1];
        double[] mouseY = new double[1];

        glfwGetCursorPos(window, mouseX, mouseY);

        if (firstMouse) {
            lastMouseX = mouseX[0];
            lastMouseY = mouseY[0];
            firstMouse = false;
            return;
        }

        float deltaX = (float) (mouseX[0] - lastMouseX);
        float deltaY = (float) (mouseY[0] - lastMouseY);

        lastMouseX = mouseX[0];
        lastMouseY = mouseY[0];

        yaw += deltaX * mouseSensitivity;
        pitch += deltaY * mouseSensitivity;

        if (pitch > 89.0f) pitch = 89.0f;
        if (pitch < -89.0f) pitch = -89.0f;
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
            position.y += moveSpeed; // upward
        }

        if (glfwGetKey(window, GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS) {
            position.y -= moveSpeed; // downward
        }
    }

    // toggles mouse lock on ESC
    public void updateMouseLock(long window) {
        boolean escPressed = glfwGetKey(window, GLFW_KEY_ESCAPE) == GLFW_PRESS;

        if (escPressed && !escWasPressed) {
            mouseLocked = !mouseLocked;
            firstMouse = true;

            glfwSetInputMode(
                    window,
                    GLFW_CURSOR,
                    mouseLocked ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL
            );
        }

        escWasPressed = escPressed;
    }

    // getter that provides the view matrix to the shader, as of now.
    public Matrix4f getViewMatrix() {
        return new Matrix4f()
                .rotateX((float) Math.toRadians(pitch))
                .rotateY((float) Math.toRadians(yaw))
                .translate(-position.x, -position.y, -position.z);

    }
}
