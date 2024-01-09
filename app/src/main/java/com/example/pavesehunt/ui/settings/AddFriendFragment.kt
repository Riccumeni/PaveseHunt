package com.example.pavesehunt.ui.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Status
import com.example.pavesehunt.ui.adapters.FriendAdapter
import com.example.testapp.data.models.User
import com.example.testapp.domain.viewmodels.UserViewModel
import com.google.android.material.tabs.TabLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

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

        var indexTabLayout = 0

        val addFriendEditText = view.findViewById<EditText>(R.id.addFriendEditText)
        val addFriendTabLayout = view.findViewById<TabLayout>(R.id.friendTabLayout)
        val friendRecyclerView = view.findViewById<RecyclerView>(R.id.friendRecyclerView)

        userViewModel.getFriends(view.context, null)

        addFriendEditText.visibility = View.GONE



        addFriendTabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    indexTabLayout = tab.position

                    when(indexTabLayout){
                        0 -> {
                            addFriendEditText.visibility = View.GONE
                            userViewModel.getFriends(view.context, null)
                        }

                        1 -> {
                            addFriendEditText.visibility = View.VISIBLE

                            val users: List<User> = listOf()

                            friendRecyclerView.apply {
                                adapter = FriendAdapter(users)
                                layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                            }
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        addFriendEditText.doOnTextChanged { text, start, before, count ->
            text.let {
                if(text!!.length >= 3){
                    userViewModel.getUsers(text.toString(), view.context)
                }
            }
        }

        userViewModel.usersResponse.observe(viewLifecycleOwner){
            when(it.status){

                Status.NOT_STARTED -> {

                }

                Status.SUCCESS -> {
                    val users = it.data as List<User>



                    friendRecyclerView.apply {
                        adapter = FriendAdapter(users = users)
                        layoutManager =
                            LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                    }


                }

                Status.LOADING -> {

                }

                Status.ERROR -> {

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