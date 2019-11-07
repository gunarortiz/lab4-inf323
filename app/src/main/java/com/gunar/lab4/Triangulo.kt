package com.gunar.lab4

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

import javax.microedition.khronos.opengles.GL10

class Triangulo {


    /* Coordenadas cartesianas (x, y, z) */
    private val vertices = floatArrayOf(

        // Adelante
        -1f, -1f, 1f, // 0
        1f, -1f, 1f, // 1
        0f, 1f, 0f, // 2

        // Abajo
        -1f, -1f, -1f, // 3
        1f, -1f, -1f, // 4
        1f, -1f, 1f, // 5
        -1f, -1f, 1f, // 6

        // Atras
        1f, -1f, -1f, // 7
        -1f, -1f, -1f, // 8
        0f, 1f, 0f, // 9

        // Derecha
        1f, -1f, 1f, // 10
        1f, -1f, -1f, // 11
        0f, 1f, 0f, // 12

        // Izquierda
        -1f, -1f, -1f, // 13
        -1f, -1f, 1f, // 14
        0f, 1f, 0f
    )// 15

    internal var maxColor = 255.toByte()

    /* Los colores x c/vÃ©rtice (r,g,b,a) */
    private val colores = byteArrayOf(
        maxColor, 0, 0, 1, // 0
        maxColor, 0, 0, 1, // 1
        maxColor, 0, 0, 1, // 2

        0, 0, maxColor, 1, // 0
        0, 0, maxColor, 1, // 1
        0, 0, maxColor, 1, // 2
        0, 0, maxColor, 1, // 3

        0, maxColor, 0, 1, // 0
        0, maxColor, 0, 1, // 1
        0, maxColor, 0, 1, // 2

        maxColor, maxColor, 0, 1, // 0
        maxColor, maxColor, 0, 1, // 1
        maxColor, maxColor, 0, 1, // 2

        maxColor, 0, maxColor, 1, // 0
        maxColor, 0, maxColor, 1, // 1
        maxColor, 0, maxColor, 1
    )// 2


    /* Indices */
    private val indices = shortArrayOf(
        //  T1          T2
        0, 1, 2, // Frente
        3, 4, 5, 3, 5, 6, // Abajo
        7, 8, 9, // Atras
        10, 11, 12, // Derecha
        13, 14, 15
    )// Izquierda

    private val bufVertices: FloatBuffer
    private val bufColores: ByteBuffer
    private val bufIndices: ShortBuffer

    init {

        /* Lee los vÃ©rtices */
        var bufByte = ByteBuffer.allocateDirect(vertices.size * 4)
        bufByte.order(ByteOrder.nativeOrder()) // Utiliza el orden del byte nativo
        bufVertices = bufByte.asFloatBuffer() // Convierte de byte a float
        bufVertices.put(vertices)
        bufVertices.rewind() // puntero al principio del buffer

        /* Lee los colores */
        bufColores = ByteBuffer.allocateDirect(colores.size)
        bufColores.put(colores)
        bufColores.position(0) // puntero al principio del buffer

        /* Lee los indices */
        bufByte = ByteBuffer.allocateDirect(indices.size * 2)
        bufByte.order(ByteOrder.nativeOrder()) // Utiliza el orden de byte nativo
        bufIndices = bufByte.asShortBuffer() // Convierte de byte a short
        bufIndices.put(indices)
        bufIndices.rewind() // puntero al principio del buffer

    }

    fun dibuja(gl: GL10) {

        /* Se habilita el acceso al arreglo de vÃ©rtices */
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)

        /* Se habilita el acceso al arreglo de colores */
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)

        /* Se especifica los datos del arreglo de vÃ©rtices */
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices)

        /* Se especifica los datos del arreglo de colores */
        gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, bufColores)

        /* Renderiza las primitivas desde los datos de los arreglos (vÃ©rtices,
         * colores e Ã­ndices) */
        gl.glDrawElements(
            GL10.GL_TRIANGLES, indices.size,
            GL10.GL_UNSIGNED_SHORT, bufIndices
        )

        /* Se deshabilita el acceso al arreglo de vÃ©rtices */
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)

        //* Se deshabilita el acceso al arreglo de colores */
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)

    }
}
