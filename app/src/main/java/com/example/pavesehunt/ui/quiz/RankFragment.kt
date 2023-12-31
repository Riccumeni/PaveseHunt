package com.example.pavesehunt.ui.quiz

import android.content.res.ColorStateList
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Status
import com.example.pavesehunt.ui.adapters.RankAdapter
import com.example.testapp.data.models.User
import com.example.testapp.domain.viewmodels.QuizViewModel
import com.google.android.material.progressindicator.CircularProgressIndicator

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

        val addFriendButton = view.findViewById<View>(R.id.addFriendButton)
        val globalLeatherboardButton = view.findViewById<View>(R.id.globalButton)
        val friendLeatherboardButton = view.findViewById<View>(R.id.friendsButton)
        val circularProgress = view.findViewById<CircularProgressIndicator>(R.id.rankCircularIndicator)



        globalLeatherboardButton.setOnClickListener {
            viewModel.getLeatherboard()

            friendLeatherboardButton.setBackgroundTintList(ColorStateList.valueOf(0xFF746551.toInt()))
            globalLeatherboardButton.setBackgroundTintList(ColorStateList.valueOf(0xFFCBB18C.toInt()))

            val globalTextButton = view.findViewById<TextView>(R.id.globalTextButton)
            val friendsTextButton = view.findViewById<TextView>(R.id.friendTextButton)

            globalTextButton.setTextColor(0xFF55442B.toInt())
            friendsTextButton.setTextColor(0xFFBBBBBB.toInt())
        }

        friendLeatherboardButton.setOnClickListener {
            viewModel.getFriendsLeatherboard(view.context)

            friendLeatherboardButton.setBackgroundTintList(ColorStateList.valueOf(0xFFCBB18C.toInt()))
            globalLeatherboardButton.setBackgroundTintList(ColorStateList.valueOf(0xFF746551.toInt()))

            val globalTextButton = view.findViewById<TextView>(R.id.globalTextButton)
            val friendsTextButton = view.findViewById<TextView>(R.id.friendTextButton)

            friendsTextButton.setTextColor(0xFF55442B.toInt())
            globalTextButton.setTextColor(0xFFBBBBBB.toInt())
        }

        viewModel.leatherboard.observe(viewLifecycleOwner){ response ->
            when(response.status){
                Status.SUCCESS -> {
                    val leatherboard: List<User> = response!!.data as List<User>


                    circularProgress.visibility = View.GONE

                    val leatherboardRecyclerView = view.findViewById<RecyclerView>(R.id.leatherboardRecyclerView)

                    leatherboardRecyclerView.apply {
                        adapter = RankAdapter(leatherboard)
                        layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                    }

                }
                Status.ERROR -> {

                }
                Status.LOADING -> {
                    circularProgress.visibility = View.VISIBLE
                }

                Status.NOT_STARTED -> {

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