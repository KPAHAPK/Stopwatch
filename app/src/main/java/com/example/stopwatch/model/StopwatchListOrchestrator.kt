package com.example.stopwatch.model

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class StopwatchListOrchestrator(
    private val stopwatchStateHolderCreator: StopwatchStateHolderCreator,
    private val scope: CoroutineScope,
) {
    private var job: Job? = null
    private var stopwatchStateHolders = ConcurrentHashMap<StopwatchTask, StopwatchStateHolder>()
    private val mutableTicker = MutableStateFlow<Map<StopwatchTask, String>>(mapOf(StopwatchTask() to "00:00:000"))
    val ticker: StateFlow<Map<StopwatchTask, String>> = mutableTicker

    fun start(stopwatchTask: StopwatchTask) {
        if (job == null) {
            startJob()
        }
        val stopwatchPositionForStart = stopwatchStateHolders.getOrPut(stopwatchTask){
            stopwatchStateHolderCreator.create()
        }
        stopwatchPositionForStart.start()
    }

    private fun startJob() {
        scope.launch {
            while (isActive) {
                val newValues = stopwatchStateHolders
                    .map { (stopwatch, stateHolder) ->
                        stopwatch to stateHolder.getStringTimeRepresentation()
                    }
                    .toMap()
                mutableTicker.value = newValues
                delay(20)
            }
        }
    }

    fun pause(stopwatchTask: StopwatchTask) {
        val stopwatchPositionForPause = stopwatchStateHolders[stopwatchTask] ?: return
        stopwatchPositionForPause.pause()
        if (!isThereRunningStopwatch()) {
            stopJob()
        }
    }

    private fun isThereRunningStopwatch(): Boolean{
        return stopwatchStateHolders.values.any{
            it.currentState is StopwatchState.Running
        }
    }

    fun stop(stopwatchTask: StopwatchTask) {
        stopwatchStateHolders.remove(stopwatchTask)
        if (stopwatchStateHolders.isEmpty()){
            stopJob()
            clearValue()
        }
    }

    private fun stopJob() {
        scope.coroutineContext.cancelChildren()
        job = null
    }

    private fun clearValue() {
    }
}