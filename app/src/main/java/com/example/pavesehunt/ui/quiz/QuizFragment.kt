package com.example.pavesehunt.ui.quiz

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.pavesehunt.R
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.pavesehunt.databinding.FragmentQuizBinding
import com.example.pavesehunt.domain.viewmodels.TopBarViewModel
import com.example.testapp.data.models.User
import com.example.testapp.domain.viewmodels.QuizViewModel
import com.example.testapp.domain.viewmodels.UserViewModel

class QuizFragment : Fragment() {

    private val quizViewModel : QuizViewModel by activityViewModels()
    private val userViewModel : UserViewModel by activityViewModels()
    private val topBarViewModel : TopBarViewModel by activityViewModels()

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

    override fun onDestroyView() {
        super.onDestroyView()

        topBarViewModel.isInQuizFragment.value = false
        topBarViewModel.screenChanged.value = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        topBarViewModel.isInQuizFragment.value = true
        topBarViewModel.screenChanged.value = true

        userViewModel.userResponse.observe(viewLifecycleOwner){ response ->

            if(response.status == STATUS.SUCCESS){
                val user = response.data as User

                binding.pointsTextView.text = user.points.toString()

                var username = user.username[0].uppercase() + user.username.substring(1, user.username.length)

                binding.usernameTextView.text = username
                Glide.with(this).load(user.imageUrl).into(binding.userImageView)

                val shared = view.context.getSharedPreferences("shared", Context.MODE_PRIVATE)


                binding.answerTextView.text = user.answer_given.toString()
                binding.remainingAnswerTextView.text = (4 - user.answer_given!!).toString()

                with(shared.edit()){
                    putInt("points", user.points)
                    apply()
                }

                binding.loadingLayout.root.visibility = View.GONE
            }
        }

        binding.quizButton.setOnClickListener {
            binding.quizButton.setBackgroundColor(this.resources.getColor(R.color.primaryContainer))
            binding.quizButton.setTextColor(this.resources.getColor(R.color.onPrimaryContainer))
            view.findNavController().navigate(R.id.action_quizFragment_to_questionFragment)
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            QuizFragment().apply {
            }
    }
}