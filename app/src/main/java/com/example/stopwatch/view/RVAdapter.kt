package com.example.stopwatch.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatch.databinding.ItemStopwatchBinding
import com.example.stopwatch.model.StopwatchTask
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class RVAdapter(private val context: Context, private val scope: CoroutineScope) :
    RecyclerView.Adapter<RVAdapter.StopwatchViewHolder>() {

    private lateinit var _binding: ItemStopwatchBinding
    private val binding
        get() = _binding!!

    private val stopwatchTasks = mutableListOf<StopwatchTask>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StopwatchViewHolder {
        _binding = ItemStopwatchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val view = binding
        return StopwatchViewHolder(view)
    }

    override fun onBindViewHolder(holder: StopwatchViewHolder, position: Int) {
        holder.bind(stopwatchTasks[position])
    }

    override fun getItemCount() = stopwatchTasks.size

    fun addStopwatchTask() {
        stopwatchTasks.add(StopwatchTask())
        notifyItemInserted(itemCount - 1)
    }

    inner class StopwatchViewHolder(private val binding: ItemStopwatchBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stopwatchTask: StopwatchTask) {
            if (context is StopwatchMainActivity) {
                with(binding) {
                    scope.launch {
                        context.getTicker().collect {
                            textTime.text = it[stopwatchTask]
                        }
                    }
                    buttonStart.setOnClickListener {
                        context.viewModel.startTicker(stopwatchTask)
                    }
                    buttonPause.setOnClickListener {
                        context.viewModel.pauseTicker(stopwatchTask)
                    }
                    buttonStop.setOnClickListener {
                        context.viewModel.stopTicker(stopwatchTask)
                    }
                }
            }
        }
    }
}