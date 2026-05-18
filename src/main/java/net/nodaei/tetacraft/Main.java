package net.nodaei.tetacraft;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main{
    public long window;

    public int width = 800;
    public int height = 600;

    public Mesh mesh = new Mesh();

    public void run() {
        init();
        loop();

        mesh.delete();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void init() {
        if (!glfwInit()) throw new IllegalStateException("Unable to initiate GLFW");

        window = glfwCreateWindow(
                width, height,
                "Vortex",
                NULL, NULL
        );

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, width, height);

        mesh.init();
    }

    public void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0.05f, 0.05f, 0.05f, 1.0f);

            mesh.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
