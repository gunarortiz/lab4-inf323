package com.gunar.lab4

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer

import javax.microedition.khronos.opengles.GL10

class Cubo {

    /**
     *
     * 3 --------- 2
     *
     * /|	       /|
     * / |	/ |
     *
     * 7 --------- 6 |
     *
     * |  |	|  |
     *
     * | 0 ------|-- 1
     *
     * | /	| /
     * |/	|/
     * 4 --------- 5
     */

    private val vertices = floatArrayOf(

        // Frente
        -1f, -1f, 1f, // 4	0
        1f, -1f, 1f, // 5	1
        1f, 1f, 1f, // 6	2
        -1f, 1f, 1f, // 7	3
        // Atr�s
        -1f, 1f, -1f, // 3	4
        1f, 1f, -1f, // 2	5
        1f, -1f, -1f, // 1	6
        -1f, -1f, -1f, // 0	7
        // Izquierda
        -1f, -1f, -1f, // 0	8
        -1f, -1f, 1f, // 4	9
        -1f, 1f, 1f, // 7	10
        -1f, 1f, -1f, // 3	11
        // Derecha
        1f, -1f, 1f, // 5	12
        1f, -1f, -1f, // 1	13
        1f, 1f, -1f, // 2	14
        1f, 1f, 1f, // 6	15
        //	Abajo
        -1f, -1f, -1f, // 0	16
        1f, -1f, -1f, // 1	17
        1f, -1f, 1f, // 5	18
        -1f, -1f, 1f, // 4	19
        //	Arriba
        -1f, 1f, 1f, // 7	20
        1f, 1f, 1f, // 6	21
        1f, 1f, -1f, // 2	22
        -1f, 1f, -1f    // 3	23
    )

    internal var maxColor = 255.toByte()

    private val colores = byteArrayOf(// Frente - lila
        maxColor, 0, maxColor, maxColor, // 4	0
        maxColor, 0, maxColor, maxColor, // 5	1
        maxColor, 0, maxColor, maxColor, // 6	2
        maxColor, 0, maxColor, maxColor, // 7	3
        // Atr�s - amarillo
        maxColor, maxColor, 0, maxColor, // 3	4
        maxColor, maxColor, 0, maxColor, // 2	5
        maxColor, maxColor, 0, maxColor, // 1	6
        maxColor, maxColor, 0, maxColor, // 0	7
        // Izquierda	- celeste
        0, maxColor, maxColor, maxColor, // 0	8
        0, maxColor, maxColor, maxColor, // 4	9
        0, maxColor, maxColor, maxColor, // 7	10
        0, maxColor, maxColor, maxColor, // 3	11
        // Derecha -	rojo
        maxColor, 0, 0, maxColor, // 5	12
        maxColor, 0, 0, maxColor, // 1	13
        maxColor, 0, 0, maxColor, // 2	14
        maxColor, 0, 0, maxColor, // 6	15
        // Abajo - azul
        0, 0, maxColor, maxColor, // 0	16
        0, 0, maxColor, maxColor, // 1	17
        0, 0, maxColor, maxColor, // 5	18
        0, 0, maxColor, maxColor, // 4	19
        // Arriba	- verde
        0, maxColor, 0, maxColor, // 7	20
        0, maxColor, 0, maxColor, // 6	21
        0, maxColor, 0, maxColor, // 2	22
        0, maxColor, 0, maxColor    // 3	23
    )
    private val indices = shortArrayOf(
        0, 1, 2, 0, 2, 3, // Frente
        4, 5, 6, 4, 6, 7, // Atr�s
        8, 9, 10, 8, 10, 11, // Izquierda
        12, 13, 14, 12, 14, 15, // Derecha
        16, 17, 18, 16, 18, 19, // Abajo
        20, 21, 22, 20, 22, 23    // Arriba
    )

    private val bufVertices: FloatBuffer
    private val bufColores: ByteBuffer
    private val bufIndices: ShortBuffer

    init {

        /* Lee los v�rtices */

        var bufByte = ByteBuffer.allocateDirect(vertices.size * 4)
        bufByte.order(ByteOrder.nativeOrder()) // Utiliza el orden de byte nativo
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

        /* Se activa el arreglo de v�rtices */
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)

        /* Se activa el arreglo de colores */
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)

        /* Se especifica los datos del arreglo de v�rtices */
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices)

        /* Se especifica los datos del arreglo de colores */
        gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, bufColores)

        /* Se dibuja el cubo */
        gl.glDrawElements(
            GL10.GL_TRIANGLES, indices.size,

            GL10.GL_UNSIGNED_SHORT, bufIndices
        )

        /* Se desactiva el arreglo de v�rtices */
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)

        /* Se desactiva el arreglo de colores */
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)

    }

}



