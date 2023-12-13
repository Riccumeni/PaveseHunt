package com.example.pavesehunt.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Status
import com.example.testapp.data.models.User
import com.example.testapp.domain.viewmodels.QuizViewModel

class RankFragment : Fragment() {

    private val viewModel : QuizViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_rank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getLeatherboard()

        val rankText = view.findViewById<TextView>(R.id.rankText)

        viewModel.leatherboard.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Status.SUCCESS -> {
                    val leatherboard: List<User> = response!!.data as List<User>

                    var text = "position          username          points\n"

                    leatherboard.forEachIndexed { index, user ->
                        text += "${index + 1}          ${user.username}          ${user.points}\n"
                    }

                    rankText.text = text
                }
                Status.ERROR -> {
                    rankText.textSize=32.0f
                    rankText.text = "There is some error"
                }
                Status.LOADING -> {
                    rankText.textSize=32.0f
                    rankText.text = "Loading"
                }
            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            RankFragment().apply {

            }
    }
}