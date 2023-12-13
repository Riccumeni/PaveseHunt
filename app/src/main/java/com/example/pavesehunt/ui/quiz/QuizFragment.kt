package com.example.pavesehunt.ui.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.pavesehunt.R
import com.example.pavesehunt.databinding.FragmentQuizBinding
import com.example.testapp.domain.viewmodels.QuizViewModel
import com.unity3d.player.UnityPlayerActivity

class QuizFragment : Fragment() {

    private val quizViewModel : QuizViewModel by activityViewModels()

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        quizViewModel.getUser()

        quizViewModel.user.observe(viewLifecycleOwner){ user ->
            if(user != null){
                binding.pointsTextView.text = user.points.toString()
                binding.usernameTextView.text = user.username

                val shared = view.context.getSharedPreferences("shared", Context.MODE_PRIVATE)

                with(shared.edit()){
                    putInt("points", user.points)
                    apply()
                }
            }
        }

        binding.rankButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_quizFragment_to_rankFragment)
        }

        binding.quizButton.setOnClickListener {
            view.findNavController().navigate(R.id.action_quizFragment_to_questionFragment)
            // startActivity(Intent(view.context, UnityPlayerActivity::class.java))
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            QuizFragment().apply {
            }
    }
}