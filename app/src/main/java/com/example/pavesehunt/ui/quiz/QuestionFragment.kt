package com.example.pavesehunt.ui.quiz

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.activityViewModels
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Question
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.pavesehunt.databinding.FragmentQuestionBinding
import com.example.testapp.data.models.User
import com.example.testapp.domain.viewmodels.QuizViewModel
import com.example.testapp.domain.viewmodels.UserViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator
import kotlinx.serialization.json.Json

class QuestionFragment : Fragment() {

    private val quizViewModel : QuizViewModel by activityViewModels()
    private val userViewModel: UserViewModel by activityViewModels()

    private var _binding: FragmentQuestionBinding? = null
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
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        quizViewModel.stopTimer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val counterIndicator = view.findViewById<LinearProgressIndicator>(R.id.counterIndicator)
        val openPoemDialogCard = view.findViewById<CardView>(R.id.openPoemCard)
        val tutorialCard = view.findViewById<CardView>(R.id.tutorialCard)

        quizViewModel.questionsResponse.observe(viewLifecycleOwner){ response ->
            when(response.status){
                STATUS.NOT_STARTED -> {
                    quizViewModel.getQuestions()
                }

                STATUS.SUCCESS -> {

                    binding.mainContent.visibility = View.VISIBLE
                    binding.loadingLayout.root.visibility = View.GONE


                    val questions = response.data as List<Question>

                    val user = userViewModel.userResponse.value!!.data as User

                    val buttons: List<Button> = listOf(
                        view.findViewById(R.id.firstAnswerButton),
                        view.findViewById(R.id.secondAnswerButton),
                        view.findViewById(R.id.thirdAnswerButton),
                        view.findViewById(R.id.fourthAnswerButton)
                    )

                    if(user.answer_given!! < questions.size){
                        quizViewModel.startTimer()

                        var answers = Json.decodeFromString<Array<String>>(questions[user.answer_given!!].answer)

                        binding.questionText.text = questions[user.answer_given!!].question

                        openPoemDialogCard.setOnClickListener {
                            showDialog(questions[user.answer_given!!].poem)
                        }



                        answers.forEachIndexed { index, answer ->
                            buttons[index].text = answer
                        }

                        buttons.forEachIndexed{ index, button ->

                            button.setOnClickListener {
                                val time: Int? = quizViewModel.counter.value

                                quizViewModel.stopTimer()

                                if(index == questions[user.answer_given!!].correct_answer){
                                    userViewModel.sendResult(
                                        time = time!!,
                                        actualProgress = user.answer_given!!,
                                        isCorrect = true
                                    )
                                }else{
                                    userViewModel.sendResult(
                                        time = time!!,
                                        actualProgress = user.answer_given!!,
                                        isCorrect = false
                                    )
                                }

                                buttons.forEachIndexed { i, button ->
                                    button.isEnabled = false
                                    buttons[i].setTextColor(0xFFFFFFFF.toInt())
                                    if(i == questions[user.answer_given!!].correct_answer){
                                        buttons[i].setBackgroundColor(0xFF1C9700.toInt())
                                    }else{
                                        if(i == index){
                                            buttons[i].setBackgroundColor(0xFF43341E.toInt())
                                        }else{
                                            buttons[i].setBackgroundColor(0xFF780000.toInt())
                                        }

                                    }
                                }

                                user.answer_given = user.answer_given!! + 1

                                if(user.answer_given!! < questions.size){
                                    Handler().postDelayed({

                                        answers = Json.decodeFromString(questions[user.answer_given!!].answer)

                                        answers.forEachIndexed{index, s ->
                                            buttons[index].setTextColor(0xFF55442B.toInt())
                                            buttons[index].setBackgroundColor(0xFFCBB18C.toInt())
                                            buttons[index].text = s
                                            buttons[index].isEnabled = true
                                        }

                                        binding.questionText.text = questions[user.answer_given!!].question

                                        quizViewModel.startTimer()
                                    }, 2000)
                                }else{
                                    Handler().postDelayed({
                                        binding.resetQuizLayout.visibility = View.VISIBLE
                                    }, 2000)
                                }
                            }
                        }
                    }else{
                        binding.resetQuizLayout.visibility = View.VISIBLE
                    }
                }

                STATUS.LOADING -> {
                    binding.loadingLayout.root.visibility = View.VISIBLE
                    binding.mainContent.visibility = View.GONE
                }

                STATUS.ERROR -> {

                }
            }
        }






        quizViewModel.counter.observe(viewLifecycleOwner){
            counterIndicator.progress = it
        }



        tutorialCard.setOnClickListener {
            showTutorialDialog()
        }
    }

    fun showDialog(poem: String){
        val dialog = Dialog(requireView().context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.poem_dialog)

        val poemTextView = dialog.findViewById<TextView>(R.id.poemTextView)
        val exitButton = dialog.findViewById<Button>(R.id.okDialogButton)

        poemTextView.text = poem

        exitButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    fun showTutorialDialog(){
        val dialog = Dialog(requireView().context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.tutorial_poem_dialog)

        val exitButton = dialog.findViewById<Button>(R.id.okDialogButton)

        exitButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            QuestionFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}