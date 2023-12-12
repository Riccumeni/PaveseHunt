package com.example.pavesehunt.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import com.example.pavesehunt.R
import com.example.testapp.MainViewModel

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val counterText = findViewById<TextView>(R.id.counterText)

        counterText.text = "0"

        viewModel.startTimer()

        viewModel.getClassification()

        viewModel.counter.observe(this){
            counterText.text = it.toString()
        }


    }
}