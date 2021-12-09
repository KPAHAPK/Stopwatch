package com.example.stopwatch

class StopwatchStateHolder(
    private val stopwatchStateCalculator: StopwatchStateCalculator,
    private val elapsedTimeCalculator: ElapsedTimeCalculator,
    private val timeStampMillisecondsFormatter: TimeStampMillisecondsFormatter,
) {
    var currentState: StopwatchState = StopwatchState.Paused(stopStateTime)

    fun start() {
        currentState = stopwatchStateCalculator.calculateRunningState(currentState)
    }

    fun pause() {
        currentState = stopwatchStateCalculator.calculatePausedState(currentState)
    }

    fun stop() {
        currentState = StopwatchState.Paused(stopStateTime)
    }

    companion object {
        private const val stopStateTime = 0L
    }

    fun getStringTimeRepresentation(): String {
        val elapsedTime = when (val currentState = currentState) {
            is StopwatchState.Running -> {
                elapsedTimeCalculator.calculate(currentState)
            }
            is StopwatchState.Paused -> {
                currentState.elapsedTime
            }
        }
        return timeStampMillisecondsFormatter.format(elapsedTime)
    }

}