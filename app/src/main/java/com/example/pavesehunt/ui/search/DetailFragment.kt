package com.example.pavesehunt.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.pavesehunt.R
import com.example.pavesehunt.domain.viewmodels.CollectionViewModel
import org.w3c.dom.Text

class DetailFragment : Fragment() {
    private val collectionsViewModel : CollectionViewModel by activityViewModels()
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
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val collection = collectionsViewModel.collectionSelected

        Glide.with(view.context).load(collection!!.image).into(view.findViewById(R.id.poemImg))
        view.findViewById<TextView>(R.id.titlePoem).text = collection.title
        view.findViewById<TextView>(R.id.textPoem).text = collection.text
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            DetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }
}