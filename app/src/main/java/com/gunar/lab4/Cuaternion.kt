package com.gunar.lab4


class Cuaternion {
    internal var w: Float = 0.toFloat()
    internal var x: Float = 0.toFloat()
    internal var y: Float = 0.toFloat()
    internal var z: Float = 0.toFloat()

    internal constructor() {
        w = 0f
        x = 0f
        y = 0f
        z = 0f
    }

    internal constructor(w: Float, x: Float, y: Float, z: Float) {
        this.w = w
        this.x = x
        this.y = y
        this.z = z
    }

    internal constructor(w: Float, v: Vector3) {
        this.w = w
        this.x = v.x
        this.y = v.y
        this.z = v.z
    }

    // norma^2 = w^2 + x^2 + y^2 + z^2
    internal fun norma2(): Float {
        return w * w + x * x + y * y + z * z
    }

    // Conjugado
    internal fun conjugado(): Cuaternion {
        x = -this.x
        y = -this.y
        z = -this.z
        return this
    }

    // q = q^(-1)
    fun inverso(): Cuaternion {
        var q = Cuaternion()
        // normal^2 = a . b
        val n = norma2()
        if (n <= 1e-8)
            println("INVERSO: Error")
        q = multiplica(Cuaternion(w, -x, -y, -z), 1 / n)
        return q
    }

    override fun toString(): String {
        return String.format(" w = %5.2f x = %5.2f y = %5.2f z = %5.2f", w, x, y, z)
    }

    companion object {
        // Cuaternion q = Cuaternion a . Cuaternion b
        internal fun multiplica(a: Cuaternion, b: Cuaternion): Cuaternion {
            val q = Cuaternion()
            q.w = a.w * b.w - a.x * b.x - a.y * b.y - a.z * b.z
            q.x = a.w * b.x + a.x * b.w + a.y * b.z - a.z * b.y
            q.y = a.w * b.y - a.x * b.z + a.y * b.w + a.z * b.x
            q.z = a.w * b.z + a.x * b.y - a.y * b.x + a.z * b.w
            return q
        }

        // Cuaternion q = Cuaternion a . b
        internal fun multiplica(a: Cuaternion, b: Float): Cuaternion {
            val q = Cuaternion()
            q.w = a.w * b
            q.x = a.x * b
            q.y = a.y * b
            q.z = a.z * b
            return q
        }

        // q' = q . p . q^(-1)
        fun rota(q: Cuaternion, p: Vector3): Vector3 {
            val p_homogeneo = Cuaternion(0f, p)
            val p_prima = multiplica(q, multiplica(p_homogeneo, q.inverso()))
            return Vector3(p_prima.x, p_prima.y, p_prima.z)
        }

        // q' = q . p . q*
        fun rota1(q: Cuaternion, p: Vector3): Vector3 {
            val p_homogeneo = Cuaternion(0f, p)
            val p_prima = multiplica(q, multiplica(p_homogeneo, q.conjugado()))
            return Vector3(p_prima.x, p_prima.y, p_prima.z)
        }

        /* Convierte el cuaternión a una matriz de rotación */
        fun rota(a: FloatArray, q: Cuaternion) {
            val d: Float
            val s: Float
            d = q.x * q.x + q.y * q.y + q.z * q.z + q.w * q.w
            s = if (d > 0.0f) 2.0f / d else 0.0f
            a[0] = 1.0f - (q.y * q.y + q.z * q.z) * s
            a[4] = (q.x * q.y - q.w * q.z) * s
            a[8] = (q.x * q.z + q.w * q.y) * s
            a[12] = 0f
            a[1] = (q.x * q.y + q.w * q.z) * s
            a[5] = 1.0f - (q.x * q.x + q.z * q.z) * s
            a[9] = (q.y * q.z - q.w * q.x) * s
            a[13] = 0f
            a[2] = (q.x * q.z - q.w * q.y) * s
            a[6] = (q.y * q.z + q.w * q.x) * s
            a[10] = 1.0f - (q.x * q.x + q.y * q.y) * s
            a[14] = 0f
            a[3] = 0f
            a[7] = 0f
            a[11] = 0f
            a[15] = 1f
        }
    }
}