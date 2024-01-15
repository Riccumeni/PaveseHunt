package com.example.pavesehunt.ui.settings

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.pavesehunt.R
import com.example.pavesehunt.databinding.FragmentSettingsBinding
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.pavesehunt.domain.viewmodels.TopBarViewModel
import com.example.pavesehunt.ui.login.LoginActivity
import com.example.testapp.domain.viewmodels.UserViewModel

class SettingsFragment : Fragment() {

    private val viewModel : UserViewModel by activityViewModels()
    private val topBarViewModel : TopBarViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
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

        binding.resetQuizCard.setOnClickListener {
            showResetDialog()
        }

        viewModel.statusSignOut.observe(viewLifecycleOwner){
            if(it == STATUS.SUCCESS){
                val c = view.context as AppCompatActivity
                startActivity(Intent(view.context, LoginActivity::class.java))
                c.finish()
            }
        }
    }

    fun showResetDialog(){
        val dialog = Dialog(requireView().context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.reset_quiz_dialog)

        val discardButton = dialog.findViewById<Button>(R.id.discardDialogButton)
        val okButton = dialog.findViewById<Button>(R.id.okDialogButton)

        okButton.setOnClickListener {
            userViewModel.resetPoints()
            dialog.dismiss()
        }

        discardButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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