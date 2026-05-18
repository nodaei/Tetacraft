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
    public Shader shader = new Shader();

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

        shader.init();
        shader.setProjection(width, height);

        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            if (w <= 0 || h <= 0) return;

            this.width = w;
            this.height = h;
            glViewport(0, 0, w, h);
            shader.setProjection(w, h);
        });

        mesh.init();
    }

    public void loop() {
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0.05f, 0.05f, 0.05f, 1.0f);

            mesh.render(shader);

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
