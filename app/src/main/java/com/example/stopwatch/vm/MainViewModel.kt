package com.example.stopwatch.vm

import androidx.lifecycle.*
import com.example.stopwatch.model.ElapsedTimeCalculator
import com.example.stopwatch.model.StopwatchListOrchestrator
import com.example.stopwatch.model.StopwatchStateCalculator
import com.example.stopwatch.model.StopwatchStateHolder
import com.example.stopwatch.view.TimeStampMillisecondsFormatter
import com.example.stopwatch.view.TimestampProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val mutableLiveData : MutableLiveData<String> = MutableLiveData()
    val liveData : LiveData<String> = mutableLiveData

    private val timestampProvider = object: TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val scopeObservable = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val scopeObserver = CoroutineScope(Dispatchers.Main + SupervisorJob())

    private val stopwatchStateOrchestrator = StopwatchListOrchestrator(
        stopwatchStateHolder = StopwatchStateHolder(
            stopwatchStateCalculator = StopwatchStateCalculator(
                timestampProvider = timestampProvider,
                elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider)
            ), elapsedTimeCalculator = ElapsedTimeCalculator(timestampProvider),
            timeStampMillisecondsFormatter = TimeStampMillisecondsFormatter()
        ), scope = scopeObservable
    )

    fun startTicker() {
        scopeObserver.launch {
            stopwatchStateOrchestrator.ticker.collect(){
                mutableLiveData.value = it
            }
        }
        stopwatchStateOrchestrator.start()
    }

    fun pauseTicker() {
        stopwatchStateOrchestrator.pause()
    }

    fun stopTicker(){
        stopwatchStateOrchestrator.stop()
    }
}