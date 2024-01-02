package com.example.pavesehunt.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pavesehunt.R
import com.example.pavesehunt.data.models.Event

class EventAdapter(val events: List<Event>): RecyclerView.Adapter<EventAdapter.CustomViewHolder>()  {
    class CustomViewHolder(val view: ViewGroup) : RecyclerView.ViewHolder(view)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EventAdapter.CustomViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_event, parent, false) as ViewGroup
        return EventAdapter.CustomViewHolder(view)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val event = events[position]

        holder.view.findViewById<TextView>(R.id.titleEventText).text = event.title
        holder.view.findViewById<TextView>(R.id.bodyEventText).text = event.text

        lateinit var schedule: String

        if(event.minute.toString().length > 1){
            schedule = "${event.hour}:${event.minute}"
        }else{
            schedule = "${event.hour}:0${event.minute}"
        }

        holder.view.findViewById<TextView>(R.id.scheduleEventText).text = schedule
    }

    override fun getItemCount(): Int {
        return events.size
    }

}