package com.gunar.lab4

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.opengles.GL10

class Rectangulo {
    /**
     * 2
     * /\
     * /  \
     * /    \
     * /      \
     * /________\
     * 0 		1
     */
    /* Coordenadas cartesianas (x, y) */
    private val vertices = floatArrayOf(
        -10f, -1f, 10f, // 0
        10f, -1f, 10f, // 1
        10f, -1f, -10f, // 2
        -10f, -1f, -10f
    )
    internal var maxColor = 255.toByte()
    private val colores = byteArrayOf(


        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor,
        maxColor
    )

    internal var bufVertices: FloatBuffer
    internal var bufColores: ByteBuffer

    init {
        /* Lee los v�rtices */
        val bufByte = ByteBuffer.allocateDirect(vertices.size * 4)
        bufByte.order(ByteOrder.nativeOrder()) // Utiliza el orden del byte nativo
        bufVertices = bufByte.asFloatBuffer() // Convierte de byte a float
        bufVertices.put(vertices)
        bufVertices.rewind() // puntero al principio del buffer

        /*read colors*/
        bufColores = ByteBuffer.allocateDirect(colores.size)
        bufColores.put(colores)
        bufColores.position(0)
    }

    fun dibuja(gl: GL10) {
        /* Se activa el arreglo de v�rtices */
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        /*colors*/
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)
        /* Se especifica los datos del arreglo de v�rtices */
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices)//3 para 3d en z
        /*Se especifica los datos del arreglo de colores*/
        gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, bufColores)

        gl.glDrawArrays(GL10.GL_TRIANGLE_FAN, 0, 4)

        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)
    }
}
