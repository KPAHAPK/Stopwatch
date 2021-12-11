package com.example.stopwatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stopwatch.*
import com.example.stopwatch.databinding.ActivityStopwatchMainBinding
import com.example.stopwatch.model.*
import com.example.stopwatch.vm.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class StopwatchMainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityStopwatchMainBinding
    private val adapter by lazy { RVAdapter(this, scopeObserver) }

    private val scopeObserver = CoroutineScope(Dispatchers.Main + SupervisorJob())
    val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel()::class.java]
    }

    fun getTicker() : StateFlow<Map<StopwatchTask, String>>{
        return viewModel.observe()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStopwatchMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        scopeObserver.launch {
            viewModel.observe().collect{
                adapter.notifyDataSetChanged()
            }
        }

        binding.rvStopwatches.layoutManager = LinearLayoutManager(this)
        binding.rvStopwatches.adapter = adapter
        binding.fabAdd.setOnClickListener{
            adapter.addStopwatchTask()
        }
    }
}