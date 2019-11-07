package com.gunar.lab4

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.ShortBuffer
import java.util.ArrayList
import java.util.Arrays
import java.util.StringTokenizer
import javax.microedition.khronos.opengles.GL10
import android.content.Context
import android.util.Log

/**
 * Clase Objeto (OpenGL)
 *
 * Lee archivo .obj y .mtl
 *
 * @author J Felipez
 * @version 2.0 30/05/2015
 */
class Objeto(contexto: Context, nombreArchivo: String) {

    internal var materiales = ArrayList<Material>()
    internal var grupos = ArrayList<Grupo>()
    internal var aristas = ArrayList<Short>()

    /* N�mero de V�rtices */
    private var numVertices: Int = 0

    /* N�mero de Normales */
    private var numNormales: Int = 0

    /* N�mero de Triangulos */
    private var numTriangulos: Int = 0

    private var bufVertices: FloatBuffer? = null
    private var bufNormales: FloatBuffer? = null
    private var bufIndices: ShortBuffer? = null

    internal inner class Grupo {
        var nombre: String                    /* Nombre del grupo */
        var triangulos: ArrayList<Int>    /* Arreglo de �ndice de triangulos */
        var material: Int = 0                    /* Indice del material del grupo */

        val numTriangulos: Int
            get() = triangulos.size

        constructor() {
            nombre = "si_falta"
            triangulos = ArrayList()
            material = 0
        }

        constructor(nombre: String) {
            this.nombre = nombre
            triangulos = ArrayList()
            material = 0
        }

        fun adiTriangulo(t: Int) {
            triangulos.add(t)
        }

        fun getTriangulo(indice: Int): Int {
            return triangulos[indice]
        }

        override fun toString(): String {
            return nombre +
                    "\n triangulos: " + triangulos +
                    "\n material  : " + material
        }
    }

    internal inner class Material {
        var nombre: String                    /* Nombre del material */
        var ambiente: FloatArray                /* Arreglo del color ambiente */
        var difuso: FloatArray                    /* Arreglo del color difuso */
        var especular: FloatArray                /* Arreglo del color especular */
        var brillo: Float = 0.toFloat()                    /* Exponente del brillo */

        constructor() {
            ambiente = FloatArray(4)
            difuso = FloatArray(4)
            especular = FloatArray(4)
            nombre = "si_falta"
            ambiente[0] = 0.2f
            ambiente[1] = 0.2f
            ambiente[2] = 0.2f
            ambiente[3] = 1.0f
            difuso[0] = 0.8f
            difuso[1] = 0.8f
            difuso[2] = 0.8f
            difuso[3] = 1.0f
            especular[0] = 0.0f
            especular[1] = 0.0f
            especular[2] = 0.0f
            especular[3] = 1.0f
        }

        constructor(
            nombre: String, ambiente: FloatArray, difuso: FloatArray, especular: FloatArray,
            brillo: Float
        ) {
            this.nombre = nombre
            this.ambiente = ambiente
            this.difuso = difuso
            this.especular = especular
            this.brillo = brillo
        }

        override fun toString(): String {
            return nombre +
                    "\n Ka: " + Arrays.toString(ambiente) +
                    "\n Kd: " + Arrays.toString(difuso) +
                    "\n Ks: " + Arrays.toString(especular) +
                    "\n Ns: " + brillo
        }
    }

    init {

        lee_archivo_obj(contexto, nombreArchivo)

    }

