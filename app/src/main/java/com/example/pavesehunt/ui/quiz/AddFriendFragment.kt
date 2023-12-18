package com.example.pavesehunt.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Status
import com.example.testapp.data.models.User
import com.example.testapp.domain.viewmodels.QuizViewModel
import com.example.testapp.domain.viewmodels.UserViewModel

class AddFriendFragment : Fragment() {

    private val userViewModel : UserViewModel by activityViewModels()

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
        return inflater.inflate(R.layout.fragment_add_friend, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val addFriendEditText = view.findViewById<EditText>(R.id.addFriendEditText)

        addFriendEditText.doOnTextChanged { text, start, before, count ->
            text.let {
                if(text!!.length >= 3){
                    userViewModel.getUsers(text.toString())
                }
            }
        }

        userViewModel.usersResponse.observe(viewLifecycleOwner){
            when(it.status){
                Status.SUCCESS -> {
                    val users = it.data as List<User>
                    val friendTextView = view.findViewById<TextView>(R.id.frientTextView)

                    users.forEach {
                        friendTextView.text = friendTextView.text.toString() + it.username + "\n"
                    }
                }

                Status.LOADING -> {

                }

                Status.ERROR -> {

                }

                Status.NOT_STARTED -> {

                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            AddFriendFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}