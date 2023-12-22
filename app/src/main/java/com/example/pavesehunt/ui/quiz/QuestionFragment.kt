package com.example.pavesehunt.ui.quiz

import android.app.AlertDialog
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
import com.example.testapp.domain.viewmodels.QuizViewModel
import com.google.android.material.progressindicator.LinearProgressIndicator

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
        val counterIndicator = view.findViewById<LinearProgressIndicator>(R.id.counterIndicator)
        val openPoemDialogCard = view.findViewById<CardView>(R.id.openPoemCard)
        val tutorialCard = view.findViewById<CardView>(R.id.tutorialCard)

        questionText.text = Questions.questions[position].question

        viewModel.counter.observe(viewLifecycleOwner){
            counterIndicator.progress = it
        }

        openPoemDialogCard.setOnClickListener {
            showDialog()
        }

        tutorialCard.setOnClickListener {
            showTutorialDialog()
        }

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
                        buttons[index].setBackgroundColor(0xFFCBB18C.toInt())
                        buttons[index].text = s
                    }

                    questionText.text = Questions.questions[position].question

                    viewModel.startTimer()
                }, 1000)
            }
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