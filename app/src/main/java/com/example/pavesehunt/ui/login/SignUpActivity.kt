package com.example.pavesehunt.ui.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.pavesehunt.R
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.pavesehunt.databinding.ActivitySignUpBinding
import com.example.pavesehunt.domain.usecases.ErrorCodes
import com.example.testapp.domain.viewmodels.UserViewModel
import java.io.ByteArrayOutputStream


class SignUpActivity : AppCompatActivity() {

    val viewModel: UserViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding
    lateinit var profileImage: ImageView

    var profileImageChanged = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = getColor(R.color.primaryContainer)

        val registerButton = findViewById<Button>(R.id.registerButton)
        profileImage = findViewById(R.id.profileImage)

        viewModel.statusSignUpEmail.observe(this){
            if(it.status == STATUS.SUCCESS){
                showSignUpDialog()
            }else if(it.status == STATUS.ERROR){

                when(it.code){

                    ErrorCodes.EMAIL_WRONG -> {
                        binding.emailEditText.error = "Email is not valid"
                    }

                    ErrorCodes.INTERNET_CONNECTION -> {
                        val toast = Toast.makeText(this, "Check your internet connection", Toast.LENGTH_LONG)
                        toast.show()
                    }

                    ErrorCodes.PASSWORD_TOO_SHORT -> {
                        binding.passwordEditText.error = "Password is too short"
                    }

                    ErrorCodes.USERNAME_ALREADY_TOKEN -> {
                        binding.usernameEditText.error = "Username already token"
                    }

                    ErrorCodes.PASSWORDS_NOT_EQUAL -> {
                        binding.passwordEditText.error = "Passwords are not equal"
                        binding.repeatPasswordEditText.error = "Passwords are not equal"
                    }

                    else -> {
                    }
                }
            }
        }


        registerButton.setOnClickListener {

            if(profileImageChanged){
                val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
                val image = stream.toByteArray()

                viewModel.signUpEmail(
                    email = binding.emailEditText.text.toString(),
                    password = binding.passwordEditText.text.toString(),
                    username = binding.usernameEditText.text.toString(),
                    repeatedPassword = binding.repeatPasswordEditText.text.toString(),
                    image = image,
                    context = this)
            }else{
                val toast = Toast.makeText(this, "Inserisci anche l'immagine", Toast.LENGTH_LONG) // in Activity
                toast.show()
            }
        }

        profileImage.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 1)
        }
    }

    fun showSignUpDialog(){
        val dialog = Dialog(this)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.reset_quiz_dialog)

        val discardButton = dialog.findViewById<Button>(R.id.discardDialogButton)
        val okButton = dialog.findViewById<Button>(R.id.okDialogButton)
        val titleDialog = dialog.findViewById<TextView>(R.id.titleDialog)
        val bodyDialog = dialog.findViewById<TextView>(R.id.bodyDialog)

        titleDialog.text = "Registrazione effettuata"
        bodyDialog.text = "Per confermare la registrazione andare nella casella di posta e cliccare su 'conferma email'"

        discardButton.visibility = View.GONE

        okButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val selectedImageUri = data?.data
        Glide.with(this).load(selectedImageUri).into(profileImage)
        binding.profileImage.scaleX = 1.0f
        binding.profileImage.scaleY = 1.0f
        profileImageChanged = true
    }
}