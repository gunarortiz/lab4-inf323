package com.gunar.lab4

object Matriz4 {
    /* Matriz Identidad */
    fun identidad(r: FloatArray) {
        r[0] = 1f
        r[4] = 0f
        r[8] = 0f
        r[12] = 0f
        r[1] = 0f
        r[5] = 1f
        r[9] = 0f
        r[13] = 0f
        r[2] = 0f
        r[6] = 0f
        r[10] = 1f
        r[14] = 0f
        r[3] = 0f
        r[7] = 0f
        r[11] = 0f
        r[15] = 1f
    }

    /* Traslación - glTranslatef */
    fun traslacion(matriz: FloatArray, tx: Float, ty: Float, tz: Float) {
        val r = FloatArray(16)
        r[0] = 1f
        r[4] = 0f
        r[8] = 0f
        r[12] = tx
        r[1] = 0f
        r[5] = 1f
        r[9] = 0f
        r[13] = ty
        r[2] = 0f
        r[6] = 0f
        r[10] = 1f
        r[14] = tz
        r[3] = 0f
        r[7] = 0f
        r[11] = 0f
        r[15] = 1f
        multiplica(matriz, matriz, r)
    }

    /* Escalación - glScalef */
    fun escalacion(matriz: FloatArray, sx: Float, sy: Float, sz: Float) {
        val r = FloatArray(16)
        r[0] = sx
        r[4] = 0f
        r[8] = 0f
        r[12] = 0f
        r[1] = 0f
        r[5] = sy
        r[9] = 0f
        r[13] = 0f
        r[2] = 0f
        r[6] = 0f
        r[10] = sz
        r[14] = 0f
        r[3] = 0f
        r[7] = 0f
        r[11] = 0f
        r[15] = 1f
        multiplica(matriz, matriz, r)
    }

    /* Rotación sobre X - glRotatef */
    fun rotacionX(matriz: FloatArray, theta: Float) {
        val r = FloatArray(16)
        val angulo = theta * Math.PI / 180
        val c = Math.cos(angulo).toFloat()
        val s = Math.sin(angulo).toFloat()
        r[0] = 1f
        r[4] = 0f
        r[8] = 0f
        r[12] = 0f
        r[1] = 0f
        r[5] = c
        r[9] = -s
        r[13] = 0f
        r[2] = 0f
        r[6] = s
        r[10] = c
        r[14] = 0f
        r[3] = 0f
        r[7] = 0f
        r[11] = 0f
        r[15] = 1f
        multiplica(matriz, matriz, r)
    }

    /* Rotación sobre Y - glRotatef */
    fun rotacionY(matriz: FloatArray, theta: Float) {
        val r = FloatArray(16)
        val angulo = theta * Math.PI / 180
        val c = Math.cos(angulo).toFloat()
        val s = Math.sin(angulo).toFloat()
        r[0] = c
        r[4] = 0f
        r[8] = s
        r[12] = 0f
        r[1] = 0f
        r[5] = 1f
        r[9] = 0f
        r[13] = 0f
        r[2] = -s
        r[6] = 0f
        r[10] = c
        r[14] = 0f
        r[3] = 0f
        r[7] = 0f
        r[11] = 0f
        r[15] = 1f
        multiplica(matriz, matriz, r)
    }

    /* Rotación sobre Z - glRotatef */
    fun rotacionZ(matriz: FloatArray, theta: Float) {
        val r = FloatArray(16)
        val angulo = theta * Math.PI / 180
        val c = Math.cos(angulo).toFloat()
        val s = Math.sin(angulo).toFloat()
        r[0] = c
        r[4] = -s
        r[8] = 0f
        r[12] = 0f
        r[1] = s
        r[5] = c
        r[9] = 0f
        r[13] = 0f
        r[2] = 0f
        r[6] = 0f
        r[10] = 1f
        r[14] = 0f
        r[3] = 0f
        r[7] = 0f
        r[11] = 0f
        r[15] = 1f
        multiplica(matriz, matriz, r)
    }

