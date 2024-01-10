package com.example.pavesehunt.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.pavesehunt.R
import com.example.pavesehunt.databinding.ActivityHomeBinding
import com.example.pavesehunt.domain.viewmodels.CollectionViewModel
import com.example.pavesehunt.domain.viewmodels.TopBarViewModel
import com.example.pavesehunt.ui.calendar.CalendarFragment
import com.example.testapp.domain.viewmodels.QuizViewModel
import com.example.pavesehunt.ui.quiz.QuizFragment
import com.example.pavesehunt.ui.search.SearchFragment
import com.example.pavesehunt.ui.settings.SettingsFragment
import com.example.testapp.domain.viewmodels.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val userViewModel : UserViewModel by viewModels()
    private val quizViewModel : QuizViewModel by viewModels()
    private val collectionViewModel : CollectionViewModel by viewModels()
    private val topBarViewModel : TopBarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel.getUser()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentView) as NavHostFragment
        val navController = navHostFragment.navController

        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)

        NavigationUI.setupWithNavController(bottomNavigation, navController)

        topBarViewModel.isInQuizFragment.observe(this){
            if(it){
                binding.rankButton.visibility = View.VISIBLE
            }else{
                binding.rankButton.visibility = View.GONE
            }
        }

        topBarViewModel.screenChanged.observe(this){
            when(navHostFragment.childFragmentManager.fragments?.get(0)){
                is QuizFragment -> {
                    binding.backButton.visibility = View.GONE
                }

                is SearchFragment -> {
                    binding.backButton.visibility = View.GONE
                }

                is SettingsFragment -> {
                    binding.backButton.visibility = View.GONE
                }

                is CalendarFragment -> {
                    binding.backButton.visibility = View.GONE
                }

                else -> {
                    binding.backButton.visibility = View.VISIBLE
                }
            }
        }

        binding.rankButton.setOnClickListener {
            navController.navigate(R.id.action_quizFragment_to_rankFragment)
        }

        binding.backButton.setOnClickListener {
            navController.popBackStack()
        }


    }
}