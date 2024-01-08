package com.example.pavesehunt.ui.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Status
import com.example.pavesehunt.domain.viewmodels.TopBarViewModel
import com.example.pavesehunt.ui.login.LoginActivity
import com.example.testapp.domain.viewmodels.UserViewModel

class SettingsFragment : Fragment() {

    private val viewModel : UserViewModel by activityViewModels()
    private val topBarViewModel : TopBarViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val signOutButton = view.findViewById<Button>(R.id.signOutButton)
        val manageFriendsButton = view.findViewById<CardView>(R.id.manageFriendCard)

        manageFriendsButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_settingsFragment_to_addFriendFragment)
        }

        topBarViewModel.screenChanged.value = true

        signOutButton.setOnClickListener {
            viewModel.signOut(view.context)
        }

        viewModel.statusSignOut.observe(viewLifecycleOwner){
            if(it == Status.SUCCESS){
                val c = view.context as AppCompatActivity
                startActivity(Intent(view.context, LoginActivity::class.java))
                c.finish()
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}