package com.snadinao.fitnessapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.snadinao.fitnessapp.R
import com.snadinao.fitnessapp.databinding.DaysListItemBinding
import com.snadinao.fitnessapp.databinding.ExerciseListItemBinding
import pl.droidsonroids.gif.GifDrawable

class ExerciseAdapter: ListAdapter<ExercisesModel, ExerciseAdapter.ExerciseHolder>(MyComparator()) {

    class ExerciseHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = ExerciseListItemBinding.bind(view)

        fun setData(exercise: ExercisesModel) = with(binding){
            tvName.text = exercise.name
            tvCount.text = exercise.time
            chBox.isChecked = exercise.isDone
            imExercise.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exercise_list_item, parent, false)
        return ExerciseHolder(view)
    }

    override fun onBindViewHolder(holder: ExerciseHolder, position: Int) {
        holder.setData(getItem(position))
    }

    class MyComparator: DiffUtil.ItemCallback<ExercisesModel>(){
        override fun areItemsTheSame(oldItem: ExercisesModel, newItem: ExercisesModel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ExercisesModel, newItem: ExercisesModel): Boolean {
            return oldItem == newItem
        }
    }
}