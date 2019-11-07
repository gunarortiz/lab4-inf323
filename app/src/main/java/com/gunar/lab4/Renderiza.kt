package com.gunar.lab4

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent

import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView.Renderer

class Renderiza(contexto: Context) : GLSurfaceView(contexto), Renderer {
    /* Objeto */
    private var cubo: Cubo? = null
    private var cubo2: Cubo? = null


    private var piramide: Piramide? = null
    private var piramide2: Piramide2? = null
    private var piramide3: Piramide? = null
    private var piramide4: Piramide? = null


    private var rectangulo: Rectangulo? = null

    /* Ancho y alto de la ventana */
    private var ancho: Int = 0
    private var alto: Int = 0
    /* Para la rotación */
    private val arcBall = ArcBall(640.0f, 480.0f)
    private val MatrizRotacion = FloatArray(16)
    private val B = FloatArray(16)

    var obj: Objeto? = null
    var obj1: Objeto? = null
    var contexto: Context? = null

    init {
        this.contexto = contexto
        /* Inicia el renderizado */
        this.setRenderer(this)
        /* La ventana solicita recibir una entrada */
        this.requestFocus()
        /* Establece que la ventana detecte el modo táctil */
        this.isFocusableInTouchMode = true
        /* Se renderizará al inicio o cuando se llame a requestRender() */
        this.renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    override fun onSurfaceCreated(gl: GL10, arg1: EGLConfig) {
        cubo = Cubo()
        cubo2 = Cubo()

        rectangulo = Rectangulo()

        piramide = Piramide()
        piramide3 = Piramide()
        piramide4 = Piramide()
        piramide2 = Piramide2()

        obj = Objeto(contexto!!, "lowpolymountains.obj")
        obj1 = Objeto(contexto!!, "porsche.obj")

//        val luz_ambiente = floatArrayOf(0f, 0f, 0f, 1f) // I
//        val luz_difusa = floatArrayOf(1f, 1f, 1f, 1f)
//        val luz_especular = floatArrayOf(1f, 1f, 1f, 1f)
//        val luz_posicion = floatArrayOf(0f, 0f, 1f, 0f) // L
//
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, luz_ambiente, 0)
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, luz_difusa, 0)
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_SPECULAR, luz_especular, 0)
//        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_POSITION, luz_posicion, 0)

        gl.glShadeModel(GL10.GL_SMOOTH)
        gl.glEnable(GL10.GL_DEPTH_TEST)
        gl.glEnable(GL10.GL_LIGHTING)
        gl.glEnable(GL10.GL_LIGHT0)
        gl.glEnable(GL10.GL_NORMALIZE)


        /* B = I */
        Matriz4.identidad(B)
        /* Deshabilita dithering, no se limita la paleta de colores */
        gl.glDisable(GL10.GL_DITHER)
        /* Habilita el modo de sombreado plano */
        gl.glShadeModel(GL10.GL_FLAT)
        /* Habilita el ocultamiento de superficies */
        gl.glEnable(GL10.GL_DEPTH_TEST)
        /* Limpia el buffer de profundidad con el valor de 1.0 */
        gl.glClearDepthf(1.0f)
        /* Acepta si valor Z de entrada es igual al valor Z del buffer de profundidad */
        gl.glDepthFunc(GL10.GL_LEQUAL)
        /* Color de fondo */
        gl.glClearColor(0f, 0f, 0f, 0f)
    }

    fun muestraCubo(gl: GL10) {
        gl.glTranslatef(3f, 0f, 0f)
        cubo!!.dibuja(gl)
    }

    fun muestraCubo2(gl: GL10) {
        gl.glTranslatef(3f, 0f, 0f)
        cubo2!!.dibuja(gl)
    }

    override fun onDrawFrame(gl: GL10) {
        /* Incializa el buffer de color y de profundidad */
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT or GL10.GL_DEPTH_BUFFER_BIT)
        /* Inicializa la Matriz del Modelo-Vista */
        gl.glLoadIdentity() // MVM = I
        gl.glMultMatrixf(MatrizRotacion, 0) // MVM = MVM * MatrizRotacion

        gl.glPushMatrix()

        gl.glScalef(0.2f, 0.2f, 0.2f)
        rectangulo!!.dibuja(gl)