    fun dibuja(gl: GL10) {

        val iterador = grupos.iterator()

        while (iterador.hasNext()) {

            /* Lee un grupo  */
            val grupo = iterador.next()

            /* Obtiene el n�mero de tri�ngulos del grupo */
            val numTriangulos = grupo.numTriangulos

            if (numTriangulos == 0)
                continue

            /* Obtiene el color del material */
            val material = materiales[grupo.material]

            /* Definici�n del material */
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, material.ambiente, 0)
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, material.difuso, 0)
            gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_SPECULAR, material.especular, 0)
            gl.glMaterialf(GL10.GL_FRONT_AND_BACK, GL10.GL_SHININESS, material.brillo)

            /* Lee las caras */
            val bufByte = ByteBuffer.allocateDirect(numTriangulos * 2 * 3 * 2)
            bufByte.order(ByteOrder.nativeOrder()) // Utiliza el orden de byte nativo
            bufIndices = bufByte.asShortBuffer() // Convierte de byte a short

            /* Lee los indices */
            for (j in 0 until numTriangulos) {
                val indice = grupo.getTriangulo(j)
                bufIndices!!.put(aristas[indice * 3 + 0])
                bufIndices!!.put(aristas[indice * 3 + 1])
                bufIndices!!.put(aristas[indice * 3 + 2])
            }

            bufIndices!!.rewind() // puntero al principio del buffer

            /* Se habilita el acceso al arreglo de v�rtices */
            gl.glEnableClientState(GL10.GL_VERTEX_ARRAY)

            /* Se habilita el acceso al arreglo de las normales */
            gl.glEnableClientState(GL10.GL_NORMAL_ARRAY)

            /* Se especifica los datos del arreglo de v�rtices */
            gl.glVertexPointer(3, GL10.GL_FLOAT, 0, bufVertices)

            /* Se especifica los datos del arreglo de las normales */
            gl.glNormalPointer(GL10.GL_FLOAT, 0, bufNormales)

            /* Renderiza las primitivas desde los datos de los arreglos (vertices,
			 * normales e indices) */
            gl.glDrawElements(GL10.GL_TRIANGLES, numTriangulos * 3, GL10.GL_UNSIGNED_SHORT, bufIndices)

            /* Se deshabilita el acceso al arreglo de v�rtices */
            gl.glDisableClientState(GL10.GL_VERTEX_ARRAY)

            /* Se deshabilita el acceso al arreglo de las normales */
            gl.glDisableClientState(GL10.GL_NORMAL_ARRAY)
        }
    }

    /**
     * Lee archivo .obj
     */
    fun lee_archivo_obj(contexto: Context, nombreArchivo: String) {
        var i: Int

        try {

            /* Obtiene la textura del directorio de assets Android */
            val `is` = contexto.assets.open(nombreArchivo)
            var buffer: BufferedReader = BufferedReader(InputStreamReader(`is`))

            /* LEE EL ARCHIVO */

            var linea: String? = ""

            var st: StringTokenizer
            var x: Float
            var y: Float
            var z: Float

            var minX = java.lang.Float.MAX_VALUE
            var maxX = java.lang.Float.MIN_VALUE
            var minY = java.lang.Float.MAX_VALUE
            var maxY = java.lang.Float.MIN_VALUE
            var minZ = java.lang.Float.MAX_VALUE
            var maxZ = java.lang.Float.MIN_VALUE

            val vertices = ArrayList<Float>()
            val normales = ArrayList<Vector3>()
            grupos.add(Grupo())
            var indiceDeGrupo = 0

            while ({ linea = buffer.readLine(); linea }() != null) {
                linea = linea!!.trim { it <= ' ' }
                if (linea!!.length > 0) {
                    if (linea!!.startsWith("mtllib ")) {            /* nombre del arch. de materiales */
                        st = StringTokenizer(linea!!.substring(7), " ")
                        lee_archivo_mtl(contexto, st.nextToken())
                    } else if (linea!!.startsWith("v ")) {        /* v�rtice */
                        numVertices++
                        st = StringTokenizer(linea!!.substring(2), " ")
                        x = java.lang.Float.parseFloat(st.nextToken())
                        y = java.lang.Float.parseFloat(st.nextToken())
                        z = java.lang.Float.parseFloat(st.nextToken())
                        vertices.add(x)
                        vertices.add(y)
                        vertices.add(z)

                        minX = Math.min(minX, x)
                        maxX = Math.max(maxX, x)
                        minY = Math.min(minY, y)
                        maxY = Math.max(maxY, y)
                        minZ = Math.min(minZ, z)
                        maxZ = Math.max(maxZ, z)

                        /* Inicializa la normal de cada v�rtice */
                        numNormales++
                        normales.add(Vector3())

                    } else if (linea!!.startsWith("f ")) {        /* cara */
                        st = StringTokenizer(linea!!.substring(2), " ")
                        val numTokens = st.countTokens()    /* Numero de tokens v/vt/vn */

                        st = StringTokenizer(linea!!.substring(2))
                        val token1 = st.nextToken().split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        aristas.add((java.lang.Short.parseShort(token1[0]) - 1).toShort())        // v0

                        val token2 = st.nextToken().split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        aristas.add((java.lang.Short.parseShort(token2[0]) - 1).toShort())        // v1

                        val token3 = st.nextToken().split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                        aristas.add((java.lang.Short.parseShort(token3[0]) - 1).toShort())        // v2

                        grupos[indiceDeGrupo].adiTriangulo(numTriangulos)
                        numTriangulos++
                        i = 3
                        while (i < numTokens) {
                            val k = aristas.size
                            aristas.add(aristas[k - 3])                        // v0
                            aristas.add(aristas[k - 1])                        // v2
                            val token4 =
                                st.nextToken().split("/".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                            aristas.add((java.lang.Short.parseShort(token4[0]) - 1).toShort())    // v3
                            grupos[indiceDeGrupo].adiTriangulo(numTriangulos)
                            numTriangulos++
                            i++
                        }
                    } else if (linea!!.startsWith("g")) {            /* nombre de grupo */
                        st = StringTokenizer(linea, " ")
                        val numTokens = st.countTokens()
                        st.nextToken()
                        if (numTokens > 1) {
                            val nombre = st.nextToken()
                            indiceDeGrupo = buscaGrupo(nombre)    // �ndice de grupo actual
                            if (indiceDeGrupo == -1) {
                                grupos.add(Grupo(nombre))
                                indiceDeGrupo = grupos.size - 1    // �ndice de grupo actual
                            }
                        }

                    } else if (linea!!.startsWith("usemtl ")) {        /* nombre del material */
                        st = StringTokenizer(linea!!.substring(7), " ")
                        val nombre = st.nextToken()
                        val indiceDeMaterial = buscaMaterial(nombre)
                        grupos[indiceDeGrupo].material = indiceDeMaterial
                    } else if (linea!![0] == '#')
                    /* comentario */
                        continue
                }
            }

            /* Reescala las coordenadas entre [-1,1] */
            var tam_max = 0f
            val escala: Float
            tam_max = Math.max(tam_max, maxX - minX)
            tam_max = Math.max(tam_max, maxY - minY)
            tam_max = Math.max(tam_max, maxZ - minZ)
            escala = 2.0f / tam_max

            /* Actualiza los v�rtices */
            i = 0
            while (i < numVertices * 3) {
                vertices[i] = escala * (vertices[i] - minX) - 1.0f
                vertices[i + 1] = escala * (vertices[i + 1] - minY) - 1.0f
                vertices[i + 2] = escala * (vertices[i + 2] - minZ) - 1.0f
                i += 3
            }

            /* Lee los v�rtices */
            var bufByte = ByteBuffer.allocateDirect(numVertices * 4 * 3)
            bufByte.order(ByteOrder.nativeOrder()) // Utiliza el orden de byte nativo
            bufVertices = bufByte.asFloatBuffer() // Convierte de byte a float

            i = 0
            while (i < numVertices * 3) {
                bufVertices!!.put(vertices[i])
                bufVertices!!.put(vertices[i + 1])
                bufVertices!!.put(vertices[i + 2])
                i += 3
            }

            val v1 = Vector3() // v1
            val v2 = Vector3() // v2
            val v3 = Vector3() // v3
            var normal = Vector3()  // normal
            var a: Int
            var b: Int
            var c: Int
            var a1: Int
            var b1: Int
            var c1: Int

            /* Lee las caras y obtiene las normales de los v�rtices */
            i = 0
            while (i < numTriangulos * 3) {
                a = aristas[i].toInt()
                b = aristas[i + 1].toInt()
                c = aristas[i + 2].toInt()

                a1 = a * 3 // Obtiene la posici�n del primer v�rtice
                v1.x = bufVertices!!.get(a1 + 0) // v1
                v1.y = bufVertices!!.get(a1 + 1)
                v1.z = bufVertices!!.get(a1 + 2)
                b1 = b * 3 // Obtiene la posici�n del segundo v�rtice
                v2.x = bufVertices!!.get(b1 + 0) // v2
                v2.y = bufVertices!!.get(b1 + 1)
                v2.z = bufVertices!!.get(b1 + 2)
                c1 = c * 3 // Obtiene la posici�n del tercer v�rtice
                v3.x = bufVertices!!.get(c1 + 0) // v3
                v3.y = bufVertices!!.get(c1 + 1)
                v3.z = bufVertices!!.get(c1 + 2)

                /* Obtiene la normal de la cara */
                normal = Vector3.normal(v1, v2, v3)

                /* Suma la normal de la cara, a la normal de cada v�rtice */
                normales[a] = normal.mas(normales[a]) // normal 1
                normales[b] = normal.mas(normales[b]) // normal 2
                normales[c] = normal.mas(normales[c]) // normal 3
                i += 3
            }

            /* Lee las normales de los v�rtices */
            bufByte = ByteBuffer.allocateDirect(numNormales * 4 * 3)
            bufByte.order(ByteOrder.nativeOrder()) // Utiliza el orden de byte nativo
            bufNormales = bufByte.asFloatBuffer() // Convierte de byte a float
            i = 0
            while (i < numNormales) {

                /* Normaliza la normal de cada v�rtice */
                normales[i].normaliza()

                bufNormales!!.put(normales[i].x)
                bufNormales!!.put(normales[i].y)
                bufNormales!!.put(normales[i].z)
                i++
            }

            bufVertices!!.rewind() // puntero al principio del buffer
            bufNormales!!.rewind() // puntero al principio del buffer

            /* Cierra el archivo */
            buffer!!.close()
//            buffer = null

        } catch (e: IOException) {
            Log.d("Rectangulo", "No puede cargar $nombreArchivo")
            throw RuntimeException("No puede cargar $nombreArchivo")
        }

    }

    /* Lee un archivo .MTL (archivo de los colores de los materiales) */
    @Throws(IOException::class)
    fun lee_archivo_mtl(contexto: Context, nombreArchivo: String) {

        try {
            /* Obtiene la textura del directorio de assets Android */
            val iss = contexto.assets.open(nombreArchivo)
            var buffer: BufferedReader = BufferedReader(InputStreamReader(iss))

            /* LEE EL ARCHIVO */

            var linea: String? = ""
            var st: StringTokenizer
            materiales.clear()
            while ({ linea = buffer.readLine(); linea }() != null) {
                linea = linea!!.trim { it <= ' ' }
                if (linea!!.length > 0) {
                    if (linea!!.startsWith("newmtl ")) {                /* nombre del material */
                        st = StringTokenizer(linea!!.substring(7), " ")
                        materiales.add(Material())
                        materiales[materiales.size - 1].nombre = st.nextToken()
                    } else if (linea!!.startsWith("Ka ")) {            /* ambiente */
                        st = StringTokenizer(linea!!.substring(3), " ")
                        val ambiente = FloatArray(4)
                        ambiente[0] = java.lang.Float.parseFloat(st.nextToken())
                        ambiente[1] = java.lang.Float.parseFloat(st.nextToken())
                        ambiente[2] = java.lang.Float.parseFloat(st.nextToken())
                        ambiente[3] = 1.0f
                        materiales[materiales.size - 1].ambiente = ambiente
                    } else if (linea!!.startsWith("Kd ")) {            /* difuso */
                        st = StringTokenizer(linea!!.substring(3), " ")
                        val difuso = FloatArray(4)
                        difuso[0] = java.lang.Float.parseFloat(st.nextToken())
                        difuso[1] = java.lang.Float.parseFloat(st.nextToken())
                        difuso[2] = java.lang.Float.parseFloat(st.nextToken())
                        difuso[3] = 1.0f
                        materiales[materiales.size - 1].difuso = difuso
                    } else if (linea!!.startsWith("Ks ")) {            /* especular */
                        st = StringTokenizer(linea!!.substring(3), " ")
                        val especular = FloatArray(4)
                        especular[0] = java.lang.Float.parseFloat(st.nextToken())
                        especular[1] = java.lang.Float.parseFloat(st.nextToken())
                        especular[2] = java.lang.Float.parseFloat(st.nextToken())
                        especular[3] = 1.0f
                        materiales[materiales.size - 1].especular = especular
                    } else if (linea!!.startsWith("Ns ")) {            /* brillo */
                        st = StringTokenizer(linea!!.substring(3), " ")
                        var brillo = java.lang.Float.parseFloat(st.nextToken())
                        /* el brillo ser� de [0..1000] */
                        brillo = brillo / 1000 * 128
                        materiales[materiales.size - 1].brillo = brillo
                    }
                }
            }

            /* Cierra el archivo */
            buffer!!.close()

//            buffer = null


        } catch (e: IOException) {
            Log.d("Rectangulo", "No puede cargar $nombreArchivo")
            throw RuntimeException("No puede cargar $nombreArchivo")
        }

    }

    /* Busca el grupo */
    fun buscaGrupo(nombre: String): Int {
        for (i in grupos.indices)
            if (nombre == grupos[i].nombre)
                return i
        return -1
    }

    /* Busca el material */
    fun buscaMaterial(nombre: String): Int {
        for (i in materiales.indices)
            if (nombre == materiales[i].nombre)
                return i
        return 0
    }

}