    /* Proyección Paralela - glOrtho */
    fun ortho(r: FloatArray, izq: Float, der: Float, abj: Float, arr: Float, cerca: Float, lejos: Float) {
        r[0] = 2 / (der - izq)
        r[4] = 0f
        r[8] = 0f
        r[12] = -(der + izq) / (der - izq)
        r[1] = 0f
        r[5] = 2 / (arr - abj)
        r[9] = 0f
        r[13] = -(arr + abj) / (arr - abj)
        r[2] = 0f
        r[6] = 0f
        r[10] = -2 / (lejos - cerca)
        r[14] = -(lejos + cerca) / (lejos - cerca)
        r[3] = 0f
        r[7] = 0f
        r[11] = 0f
        r[15] = 1f
    }

    /* Proyección Perspectiva - glFrustum */
    fun frustum(r: FloatArray, izq: Float, der: Float, abj: Float, arr: Float, cerca: Float, lejos: Float) {
        r[0] = 2 * cerca / (der - izq)
        r[4] = 0f
        r[8] = (der + izq) / (der - izq)
        r[12] = 0f
        r[1] = 0f
        r[5] = 2 * cerca / (arr - abj)
        r[9] = (arr + abj) / (arr - abj)
        r[13] = 0f
        r[2] = 0f
        r[6] = 0f
        r[10] = -(lejos + cerca) / (lejos - cerca)
        r[14] = -2f * lejos * cerca / (lejos - cerca)
        r[3] = 0f
        r[7] = 0f
        r[11] = -1f
        r[15] = 0f
    }

    /* Proyección Perspectiva - gluPerspective */
    fun perspective(r: FloatArray, fovy: Float, aspecto: Float, cerca: Float, lejos: Float) {
        val ang = fovy.toDouble() * 0.5 * (Math.PI / 180)
        val f = if (Math.abs(Math.sin(ang)) < 1e-8) 0f else 1 / Math.tan(ang).toFloat()
        r[0] = f // aspecto
        r[4] = 0f
        r[8] = 0f
        r[12] = 0f
        r[1] = 0f
        r[5] = f
        r[9] = 0f
        r[13] = 0f
        r[2] = 0f
        r[6] = 0f
        r[10] = -(lejos + cerca) / (lejos - cerca)
        r[14] = -2.0f * lejos * cerca / (lejos - cerca)
        r[3] = 0f
        r[7] = 0f
        r[11] = -1.0f
        r[15] = 0f
    }

    /* Camara - gluLookAt */
    fun lookAt(
        r: FloatArray, vistaX: Float, vistaY: Float, vistaZ: Float, centroX: Float,
        centroY: Float, centroZ: Float, arribaX: Float, arribaY: Float, arribaZ: Float
    ) {
        val vista = Vector3(vistaX, vistaY, vistaZ)
        val centro = Vector3(centroX, centroY, centroZ)
        val arriba = Vector3(arribaX, arribaY, arribaZ)
        /* n = vista - centro */
        val n = vista.menos(centro)
        /* u = u / || u || */
        n.normaliza()
        /* v = arriba */
        var v = arriba
        /* v = v / || v || */
        //v.normaliza(); // No es necesario!
        /* u = v x n */
        val u = v.producto_vectorial(n)
        /* u = u / || u || */
        u.normaliza()
        /* Recalcula v: v = n x u */
        v = n.producto_vectorial(u)
        r[0] = u.x
        r[4] = u.y
        r[8] = u.z
        r[12] = -(vistaX * u.x + vistaY * u.y + vistaZ * u.z)
        r[1] = v.x
        r[5] = v.y
        r[9] = v.z
        r[13] = -(vistaX * v.x + vistaY * v.y + vistaZ * v.z)
        r[2] = n.x
        r[6] = n.y
        r[10] = n.z
        r[14] = -(vistaX * n.x + vistaY * n.y + vistaZ * n.z)
        r[3] = 0f
        r[7] = 0f
        r[11] = 0f
        r[15] = 1f
    }

    /* Multiplicación de matrices de 4 x 4 */
    fun multiplica(c: FloatArray, a: FloatArray, b: FloatArray) {
        val r = FloatArray(16)
        for (i in 0..3) {
            for (j in 0..3) {
                var s = 0f
                for (k in 0..3)
                    s = s + a[i + k * 4] * b[k + j * 4]
                r[i + j * 4] = s
            }
        }
        for (i in 0..15)
            c[i] = r[i]
    }

