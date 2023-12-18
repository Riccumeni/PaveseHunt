package com.example.pavesehunt.ui.login

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Status
import com.example.pavesehunt.databinding.ActivitySignUpBinding
import com.example.testapp.domain.viewmodels.UserViewModel
import java.io.ByteArrayOutputStream


class SignUpActivity : AppCompatActivity() {

    val viewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding
    lateinit var profileImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val registerButton = findViewById<Button>(R.id.registerButton)
        profileImage = findViewById(R.id.profileImage)

        viewModel.statusSignUpEmail.observe(this){
            if(it == Status.SUCCESS){
                val toast = Toast.makeText(this, "Registrazione effettuata", Toast.LENGTH_LONG) // in Activity
                toast.show()

                Handler().postDelayed({
                    finish()
                }, 1000)
            }
        }


        registerButton.setOnClickListener {

            if(binding.usernameEditText.editText!!.text!!.isNotEmpty() &&
                binding.emailEditText.editText!!.text!!.isNotEmpty() &&
                binding.passwordEditText.editText!!.text!!.isNotEmpty() &&
                binding.repeatPasswordEditText.editText!!.text!!.isNotEmpty()){
                val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val image = stream.toByteArray()

                viewModel.signUpEmail(
                    binding.emailEditText.editText!!.text.toString()
                    , binding.passwordEditText.editText!!.text.toString(), binding.usernameEditText.editText!!.text.toString(), image, this)
            }else{
                val toast = Toast.makeText(this, "Compila tutti i campi", Toast.LENGTH_LONG) // in Activity
                toast.show()
            }
        }

        profileImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImageUri = data?.data
        Glide.with(this).load(selectedImageUri).into(profileImage)
    }
}