package net.nodaei.tetacraft;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11C.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main{
    public long window;

    public Mesh mesh;
    public Shader shader;

    public void run() {
        init();
        loop();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void init() {
        if (!glfwInit()) throw new IllegalStateException("Unable to initiate GLFW");

        window = glfwCreateWindow(
                800, 600,
                "Title",
                NULL, NULL
        );

        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        mesh = new Mesh();
        shader = new Shader();

        shader.readFile("/assets/tetacraft/shaders/base.vert");

        mesh.init();
    }

    public void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0.05f, 0.05f, 0.05f, 0.05f);

            mesh.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