    /* Multiplicación de matriz 4 x 4 por vector 3 */
    fun multiplicaMV(c: FloatArray, a: FloatArray, b: FloatArray) {
        val r = FloatArray(3)
        /*
| a[0] a[4] a[ 8] a[12] | | b[0] |
| a[1] a[5] a[ 9] a[13] | * | b[1] |
| a[2] a[6] a[10] a[14] | | b[2] |
| a[3] a[7] a[11] a[15] |
*/
        r[0] = a[0] * b[0] + a[4] * b[1] + a[8] * b[2] + a[12]
        r[1] = a[1] * b[0] + a[5] * b[1] + a[9] * b[2] + a[13]
        r[2] = a[2] * b[0] + a[6] * b[1] + a[10] * b[2] + a[14]
        for (i in 0..2)
            c[i] = r[i]
    }

    /* Transpuesta de una matriz 4 x 4 */
    fun transpuesta(r: FloatArray, m: FloatArray) {
        var i = 0
        for (j in 0..3)
            for (k in 0..3) {
                r[j + k * 4] = m[i]
                i++
            }
    }

    /* Invierte una matriz de 4 x 4, b = inv(a) */
    /* Aquí está una versión eficiente, utilizando el
     * Teorema de Expansión de Laplace (página 9) */
    fun invierte(b: FloatArray, a: FloatArray) {
        /*
| a[0] a[4] a[ 8] a[12] |
| a[1] a[5] a[ 9] a[13] |
| a[2] a[6] a[10] a[14] |
| a[3] a[7] a[11] a[15] |
*/
        val s0 = a[0] * a[5] - a[1] * a[4]
        val s1 = a[0] * a[9] - a[1] * a[8]
        val s2 = a[0] * a[13] - a[1] * a[12]
        val s3 = a[4] * a[9] - a[5] * a[8]
        val s4 = a[4] * a[13] - a[5] * a[12]
        val s5 = a[8] * a[13] - a[9] * a[12]
        val c5 = a[10] * a[15] - a[11] * a[14]
        val c4 = a[6] * a[15] - a[7] * a[14]
        val c3 = a[6] * a[11] - a[7] * a[10]
        val c2 = a[2] * a[15] - a[3] * a[14]
        val c1 = a[2] * a[11] - a[3] * a[10]
        val c0 = a[2] * a[7] - a[3] * a[6]
        // Se deberia verificar por el determinante igual a 0
        val invdet = 1.0f / (s0 * c5 - s1 * c4 + s2 * c3 + s3 * c2 - s4 * c1 + s5 * c0)
        b[0] = (a[5] * c5 - a[9] * c4 + a[13] * c3) * invdet
        b[4] = (-a[4] * c5 + a[8] * c4 - a[12] * c3) * invdet
        b[8] = (a[7] * s5 - a[11] * s4 + a[15] * s3) * invdet
        b[12] = (-a[6] * s5 + a[10] * s4 - a[14] * s3) * invdet
        b[1] = (-a[1] * c5 + a[9] * c2 - a[13] * c1) * invdet
        b[5] = (a[0] * c5 - a[8] * c2 + a[12] * c1) * invdet
        b[9] = (-a[3] * s5 + a[11] * s2 - a[15] * s1) * invdet
        b[13] = (a[2] * s5 - a[10] * s2 + a[14] * s1) * invdet
        b[2] = (a[1] * c4 - a[5] * c2 + a[13] * c0) * invdet
        b[6] = (-a[0] * c4 + a[4] * c2 - a[12] * c0) * invdet
        b[10] = (a[3] * s4 - a[7] * s2 + a[15] * s0) * invdet
        b[14] = (-a[2] * s4 + a[6] * s2 - a[14] * s0) * invdet
        b[3] = (-a[1] * c3 + a[5] * c1 - a[9] * c0) * invdet
        b[7] = (a[0] * c3 - a[4] * c1 + a[8] * c0) * invdet
        b[11] = (-a[3] * s3 + a[7] * s1 - a[11] * s0) * invdet
        b[15] = (a[2] * s3 - a[6] * s1 + a[10] * s0) * invdet
    }

    /* Copia b = a */
    fun copia(b: FloatArray, a: FloatArray) {
        for (i in 0..15)
            b[i] = a[i]
    }
}