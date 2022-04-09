package com.snadinao.fitnessapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snadinao.fitnessapp.R
import com.snadinao.fitnessapp.databinding.DaysListItemBinding

class DaysAdapter(var listener: Listener): ListAdapter<DayModel, DaysAdapter.DayHolder>(MyComparator()) {

    class DayHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = DaysListItemBinding.bind(view)

        fun setData(day: DayModel, listener: Listener) = with(binding){
            val name = root.context.getString(R.string.day) + " ${adapterPosition + 1}"
            tvName.text = name
            val exCounter = root.context.getString(R.string.exercises) +" " + day.exercise.split(",").size.toString()
            tvExCounter.text = exCounter
            chBox.isChecked = day.isDone
            itemView.setOnClickListener {
                listener.onClick(day.copy(dayNumber = adapterPosition +1))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DayHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.days_list_item, parent, false)
        return DayHolder(view)
    }

    override fun onBindViewHolder(holder: DayHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class MyComparator: DiffUtil.ItemCallback<DayModel>(){
        override fun areItemsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: DayModel, newItem: DayModel): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener{
        fun onClick(day: DayModel)
    }
}