package net.nodaei.tetacraft;

import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.io.InputStream;
import java.nio.FloatBuffer;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    public int shaderProgram;

    public float FOV = 70.0f;

    public Matrix4f model = new Matrix4f();
    public Matrix4f view = new Matrix4f();
    public Matrix4f projection = new Matrix4f();

    public int modelLoc;
    public int viewLoc;
    public int projLoc;

    public String vertexShaderID;
    public String fragShaderID;

    public void init() {
        vertexShaderID = readFile("/assets/tetacraft/shaders/base.vert");
        fragShaderID = readFile("/assets/tetacraft/shaders/base.frag");

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexShaderID);
        glCompileShader(vertexShader);

        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragShader, fragShaderID);
        glCompileShader(fragShader);

        shaderProgram = glCreateProgram();

        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragShader);

        glLinkProgram(shaderProgram);

        modelLoc = glGetUniformLocation(shaderProgram, "model");
        viewLoc = glGetUniformLocation(shaderProgram, "view");
        projLoc = glGetUniformLocation(shaderProgram, "projection");

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
        }
    }

    public void render(Camera camera) {
        glUseProgram(shaderProgram);

        view = camera.getViewMatrix();

        FloatBuffer buffer = MemoryUtil.memAllocFloat(16);

        try {
            model.get(buffer);
            glUniformMatrix4fv(modelLoc, false, buffer);
            buffer.clear();

            view.get(buffer);
            glUniformMatrix4fv(viewLoc, false, buffer);
            buffer.clear();

            projection.get(buffer);
            glUniformMatrix4fv(projLoc, false, buffer);
        } finally {
            MemoryUtil.memFree(buffer);
        }
    }

    public void setProjection(int width, int height) {
        projection.identity().perspective(
                (float) Math.toRadians(FOV),
                (float) width / height,
                0.1f,
                Float.POSITIVE_INFINITY
        );
    }

    public String readFile(String path) {
        try (InputStream stream = Shader.class.getResourceAsStream(path)) {
            if (stream == null) throw new RuntimeException("Shader not found:" + path);

            return new String(
                    stream.readAllBytes(),
                    StandardCharsets.UTF_8
            );

        } catch (Exception exception) {
            throw new RuntimeException("Failed to load shader:" + path, exception);
        }
    }
}
