package com.snadinao.fitnessapp.fragments

import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.snadinao.fitnessapp.databinding.WaitingFragmentBinding
import com.snadinao.fitnessapp.utils.FragmentManager
import com.snadinao.fitnessapp.utils.MainViewModel
import com.snadinao.fitnessapp.utils.TimeUtils

const val COUNT_DOWN_TIMER = 11000L

class WaitingFragment : Fragment() {

    private lateinit var binding: WaitingFragmentBinding
    private lateinit var timer: CountDownTimer
    private var actBar: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = WaitingFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actBar = (activity as AppCompatActivity).supportActionBar
        actBar?.title = getString(R.string.exercise)
        binding.pBar.max = COUNT_DOWN_TIMER.toInt()
        startTimer()
    }

    private fun startTimer() = with(binding){
        timer = object: CountDownTimer(COUNT_DOWN_TIMER, 1){
            override fun onTick(restTime: Long) {
                tvTimer.text = TimeUtils.getTime(restTime)
                pBar.progress = restTime.toInt()
            }

            override fun onFinish() {
                FragmentManager.setFragment(ExercisesFragment.newInstance(), activity as AppCompatActivity)
            }
        }.start()
    }

    override fun onDetach() {
        super.onDetach()
        timer.cancel()
    }

    companion object {
        @JvmStatic
        fun newInstance() = WaitingFragment()
    }
}