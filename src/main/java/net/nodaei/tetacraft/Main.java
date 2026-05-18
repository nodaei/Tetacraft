package net.nodaei.tetacraft;

import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Main{
    public long window;

    public int width = 800;
    public int height = 600;

    public float deltaTime;
    public float lastFrame;

    public Mesh mesh = new Mesh();
    public Shader shader = new Shader();
    public Camera camera = new Camera();

    public void run() {
        init();
        loop();

        mesh.delete();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void init() {
        // we check first if glfw is ready, if not, it stops the program.
        if (!glfwInit()) throw new IllegalStateException("Unable to initiate GLFW");

        // we create the window
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

            // delta time logic so the movement is normalized independent of the fps
            float currentFrame = (float) glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;

            camera.update(window, deltaTime);
            shader.render(camera);
            mesh.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}
