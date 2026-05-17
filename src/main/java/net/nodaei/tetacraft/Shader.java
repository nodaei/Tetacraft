package net.nodaei.tetacraft;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.lwjgl.opengl.GL20.*;

public class Shader {
    public int shaderProgram;

    public String vertexShaderID;
    public String fragShaderID;

    public int create() {
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

        if (glGetProgrami(shaderProgram, GL_LINK_STATUS) == GL_FALSE) {
            throw new RuntimeException(glGetProgramInfoLog(shaderProgram));
        }

        return shaderProgram;
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
