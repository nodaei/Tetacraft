package net.nodaei.tetacraft;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;

    private FloatBuffer posBuffer;
    private IntBuffer idxBuffer;

    private final float[] vertices = {
            -0.5f, 0.5f, 0.0f,
            -0.5f, -0.5f, 0.0f,
            0.5f, -0.5f, 0.0f,
            0.5f, 0.5f, 0.0f
    };

    private final int[] indices = {
            0, 1, 3, //first triangle
            3, 1, 2 //second triangle
    };

    public void init() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        int vboId = glGenBuffers();
        posBuffer = MemoryUtil.memAllocFloat(vertices.length);
        posBuffer.put(vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0,0);
        glEnableVertexAttribArray(0);

        int eboId = glGenBuffers();
        idxBuffer = MemoryUtil.memAllocInt(indices.length);
        idxBuffer.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, idxBuffer, GL_STATIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
    }

    public void delete() {
        glBindVertexArray(0);
        glUseProgram(0);
        MemoryUtil.memFree(posBuffer);
        MemoryUtil.memFree(idxBuffer);
    }
}
