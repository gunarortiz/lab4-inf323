package com.gunar.lab4

/**
 * ArcBall
 * Obtiene los vectores U y V de la esfera (x^2 + y^2 + z^2 = 1).
 *
 *
 */
class ArcBall(w: Float, h: Float) {
    internal var U: Vector3
    internal var V: Vector3
    internal var ajustaAncho: Float = 0.toFloat()
    internal var ajustaAlto: Float = 0.toFloat()

    internal inner class Punto2f(var x: Float, var y: Float)

    init {
        U = Vector3()
        V = Vector3()
        ajusta(w, h)
    }

    /* Ajusta el ancho y alto de la ventana */
    fun ajusta(w: Float, h: Float) {
        if (!(w > 1.0f && h > 1.0f))
            println("ERROR")
        /* Ajusta el factor para el ancho y alto (2 = [-1..1]) */
        ajustaAncho = 2.0f / (w - 1.0f)
        ajustaAlto = 2.0f / (h - 1.0f)
    }

    /* Obtiene el vector dado un punto (x,y) */
    fun obtieneVector(vector: Vector3, x: Float, y: Float) {
        /* Copia punto */
        val temp = Punto2f(x, y)
        /* Ajusta las coordenadas del punto al rango [-1..1] */
        temp.x = temp.x * ajustaAncho - 1.0f
        temp.y = 1.0f - temp.y * ajustaAlto
        /* Calcula el cuadrado de la longitud del vector */
        val longitud2 = temp.x * temp.x + temp.y * temp.y
        /*
         * Considerando que: radio^2 = x^2 + y^2 + z^2
         * ¿Cuales son los valores de x, y y z?
         *
         * Si el punto está fuera de la esfera... (longitud2 > 1)
         */
        if (longitud2 > 1.0f) {
            /* Calcula un factor de normalización (radio / sqrt(longitud2)) */
            val norma = (1.0 / Math.sqrt(longitud2.toDouble())).toFloat()
            /* Retorna el vector "normalizado", un punto sobre la esfera */
            vector.x = temp.x * norma
            vector.y = temp.y * norma
            vector.z = 0.0f
        } else { /* e.o.c. está dentro */
            /*
             * Retorna un vector, un punto dentro la esfera
             * z = sqrt(radio^cuadrado - (x^2 + y^2))
             */
            vector.x = temp.x
            vector.y = temp.y
            vector.z = Math.sqrt((1.0f - longitud2).toDouble()).toFloat()
        }
    }

    /* Obtiene el vector U */
    fun primerPunto(x: Float, y: Float) {
        obtieneVector(U, x, y)
    }

    /* Obtiene el Cuaternion de U y V */
    fun segundoPunto(x: Float, y: Float): Cuaternion {
        val q = Cuaternion()
        /* Obtiene el vector V */
        obtieneVector(V, x, y)
        /* Retorna el cuaternión equivalente a la rotación. */
        if (q != null) {
            /* Calcula la Normal = U x V */
            val Normal = U.producto_vectorial(V)
            /* Calcula la longitud de la normal */
            if (Normal.longitud() > Epsilon) { /* si no es cero */
                q.x = Normal.x
                q.y = Normal.y
                q.z = Normal.z
                /* w = (theta / 2), donde theta es el ángulo de rotación */
                q.w = U.producto_escalar(V)
            } else { /* si es cero */
                /* U y V coinciden */
                q.w = 0.0f
                q.z = q.w
                q.y = q.z
                q.x = q.y
            }
        }
        return q
    }

    companion object {
        private val Epsilon = 1.0e-5f
    }
}

