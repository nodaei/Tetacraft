package net.nodaei.tetacraft.Render;

import net.nodaei.tetacraft.Utils;
import org.joml.Matrix4f;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    public int shaderProgram;
    public float FOV = 70.0f;

    public Matrix4f model      = new Matrix4f();
    public Matrix4f view       = new Matrix4f();
    public Matrix4f projection = new Matrix4f();

    // Uniform locations
    public int modelLoc;
    public int  viewLoc;
    public int  projLoc;
    public int   texLoc;

    public void init() {
        String vertexSource = Utils.readFile("/assets/tetacraft/shaders/base.vert");
        String fragmentSource = Utils.readFile("/assets/tetacraft/shaders/base.frag");

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexSource);
        glCompileShader(vertexShader);
        if (glGetShaderi(vertexShader, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Vertex shader error: " + glGetShaderInfoLog(vertexShader));

        int fragShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragShader, fragmentSource);
        glCompileShader(fragShader);
        if (glGetShaderi(fragShader, GL_COMPILE_STATUS) == GL_FALSE)
            throw new RuntimeException("Vertex shader error: " + glGetShaderInfoLog(fragShader));

        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexShader);
        glAttachShader(shaderProgram, fragShader);
        glLinkProgram(shaderProgram);

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE)
            throw new RuntimeException("Shader link error: " + glGetProgramInfoLog(shaderProgram));

        glDeleteShader(vertexShader);
        glDeleteShader(fragShader);

        modelLoc = glGetUniformLocation(shaderProgram, "model");
        viewLoc  = glGetUniformLocation(shaderProgram, "view");
        projLoc  = glGetUniformLocation(shaderProgram, "projection");
        texLoc   = glGetUniformLocation(shaderProgram, "uTexture");
    }

    public void render(Camera camera) {
        glUseProgram(shaderProgram);
        glUniform1i(texLoc, 0);

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
}
