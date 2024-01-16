package com.example.pavesehunt.ui

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.pavesehunt.R
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.pavesehunt.ui.home.HomeActivity
import com.example.pavesehunt.ui.login.LoginActivity
import com.example.pavesehunt.domain.viewmodels.MainViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*

        if(Build.VERSION.SDK_INT < 30){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }

        installSplashScreen().apply {
            if(Build.VERSION.SDK_INT >= 30){
                var status = STATUS.LOADING

                val sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE)
                val token = sharedPref.getString("token", "")

                if(token != ""){
                    runBlocking {
                        launch {
                            viewModel.checkToken(token!!)
                        }
                    }
                }else{
                    viewModel.status.value = STATUS.ERROR
                }

                viewModel.status.observe(this@MainActivity){
                    status = it
                }

                this.setKeepOnScreenCondition {
                    status == STATUS.LOADING
                }

                this.setOnExitAnimationListener{
                    if(viewModel.status.value == STATUS.SUCCESS){
                        startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                        finish()
                    } else if(viewModel.status.value == STATUS.ERROR){
                        startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                        finish()
                    }
                }
            }
        }

        */

        setContentView(R.layout.activity_main)

        var status = STATUS.LOADING
        val sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", "")

        if(token != ""){
            runBlocking {
                launch {
                    viewModel.checkToken(token!!)
                }
            }
        }else{
            viewModel.status.value = STATUS.ERROR
        }

        viewModel.status.observe(this@MainActivity){
            status = it
        }

        if(viewModel.status.value == STATUS.SUCCESS){
            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
            finish()
        } else if(viewModel.status.value == STATUS.ERROR){
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }


        /*
        
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    var status = STATUS.LOADING
                    val sharedPref = getSharedPreferences("shared", Context.MODE_PRIVATE)
                    val token = sharedPref.getString("token", "")

                    if(token != ""){
                        runBlocking {
                            launch {
                                viewModel.checkToken(token!!)
                            }
                        }
                    }else{
                        viewModel.status.value = STATUS.ERROR
                    }

                    viewModel.status.observe(this@MainActivity){
                        status = it
                    }

                    return if (status == STATUS.SUCCESS || status == STATUS.ERROR) {
                        // The content is ready. Start drawing.
                        if(viewModel.status.value == STATUS.SUCCESS){
                            startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                            finish()
                        } else if(viewModel.status.value == STATUS.ERROR){
                            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                            finish()
                        }
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        // The content isn't ready. Suspend.
                        false
                    }
                }
            }
        )

        */
    }
}