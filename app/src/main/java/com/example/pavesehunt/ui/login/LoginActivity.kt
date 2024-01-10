package com.example.pavesehunt.ui.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Status
import com.example.pavesehunt.databinding.ActivityLoginBinding
import com.example.testapp.domain.viewmodels.UserViewModel

import com.example.pavesehunt.ui.home.HomeActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    val viewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.primaryContainer)

        binding.loginButton.setOnClickListener {

            viewModel.login(binding.emailEditText.text.toString(),binding.passwordEditText.text.toString(), this)
        }

        binding.registerButton.setOnClickListener {
            //viewModel.registerEmail("gageve4075@apdiv.com", "gageve", "gianfranco", this)
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        viewModel.status.observe(this){ status ->
            if(status == Status.SUCCESS){
                startActivity(Intent(this, HomeActivity::class.java))
            }
            if(status == Status.ERROR){
                binding.emailEditText.error = "Invalid credential"
                binding.passwordEditText.error = "Invalid credential"
            }
        }
    }
}