package com.example.pavesehunt.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Status
import com.example.pavesehunt.ui.home.HomeActivity
import com.example.pavesehunt.ui.login.LoginActivity
import com.example.pavesehunt.domain.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen().apply {

            var status = Status.LOADING

            val sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE)
            val token = sharedPref.getString("token", "")

            if(token != ""){
                viewModel.checkToken(token!!)
            }else{
                viewModel.status.value = Status.ERROR
            }

            viewModel.status.observe(this@MainActivity){
                status = it
            }

            this.setKeepOnScreenCondition {
                status == Status.LOADING
            }

            // TODO: RIMETTERE A POSTO

            this.setOnExitAnimationListener{
                if(viewModel.status.value == Status.SUCCESS){
                    startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                    finish()
                } else if(viewModel.status.value == Status.ERROR){
                    startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                    finish()
                }
            }

        }
        setContentView(R.layout.activity_main)
    }
}