//
//        gl.glTranslatef(0f, 0f, 0f)
//        piramide2!!.dibuja(gl)
//
//        gl.glTranslatef(0f, 2f, 0f)
//        piramide!!.dibuja(gl)
//
//        gl.glTranslatef(-3f, -2f, -9f)
//        muestraCubo(gl)
//
//        gl.glTranslatef(-12f, 0f, 9f)
//        muestraCubo2(gl)
//
//        gl.glTranslatef(9f, 0f, 9f)
//        piramide3!!.dibuja(gl)

        gl.glPushMatrix()
        gl.glTranslatef(-3f, 14f, -5f)
        gl.glScalef(22f,22f,22f)
        obj!!.dibuja(gl)
        gl.glPopMatrix()

        gl.glPushMatrix()
        gl.glTranslatef(9f, 1.5f, 5f)
        gl.glScalef(2f,2f,2f)
        gl.glRotatef(-90f, 0f, 1f, 0f)
        obj1!!.dibuja(gl)
        gl.glPopMatrix()

        gl.glPushMatrix()
        gl.glTranslatef(4f, 1.5f, 8f)
        gl.glScalef(2f,2f,2f)
        gl.glRotatef(-90f, 0f, 1f, 0f)
        obj1!!.dibuja(gl)
        gl.glPopMatrix()


        gl.glPushMatrix()
        gl.glTranslatef(-1f, 1.5f, 5f)
        gl.glScalef(2f,2f,2f)
        gl.glRotatef(-90f, 0f, 1f, 0f)
        obj1!!.dibuja(gl)
        gl.glPopMatrix()

        gl.glPushMatrix()
        gl.glTranslatef(-6f, 1.5f, 8f)
        gl.glScalef(2f,2f,2f)
        gl.glRotatef(-90f, 0f, 1f, 0f)
        obj1!!.dibuja(gl)
        gl.glPopMatrix()


        gl.glPopMatrix()
    }

    override fun onSurfaceChanged(gl: GL10, w: Int, h: Int) {
        ancho = w
        alto = h
        /* Ventana de despliegue */
        gl.glViewport(0, 0, ancho, alto)
        /* Matriz de Proyección */
        gl.glMatrixMode(GL10.GL_PROJECTION)
        /* Inicializa la Matriz de Proyección */
        gl.glLoadIdentity()
        /* Proyección paralela */
        if (w <= h)
            gl.glOrthof(-2f, 2f, -2 * h.toFloat() / w.toFloat(), 2 * h.toFloat() / w.toFloat(), -10f, 10f)
        else
            gl.glOrthof(-2 * w.toFloat() / h.toFloat(), 2 * w.toFloat() / h.toFloat(), -2f, 2f, -10f, 10f)
        /* Matriz del Modelo-Vista */
        gl.glMatrixMode(GL10.GL_MODELVIEW)
        /* Inicializa la Matriz del Modelo-Vista */
        gl.glLoadIdentity()
        Matriz4.identidad(MatrizRotacion)
        /* Ajusta el ancho a [-1..1] y el alto a [-1..1] */
        arcBall.ajusta(ancho.toFloat(), alto.toFloat())
    }

    /**
     * Maneja los eventos del movimiento en la pantalla táctil.
     */
    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x = e.x
        val y = e.y
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                /* B = MatrizRotacion */
                Matriz4.copia(B, MatrizRotacion)
                arcBall.primerPunto(x, y)
            }
            MotionEvent.ACTION_MOVE -> {
                /* Actualiza el segundo vector y obtiene el cuaternión */
                val q = arcBall.segundoPunto(x, y)
                /* Convierte el cuaternión a una matriz de rotación */
                Cuaternion.rota(MatrizRotacion, q)
                /* MatrizRotacion = MatrizRotacion * B */
                Matriz4.multiplica(MatrizRotacion, MatrizRotacion, B)
                requestRender()
            }
        }
        return true
    }
}


