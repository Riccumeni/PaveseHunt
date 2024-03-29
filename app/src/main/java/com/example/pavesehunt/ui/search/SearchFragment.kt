package com.example.pavesehunt.ui.search

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Collection
import com.example.pavesehunt.domain.usecases.STATUS
import com.example.pavesehunt.databinding.FragmentSearchBinding
import com.example.pavesehunt.domain.viewmodels.CollectionViewModel
import com.example.pavesehunt.domain.viewmodels.TopBarViewModel
import com.example.pavesehunt.ui.adapters.CollectionAdapter
import com.google.android.material.button.MaterialButton

class SearchFragment : Fragment() {

    private val collectionsViewModel : CollectionViewModel by activityViewModels()
    private val topBarViewModel : TopBarViewModel by activityViewModels()

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()

        topBarViewModel.screenChanged.value = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        collectionsViewModel.getCollections()
        topBarViewModel.screenChanged.value = true

        binding.searchEditText.addTextChangedListener {
            if(collectionsViewModel.collectionsResponse.value!!.status === STATUS.SUCCESS){
                collectionsViewModel.getCollectionsFiltered(it.toString())
            }
        }

        collectionsViewModel.collectionsFiltered.observe(viewLifecycleOwner){
            if(collectionsViewModel.collectionsResponse.value!!.status === STATUS.SUCCESS){
                view.findViewById<RecyclerView>(R.id.searchRecyclerView).apply {
                    adapter = CollectionAdapter(it, collectionsViewModel)
                    layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                }
            }
        }

        collectionsViewModel.collectionsResponse.observe(viewLifecycleOwner){
            when(it.status){
                STATUS.SUCCESS -> {

                    val collections = it.data as List<Collection>

                    binding.searchRecyclerView.apply {
                        adapter = CollectionAdapter(collections, collectionsViewModel)
                        layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                    }

                    binding.searchRecyclerView.visibility = View.VISIBLE
                    binding.loadingLayout.root.visibility = View.GONE
                }

                STATUS.LOADING -> {
                    binding.loadingLayout.root.visibility = View.VISIBLE
                    binding.errorLayout.root.visibility = View.GONE
                }

                STATUS.ERROR -> {
                    binding.errorLayout.root.visibility = View.VISIBLE
                    binding.loadingLayout.root.visibility = View.GONE

                }

                STATUS.NOT_STARTED -> {

                }
            }
        }

        binding.errorLayout.root.findViewById<MaterialButton>(R.id.retryButton).setOnClickListener {
            collectionsViewModel.getCollections()
        }
    }

    companion object {

        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {

            }
    }
}