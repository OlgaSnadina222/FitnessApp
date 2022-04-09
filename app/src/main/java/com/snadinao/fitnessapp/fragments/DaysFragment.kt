package com.snadinao.fitnessapp.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snadinao.fitnessapp.R
import com.snadinao.fitnessapp.adapters.DayModel
import com.snadinao.fitnessapp.adapters.DaysAdapter
import com.snadinao.fitnessapp.adapters.ExercisesModel
import com.snadinao.fitnessapp.databinding.FragmentDaysBinding
import com.snadinao.fitnessapp.utils.DialogManager
import com.snadinao.fitnessapp.utils.FragmentManager
import com.snadinao.fitnessapp.utils.MainViewModel

class DaysFragment : Fragment(), DaysAdapter.Listener {

    private lateinit var binding: FragmentDaysBinding
    private var actBar: ActionBar? = null
    private lateinit var adapter: DaysAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model.currentDay = 0
        initRcView()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        return inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.reset){
            DialogManager.showDialog(
                activity as AppCompatActivity,
                R.string.reset_days_message,
                object: DialogManager.Listener{
                    override fun onClick() {
                        model.pref?.edit()?.clear()?.apply()
                        adapter.submitList(fillDaysArray())
                    }
                }
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initRcView() = with(binding){
        adapter = DaysAdapter(this@DaysFragment)
        actBar = (activity as AppCompatActivity).supportActionBar
        actBar?.title = getString(R.string.days)
        rcViewDays.layoutManager = LinearLayoutManager(activity as AppCompatActivity)
        rcViewDays.adapter = adapter
        adapter.submitList(fillDaysArray())
    }

    private fun fillDaysArray(): ArrayList<DayModel>{
        var daysDoneCounter = 0
        val tempArray = ArrayList<DayModel>()
        resources.getStringArray(R.array.days_exercises).forEach {
            model.currentDay++
            val exCounter = it.split(",").size
            tempArray.add(DayModel(it, 0,model.getExerciseCount() == exCounter))
        }
        binding.pBar.max = tempArray.size
        tempArray.forEach {
            if (it.isDone) daysDoneCounter++
        }
        updateRestDaysUI(tempArray.size - daysDoneCounter, tempArray.size)
        return tempArray
    }

    private fun updateRestDaysUI(restDays: Int, days: Int) = with(binding){
        val rDays = getString(R.string.rest) + " $restDays " + getString(R.string.rest_days)
        tvRestDays.text = rDays
        pBar.progress = days - restDays
    }

    private fun fillExercisesList(day: DayModel){
        val tempList = ArrayList<ExercisesModel>()
        day.exercise.split(",").forEach{
            val exercisesList = resources.getStringArray(R.array.exercises)
            val exercise = exercisesList[it.toInt()]
            val exerciseArray = exercise.split("|")
            tempList.add(ExercisesModel(exerciseArray[0],exerciseArray[1], false, exerciseArray[2]))
        }
        model.mutableListExercise.value = tempList
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }

    override fun onClick(day: DayModel) {
        if (!day.isDone) {
            fillExercisesList(day)
            model.currentDay = day.dayNumber
            FragmentManager.setFragment(
                ExercisesListFragment.newInstance(),
                activity as AppCompatActivity)
        } else {
            DialogManager.showDialog(
                activity as AppCompatActivity,
                R.string.reset_day_message,
                object: DialogManager.Listener{
                    override fun onClick() {
                        model.savePref(day.dayNumber.toString(), 0)
                        FragmentManager.setFragment(
                            ExercisesListFragment.newInstance(),
                            activity as AppCompatActivity)
                    }
                }
            )
        }
    }
}