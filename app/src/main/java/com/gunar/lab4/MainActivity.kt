package com.gunar.lab4

import androidx.appcompat.app.AppCompatActivity

import android.opengl.GLSurfaceView
import android.os.Bundle
import android.view.Window
import android.view.WindowManager

class MainActivity : AppCompatActivity() {

    private var superficie: GLSurfaceView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* Ventana sin tÃ­tulo */
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        /* Se establece las banderas de la ventana de esta Actividad */
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        /* Se crea el objeto Renderiza */
        superficie = Renderiza(this)
        /*
         * Activity <- GLSurfaceView : Coloca la Vista de la Superficie del
         * OpenGL como un Contexto de Ã©sta Actividad.
         */
        setContentView(superficie)
        // setContentView(R.layout.activity_main);
    }

    /**
     * Recuerda que debe reanudar superficie
     */
    override fun onResume() {
        super.onResume()
        superficie!!.onResume()
    }

    /**
     * TambiÃ©n pausa la superficie
     */
    override fun onPause() {
        super.onPause()
        superficie!!.onPause()
    }

}
