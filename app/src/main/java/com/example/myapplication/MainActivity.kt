package com.example.myapplication

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PackageManagerCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.CheckBox
import android.widget.LinearLayout


class MainActivity : AppCompatActivity(), SensorEventListener {
    private var contador = 0;
    private lateinit var sensorManager: SensorManager;
    private var sensorPasos : Sensor? = null;
    private lateinit var tvContador : TextView;


    private val listaChecklists = listOf(
        Checklist("Rutina de fuerza"),
        Checklist("Natación"),
        Checklist("Yoga"),
        Checklist("Ciclismo"),
        Checklist("Correr"),
        Checklist("Entrenamiento HIIT"),
        Checklist("Pilates")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvContador = findViewById<TextView>(R.id.tvContador);
        val btnSumar = findViewById<Button>(R.id.btnSumar);
        val btnReiniciar = findViewById<Button>(R.id.btnReiniciar);
        val btnDetalle = findViewById<Button>(R.id.btnDetalle);

        val columnaChecklist = findViewById<LinearLayout>(R.id.columnaChecklist);

        for (actividad in listaChecklists) {
            val card = layoutInflater.inflate(
                R.layout.item_checklist,
                columnaChecklist,
                false
            )

            val fondoCard =
                card.findViewById<LinearLayout>(R.id.cardChecklist)

            val nombre =
                card.findViewById<TextView>(R.id.tvNombre)

            val check =
                card.findViewById<CheckBox>(R.id.checkActividad)

            nombre.text = actividad.nombre

            check.setOnCheckedChangeListener { _, marcado ->
                if (marcado) {
                    fondoCard.setBackgroundColor(
                        Color.rgb(200, 240, 210)
                    )
                } else {
                    fondoCard.setBackgroundResource(
                        R.drawable.fondochecklist
                    )
                }
            }

            columnaChecklist.addView(card)
        }


//        val checkFuerza = findViewById<CheckBox>(R.id.checkFuerza);
//        val cardFuerza = findViewById<LinearLayout>(R.id.cardFuerza);
//
//        val checkNatacion = findViewById<CheckBox>(R.id.checkNatacion)
//        val cardNatacion = findViewById<LinearLayout>(R.id.cardNatacion)
//
//        val checkYoga = findViewById<CheckBox>(R.id.checkYoga)
//        val cardYoga = findViewById<LinearLayout>(R.id.cardYoga)

//
//        checkFuerza.setOnCheckedChangeListener { _, marcado -> cambiarColorCard(cardFuerza, marcado)  }
//        checkYoga.setOnCheckedChangeListener { _, marcado -> cambiarColorCard(cardYoga, marcado)  }
//        checkNatacion.setOnCheckedChangeListener { _, marcado -> cambiarColorCard(cardNatacion, marcado)  }


        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACTIVITY_RECOGNITION )!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),100)
        } else{
            configurarSensor();
        }


        btnSumar.setOnClickListener{
            contador++;

            tvContador.text = "Numero de pasos: $contador";
        }

        btnReiniciar.setOnClickListener {
            contador=0;
            tvContador.text = "Numero de pasos: $contador";

        }

        btnDetalle.setOnClickListener{
            val intent = Intent(this, Main2Activity::class.java);

            intent.putExtra("Pasos",  contador);
            startActivity(intent);
        }
    }

    //override fun OnRequestPermissionsResult(){

   // }

    private fun cambiarColorCard(
        card: LinearLayout,
        marcado: Boolean
    ) {
        val fondo = GradientDrawable()
        fondo.cornerRadius = 12f

        if (marcado) {
            fondo.setColor(Color.rgb(220, 252, 231))
            fondo.setStroke(2, Color.rgb(22, 163, 74))
        } else {
            fondo.setColor(Color.WHITE)
            fondo.setStroke(1, Color.rgb(221, 221, 221))
        }

        card.background = fondo
    }




    private fun configurarSensor(){
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager;
        sensorPasos = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);

        if(sensorPasos==null){
            tvContador.text= "ERROR AL CONFIGURAR EL SENSOR DE PASOS";
        }
    }

    override fun onResume() {
        super.onResume()

        sensorPasos?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI);
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this);
    }

    override fun onSensorChanged(event: SensorEvent){
        if(event?.sensor?.type == Sensor.TYPE_STEP_COUNTER){
            val pasosTotales = event.values[0].toInt();
            tvContador.text = "Pasos de hoy: $pasosTotales";
        }

    }

    override fun onAccuracyChanged( sensor: Sensor?, accuracy: Int){
    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String?>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        if (requestCode==100){
//            if (grantResults.isNotEmpty()&& grantResults[0] == PackageManager.PERMISSION_GRANTED)
//                configurarSensor();
//        }
//        else{
//            tvContador.text = "Permiso de sensor no concedido";
//        }
//    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                configurarSensor();
            } else{
                tvContador.text = "Permiso de sensor no concedido";
            }
        }
    }



}