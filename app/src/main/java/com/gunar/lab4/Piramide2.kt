package com.gunar.lab4

import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import javax.microedition.khronos.opengles.GL10

class Piramide2 {

    private val vertices = floatArrayOf(

        // Frente
        -1f, 1f, 1f, // 4   0
        1f, 1f, 1f, // 5   1
        0f, -1f, 0f, // 6   2
        0f, -1f, 0f, // 7   3

        // Atr�s
        0f, -1f, 0f, // 3   4
        0f, -1f, 0f, // 2   5
        1f, 1f, -1f, // 1   6
        -1f, 1f, -1f, // 0   7
        // Izquierda
        -1f, 1f, -1f, // 0   8
        -1f, 1f, 1f, // 4   9
        0f, -1f, 0f, // 7  10
        0f, -1f, 0f, // 3  11
        // Derecha
        1f, 1f, 1f, // 5  12
        1f, 1f, -1f, // 1  13
        0f, -1f, 0f, // 2  14
        0f, -1f, 0f, // 6  15
        // Abajo
        -1f, 1f, -1f, // 0  16
        1f, 1f, -1f, // 1  17
        1f, 1f, 1f, // 5  18
        -1f, 1f, 1f
    )// 4  19
    /*// Arriba
			-1,  1,  1, // 7  20
			1,  1,  1, // 6  21
			0,  1, 0, // 2  22
			0,  1, 0  // 3  23*/
    internal var maxColor = 255.toByte()
    private val colores = byteArrayOf(
        // Frente-lila
        0, maxColor, maxColor, maxColor, // 0	8
        0, maxColor, maxColor, maxColor, // 4	9
        0, maxColor, maxColor, maxColor, // 7	10
        0, maxColor, maxColor, maxColor, // 3	11
        // Atr�s-amarillo
        maxColor, 0, 0, maxColor, // 5  12
        maxColor, 0, 0, maxColor, // 1  13
        maxColor, 0, 0, maxColor, // 2  14
        maxColor, 0, 0, maxColor, // 6  15
        // Izquierda-celeste


        maxColor, 0, maxColor, maxColor, // 4   0
        maxColor, 0, maxColor, maxColor, // 5   1
        maxColor, 0, maxColor, maxColor, // 6   2
        maxColor, 0, maxColor, maxColor, // 7   3

        //Derecha-rojo
        maxColor, maxColor, 0, maxColor, // 3   4
        maxColor, maxColor, 0, maxColor, // 2   5
        maxColor, maxColor, 0, maxColor, // 1   6
        maxColor, maxColor, 0, maxColor, // 0   7*/


        // Abajo-azul
        0, 0, maxColor, maxColor, // 0  16
        0, 0, maxColor, maxColor, // 1  17
        0, 0, maxColor, maxColor, // 5  18
        0, 0, maxColor, maxColor
    )// 4  19
    // Arriba-verde
    /*0, maxColor, 0, maxColor, // 7  20
			0, maxColor, 0, maxColor, // 6  21
			0, maxColor, 0, maxColor, // 2  22
			0, maxColor, 0, maxColor// 3  23*/
    private val indices = shortArrayOf(
        0, 1, 2, 0, 2, 3, // Frente
        4, 5, 6, 4, 6, 7, // Atr�s
        8, 9, 10, 8, 10, 11, // Izquierda
        12, 13, 14, 12, 14, 15, // Derecha
        16, 17, 18, 16, 18, 19
    )// Abajo
    //20, 21, 22, 20, 22, 23  // Arriba
    private val bufVertices: FloatBuffer
    private val bufColores: ByteBuffer
    private val bufIndices: ShortBuffer

    init {
        /* Lee losv�rtices*/
        var bufByte = ByteBuffer.allocateDirect(vertices.size * 4)
        bufByte.order(ByteOrder.nativeOrder()) // Utilizael ordendebyte nativo
        bufVertices = bufByte.asFloatBuffer() // Conviertedebyte a float
        bufVertices.put(vertices)
        bufVertices.rewind() // punteroalprincipiodelbuffer
        /* Lee loscolores*/
        bufColores = ByteBuffer.allocateDirect(colores.size)
        bufColores.put(colores)
        bufColores.position(0) // punteroalprincipiodelbuffer
        /* Lee losindices */
        bufByte = ByteBuffer.allocateDirect(indices.size * 2)
        bufByte.order(ByteOrder.nativeOrder()) // Utilizael ordendebyte nativo
        bufIndices = bufByte.asShortBuffer() // Conviertedebyte a short
        bufIndices.put(indices)
        bufIndices.rewind() // punteroalprincipiodelbuffer
    }

    fun dibuja(gl: GL10) {
        /* Seactivael arreglodev�rtices*/
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)
        /* Seactivael arreglodecolores*/
        gl.glEnableClientState(GL10.GL_COLOR_ARRAY)
        /* Seespecificalosdatosdelarreglodev�rtices*/
        gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices)
        /* Seespecificalosdatosdelarreglodecolores*/
        gl.glColorPointer(4, GL10.GL_UNSIGNED_BYTE, 0, bufColores)
        /* Sedibujael cubo*/
        gl.glDrawElements(GL10.GL_TRIANGLES, indices.size, GL10.GL_UNSIGNED_SHORT, bufIndices)
        //gl.glDrawElements(GL10.GL_LINE_STRIP, indices.length,GL10.GL_UNSIGNED_SHORT, bufIndices);//para solo lineas
        /* Sedesactivael arreglodev�rtices*/
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)
        /* Sedesactivael arreglodecolores*/
        gl.glDisableClientState(GL10.GL_COLOR_ARRAY)
    }

}
