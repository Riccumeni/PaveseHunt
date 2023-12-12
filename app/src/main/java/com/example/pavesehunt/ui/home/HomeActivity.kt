package com.example.pavesehunt.ui.home

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.pavesehunt.R
import com.example.pavesehunt.databinding.ActivityHomeBinding
import com.example.testapp.domain.viewmodels.QuizViewModel
import com.example.pavesehunt.ui.quiz.QuizFragment
import com.example.pavesehunt.ui.search.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

enum class STACK{
    QUIZ,
    RANK,
    SETTINGS
}

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }
}