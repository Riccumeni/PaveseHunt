package com.example.pavesehunt.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.findViewTreeLifecycleOwner
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Event
import com.example.pavesehunt.domain.viewmodels.EventsViewModel
import java.time.LocalDateTime
import java.util.Calendar

class DateAdapter(val context: Context, var inflater: LayoutInflater?, val lifecycleOwner: LifecycleOwner, val eventsViewModel: EventsViewModel, var dayCount: Int, val daysInEvent: ArrayList<Int>): BaseAdapter() {

    override fun getCount(): Int {
        return dayCount
    }

    override fun getItem(p0: Int): Any {
        return 0
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        lateinit var view: View

        val day = p0 + 1

        if(inflater == null){
            inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        }

        if(p1 == null){
            view = inflater!!.inflate(R.layout.view_date, null)
        }

        var layout = p1 ?: view


        val calendar = Calendar.getInstance()

        val current = LocalDateTime.of(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,  // Adjust month to be 1-based
            calendar.get(Calendar.DAY_OF_MONTH),
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            calendar.get(Calendar.SECOND)
        )

        if(day == current.dayOfMonth){
            layout.findViewById<LinearLayout>(R.id.dateView).setBackgroundColor(0xFFFF0000.toInt())
        }

        daysInEvent.forEach {
            if(it == day){
                layout.findViewById<LinearLayout>(R.id.dateView).setBackgroundColor(0xFFAAAAAA.toInt())
            }
        }

        layout.findViewById<TextView>(R.id.numberDayText).text = (day).toString()


        layout.findViewById<LinearLayout>(R.id.dateView).setOnClickListener {
            eventsViewModel.previousDay = eventsViewModel.selectedDay.value
            eventsViewModel.selectedDay.value = day
            layout.findViewById<LinearLayout>(R.id.dateView).setBackgroundColor(0xFFFF0000.toInt())
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
                    layout.findViewById<LinearLayout>(R.id.dateView).setBackgroundColor(0xFFAAAAAA.toInt())
                }
            }
        }

        return p1 ?: view
    }
}