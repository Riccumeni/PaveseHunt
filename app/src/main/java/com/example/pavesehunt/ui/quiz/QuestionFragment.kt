package com.example.pavesehunt.ui.quiz

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.example.pavesehunt.R
import com.example.pavesehunt.common.Questions
import com.example.testapp.domain.viewmodels.QuizViewModel

class QuestionFragment : Fragment() {

    private val viewModel : QuizViewModel by activityViewModels()

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
        return inflater.inflate(R.layout.fragment_question, container, false)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        viewModel.stopTimer()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.startTimer()

        val shared = view.context.getSharedPreferences("shared", Context.MODE_PRIVATE)

        var position = shared.getInt("indexQuestion", 0)

        val questionText = view.findViewById<TextView>(R.id.questionText)

        questionText.text = Questions.questions[position].question

        val buttons: List<Button> = listOf(
            view.findViewById(R.id.firstAnswerButton),
            view.findViewById(R.id.secondAnswerButton),
            view.findViewById(R.id.thirdAnswerButton),
            view.findViewById(R.id.fourthAnswerButton)
        )

        Questions.questions[position].answers.forEachIndexed{index, s ->
            buttons[index].text = s
        }

        buttons.forEachIndexed{ index, button ->
            button.setOnClickListener {

                val time: Int? = viewModel.counter.value

                viewModel.stopTimer()

                if(index == Questions.questions[position].indexOfCorrectAnswer){
                    viewModel.addPoints(time!!, view.context)
                }
                buttons.forEachIndexed { i, button ->
                    if(i == Questions.questions[position].indexOfCorrectAnswer){
                        buttons[i].setBackgroundColor(0xFF00FF00.toInt())
                    }else{
                        if(i == index){
                            buttons[i].setBackgroundColor(0xFF0000FF.toInt())
                        }else{
                            buttons[i].setBackgroundColor(0xFFFF0000.toInt())
                        }

                    }
                }

                position += 1

                with(shared.edit()){
                    putInt("indexQuestion", position)
                    apply()
                }

                Handler().postDelayed({
                    Questions.questions[position].answers.forEachIndexed{index, s ->
                        buttons[index].setBackgroundColor(0xFFC4C4C4.toInt())
                        buttons[index].text = s
                    }

                    questionText.text = Questions.questions[position].question

                    viewModel.startTimer()
                }, 1000)
            }
        }

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