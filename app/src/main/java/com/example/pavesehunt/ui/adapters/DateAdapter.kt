package com.example.pavesehunt.ui.adapters

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.pavesehunt.R
import com.example.pavesehunt.common.TimeHelper
import com.example.pavesehunt.data.models.Event
import com.example.pavesehunt.domain.viewmodels.EventsViewModel
import java.time.LocalDateTime
import java.util.Calendar

class DateAdapter(val context: Context, var inflater: LayoutInflater?, val startDay: Int, val lifecycleOwner: LifecycleOwner, val eventsViewModel: EventsViewModel, var dayCount: Int, val daysInEvent: ArrayList<Int>): BaseAdapter() {


    var dayToSkip = 0
    override fun getCount(): Int {
        when(startDay){
            3 -> {
                dayToSkip = 1
            }

            4 -> {
                dayToSkip = 2
            }

            5 -> {
                dayToSkip = 3
            }

            6 -> {
                dayToSkip = 4
            }

            7 -> {
                dayToSkip = 5
            }

            1 -> {
                dayToSkip = 6
            }
        }
        return dayCount + dayToSkip
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        lateinit var view: View

        val day = p0 + 1 - dayToSkip

        if(inflater == null){
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if(p1 == null){
            view = inflater!!.inflate(R.layout.view_date, null)
        }

        var layout = p1 ?: view

        if(p0 < dayToSkip){
            layout.findViewById<LinearLayout>(R.id.dateView).visibility = View.GONE
        }

        val calendar = Calendar.getInstance()

        val current = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,  // Adjust month to be 1-based
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )

        daysInEvent.forEach {
            if(it == day){
                layout.findViewById<LinearLayout>(R.id.dateView).setBackgroundColor(0xFF746551.toInt())
            }
        }

        layout.findViewById<TextView>(R.id.numberDayText).text = (day).toString()


        layout.findViewById<LinearLayout>(R.id.dateView).setOnClickListener {
            eventsViewModel.previousDay = eventsViewModel.selectedDay.value
            eventsViewModel.selectedDay.value = day
            layout.findViewById<LinearLayout>(R.id.dateView).setBackgroundColor(0xFFCBB18C.toInt())
        }

        eventsViewModel.selectedDay.observe(lifecycleOwner){
            if(day == eventsViewModel.previousDay){
                val events = eventsViewModel.eventsResponse.value!!.data as List<Event>

                var isInEvent = false

                events.forEach { event ->
                    if(event.day == eventsViewModel.previousDay){
                        isInEvent = true
                    }
                }

                if(!isInEvent){
                    layout.findViewById<LinearLayout>(R.id.dateView).setBackgroundColor(0xFF1F1B17.toInt())
                }else{
                    layout.findViewById<LinearLayout>(R.id.dateView).setBackgroundColor(0xFF746551.toInt())
                }
            }
        }
        return p1 ?: view
    }
}