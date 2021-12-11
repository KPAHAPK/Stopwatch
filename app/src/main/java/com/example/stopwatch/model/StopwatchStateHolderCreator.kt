package com.example.stopwatch.model

import com.example.stopwatch.view.TimeStampMillisecondsFormatter

class StopwatchStateHolderCreator(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timeStampMillisecondsFormatter: TimeStampMillisecondsFormatter,
) {

    fun create() : StopwatchStateHolder {
        return StopwatchStateHolder(stopwatchStateCalculator,
            elapsedTimeCalculator,
            timeStampMillisecondsFormatter)
    }
}