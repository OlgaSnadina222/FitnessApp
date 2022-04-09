package com.snadinao.fitnessapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.snadinao.fitnessapp.R
import com.snadinao.fitnessapp.adapters.DayModel
import com.snadinao.fitnessapp.adapters.DaysAdapter
import com.snadinao.fitnessapp.adapters.ExerciseAdapter
import com.snadinao.fitnessapp.databinding.ExercisesListFragmentBinding
import com.snadinao.fitnessapp.databinding.FragmentDaysBinding
import com.snadinao.fitnessapp.utils.FragmentManager
import com.snadinao.fitnessapp.utils.MainViewModel

class ExercisesListFragment : Fragment() {

    private lateinit var binding: ExercisesListFragmentBinding
    private lateinit var adapter: ExerciseAdapter
    private var actBar: ActionBar? = null
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ExercisesListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        model.mutableListExercise.observe(viewLifecycleOwner){
            for (i in 0 until model.getExerciseCount()){
                it[i] = it[i].copy(isDone = true)
            }
            adapter.submitList(it)
        }
    }

    private fun init() = with(binding){
        actBar = (activity as AppCompatActivity).supportActionBar
        actBar?.title = getString(R.string.exercise)
        adapter = ExerciseAdapter()
        rcView.layoutManager = LinearLayoutManager(activity)
        rcView.adapter = adapter
        bStart.setOnClickListener {
            FragmentManager.setFragment(WaitingFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExercisesListFragment()
    }
}