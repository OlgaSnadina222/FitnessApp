package com.snadinao.fitnessapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.snadinao.fitnessapp.R
import com.snadinao.fitnessapp.databinding.DayFinishBinding
import com.snadinao.fitnessapp.utils.FragmentManager
import pl.droidsonroids.gif.GifDrawable

class DayFinishFragment : Fragment() {

    private lateinit var binding: DayFinishBinding
    private var actBar: ActionBar? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DayFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        actBar = (activity as AppCompatActivity).supportActionBar
        actBar?.title = getString(R.string.done)
        binding.imMain.setImageDrawable(GifDrawable((activity as AppCompatActivity).assets, "congrats.gif"))
        binding.bDone.setOnClickListener {
            FragmentManager.setFragment(DaysFragment.newInstance(), activity as AppCompatActivity)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = DayFinishFragment()
    }
}