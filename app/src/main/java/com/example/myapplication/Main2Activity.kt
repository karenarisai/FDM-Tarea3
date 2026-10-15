package com.example.myapplication

import android.os.Bundle
import android.widget.TextView
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Main2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main2)

        val tvResumen = findViewById<TextView>(R.id.tvResumen);
        val btnRegresar = findViewById<Button>(R.id.btnRegresar);

        val contador = intent.getIntExtra("PASOS",0);

        btnRegresar.setOnClickListener {
            finish();
        }

    }
}