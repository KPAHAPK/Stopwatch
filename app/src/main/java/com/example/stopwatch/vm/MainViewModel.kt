package com.example.stopwatch.vm

import androidx.lifecycle.*
import androidx.recyclerview.widget.RecyclerView
import com.example.stopwatch.model.*
import com.example.stopwatch.view.TimeStampMillisecondsFormatter
import com.example.stopwatch.view.TimestampProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val mutableLiveData = MutableLiveData<Map<StopwatchTask, String>>()
    val livedata: LiveData<Map<StopwatchTask, String>> = mutableLiveData

    private val timestampProvider = object: TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }
    private val scopeObservable = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val stopwatchStateOrchestrator = StopwatchListOrchestrator(
        stopwatchStateHolderCreator = StopwatchStateHolderCreator(
            stopwatchStateCalculator = StopwatchStateCalculator(
                timestampProvider = timestampProvider,
                elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider)
            ), elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider),
            timeStampMillisecondsFormatter = TimeStampMillisecondsFormatter()
        ), scope = scopeObservable
    )

    fun observe(): StateFlow<Map<StopwatchTask, String>>{
        return stopwatchStateOrchestrator.ticker
    }

    fun startTicker(stopwatchTask: StopwatchTask) {
        stopwatchStateOrchestrator.start(stopwatchTask)
    }

    fun pauseTicker(stopwatchTask: StopwatchTask) {
        stopwatchStateOrchestrator.pause(stopwatchTask)
    }

    fun stopTicker(stopwatchTask: StopwatchTask){
        stopwatchStateOrchestrator.stop(stopwatchTask)
    }
}