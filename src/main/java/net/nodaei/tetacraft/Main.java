package net.nodaei.tetacraft;

import net.nodaei.tetacraft.Render.Camera;
import net.nodaei.tetacraft.Render.Mesh;
import net.nodaei.tetacraft.Render.Shader;
import net.nodaei.tetacraft.Render.Texture;
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
    public Texture texture;

    public void run() {
        init();
        loop();

        // cleanup of GPU before exit
        mesh.delete();
        texture.delete();

        glfwDestroyWindow(window);
        glfwTerminate();
    }

    public void init() {
        // check if glfw has been initialized correctly, return error if not.
        if (!glfwInit()) throw new IllegalStateException("Unable to initiate GLFW");

        // creates window at launch
        window = glfwCreateWindow(
                width, height,
                "Vortex",
                NULL, NULL
        );

        // nothing GL-related before this line.
        glfwMakeContextCurrent(window);
        GL.createCapabilities();

        // disables the cursor as soon as the engine initializes.
        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);

        // enables or disables v-sync.
        // 1 = on, 0 = off, -1 = adaptive (only if supported)
        glfwSwapInterval(1);

        glEnable(GL_DEPTH_TEST);
        glViewport(0, 0, width, height);

        shader.init();
        shader.setProjection(width, height);

        // Resize callback; keeps projection and viewport in sync with the window
        glfwSetFramebufferSizeCallback(window, (win, w, h) -> {
            if (w <= 0 || h <= 0) return;
            this.width = w;
            this.height = h;
            glViewport(0, 0, w, h);
            shader.setProjection(w, h);
        });

        mesh.init();
        texture = new Texture(new String[]{
                "src/main/resources/assets/tetacraft/textures/blocks/dirt.png"
        });
    }

    public void loop() {
        while (!glfwWindowShouldClose(window)) {
            // Clear color and depth each frame
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            glClearColor(0.05f, 0.05f, 0.05f, 1.0f);

            // Delta time: time since last frame in seconds.
            // it's used so the movement isn't affected by the fps
            float currentFrame = (float) glfwGetTime();
            deltaTime = currentFrame - lastFrame;
            lastFrame = currentFrame;
            camera.update(window, deltaTime);

            // Bind texture to slot 0 before rendering
            texture.bind(0);
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
