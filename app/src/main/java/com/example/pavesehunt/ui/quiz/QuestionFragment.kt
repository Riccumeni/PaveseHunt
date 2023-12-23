package com.example.pavesehunt.ui.quiz

import android.app.Dialog
import android.content.Context
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
import com.example.pavesehunt.common.Questions
import com.example.pavesehunt.data.models.Status
import com.example.pavesehunt.databinding.FragmentQuestionBinding
import com.example.pavesehunt.databinding.FragmentQuizBinding
import com.example.testapp.data.models.User
import com.example.testapp.domain.viewmodels.QuestionTwo
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

        quizViewModel.questionsResponse.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Status.NOT_STARTED -> {
                    quizViewModel.getQuestions()
                }

                Status.SUCCESS -> {

                    val questions = response.data as List<QuestionTwo>

                    val user = userViewModel.userResponse.value!!.data as User

                    binding.questionText.text = questions[user.answer_given!!].question

                    val buttons: List<Button> = listOf(
                        view.findViewById(R.id.firstAnswerButton),
                        view.findViewById(R.id.secondAnswerButton),
                        view.findViewById(R.id.thirdAnswerButton),
                        view.findViewById(R.id.fourthAnswerButton)
                    )

                    var answers = Json.decodeFromString<Array<String>>(questions[user.answer_given!!].answer)

                    answers.forEachIndexed { index, answer ->
                        buttons[index].text = answer
                    }

                    buttons.forEachIndexed{ index, button ->
                        button.setOnClickListener {

                            val time: Int? = quizViewModel.counter.value

                            quizViewModel.stopTimer()

                            if(index == questions[user.answer_given!!].correct_answer){
                                quizViewModel.addPoints(time!!, view.context)
                            }
                            buttons.forEachIndexed { i, button ->
                                if(i == questions[user.answer_given!!].correct_answer){
                                    buttons[i].setBackgroundColor(0xFF00FF00.toInt())
                                }else{
                                    if(i == index){
                                        buttons[i].setBackgroundColor(0xFF0000FF.toInt())
                                    }else{
                                        buttons[i].setBackgroundColor(0xFFFF0000.toInt())
                                    }

                                }
                            }

                            user.answer_given = user.answer_given!! + 1

                            Handler().postDelayed({

                                answers = Json.decodeFromString(questions[user.answer_given!!].answer)

                                answers.forEachIndexed{index, s ->
                                    buttons[index].setBackgroundColor(0xFFCBB18C.toInt())
                                    buttons[index].text = s
                                }

                                binding.questionText.text = Questions.questions[user.answer_given!!].question

                                quizViewModel.startTimer()
                            }, 1000)
                        }
                    }


                }

                Status.LOADING -> {

                }

                Status.ERROR -> {

                }
            }
        }

        quizViewModel.startTimer()

        val counterIndicator = view.findViewById<LinearProgressIndicator>(R.id.counterIndicator)
        val openPoemDialogCard = view.findViewById<CardView>(R.id.openPoemCard)
        val tutorialCard = view.findViewById<CardView>(R.id.tutorialCard)


        quizViewModel.counter.observe(viewLifecycleOwner){
            counterIndicator.progress = it
        }

        openPoemDialogCard.setOnClickListener {
            showDialog()
        }

        tutorialCard.setOnClickListener {
            showTutorialDialog()
        }
    }

    fun showDialog(){
        val dialog = Dialog(requireView().context)

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.poem_dialog)

        val poemTextView = dialog.findViewById<TextView>(R.id.poemTextView)
        val exitButton = dialog.findViewById<Button>(R.id.okDialogButton)

        poemTextView.text = "Nel mezzo del cammin di nostra vita\n" +
                "mi ritrovai per una selva oscura,\n" +
                "ché la diritta via era smarrita.\n" +
                "\n" +
                "Ahi quanto a dir qual era è cosa dura\n" +
                "esta selva selvaggia e aspra e forte\n" +
                "che nel pensier rinova la paura!\n" +
                "\n" +
                "Tant’è amara che poco è più morte;\n" +
                "ma per trattar del ben ch’i’ vi trovai,\n" +
                "dirò de l’altre cose ch’i’ v’ ho scorte."

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