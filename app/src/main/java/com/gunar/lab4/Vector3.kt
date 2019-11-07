package com.gunar.lab4

class Vector3 {
    /**
     * Retorna la coordenada x del Vector.
     *
     * @return x
     */
    var x: Float = 0.toFloat()
    /**
     * Retorna la coordenada y del Vector.
     *
     * @return y
     */
    var y: Float = 0.toFloat()
    /**
     * Retorna la coordenada z del Vector.
     *
     * @return z
     */
    var z: Float = 0.toFloat()

    /**
     * Construye un nuevo Vector.
     */
    constructor() {
        z = 0f
        y = z
        x = y
    }

    /**
     * Construye un nuevo Vector.
     */
    constructor(x: Float, y: Float, z: Float) {
        this.x = x
        this.y = y
        this.z = z
    }

    /**
     * u = Suma de vectores
     * u = v1 + v2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * u = (v1.x + v2.x, v1.y + v2.y, v1.z + v2.z)
     */
    fun mas(v2: Vector3): Vector3 {
        return Vector3(this.x + v2.x, this.y + v2.y, this.z + v2.z)
    }

    /**
     * u = Resta de vectores
     * u = v1 - v2 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * u = (v1.x - v2.x, v1.y - v2.y, v1.z - v2.z)
     */
    fun menos(v2: Vector3): Vector3 {
        return Vector3(this.x - v2.x, this.y - v2.y, this.z - v2.z)
    }

    /**
     * u x v = Producto vectorial o producto cruz
     * u = (u.x, u.y, u.z) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * v = (v.x, v.y, v.z) u x v = (u.y * v.z - u.z * v.y,
     * u.z * v.x - u.x * v.z,
     * u.x * v.y - u.y * v.x)
     */
    fun producto_vectorial(v2: Vector3): Vector3 {
        val r = Vector3()
        r.x = this.y * v2.z - this.z * v2.y
        r.y = this.z * v2.x - this.x * v2.z
        r.z = this.x * v2.y - this.y * v2.x
        return r
    }

    /**
     * u . v = Producto escalar o producto punto
     * u = (u.x, u.y, u.z) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * v = (v.x, v.y, v.z) u . v = u.x v.x + u.y v.y + u.z v.z
     *
     */
    fun producto_escalar(v2: Vector3): Float {
        return this.x * v2.x + this.y * v2.y + this.z * v2.z
    }

    /**
     * |v| = Longitud de un vector o magnitud
     * v = (x, y, z) ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * |v| = raiz_cuadrada (x^2 + y^2 + z^2)
     *
     */
    fun longitud(): Float {
        return Math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }

    /**
     * v u = Vector unitario o de longitud 1
     * u = --- ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * |v| u = Vector normalizado
     *
     */
    fun normaliza() {
        val longitud = longitud()
        if (longitud > 0) {
            x = x / longitud
            y = y / longitud
            z = z / longitud
        }
    }

    override fun toString(): String {
        return "Vector3 [x=$x, y=$y, z=$z]"
    }

    companion object {
        /**
         * 3
         * /\
         * / \
         * v / \
         * / \
         * /________\
         * 1 u 2
         */
        fun normal(v1: Vector3, v2: Vector3, v3: Vector3): Vector3 {
            var u = Vector3() // vector u
            var v = Vector3() // vector v
            var n = Vector3() // vector n
            /* Calcula los vectores u y v */
            u = v2.menos(v1)
            v = v3.menos(v1)
            /* n = u x v */
            n = u.producto_vectorial(v)
            /* Normaliza */
            n.normaliza()
            return n
        }
    }
}