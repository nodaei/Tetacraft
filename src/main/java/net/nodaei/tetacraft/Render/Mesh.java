package net.nodaei.tetacraft.Render;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    // idk
    private int vaoId;
    private int vboId;
    private int eboId;

    // idk
    private FloatBuffer posBuffer;
    private IntBuffer idxBuffer;

    // The vertices coordinate
    private final float[] vertices = {
            //
            // front face
            -0.5f, 0.5f, 0.5f, // V0
            -0.5f, -0.5f, 0.5f, // V1
            0.5f, -0.5f, 0.5f, // V2
            0.5f, 0.5f, 0.5f, // V3
            // back face
            -0.5f, 0.5f, -0.5f, // V4
            0.5f, 0.5f, -0.5f, // V5
            -0.5f, -0.5f, -0.5f, // V6
            0.5f, -0.5f, -0.5f, // V7
    };

    // the order of which the vertices are getting rendered.
    private final int[] indices = {
            0, 1, 3, 3, 1, 2, // front face
            4, 0, 3, 5, 4, 3, // top face
            3, 2, 7, 5, 3, 7, // right face
            6, 1, 0, 6, 0, 4, // left face
            2, 1, 6, 2, 6, 7, // bottom face
            7, 6, 4, 7, 4, 5, // back face

    };

    public void init() {
        vaoId = glGenVertexArrays();
        glBindVertexArray(vaoId);

        vboId = glGenBuffers();
        posBuffer = MemoryUtil.memAllocFloat(vertices.length);
        posBuffer.put(vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0,0);
        glEnableVertexAttribArray(0);

        eboId = glGenBuffers();
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
        glDeleteVertexArrays(vaoId);

        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
        MemoryUtil.memFree(posBuffer);
        MemoryUtil.memFree(idxBuffer);
    }
}
