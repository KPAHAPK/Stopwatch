package com.example.stopwatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.stopwatch.*
import com.example.stopwatch.databinding.ActivityStopwatchMainBinding
import com.example.stopwatch.vm.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class StopwatchMainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStopwatchMainBinding

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding){
            viewModel.liveData.observe(this@StopwatchMainActivity){
                textTime.text = it
            }
            buttonStart.setOnClickListener {
                viewModel.startTicker()
            }
            buttonPause.setOnClickListener{
                viewModel.pauseTicker()
            }
            buttonStop.setOnClickListener{
                viewModel.stopTicker()
            }
        }
    }
}