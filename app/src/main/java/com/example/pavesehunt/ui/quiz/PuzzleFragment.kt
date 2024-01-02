package com.example.pavesehunt.ui.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pavesehunt.data.models.Event
import com.example.pavesehunt.data.models.Status
import com.example.pavesehunt.databinding.FragmentPuzzleBinding
import com.example.pavesehunt.domain.viewmodels.EventsViewModel
import com.example.pavesehunt.ui.adapters.DateAdapter
import com.example.pavesehunt.ui.adapters.EventAdapter
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.Calendar

class PuzzleFragment : Fragment() {

    private val eventsViewModel: EventsViewModel by activityViewModels()

    private var _binding: FragmentPuzzleBinding? = null
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
        _binding = FragmentPuzzleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = Calendar.getInstance()

        val current = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )

        binding.monthText.text = (current.month).toString()

        val currentYearMonth = YearMonth.now()
        val numberOfDaysInMonth = currentYearMonth.lengthOfMonth()

        eventsViewModel.eventsResponse.observe(viewLifecycleOwner){
            when(it.status){
                Status.NOT_STARTED -> {
                    eventsViewModel.getEventsByMonthAndYear(current.month.value, current.year)
                }
                Status.SUCCESS -> {
                    val events = it.data as List<Event>
                    val daysInEvent = ArrayList<Int>()

                    events.forEach { event ->
                        daysInEvent.add(event.day)
                    }

                    eventsViewModel.selectedDay.value = current.dayOfMonth

                    binding.calendarView.apply {
                        adapter = DateAdapter(view.context, lifecycleOwner = viewLifecycleOwner, eventsViewModel = eventsViewModel, dayCount = numberOfDaysInMonth, inflater = null, daysInEvent = daysInEvent)
                    }
                }
                Status.LOADING -> {

                }

                Status.ERROR -> {

                }
            }
        }

        eventsViewModel.selectedDay.observe(viewLifecycleOwner){ selectedDay ->
            val events = eventsViewModel.eventsResponse.value!!.data as List<Event>?

            if(events != null){

                val filteredEventByDay = events.filter { event -> event.day == selectedDay }

                binding.eventsRecyclerView.apply {
                    adapter = EventAdapter(filteredEventByDay)
                    layoutManager = LinearLayoutManager(view.context, LinearLayoutManager.VERTICAL, false)
                }
            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            PuzzleFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}