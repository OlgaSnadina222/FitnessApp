package com.snadinao.fitnessapp.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.snadinao.fitnessapp.R
import com.snadinao.fitnessapp.adapters.ExercisesModel
import com.snadinao.fitnessapp.databinding.ExerciseBinding
import com.snadinao.fitnessapp.utils.FragmentManager
import com.snadinao.fitnessapp.utils.MainViewModel
import com.snadinao.fitnessapp.utils.TimeUtils
import pl.droidsonroids.gif.GifDrawable

class ExercisesFragment : Fragment() {

    private lateinit var binding: ExerciseBinding
    private var timer: CountDownTimer? = null
    private var exerciseCounter = 0
    private var exerciseList: ArrayList<ExercisesModel>? = null
    private var actBar: ActionBar? = null
    private var currentDay = 0
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExerciseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentDay = model.currentDay
        exerciseCounter = model.getExerciseCount()
        actBar = (activity as AppCompatActivity).supportActionBar
        model.mutableListExercise.observe(viewLifecycleOwner){
            exerciseList = it
            nextExercise()
        }
        binding.bNext.setOnClickListener {
            nextExercise()
        }
    }

    private fun nextExercise(){
        if (exerciseCounter < exerciseList?.size!!){
            val ex = exerciseList?.get(exerciseCounter++) ?: return
            showExercise(ex)
            setExerciseType(ex)
            showNextExercise()
        } else {
            exerciseCounter++
            FragmentManager.setFragment(DayFinishFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    private fun showExercise(exercise: ExercisesModel) = with(binding){
        imMain.setImageDrawable(GifDrawable(root.context.assets, exercise.image))
        tvName.text = exercise.name
        val title = "$exerciseCounter / ${exerciseList?.size}"
        actBar?.title = title
    }

    private fun setExerciseType(exercise: ExercisesModel){
        if (exercise.time.startsWith("x")){
            binding.tvTime.text = exercise.time
        } else {
            startTimer(exercise)
        }
    }

    private fun showNextExercise() = with(binding){
        if (exerciseCounter < exerciseList?.size!!){
            val ex = exerciseList?.get(exerciseCounter) ?: return
            imNext.setImageDrawable(GifDrawable(root.context.assets, ex.image))
            setTimeType(ex)
        } else {
            imNext.setImageDrawable(GifDrawable(root.context.assets, "congrats.gif"))
            tvNextName.text = getString(R.string.done)
        }
    }

    private fun  setTimeType(ex: ExercisesModel){
        if (ex.time.startsWith("x")){
            binding.tvNextName.text = ex.time
        } else {
            val name = ex.name + ": ${TimeUtils.getTime(ex.time.toLong() * 1000)}"
            binding.tvNextName.text = name
        }
    }

    private fun startTimer(exercise: ExercisesModel) = with(binding){
        progressBar.max= exercise.time.toInt() * 1000
        timer?.cancel()
        timer = object: CountDownTimer(exercise.time.toLong() * 1000, 1){
            override fun onTick(restTime: Long) {
                tvTime.text = TimeUtils.getTime(restTime)
                progressBar.progress = restTime.toInt()
            }

            override fun onFinish() {
                nextExercise()
            }
        }.start()
    }

    override fun onDetach() {
        super.onDetach()
        model.savePref(currentDay.toString(), exerciseCounter - 1)
        timer?.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExercisesFragment()
    }
}