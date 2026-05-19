package net.nodaei.tetacraft.Render;

import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class Mesh {
    private int vaoId;
    private int vboId;
    private int eboId;

    private final float[] vertices = {
            // front face       ( layer 0 = side )
                -0.5f,  0.5f, 0.5f, 0.0f, 1.0f, 0, // V0
                -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 0, // V1
                 0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0, // V2
                 0.5f,  0.5f, 0.5f, 1.0f, 1.0f, 0, // V3

            // back face         ( layer 0 = side )
                -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 0, // V4
                 0.5f,  0.5f, -0.5f, 0.0f, 1.0f, 0, // V5
                -0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0, // V6
                 0.5f, -0.5f, -0.5f, 0.0f, 0.0f, 0, // V7

            // right face        ( layer 0 = side )
                0.5f,  0.5f,  0.5f,  0.0f, 1.0f, 0, // V8
                0.5f, -0.5f,  0.5f,  0.0f, 0.0f, 0, // V9
                0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 0, // V10
                0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 0, // V11

            // left face          ( layer 0 = side )
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 0, // V12
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 0, // V13
                -0.5f, -0.5f,  0.5f,  1.0f, 0.0f, 0, // V14
                -0.5f,  0.5f,  0.5f,  1.0f, 1.0f, 0, // V15

            // top face            ( layer 1 = top )
                -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, 1, // V16
                -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, 1, // V17
                 0.5f,  0.5f,  0.5f,  1.0f, 0.0f, 1, // V18
                 0.5f,  0.5f, -0.5f,  1.0f, 1.0f, 1, // V19

            // bottom face      ( layer 2 = bottom )
                -0.5f, -0.5f,  0.5f,  0.0f, 1.0f, 2, // V20
                -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, 2, // V21
                 0.5f, -0.5f, -0.5f,  1.0f, 0.0f, 2, // V22
                 0.5f, -0.5f,  0.5f,  1.0f, 1.0f, 2, // V23
            //    x      y      z      u     v    layer
    };

    // the order of which the vertices are getting rendered.
    private final int[] indices = {
            0,  1,  3,  3,  1,  2,  // front
            4,  5,  6,  6,  5,  7,  // back
            8,  9,  11, 11, 9,  10, // right
            12, 13, 15, 15, 13, 14, // left
            16, 17, 19, 19, 17, 18, // top
            20, 21, 23, 23, 21, 22, // bottom
    };

    public void init() {
        vaoId = glGenVertexArrays();
        vboId = glGenBuffers();
        eboId = glGenBuffers();

        glBindVertexArray(vaoId);

        // Upload Vertex data
        FloatBuffer vertexData = MemoryUtil.memAllocFloat(vertices.length);
        vertexData.put(vertices).flip();
        glBindBuffer(GL_ARRAY_BUFFER, vboId);
        glBufferData(GL_ARRAY_BUFFER, vertexData, GL_STATIC_DRAW);
        MemoryUtil.memFree(vertexData);

        // Upload Index data
        IntBuffer indexData = MemoryUtil.memAllocInt(indices.length);
        indexData.put(indices).flip();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexData, GL_STATIC_DRAW);
        MemoryUtil.memFree(indexData);

        // 6 is the amount of bytes per vertex.
        // (x, y, z, u, v, layer)
        int stride = 6 * Float.BYTES;

        // Sends the desired data to the shader:

        // x, y, z
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, 0);
        glEnableVertexAttribArray(0);

        // u, v
        glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 3L * Float.BYTES);
        glEnableVertexAttribArray(1);

        // layer
        glVertexAttribPointer(2, 1, GL_FLOAT, false, stride, 5L * Float.BYTES);
        glEnableVertexAttribArray(2);


        // cleaning
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    public void render() {
        glBindVertexArray(vaoId);
        glDrawElements(GL_TRIANGLES, indices.length, GL_UNSIGNED_INT, 0);
        glBindVertexArray(0);
    }

    public void delete() {
        glDeleteVertexArrays(vaoId);
        glDeleteBuffers(vboId);
        glDeleteBuffers(eboId);
    }
}
